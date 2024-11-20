package com.example.lab2_ph2616.DTO;

public class Categoty {
    int id;
    String name;

    public Categoty(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Categoty(String name) {
        this.name = name;
    }
    public Categoty() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
