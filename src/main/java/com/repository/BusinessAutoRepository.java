package com.repository;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.model.BusinessAuto;
import com.model.BusinessClassAuto;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Singleton
public class BusinessAutoRepository implements CrudRepository<BusinessAuto> {

    private final List<BusinessAuto> businessAutos;

    private static BusinessAutoRepository instance;

    public BusinessAutoRepository() {
        businessAutos = new LinkedList<>();
    }

    public static BusinessAutoRepository getInstance() {
        if (instance == null) {
            instance = new BusinessAutoRepository();
        }
        return instance;
    }


    public List<BusinessAuto> getAll() {
        return businessAutos;
    }

    public boolean save(BusinessAuto businessAuto) {
        if (businessAuto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        if (businessAuto.getPrice().equals(BigDecimal.ZERO)) {
            businessAuto.setPrice(BigDecimal.valueOf(-1));
        }
        businessAutos.add(businessAuto);
        return true;
    }

    public boolean saveAll(List<BusinessAuto> businessAuto) {
        if (businessAuto == null) {
            return false;
        }
        return businessAutos.addAll(businessAuto);
    }

    public boolean update(BusinessAuto businessAuto) {
        final Optional<BusinessAuto> optionalBusinessAuto = findById(businessAuto.getId());
        if (optionalBusinessAuto.isPresent()) {
            optionalBusinessAuto.ifPresent(founded -> BusinessAutoCopy.copy(businessAuto, founded));
            return true;
        }
        return false;
    }


    public boolean delete(String id) {
        final Iterator<BusinessAuto> iterator = businessAutos.iterator();
        while (iterator.hasNext()) {
            final BusinessAuto businessAuto = iterator.next();
            if (businessAuto.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<BusinessAuto> findByBusinessClass(BusinessClassAuto businessClassAuto) {
        for (BusinessAuto businessAuto : businessAutos) {
            if (businessAuto.getBusinessClassAuto().equals(businessClassAuto)) {
                return Optional.of(businessAuto);
            }
        }
        return Optional.empty();
    }

    public Optional<BusinessAuto> findById(String id) {
        for (BusinessAuto businessAuto : businessAutos) {
            if (businessAuto.getId().equals(id)) {
                return Optional.of(businessAuto);
            }
        }
        return Optional.empty();
    }


    private static class BusinessAutoCopy {
        static void copy(final BusinessAuto from, final BusinessAuto to) {
            to.setManufacturer(from.getManufacturer());
            to.setBusinessClassAuto(from.getBusinessClassAuto());
            to.setModel(from.getModel());
            to.setPrice(from.getPrice());
        }
    }
}