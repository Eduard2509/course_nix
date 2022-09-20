package com;

import com.command.Action;
import com.command.Command;
import com.concurrency.Fabric;
import com.util.UserInputUtil;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        Fabric fabric = new Fabric();
        fabric.execute();


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
