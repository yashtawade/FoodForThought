package com.yashtawade.foodforthought.models;

import java.util.ArrayList;
import java.util.List;

public class InstructionStep {
    private String name;
    private List<Step> steps = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
