package com;

import com.command.Action;
import com.command.Command;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Vehicle;
import com.repository.AutoRepository;
import com.repository.CrudRepository;
import com.service.AutoService;
import com.tree.Tree;
import com.util.UserInputUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {


    public static void main(String[] args) {

        final AutoService AUTO_SERVICE = AutoService.getInstance();

        Random random = new Random();

        List<String> details = new ArrayList<>();
        details.add("door");
        details.add("Wildshield");
        details.add("Wheel");
        details.add("steering wheel");


        Auto auto = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1, details
        );

        Auto auto1 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.KIA,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1, details
        );

        Auto auto2 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.KIA,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1, details
        );

        Auto auto3 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1, details
        );

        Auto auto4 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.OPEL,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1, details
        );

        List<Auto> list = new ArrayList<>();
        list.add(auto);
        list.add(auto1);
        list.add(auto2);
        list.add(auto3);
        list.add(auto4);

        System.out.println(auto.getDetails());
        System.out.println(AUTO_SERVICE.checkDetailAuto(list, "Wheel"));
        AUTO_SERVICE.getRichAuto(list, BigDecimal.valueOf(250));
        System.out.println(AUTO_SERVICE.sumVehicle(list));
        AUTO_SERVICE.sortedAuto(list);
        AUTO_SERVICE.getPriceStatistics(list);


//        final Action[] actions = Action.values();
//        final List<String> names = getNames(actions);
//        Command command;
//        do {
//            command = executeCommand(actions, names);
//        } while (command != null);
//    }
//
//    private static Command executeCommand(Action[] actions, List<String> names) {
//        int userInput = UserInputUtil.getUserInput("What you want:", names);
//        final Action action = actions[userInput];
//        return action.execute();
//    }
//
//    private static List<String> getNames(Action[] actions) {
//        final List<String> names = new ArrayList<>(actions.length);
//        for (Action action : actions) {
//            names.add(action.getName());
//        }
//        return names;
    }

}
