package com.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Engine {
    private String id;
    private double volume;
    private String brand;

    public Engine(double volume, String brand) {
        this.id = UUID.randomUUID().toString();
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
}
