package com.builder;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.model.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AutoBuilder implements Builder {
    private String id;
    private Engine engine;
    private VehicleType type;
    private Manufacturer manufacturer;
    private List<String> details;
    private String currency;
    private String model;
    private int count;
    private BigDecimal price;
    private String bodyType;
    private LocalDateTime created;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setVehicleType(VehicleType type) {
        this.type = type;
    }

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public void setCount(int count) {
        if (count > 0) {
            this.count = count;
        } else {
            throw new IllegalArgumentException("Count don't must be equal to 0");
        }
    }

    @Override
    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price don't must be equal to 0");
        }
    }

    @Override
    public void setBodyType(String bodyType) {
        if (bodyType.length() < 20) {
            this.bodyType = bodyType;
        } else {
            throw new IllegalArgumentException("Body type should be less than 20 characters");
        }
    }

    @Override
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public Auto getAuto() {
        Objects.requireNonNull(price);
        Objects.requireNonNull(id);
        return new Auto(model, manufacturer, price,
                bodyType, count, details, engine, currency, created);
    }

}
