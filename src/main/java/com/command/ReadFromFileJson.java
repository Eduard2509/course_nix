package com.command;

import com.model.Auto;
import com.service.AutoService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromFileJson implements Command {
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final Pattern PATTERN = Pattern.compile("\"(?<key>[\\S]+)\": (\"(?<value>[\\S ]+)\")?");


    @Override
    public void execute(){
        System.out.println(readJsonAndCreateAuto());
    }

    private InputStream getFileFromResourceAsStream(String fileName){
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public Auto readJsonAndCreateAuto() {
        ReadFromFileJson readFromFileJson = new ReadFromFileJson();
        String fileName = "auto.json";
        InputStream inputStream = readFromFileJson.getFileFromResourceAsStream(fileName);
        final Map<String, Object> map = new HashMap<>();
        try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
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
