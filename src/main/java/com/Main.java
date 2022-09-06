package com;

import com.command.Action;
import com.command.Command;
import com.util.UserInputUtil;
import org.flywaydb.core.Flyway;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource( "postgres://xallqvkrezbfkh:2afb9e7ebdb2c2ddaada878ee6e6db773497e8fda4096a6dd71e59e19c4cb1b1@ec2-44-207-133-100.compute-1.amazonaws.com:5432/d9fblvo32d3uuj" , "postgres" , "2afb9e7ebdb2c2ddaada878ee6e6db773497e8fda4096a6dd71e59e19c4cb1b1" )
                .baselineOnMigrate(true)
                .locations("db/migration")
                .load();
        flyway.migrate();

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
