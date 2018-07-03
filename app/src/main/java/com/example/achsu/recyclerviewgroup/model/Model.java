package com.example.achsu.recyclerviewgroup.model;

public class Model {
    private int type;
    private String name;
    private String value;

    public Model(int type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public interface Type {
        int COMMON = 1;
        int GROUP = 2;
        int CHILD = 3;
    }
}
