package com.discountUtil;

import com.model.Vehicle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class DiscountUtil<T extends Vehicle> {

    private static final Random RANDOM = new Random();
    private T vehicle;

    public DiscountUtil(T vehicle) {
        this.vehicle = vehicle;
    }

    public void printAll(List<T> vehicles) {
        for (T vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    public void printVehicle(T vehicle) {
        System.out.println(vehicle);
    }

    public BigDecimal getDiscount(T vehicle) {
        BigDecimal price = vehicle.getPrice();
        BigDecimal discount = randomDiscount(price);
        System.out.println("You get discount: " + price.subtract(discount));
        System.out.println("Price with discount: " + discount);
        vehicle.setPrice(discount);
        return discount;
    }

    private static BigDecimal randomDiscount(BigDecimal price) {
        BigDecimal randomDiscount = price
                .subtract(price
                        .multiply(BigDecimal.valueOf(RANDOM.nextInt(10, 30))
                                .divide(BigDecimal.valueOf(100))));
        return randomDiscount;
    }

    public <O extends Number> BigDecimal upPrice(O price) {

        if ((price.getClass().equals(Integer.class))) {
            vehicle.setPrice(vehicle.getPrice().add(BigDecimal.valueOf((Integer) price)));
            System.out.println(vehicle);
        } else {
            vehicle.setPrice(vehicle.getPrice().add(BigDecimal.valueOf((Double) price)));
            System.out.println(vehicle);
        }
        return vehicle.getPrice();
    }

}
