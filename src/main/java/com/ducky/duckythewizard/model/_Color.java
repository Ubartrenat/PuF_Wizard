package com.ducky.duckythewizard.model;

public class _Color {

    private final String name;
    private final String value; // HSLA/RGB to be defined

    public _Color(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
