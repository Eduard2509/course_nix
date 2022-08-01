package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class Auto extends Vehicle {
    private String bodyType;
    private List<String> details;

    public Auto(String model, Manufacturer manufacturer, BigDecimal price, String bodyType, int count, List<String> details) {
        super(model, manufacturer, price, count, VehicleType.AUTO);
        this.bodyType = bodyType;
        this.details = details;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Auto{");
        sb.append("bodyType='").append(bodyType).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", price=").append(price);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", restyling='").append(restyling).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
