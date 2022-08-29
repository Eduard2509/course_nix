package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Invoice {
    private String id;
    private LocalDateTime created;
    private List<Vehicle> vehicles;
    private BigDecimal price;


    public Invoice(String id, LocalDateTime created, List<Vehicle> vehicles, BigDecimal price) {
        this.id = id;
        this.created = created;
        this.vehicles = vehicles;
        this.price = price;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Invoice{");
        sb.append("id='").append(id).append('\'');
        sb.append(", created=").append(created);
        sb.append(", vehicles=").append(vehicles);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}