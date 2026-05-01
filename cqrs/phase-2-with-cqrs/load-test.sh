#!/bin/bash
# Load test for Phase 2: WITH CQRS
# Shows: heavy reads do NOT affect command latency
# because reads go to a DIFFERENT service + DIFFERENT DB

now_ms() { python3 -c 'import time; print(int(time.time()*1000))'; }

CMD_URL="http://localhost:8081"   # order-command-service → Postgres
QRY_URL="http://localhost:8082"   # order-query-service  → Cassandra
CMD_LOG="/tmp/phase2-cmd-latency.log"
CMD_UNDER_LOAD_LOG="/tmp/phase2-cmd-under-load-latency.log"
NUM_COMMANDS=20
NUM_QUERIES=200  # Heavy read flood

echo "============================================"
echo "  Phase 2: WITH CQRS - Load Test"
echo "  Commands → Postgres + Kafka (port 8081)"
echo "  Queries  → Cassandra       (port 8082)"
echo "  Showing: read load does NOT impact writes"
echo "============================================"
echo ""

# --- Seed orders ---
echo ">> Seeding 10 orders via command-service..."
for i in $(seq 1 10); do
  curl -s -X POST "$CMD_URL/orders" \
    -H "Content-Type: application/json" \
    -d "{\"customerId\":$i,\"customerName\":\"Customer-$i\",\"itemName\":\"Item-$i\",\"quantity\":$i,\"price\":$((i * 10)).99}" > /dev/null
done
sleep 2  # Let Kafka sync to Cassandra
echo "   Done. Kafka synced."
echo ""

# ============================================================
# TEST 1: Command latency WITHOUT read load (baseline)
# ============================================================
echo ">> TEST 1: $NUM_COMMANDS commands with NO read load (baseline)"
> "$CMD_LOG"
for i in $(seq 1 $NUM_COMMANDS); do
  START=$(now_ms)
  curl -s -X POST "$CMD_URL/orders" \
    -H "Content-Type: application/json" \
    -d "{\"customerId\":$i,\"customerName\":\"BaseUser-$i\",\"itemName\":\"Product-$i\",\"quantity\":1,\"price\":9.99}" > /dev/null
  END=$(now_ms)
  LATENCY=$((END - START))
  echo "$LATENCY" >> "$CMD_LOG"
done

BASELINE_AVG=$(python3 -c "
lines = open('$CMD_LOG').readlines()
vals = [int(x.strip()) for x in lines if x.strip()]
print(int(sum(vals)/len(vals)))
")
BASELINE_MAX=$(sort -n "$CMD_LOG" | tail -1)
echo "   Avg command latency: ${BASELINE_AVG}ms"
echo "   Max command latency: ${BASELINE_MAX}ms"
echo ""

# ============================================================
# TEST 2: Command latency WITH heavy read load
# ============================================================
echo ">> TEST 2: $NUM_COMMANDS commands WITH $NUM_QUERIES concurrent reads"
echo "   Commands → Postgres (port 8081)"
echo "   Reads    → Cassandra (port 8082)  ← DIFFERENT service & DB!"
echo ""

# Start heavy read flood against QUERY service (Cassandra)
echo "   Starting read flood on query-service (Cassandra)..."
for i in $(seq 1 $NUM_QUERIES); do
  if [ $((i % 2)) -eq 0 ]; then
    curl -s "$QRY_URL/orders" > /dev/null &
  else
    curl -s "$QRY_URL/orders/$((RANDOM % 10 + 1))" > /dev/null &
  fi
done

# Measure command latency WHILE reads are hammering query-service
> "$CMD_UNDER_LOAD_LOG"
for i in $(seq 1 $NUM_COMMANDS); do
  START=$(now_ms)
  curl -s -X POST "$CMD_URL/orders" \
    -H "Content-Type: application/json" \
    -d "{\"customerId\":$i,\"customerName\":\"LoadUser-$i\",\"itemName\":\"Product-$i\",\"quantity\":1,\"price\":9.99}" > /dev/null
  END=$(now_ms)
  LATENCY=$((END - START))
  echo "$LATENCY" >> "$CMD_UNDER_LOAD_LOG"
done
wait  # wait for background reads to finish

LOADED_AVG=$(python3 -c "
lines = open('$CMD_UNDER_LOAD_LOG').readlines()
vals = [int(x.strip()) for x in lines if x.strip()]
print(int(sum(vals)/len(vals)))
")
LOADED_MAX=$(sort -n "$CMD_UNDER_LOAD_LOG" | tail -1)
echo "   Avg command latency: ${LOADED_AVG}ms"
echo "   Max command latency: ${LOADED_MAX}ms"
echo ""

# ============================================================
# COMPARISON
# ============================================================
INCREASE=$(python3 -c "
b=$BASELINE_AVG; l=$LOADED_AVG
if b > 0: print(f'{((l-b)/b)*100:.0f}')
else: print('0')
")

echo "============================================"
echo "  RESULTS COMPARISON"
echo "============================================"
echo ""
echo "  Command Latency     | No Load  | Under Read Load"
echo "  --------------------|----------|----------------"
echo "  Average             | ${BASELINE_AVG}ms     | ${LOADED_AVG}ms"
echo "  Max                 | ${BASELINE_MAX}ms     | ${LOADED_MAX}ms"
echo ""
echo "  Latency increase: ~${INCREASE}%"
echo ""
echo "============================================"
echo "  WHY IS IT STABLE?"
echo "============================================"
echo "  Reads and writes are FULLY SEPARATED:"
echo "    - Writes → order-command-service → Postgres"
echo "    - Reads  → order-query-service  → Cassandra"
echo "    - Different services"
echo "    - Different databases"
echo "    - Different connection pools"
echo "    - Different thread pools"
echo ""
echo "  200 concurrent reads on Cassandra do NOT"
echo "  touch Postgres or the command service at all!"
echo "============================================"
echo ""

# --- Eventual consistency demo ---
echo ">> Bonus: Eventual Consistency Demo"
echo "   Creating order via command-service..."
RESP=$(curl -s -X POST "$CMD_URL/orders" \
  -H "Content-Type: application/json" \
  -d '{"customerId":99,"customerName":"EventualUser","itemName":"TestItem","quantity":1,"price":1.00}')
ORDER_ID=$(echo $RESP | python3 -c "import sys,json; print(json.load(sys.stdin)['id'])")
echo "   Order #$ORDER_ID created in Postgres."
echo "   Querying immediately from Cassandra..."
IMMEDIATE=$(curl -s "$QRY_URL/orders/$ORDER_ID" 2>/dev/null)
if echo "$IMMEDIATE" | python3 -c "import sys,json; json.load(sys.stdin)" 2>/dev/null; then
  echo "   Found! (Kafka synced within ms)"
else
  echo "   Not found yet — eventual consistency!"
  sleep 2
  echo "   After 2s: $(curl -s "$QRY_URL/orders/$ORDER_ID" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'Order #{d[\"id\"]} - {d[\"customerName\"]} - {d[\"status\"]}')")"
fi
