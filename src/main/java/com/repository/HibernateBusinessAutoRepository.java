package com.repository;

import com.model.BusinessAuto;

import java.util.List;
import java.util.Optional;

public class HibernateBusinessAutoRepository implements CrudRepository<BusinessAuto>{
    @Override
    public Optional<BusinessAuto> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<BusinessAuto> getAll() {
        return null;
    }

    @Override
    public boolean save(BusinessAuto auto) {
        return false;
    }

    @Override
    public boolean saveAll(List<BusinessAuto> auto) {
        return false;
    }

    @Override
    public boolean update(BusinessAuto auto) {
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
