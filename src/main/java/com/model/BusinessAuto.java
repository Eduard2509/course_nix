package com.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class BusinessAuto extends Vehicle {

    private BusinessClassAuto businessClassAuto;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    public BusinessAuto(){}

    public BusinessAuto(String id,
                        String model,
                        Manufacturer manufacturer,
                        BigDecimal price,
                        BusinessClassAuto businessClassAuto,
                        int count) {
        super(model, manufacturer, price, count, VehicleType.BUSINESS_AUTO);
        this.businessClassAuto = businessClassAuto;
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessAuto that = (BusinessAuto) o;
        return businessClassAuto == that.businessClassAuto && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessClassAuto, id);
    }
}

