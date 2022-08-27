package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SportAuto extends Vehicle {

    private String bodyType;
    private int maxSpeed;
    private String id;

    public SportAuto(String id, String model, Manufacturer manufacturer,
                     BigDecimal price, String bodyType, int maxSpeed, int count) {
        super(model, manufacturer, price, count, VehicleType.SPORT_AUTO);
        this.id = id;
        this.maxSpeed = maxSpeed;
        this.bodyType = bodyType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SportAuto{");
        sb.append("maxSpeed=").append(maxSpeed);
        sb.append(", id='").append(id).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", price=").append(price);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append('}');
        return sb.toString();
    }
}
