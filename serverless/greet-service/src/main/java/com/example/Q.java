package com.example;

import java.util.List;

class Apple {
    private int weight;
    private String color;

    public Apple(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }
}

public class Q {

    public static void main(String[] args) {

        List<Apple> inventory = List.of(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        inventory.stream()
                .filter(a -> a.getColor().equals("green"))
                .forEach(System.out::println);

    }
    // imperative style
    // intention + implementation mixed

    // declarative style
    // intention + implementation separated
    // with functional programming

}
