package net.anomalija.portal.anomalia.Model;

import java.io.Serializable;

public class Category implements Serializable {
    private int category_id;
    private String name;
    public Category(int id, String name){
        this.category_id = id;
        this.name = name;

    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category name " + getName() + " Category id: " + getCategory_id();
    }
}