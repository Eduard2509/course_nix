package com.command;

import com.model.VehicleType;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;
import com.util.UserInputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Delete implements Command {

    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final SportAutoService SPORT_AUTO_SERVICE = SportAutoService.getInstance();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = BusinessAutoService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final List<String> names = getNames(values);
        final int userInput = UserInputUtil.getUserInput("What you want to delete:", names);
        final VehicleType value = values[userInput];

        switch (value) {
            case BUSINESS_AUTO -> {
                System.out.println("Please enter car's id");
                String idCar = SCANNER.nextLine();
                BUSINESS_AUTO_SERVICE.delete(idCar);
            }
            case AUTO -> {
                System.out.println("Please enter car's id");
                String idCar = SCANNER.nextLine();
                AUTO_SERVICE.delete(idCar);
            }
            case SPORT_AUTO -> {
                System.out.println("Please enter car's id");
                String idCar = SCANNER.nextLine();
                SPORT_AUTO_SERVICE.delete(idCar);
            }
            default -> throw new IllegalArgumentException("Cannot delete " + value);
        }
    }

    private static List<String> getNames(VehicleType[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (VehicleType type : values) {
            names.add(type.name());
        }
        return names;
    }
}

