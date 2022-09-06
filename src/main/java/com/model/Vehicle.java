package com.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vehicle {
    @Id
    protected  String id;
    protected String model;
    protected BigDecimal price;
    @Enumerated(EnumType.STRING)
    protected Manufacturer manufacturer;
    protected String restyling;
    protected LocalDateTime created;
    protected int count;
//    protected String invoiceId;
    @Enumerated(EnumType.STRING)
    protected VehicleType vehicleType;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;


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
