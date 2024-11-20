package com.example.lab2_ph2616.DTO;

public class Product {
    private int id,id_cat;
    private String name;
    private double price;

    public Product(int id, int id_cat, String name, double price) {
        this.id = id;
        this.id_cat = id_cat;
        this.name = name;
        this.price = price;
    }
    public Product(double price, String name, int id_cat) {
        this.price = price;
        this.name = name;
        this.id_cat = id_cat;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
