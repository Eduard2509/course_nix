package com.service;

import com.model.Auto;
import com.model.BusinessAuto;
import com.model.SportAuto;
import com.repository.AutoRepository;
import com.repository.BusinessAutoRepository;
import com.repository.SportAutoRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

public class ProgramRun {
    private static final AutoService AUTO_SERVICE = new AutoService(new AutoRepository());
    private static final SportAutoService SPORT_AUTO_SERVICE = new SportAutoService(new SportAutoRepository());
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = new BusinessAutoService(new BusinessAutoRepository());

    public void go() {
        System.out.println("Welcome in my project");
        System.out.println();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                System.out.println();
                navigation();
                caseLogic(reader);
            }
        } catch (IOException e) {
            System.out.println("error: " + e);
        }
    }

    private static void caseLogic(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        switch (line) {
            case "1" -> {
                final List<Auto> autos = AUTO_SERVICE.createAndSave(5);
                AUTO_SERVICE.saveAllVehicles(autos);
                AUTO_SERVICE.printAll();
            }
            case "2" -> {
                final List<BusinessAuto> autos1 = BUSINESS_AUTO_SERVICE.createAndSave(5);
                BUSINESS_AUTO_SERVICE.saveAllVehicles(autos1);
                BUSINESS_AUTO_SERVICE.printAll();
            }
            case "3" -> {
                final List<SportAuto> autos2 = SPORT_AUTO_SERVICE.createAndSave(5);
                SPORT_AUTO_SERVICE.saveAllVehicles(autos2);
                SPORT_AUTO_SERVICE.printAll();
            }
            case "4" -> {
                System.out.println("Please enter auto's id");
                String idAuto = reader.readLine();
                System.out.println("Please enter price: ");
                BigDecimal priceAuto = new BigDecimal(reader.readLine());
                AUTO_SERVICE.updateVehicleByPrice(idAuto, priceAuto);
                AUTO_SERVICE.printAll();
            }

            case "5" -> {
                System.out.println("Please enter auto's id");
                String idBusinessAuto = reader.readLine();
                System.out.println("Please enter price: ");
                BigDecimal priceBusinessAuto = new BigDecimal(reader.readLine());
                BUSINESS_AUTO_SERVICE.updateVehicleByPrice(idBusinessAuto, priceBusinessAuto);
                BUSINESS_AUTO_SERVICE.printAll();
            }

            case "6" -> {
                System.out.println("Please enter auto's id");
                String idSportAuto = reader.readLine();
                System.out.println("Please enter price: ");
                BigDecimal priceSportAuto = new BigDecimal(reader.readLine());
                SPORT_AUTO_SERVICE.updateVehicleByPrice(idSportAuto, priceSportAuto);
                SPORT_AUTO_SERVICE.printAll();
            }

            case "7" -> {
                System.out.println("Please enter car's id");
                String idCar = reader.readLine();
                AUTO_SERVICE.delete(idCar);
                AUTO_SERVICE.printAll();
            }
            case "8" -> {
                System.out.println("Please enter car's id");
                String idCar = reader.readLine();
                BUSINESS_AUTO_SERVICE.delete(idCar);
                BUSINESS_AUTO_SERVICE.printAll();
            }
            case "9" -> {
                System.out.println("Please enter car's id");
                String idCar = reader.readLine();
                SPORT_AUTO_SERVICE.delete(idCar);
                SPORT_AUTO_SERVICE.printAll();
            }
            case "0" -> System.exit(0);
        }
    }

    private static void navigation() {
        System.out.println(" 1 -> Create auto");
        System.out.println(" 2 -> Create Business Autos");
        System.out.println(" 3 -> Create Sport Autos");
        System.out.println(" 4 -> Update auto");
        System.out.println(" 5 -> Update Business Auto");
        System.out.println(" 6 -> Update Sport Auto");
        System.out.println(" 7 -> Delete auto");
        System.out.println(" 8 -> Delete Business Auto");
        System.out.println(" 9 -> Delete Sport Auto");
        System.out.println(" 0 -> Exit with program");
    }

}
