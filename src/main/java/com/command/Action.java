package com.command;

import lombok.Getter;

@Getter
public enum Action {
    CREATE("Create vehicles", new Create()),
    UPDATE("Update vehicle", new Update()),
    DELETE("Delete vehicle", new Delete()),
    PRINT("Print vehicles", new Print()),
    READ_FROM_FILE_XML("Read from xml file", new ReadFromFileXml()),
    READ_FROM_FILE_JSON("Read from json file", new ReadFromFileJson()),
    EXIT("Exit", null);

    private String name;
    private Command command;

    Action(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public Command execute() {
        if (command != null) {
            command.execute();
        }
        return command;
    }
}
