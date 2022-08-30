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
@Entity
public class SportAuto extends Vehicle {

    private String bodyType;
    private int maxSpeed;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    public SportAuto(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportAuto sportAuto = (SportAuto) o;
        return maxSpeed == sportAuto.maxSpeed && Objects.equals(bodyType, sportAuto.bodyType) && id.equals(sportAuto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyType, maxSpeed, id);
    }
}
