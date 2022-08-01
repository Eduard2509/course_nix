package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BusinessAuto extends Vehicle {

    private BusinessClassAuto businessClassAuto;

    public BusinessAuto(String model,
                        Manufacturer manufacturer,
                        BigDecimal price,
                        BusinessClassAuto businessClassAuto,
                        int count) {
        super(model, manufacturer, price, count, VehicleType.BUSINESSAUTO);
        this.businessClassAuto = businessClassAuto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BusinessAuto{");
        sb.append("businessClassAuto=").append(businessClassAuto);
        sb.append(", id='").append(id).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", price=").append(price);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append('}');
        return sb.toString();
    }
}

