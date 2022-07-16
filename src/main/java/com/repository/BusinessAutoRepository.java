package com.repository;

import com.model.Auto;
import com.model.BusinessAuto;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BusinessAutoRepository {

    private final List<BusinessAuto> businessAutos;

    public BusinessAutoRepository() {
        businessAutos = new LinkedList<>();
    }

    public BusinessAuto getById(String id) {
        for (BusinessAuto businessAuto : businessAutos) {
            if (businessAuto.getId().equals(id)) {
                return businessAuto;
            }
        }
        return null;
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
        final BusinessAuto founded = getById(businessAuto.getId());
        if (founded != null) {
            BusinessAutoCopy.copy(businessAuto, founded);
            return true;
        }
        return false;
    }

    public boolean updateByBodyType(String bodyType, BusinessAuto copyFrom) {
        for (BusinessAuto auto : businessAutos) {
            if (auto.getBodyType().equals(bodyType)) {
                BusinessAutoRepository.BusinessAutoCopy.copy(copyFrom, auto);
            }
        }
        return true;
    }


    public boolean delete(String id) {
        final Iterator<BusinessAuto> iterator = businessAutos.iterator();
        while (iterator.hasNext()) {
            final BusinessAuto businessAuto = iterator.next();
            if(businessAuto.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    private static class BusinessAutoCopy {
        static void copy(final BusinessAuto from, final BusinessAuto to) {
            to.setManufacturer(from.getManufacturer());
            to.setBusinessClassAuto(from.getBusinessClassAuto());
            to.setBodyType(from.getBodyType());
            to.setModel(from.getModel());
            to.setPrice(from.getPrice());
        }
    }
}
