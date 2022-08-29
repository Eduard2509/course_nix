package com.service;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.Auto;
import com.model.Engine;
import com.model.Manufacturer;
import com.repository.AutoRepository;
import com.repository.CrudRepository;
import com.repository.DBAutoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Singleton
public class AutoService extends VehicleService<Auto> {

    private static AutoService instance;

    @Autowired
    public AutoService(CrudRepository<Auto> repository) {
        super(repository);
    }

    public static AutoService getInstance() {
        if (instance == null) {
            instance = new AutoService(DBAutoRepository.getInstance());
        }
        return instance;
    }

    @Override
    protected Auto creat() {
        Engine engine = new Engine(3.3, "Sport");
        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");
        return new Auto(UUID.randomUUID().toString(),
                "Model-" + RANDOM.nextInt(1000),
                getRandomManufacturer(),
                BigDecimal.valueOf(RANDOM.nextDouble(1000.0)),
                "Model-" + RANDOM.nextInt(1000),
                1, details, engine, "$", LocalDateTime.now());
    }

    public void findAutoPrice(String id) {
        Auto aThrow = repository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Auto with id " + id + " is missing"));
        System.out.println(aThrow.getPrice());
    }

    public Auto findAutoBodyType(String id) {
        AtomicReference<Auto> atomicReference = new AtomicReference<>();
        repository.findById(id).ifPresent(auto -> {
            System.out.println(auto.getBodyType());
            atomicReference.set(auto);
        });
        return atomicReference.get();
    }

    public Auto createAutoWithoutId(String id) {
        Auto auto = repository.findById(id).orElseGet(() -> creat());
        System.out.println(auto.getId());
        return auto;
    }


    public boolean checkDetailAuto(List<Auto> autos, String detail) {
        return autos.stream()
                .flatMap(auto -> auto.getDetails().stream())
                .anyMatch(auto -> auto.equals(detail));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AutoService{");
        sb.append("createVehicleByMap=").append(createVehicleByMap);
        sb.append('}');
        return sb.toString();
    }

    public Function<Map<String, Object>, Auto> createVehicleByMap = stringObjectMap -> {

        Engine engine = new Engine(Integer.parseInt(stringObjectMap.getOrDefault("volume", 0).toString()),
                (String) stringObjectMap.getOrDefault("brand", "nonBrand"));

        List<String> details = new ArrayList<>();
        details.add("Door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("Steering wheel");

        Object id = stringObjectMap.getOrDefault("id", "id0");
        Object model = stringObjectMap.getOrDefault("model", "Model0");
        Object type = stringObjectMap.getOrDefault("bodyType", "Type0");
        Object manufacturer = stringObjectMap.getOrDefault("manufacturer", Manufacturer.NON);
        Object price = stringObjectMap.getOrDefault("price", BigDecimal.ZERO);
        Object count = stringObjectMap.getOrDefault("count", 0);
        Object details1 = stringObjectMap.getOrDefault("Details", details);
        Object created = stringObjectMap.getOrDefault("created", LocalDateTime.now());
        Object engine1 = stringObjectMap.getOrDefault("engine", engine);
        Object currency = stringObjectMap.getOrDefault("currency", "$");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        return new Auto((String) id,
                (String) model,
                Manufacturer.valueOf(manufacturer.toString()),
                BigDecimal.valueOf(Long.parseLong(price.toString())),
                (String) type,
                Integer.parseInt(count.toString()),
                (List<String>) details1,
                engine,
                (String) currency,
                LocalDateTime.parse(created.toString(), formatter));
    };

}

