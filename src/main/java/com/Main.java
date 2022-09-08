package com;

import com.command.Action;
import com.command.Command;
import com.config.MongoConfig;
import com.model.Auto;
import com.model.BusinessAuto;
import com.model.SportAuto;
import com.mongodb.client.MongoDatabase;
import com.repository.MongoSportAutoRepository;
import com.service.AutoService;
import com.service.BusinessAutoService;
import com.service.SportAutoService;
import com.util.UserInputUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        MongoConfig mongoConfig = new MongoConfig();
//        MongoDatabase database = mongoConfig.connect("course_nix");
//        database.drop();
//        AutoService autoService = AutoService.getInstance();
//        List<Auto> andSave = autoService.createAndSave(5);
//        System.out.println("Print All:");
//        autoService.printAll();
//        System.out.println();
//        System.out.println("Update");
//        andSave.get(1).setPrice(BigDecimal.valueOf(8000));
//        autoService.update(andSave.get(1));
//        System.out.println();
//        System.out.println("Find sport auto");
//        System.out.println(autoService.findOneById(andSave.get(2).getId()));
//        System.out.println();
//        System.out.println("Delete sport auto");
//        autoService.delete(andSave.get(0).getId());
//        autoService.printAll();
//        MongoConfig mongoConfig = new MongoConfig();
//        MongoDatabase database = mongoConfig.connect("course_nix");
//        database.drop();
//        SportAutoService sportAutoService = SportAutoService.getInstance();
//        List<SportAuto> andSave = sportAutoService.createAndSave(5);
//        System.out.println("Print All:");
//        sportAutoService.printAll();
//        System.out.println();
//        System.out.println("Update");
//        andSave.get(1).setPrice(BigDecimal.valueOf(8000));
//        sportAutoService.update(andSave.get(1));
//        System.out.println();
//        System.out.println("Find sport auto");
//        System.out.println(sportAutoService.findSportAuto(andSave.get(2).getId()));
//        System.out.println();
//        System.out.println("Delete sport auto");
//        sportAutoService.delete(andSave.get(0).getId());
//        sportAutoService.printAll();
//        MongoConfig mongoConfig = new MongoConfig();
//        MongoDatabase database = mongoConfig.connect("course_nix");
//        database.drop();
//        BusinessAutoService businessAutoService = BusinessAutoService.getInstance();
//        List<BusinessAuto> andSave = businessAutoService.createAndSave(5);
//        businessAutoService.printAll();
//        System.out.println();
//        andSave.get(1).setPrice(BigDecimal.valueOf(8000));
//        businessAutoService.update(andSave.get(1));
//        System.out.println();
//        businessAutoService.findBusinessAuto(andSave.get(0).getId());
//        System.out.println();
//        businessAutoService.delete(andSave.get(0).getId());
//        businessAutoService.printAll();
//    }
        final Action[] actions = Action.values();
        final List<String> names = getNames(actions);
        Command command;
        do {
            command = executeCommand(actions, names);
        } while (command != null);
    }

    private static Command executeCommand(Action[] actions, List<String> names) {
        int userInput = UserInputUtil.getUserInput("What you want:", names);
        final Action action = actions[userInput];
        return action.execute();
    }

    private static List<String> getNames(Action[] actions) {
        final List<String> names = new ArrayList<>(actions.length);
        for (Action action : actions) {
            names.add(action.getName());
        }
        return names;
    }
}
