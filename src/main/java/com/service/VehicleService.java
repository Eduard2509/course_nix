package com.service;

import com.model.Manufacturer;
import com.model.Vehicle;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class VehicleService<T extends Vehicle> {

    protected CrudRepository<T> repository;
    protected static final Random RANDOM = new Random();
    protected static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);

    protected VehicleService(CrudRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> createAndSave(int count) {
        List<T> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final T vehicle = creat();
            result.add(vehicle);
            repository.save(vehicle);
            LOGGER.info("Created auto {}", vehicle.getId());
        }
        return result;
    }

    protected abstract T creat();

    protected Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void saveAllVehicles(List<T> vehicle) {
        repository.saveAll(vehicle);
    }

    public void printAll() {
        for (T vehicle : repository.getAll()) {
            System.out.println(vehicle);
        }
    }

    public void update(T vehicle) {
        repository.update(vehicle);
    }

    public void updateVehicleByPrice(String id, BigDecimal price) {
        Optional<T> vehicle = repository.findById(id);
        vehicle.ifPresent(vehicle1 -> {
            vehicle1.setPrice(price);
            repository.update(vehicle1);
        });

    }

    public void delete(String id) {
        repository.delete(id);
        LOGGER.info("Deleted auto {}", id);
    }

    public Optional<T> findOneById(String id) {
        return id == null ? repository.findById("") : repository.findById(id);
    }


    public void getRichAuto(List<T> vehicles, BigDecimal price) {
        System.out.println(vehicles.stream()
                .filter(value -> value.getPrice().compareTo(price) > 0)
                .map(Vehicle::getModel)
                .collect(Collectors.toList()));
    }

    public int sumVehicle(List<T> vehicles) {
        return vehicles.stream()
                .map(Vehicle::getCount)
                .reduce(0, Integer::sum);
    }

    final Comparator<Vehicle> comparator = Comparator.comparing(Vehicle::getManufacturer)
            .reversed();

    public void sortedAuto(List<T> vehicles) {
        System.out.println(vehicles.stream()
                .sorted(comparator)
                .distinct()
                .collect(Collectors.toMap(Vehicle::getId, Vehicle::getModel)));
    }

    public void getPriceStatistics(List<T> vehicles) {
        System.out.println(vehicles.stream()
                .map(Vehicle::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics());
    }


    Predicate<Vehicle> predicate = vehicle -> vehicle.getPrice().compareTo(BigDecimal.ZERO) > 0;


}
