package com.harshit.resume.model;

import java.util.ArrayList;
import java.util.List;

public class SkillCategory {

    private String label;
    private List<String> items = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
