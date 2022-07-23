package com;

import com.model.Auto;
import com.model.Manufacturer;
import com.model.Vehicle;
import com.model.garage.GarageLinkedList;
import com.repository.AutoRepository;
import com.service.AutoService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {

    private static final AutoService AUTO_SERVICE = new AutoService(new AutoRepository());

    public static void main(String[] args) {


        Random random = new Random();

        GarageLinkedList garageLinkedList = new GarageLinkedList();
        Auto autoFrom = new Auto("Model-" + random.nextInt(1000),
                Manufacturer.ZAZ,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1);

        Auto auto = new Auto("Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1);

        Auto auto1 = new Auto("Model" + random.nextInt(100),
                Manufacturer.KIA,
                BigDecimal.valueOf(random.nextDouble(10.0)),
                "Model-" + random.nextInt(10), 1);

        garageLinkedList.add(auto1);
        garageLinkedList.add(auto);
        garageLinkedList.addHead(auto1);
        garageLinkedList.printAll();
        System.out.println("--------------");
        System.out.println(garageLinkedList.getVehicleByRestyling(auto.getRestyling()));
        System.out.println("--------------");
        garageLinkedList.forEach();
        System.out.println("---------------");
        garageLinkedList.deleteVehicleByRestyling(auto1.getRestyling());
        System.out.println("Print after delete");
        garageLinkedList.printAll();
        System.out.println(garageLinkedList.sumRestyling(auto1));
        System.out.println("--------");
        garageLinkedList.update(auto, autoFrom);
        garageLinkedList.printAll();
        System.out.println(garageLinkedList.getDateFirstRestyling());
        System.out.println(garageLinkedList.getDateLastRestyling());

        System.out.println("Use compartors: ");

        final List<Auto> autos = AUTO_SERVICE.createAndSave(5);

        final Comparator<Vehicle> comparator = Comparator.comparing(Vehicle::getPrice)
                .reversed()
                .thenComparing(vehicle -> vehicle.getClass().getSimpleName())
                .thenComparing(Vehicle::getCount);

        autos.sort(comparator);
        for (Auto vehicle : autos) {
            System.out.println(vehicle);
        }

    }

//        ProgramRun programRun = new ProgramRun();
//        programRun.go();
}