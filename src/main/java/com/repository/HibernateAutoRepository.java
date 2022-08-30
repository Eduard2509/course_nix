package com.repository;

import com.model.Auto;
import com.model.Vehicle;

import java.util.List;
import java.util.Optional;

public class HibernateAutoRepository implements CrudRepository<Auto> {
    
    @Override
    public Optional findById(String id) {
        return Optional.empty();
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public boolean save(Auto auto) {
        return false;
    }

    @Override
    public boolean update(Auto auto) {
        return false;
    }


    @Override
    public boolean saveAll(List auto) {
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
