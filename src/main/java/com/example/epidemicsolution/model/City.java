package com.example.epidemicsolution.model;

public class City {

    private String city;
    private int x;
    private int y;
    private int id;

    public City(String city, int x, int y, int id) {
        this.city = city;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

