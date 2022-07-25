package com.repository;


import com.model.SportAuto;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SportAutoRepository implements CrudRepository<SportAuto> {

    private final List<SportAuto> sportAutos;
    private static SportAutoRepository instance;

    public SportAutoRepository() {
        sportAutos = new LinkedList<>();
    }

    public static SportAutoRepository getInstance() {
        if (instance == null) {
            instance = new SportAutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<SportAuto> findById(String id) {
        for (SportAuto sportAuto : sportAutos) {
            if (sportAuto.getId().equals(id)) {
                return Optional.of(sportAuto);
            }
        }
        return Optional.empty();
    }

    public Optional<SportAuto> findByBodyType(String bodyType) {
        for (SportAuto sportAuto : sportAutos) {
            if (sportAuto.getBodyType().equals(bodyType)) {
                return Optional.of(sportAuto);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<SportAuto> getAll() {
        return sportAutos;
    }

    @Override
    public boolean save(SportAuto sportAuto) {
        if (sportAuto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        if (sportAuto.getPrice().equals(BigDecimal.ZERO)) {
            sportAuto.setPrice(BigDecimal.valueOf(-1));
        }
        sportAutos.add(sportAuto);
        return true;
    }

    @Override
    public boolean saveAll(List<SportAuto> sportAuto) {
        if (sportAuto == null) {
            return false;
        }
        return sportAutos.addAll(sportAuto);
    }

    @Override
    public boolean update(SportAuto sportAuto) {
        final Optional<SportAuto> optionalSportAuto = findById(sportAuto.getId());
        if (optionalSportAuto.isPresent()) {
            optionalSportAuto.ifPresent(founded -> SportAutoCopy.copy(sportAuto, founded));
            return true;
        }
        return false;
    }

    public boolean check(SportAuto sportAuto) {
        if (sportAuto.getPrice() == null) {
            sportAuto.setPrice(BigDecimal.ZERO);
        }
        return true;
    }

    public boolean updateByBodyType(String bodyType, SportAuto copyFrom) {
        for (SportAuto auto : sportAutos) {
            if (auto.getBodyType().equals(bodyType)) {
                SportAutoCopy.copy(auto, copyFrom);
            }
        }
        return true;
    }

    @Override
    public boolean delete(String id) {
        Iterator<SportAuto> iterator = sportAutos.iterator();
        while (iterator.hasNext()) {
            final SportAuto sportAuto = iterator.next();
            if (sportAuto.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    private static class SportAutoCopy {
        static void copy(SportAuto to, SportAuto from) {
            to.setMaxSpeed(from.getMaxSpeed());
            to.setBodyType(from.getBodyType());
            to.setManufacturer(from.getManufacturer());
            to.setPrice(from.getPrice());
            to.setModel(from.getModel());
        }
    }
}
