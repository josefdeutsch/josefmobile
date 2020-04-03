package com.josef.mobile.model;

import androidx.annotation.NonNull;

public class MainActivityAdapterBody {

    public String name;
    public String description;

    public MainActivityAdapterBody(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
