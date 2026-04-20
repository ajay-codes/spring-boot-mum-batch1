package com.example.web;

import com.example.entity.FoodOrder;
import com.example.entity.MenuItem;
import com.example.entity.Restaurant;
import com.example.service.OrderService;
import com.example.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/consumer")
@RequiredArgsConstructor
public class ConsumerController {

    private final RestaurantService restaurantService;
    private final OrderService orderService;

    // Page 1: Browse restaurants and place orders
    @GetMapping("/restaurants")
    public String browseRestaurants(Model model) {
        List<Restaurant> restaurants = restaurantService.getActiveRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "consumer/restaurants";
    }

    @GetMapping("/restaurants/{id}/menu")
    public String viewMenu(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        List<MenuItem> menuItems = restaurantService.getAvailableMenuItems(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItems", menuItems);
        return "consumer/restaurants";
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestParam Long consumerId,
                             @RequestParam Long restaurantId,
                             @RequestParam List<Long> menuItemIds,
                             @RequestParam List<Integer> quantities,
                             @RequestParam String deliveryAddress) {
        orderService.placeOrder(consumerId, restaurantId, menuItemIds, quantities, deliveryAddress);
        return "redirect:/consumer/orders?consumerId=" + consumerId;
    }

    // Page 2: My orders
    @GetMapping("/orders")
    public String myOrders(@RequestParam(defaultValue = "1") Long consumerId, Model model) {
        List<FoodOrder> orders = orderService.getOrdersByConsumer(consumerId);
        model.addAttribute("orders", orders);
        model.addAttribute("consumerId", consumerId);
        return "consumer/orders";
    }
}
