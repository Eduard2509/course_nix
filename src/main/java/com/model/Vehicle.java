package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class Vehicle {
    protected String id;
    protected String model;
    protected BigDecimal price;
    protected Manufacturer manufacturer;
    protected String restyling;
    protected LocalDateTime created;
    protected int count;
    protected VehicleType vehicleType;


    protected Vehicle(String model, Manufacturer manufacturer, BigDecimal price, int count, VehicleType vehicleType) {
        this.id = UUID.randomUUID().toString();
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.count = count;
        this.restyling = UUID.randomUUID().toString();
        this.created = LocalDateTime.now();
        this.vehicleType = vehicleType;
    }

    protected Vehicle() {
    }
}
