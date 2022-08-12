package com.builder;

import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.model.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface Builder {
    void setVehicleType(VehicleType type);

    void setEngine(Engine engine);

    void setManufacturer(Manufacturer manufacturer);

    void setDetails(List<String> details);

    void setCurrency(String currency);

    void setModel(String model);

    void setCount(int count);

    void setPrice(BigDecimal price);

    void setBodyType(String bodyType);

    void setCreated(LocalDateTime created);

    void setId(String id);

    Auto getAuto();
}
