package com.command;

import com.model.VehicleType;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;
import com.util.UserInputUtil;

import java.util.ArrayList;
import java.util.List;

public class Print implements Command {

    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final SportAutoService SPORT_AUTO_SERVICE = SportAutoService.getInstance();
    private static final BusinessAutoService BUSINESS_AUTO_SERVICE = BusinessAutoService.getInstance();

    @Override
    public void execute() {
        final VehicleType[] values = VehicleType.values();
        final List<String> names = getNames(values);
        final int userInput = UserInputUtil.getUserInput("What you want to print:", names);
        final VehicleType value = values[userInput];

        switch (value) {
            case AUTO -> AUTO_SERVICE.printAll();
            case BUSINESSAUTO -> BUSINESS_AUTO_SERVICE.printAll();
            case SPORTAUTO -> SPORT_AUTO_SERVICE.printAll();
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
