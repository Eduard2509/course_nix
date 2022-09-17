package com.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private LocalDateTime created;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Vehicle> vehicles;
    private BigDecimal price;

    public Invoice() {
    }

    public Invoice(String id, LocalDateTime created, Set<Vehicle> vehicles, BigDecimal price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return id.equals(invoice.id) && Objects.equals(created, invoice.created) && Objects.equals(vehicles, invoice.vehicles) && Objects.equals(price, invoice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, vehicles, price);
    }
}
