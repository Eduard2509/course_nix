package com.command;

import com.model.Auto;
import com.service.AutoService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromFileJson implements Command {
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final Pattern PATTERN = Pattern.compile("\"(?<key>[\\S]+)\": (\"(?<value>[\\S ]+)\")?");

    @Override
    public void execute() {
        System.out.println(readJsonAndCreateAuto());
    }

    public Auto readJsonAndCreateAuto() {
        File file = new File("src/main/resources/auto.json");
        final Map<String, Object> map = new HashMap<>();
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final Matcher matcher = PATTERN.matcher(line);
                if (matcher.find()) {
                    if (matcher.group("value") != null) {
                        map.put(matcher.group("key"), matcher.group("value"));
                        System.out.println("Element vehicle: " + matcher.group("value"));
                    } else {
                        map.put(matcher.group("key"), matcher.group("key"));
                        System.out.println("Element name: " + matcher.group("key"));
                    }
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return AUTO_SERVICE.createVehicleByMap.apply(map);
    }
}
