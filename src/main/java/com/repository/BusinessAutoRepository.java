package com.repository;

import com.model.Auto;
import com.model.BusinessAuto;

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

    public boolean create(BusinessAuto businessAuto) {
        businessAutos.add(businessAuto);
        return true;
    }

    public boolean create(List<BusinessAuto> businessAuto) {
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
