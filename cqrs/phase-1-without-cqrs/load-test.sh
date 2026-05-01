#!/bin/bash
# Load test for Phase 1: WITHOUT CQRS
# Shows: heavy reads slow down command (write) latency
# because both share the SAME service + SAME Postgres DB

now_ms() { python3 -c 'import time; print(int(time.time()*1000))'; }

BASE_URL="http://localhost:8080"
CMD_LOG="/tmp/phase1-cmd-latency.log"
CMD_UNDER_LOAD_LOG="/tmp/phase1-cmd-under-load-latency.log"
NUM_COMMANDS=20
NUM_QUERIES=200  # Heavy read flood

echo "============================================"
echo "  Phase 1: WITHOUT CQRS - Load Test"
echo "  Single order-service → Postgres"
echo "  Showing: read load impacts write latency"
echo "============================================"
echo ""

# --- Seed orders ---
echo ">> Seeding 10 orders..."
for i in $(seq 1 10); do
  curl -s -X POST "$BASE_URL/orders" \
    -H "Content-Type: application/json" \
    -d "{\"customerId\":$i,\"customerName\":\"Customer-$i\",\"itemName\":\"Item-$i\",\"quantity\":$i,\"price\":$((i * 10)).99}" > /dev/null
done
echo "   Done."
echo ""

# ============================================================
# TEST 1: Command latency WITHOUT read load (baseline)
# ============================================================
echo ">> TEST 1: $NUM_COMMANDS commands with NO read load (baseline)"
> "$CMD_LOG"
for i in $(seq 1 $NUM_COMMANDS); do
  START=$(now_ms)
  curl -s -X POST "$BASE_URL/orders" \
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
# TEST 2: Command latency WITH heavy read load (contention!)
# ============================================================
echo ">> TEST 2: $NUM_COMMANDS commands WITH $NUM_QUERIES concurrent reads"
echo "   (both hitting same Postgres via same service)"
echo ""

# Start heavy read flood in background
echo "   Starting read flood..."
for i in $(seq 1 $NUM_QUERIES); do
  if [ $((i % 2)) -eq 0 ]; then
    curl -s "$BASE_URL/orders" > /dev/null &
  else
    curl -s "$BASE_URL/orders/$((RANDOM % 10 + 1))" > /dev/null &
  fi
done

# Now measure command latency WHILE reads are hammering
> "$CMD_UNDER_LOAD_LOG"
for i in $(seq 1 $NUM_COMMANDS); do
  START=$(now_ms)
  curl -s -X POST "$BASE_URL/orders" \
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
else: print('N/A')
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
echo "  WHY?"
echo "============================================"
echo "  Both reads(70%) and writes(30%) share:"
echo "    - Same Spring Boot service (port 8080)"
echo "    - Same Postgres database"
echo "    - Same connection pool"
echo "    - Same thread pool"
echo ""
echo "  Heavy reads starve writes for resources!"
echo "  This is the problem CQRS solves."
echo "============================================"
