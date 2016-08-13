package com.example.yura.martian_alerts.entities;

import java.util.HashMap;
import java.util.Map;

public class AlertMenuEntity {
    private int id;
    private String name;
    private boolean enable;
    private boolean urgent;
    private Map<Integer, Integer> vibrationType = new HashMap<>();

    public AlertMenuEntity(int id, String name, boolean enable, boolean urgent){
        this.id = id;
        this.name = name;
        this.enable = enable;
        this.urgent = urgent;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Map<Integer, Integer> getVibrationType() {
        return vibrationType;
    }

    public void setVibrationType(Map<Integer, Integer> vibrationType) {
        this.vibrationType = vibrationType;
    }

    public void addVibrationType(Integer type, Integer value){
        vibrationType.put(type, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
