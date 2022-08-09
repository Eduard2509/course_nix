package com.command;

import com.model.Auto;
import com.service.AutoService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromFileXml implements Command {
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final Pattern PATTERN = Pattern.compile(
            "<(?<model>[^?\\/]*?(([\\S]*)=\"(?<currency>.*)\")?)>((?<price>[\\S ]*?)\\<\\/(.+)\\>)?");

    @Override
    public void execute() {
        System.out.println(readXmlAndCreateAuto());
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public Auto readXmlAndCreateAuto() {
        ReadFromFileXml readFromFileXml = new ReadFromFileXml();
        String fileName = "auto.xml";
        InputStream inputStream = readFromFileXml.getFileFromResourceAsStream(fileName);
        final Map<String, Object> map = new HashMap<>();
        try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final Matcher matcher = PATTERN.matcher(line);
                if (matcher.find()) {

                    if (matcher.group("price") != null) {
                        map.put(matcher.group("model"), matcher.group("price"));
                        System.out.println("Element vehicle: " + matcher.group("price"));
                    }
                    if (matcher.group("model") != null && matcher.group("price") == null) {
                        map.put(matcher.group("model"), matcher.group("model"));
                        System.out.println("Element vehicle: " + matcher.group("model"));
                    }
                    if (matcher.group("currency") != null) {
                        map.put(matcher.group(7), matcher.group("price"));
                        System.out.println("Element vehicle: " + matcher.group("currency"));
                    }

                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return AUTO_SERVICE.createVehicleByMap.apply(map);
    }
}
