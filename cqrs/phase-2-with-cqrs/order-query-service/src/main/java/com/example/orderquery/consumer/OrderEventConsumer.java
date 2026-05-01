package com.example.orderquery.consumer;

import com.example.orderquery.entity.OrderReadModel;
import com.example.orderquery.event.OrderEvent;
import com.example.orderquery.repository.OrderReadModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final OrderReadModelRepository repository;

    @KafkaListener(topics = "order-events", groupId = "order-query-service")
    public void handleOrderEvent(OrderEvent event) {
        log.info("[KAFKA-CONSUMER] Received {} for order #{}", event.getEventType(), event.getOrderId());
        switch (event.getEventType()) {
            case "ORDER_CREATED" -> {
                OrderReadModel model = OrderReadModel.builder()
                        .id(event.getOrderId())
                        .customerId(event.getCustomerId())
                        .customerName(event.getCustomerName())
                        .itemName(event.getItemName())
                        .quantity(event.getQuantity())
                        .price(event.getPrice())
                        .status(event.getStatus())
                        .createdAt(event.getCreatedAt())
                        .build();
                repository.save(model);
                log.info("Order #{} saved to Cassandra read store", event.getOrderId());
            }
            case "ORDER_STATUS_UPDATED" -> {
                repository.findById(event.getOrderId()).ifPresentOrElse(
                        model -> {
                            model.setStatus(event.getStatus());
                            repository.save(model);
                            log.info("Order #{} status updated to {} in Cassandra", event.getOrderId(), event.getStatus());
                        },
                        () -> log.warn("Order #{} not found in read store", event.getOrderId())
                );
            }
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}
