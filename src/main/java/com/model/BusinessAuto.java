package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BusinessAuto extends Auto {

    private BusinessClassAuto businessClassAuto;

    public BusinessAuto(String model, Manufacturer manufacturer, BigDecimal price, String bodyType, BusinessClassAuto businessClassAuto) {
        super(model, manufacturer, price, bodyType);
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

