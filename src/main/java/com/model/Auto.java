package com.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Auto extends Vehicle {
    private String bodyType;
    private List<String> details;
    private Engine engine;
    private String currency;
    private String id;

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
}
