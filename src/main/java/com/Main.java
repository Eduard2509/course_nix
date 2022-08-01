package com;

import com.command.Action;
import com.command.Command;
import com.model.Auto;
import com.model.Manufacturer;
import com.tree.Tree;
import com.util.UserInputUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {


    public static void main(String[] args) {

        Random random = new Random();

        Auto auto = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1
        );

        Auto auto1 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.KIA,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1
        );

        Auto auto2 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.KIA,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1
        );

        Auto auto3 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.BMW,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1
        );

        Auto auto4 = new Auto(
                "Model-" + random.nextInt(1000),
                Manufacturer.OPEL,
                BigDecimal.valueOf(random.nextDouble(1000.0)),
                "Model-" + random.nextInt(1000), 1
        );

        Tree tree = new Tree();
        tree.add(auto1);
        tree.add(auto2);
        tree.add(auto3);
        tree.add(auto4);
        tree.printTree();
        System.out.println(tree.sumLeftBranch());
        System.out.println(tree.sumRightBranch());
        System.out.println();
        System.out.println("------------------- Tree End -------------------");
        System.out.println();

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
