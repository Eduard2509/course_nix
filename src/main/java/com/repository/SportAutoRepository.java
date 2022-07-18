package com.repository;


import com.model.SportAuto;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SportAutoRepository {

    private final List<SportAuto> sportAutos;

    public SportAutoRepository() {
        sportAutos = new LinkedList<>();
    }

    public SportAuto getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        for (SportAuto sportAuto : sportAutos) {
            if (sportAuto.getId().equals(id)) {
                return sportAuto;
            }

        }
        return null;
    }

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

    public List<SportAuto> getAll() {
        return sportAutos;
    }

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

    public boolean saveAll(List<SportAuto> sportAuto) {
        if (sportAuto == null) {
            return false;
        }
        return sportAutos.addAll(sportAuto);
    }

    public boolean update(SportAuto sportAuto) {
        final SportAuto founded = getById(sportAuto.getId());
        if (founded != null) {
            SportAutoCopy.copy(sportAuto, founded);
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
