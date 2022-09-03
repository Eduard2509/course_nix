package com.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Auto extends Vehicle {
    private String bodyType;

    @Transient
    private List<String> details;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "engine_ID")
    private Engine engine;
    private String currency;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    public Auto() {
    }

    public Auto(String id, String model, Manufacturer manufacturer, BigDecimal price,
                String bodyType, int count, List<String> details, Engine engine, String currency, LocalDateTime created) {
        super(model, manufacturer, price, count, VehicleType.AUTO);
        this.id = id;
        this.bodyType = bodyType;
        this.details = details;
        this.engine = engine;
        this.currency = currency;
        this.created = created;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Auto{");
        sb.append("bodyType='").append(bodyType).append('\'');
        sb.append(", details=").append(details);
        sb.append(", engine=").append(engine);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", price=").append(price);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", restyling='").append(restyling).append('\'');
        sb.append(", created='").append(created).append('\'');
        sb.append(", count=").append(count);
        sb.append(", vehicleType=").append(vehicleType);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auto auto = (Auto) o;
        return Objects.equals(bodyType, auto.bodyType) && Objects.equals(details, auto.details) && Objects.equals(engine, auto.engine) && Objects.equals(currency, auto.currency) && id.equals(auto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyType, details, engine, currency, id);
    }
}
