package com.repository;

import com.model.SportAuto;

import java.util.List;
import java.util.Optional;

public class HibernateSportAutoRepository implements CrudRepository<SportAuto>{
    @Override
    public Optional<SportAuto> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<SportAuto> getAll() {
        return null;
    }

    @Override
    public boolean save(SportAuto auto) {
        return false;
    }

    @Override
    public boolean saveAll(List<SportAuto> auto) {
        return false;
    }

    @Override
    public boolean update(SportAuto auto) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public void clear() {

    }
}
