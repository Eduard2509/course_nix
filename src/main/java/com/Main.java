package com;

import com.model.Auto;
import com.model.BusinessAuto;
import com.model.Manufacturer;
import com.model.SportAuto;
import com.repository.SportAutoRepository;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;

import java.util.List;

public class Main {
    private static final AutoService AUTO_SERVICE = new AutoService();
    private static final SportAutoService SPORT_AUTO_SERVICE = new SportAutoService();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = new BusinessAutoService();

    public static void main(String[] args) {
        final List<Auto> autos = AUTO_SERVICE.createAutos(10);
        AUTO_SERVICE.saveAutos(autos);
        AUTO_SERVICE.printAll();
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println();

        final List<SportAuto> autos1 = SPORT_AUTO_SERVICE.createSportAutos(5);
        SPORT_AUTO_SERVICE.saveSportAuto(autos1);
        SPORT_AUTO_SERVICE.printAll();
        System.out.println();
        SportAutoRepository sportAutoRepository = new SportAutoRepository();




        final List<BusinessAuto> autos2 = BUSINESS_AUTO_SERVICE.createBusinessAutos(5);
        BUSINESS_AUTO_SERVICE.saveBusinessAutos(autos2);
        BUSINESS_AUTO_SERVICE.printAll();
        System.out.println();

        BUSINESS_AUTO_SERVICE.printAll();
    }
}