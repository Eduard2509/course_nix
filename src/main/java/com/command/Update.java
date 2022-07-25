package com.command;

import com.model.VehicleType;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;
import com.util.UserInputUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Update implements Command {

    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final SportAutoService SPORT_AUTO_SERVICE = SportAutoService.getInstance();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = BusinessAutoService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final List<String> names = getNames(values);
        final int userInput = UserInputUtil.getUserInput("What you want to create:", names);
        final VehicleType value = values[userInput];

        switch (value) {
            case BUSINESSAUTO -> {
                System.out.println("Please enter auto's id");
                String idAuto = SCANNER.nextLine();
                System.out.println("Please enter price: ");
                BigDecimal priceAuto = new BigDecimal(SCANNER.nextLine());
                BUSINESS_AUTO_SERVICE.updateVehicleByPrice(idAuto, priceAuto);
            }
            case AUTO -> {
                System.out.println("Please enter auto's id");
                String idAuto = SCANNER.nextLine();
                System.out.println("Please enter price: ");
                BigDecimal priceAuto = new BigDecimal(SCANNER.nextLine());
                AUTO_SERVICE.updateVehicleByPrice(idAuto, priceAuto);
            }
            case SPORTAUTO -> {
                System.out.println("Please enter auto's id");
                String idSportAuto = SCANNER.nextLine();
                System.out.println("Please enter price: ");
                BigDecimal priceSportAuto = new BigDecimal(SCANNER.nextLine());
                SPORT_AUTO_SERVICE.updateVehicleByPrice(idSportAuto, priceSportAuto);
            }
            default -> throw new IllegalArgumentException("Cannot build " + value);
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
