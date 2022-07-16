package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SportAuto extends Auto {

    private int maxSpeed;

    public SportAuto(String model, Manufacturer manufacturer, BigDecimal price, String bodyType, int maxSpeed) {
        super(model, manufacturer, price, bodyType);
        this.maxSpeed = maxSpeed;
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
