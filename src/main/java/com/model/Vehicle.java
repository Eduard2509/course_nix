package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@Getter
@Setter
public abstract class Vehicle {
    protected final String id;
    protected String model;
    protected BigDecimal price;
    protected Manufacturer manufacturer;
    protected String restyling;
    protected String time;
    protected int count;

    protected Vehicle(String model, Manufacturer manufacturer, BigDecimal price, int count) {
        this.id = UUID.randomUUID().toString();
        this.model = model;
        this.manufacturer = manufacturer;
        this.price = price;
        this.count = count;
        this.restyling = UUID.randomUUID().toString();
        this.time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
