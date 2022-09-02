package com.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private double volume;
    private String brand;

    public Engine() {
    }

    public Engine(double volume, String brand) {
        this.volume = volume;
        this.brand = brand;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Engine{");
        sb.append("volume=").append(volume);
        sb.append(", brand='").append(brand).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @OneToOne(mappedBy = "engine")
    private Auto auto;
}
