package com.example.dvidr_000.lighthauzproject;

/**
 * Created by richentra on 31-Dec-16.
 */

public class Category{
    private String name;
    private boolean selected;

    public Category(String name, boolean selected){
        this.name=name;
        this.selected=selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}