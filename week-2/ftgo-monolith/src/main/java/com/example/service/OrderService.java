package com.example.service;

import com.example.entity.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final FoodOrderRepository foodOrderRepository;
    private final ConsumerRepository consumerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final PaymentService paymentService;
    private final BillingService billingService;
    private final NotificationService notificationService;
    private final DeliveryService deliveryService;

    public FoodOrder placeOrder(Long consumerId, Long restaurantId, List<Long> menuItemIds, List<Integer> quantities,
            String deliveryAddress) {

        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        FoodOrder order = FoodOrder.builder()
                .consumer(consumer)
                .restaurant(restaurant)
                .status(OrderStatus.PLACED)
                .orderTime(LocalDateTime.now())
                .deliveryAddress(deliveryAddress)
                .lineItems(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < menuItemIds.size(); i++) {
            MenuItem menuItem = menuItemRepository.findById(menuItemIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            int qty = quantities.get(i);
            BigDecimal lineTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);

            OrderLineItem lineItem = OrderLineItem.builder()
                    .foodOrder(order)
                    .menuItem(menuItem)
                    .quantity(qty)
                    .price(lineTotal)
                    .build();
            order.getLineItems().add(lineItem);
        }
        order.setTotalAmount(total);

        FoodOrder savedOrder = foodOrderRepository.save(order);

        // Process payment
        paymentService.processPayment(savedOrder);

        // Generate bill
        billingService.generateBill(savedOrder);

        // Create delivery
        deliveryService.createDelivery(savedOrder);

        // Send notifications
        notificationService.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    public List<FoodOrder> getOrdersByConsumer(Long consumerId) {
        return foodOrderRepository.findByConsumerId(consumerId);
    }

    public List<FoodOrder> getOrdersByRestaurant(Long restaurantId) {
        return foodOrderRepository.findByRestaurantId(restaurantId);
    }

    public FoodOrder getOrder(Long orderId) {
        return foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    public FoodOrder updateOrderStatus(Long orderId, OrderStatus status) {
        FoodOrder order = getOrder(orderId);
        order.setStatus(status);
        FoodOrder saved = foodOrderRepository.save(order);
        notificationService.sendOrderStatusUpdate(saved);
        return saved;
    }
}
