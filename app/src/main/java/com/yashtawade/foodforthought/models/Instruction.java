package com.yashtawade.foodforthought.models;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    private int instructionId;
    private Recipe recipe;
    private List<InstructionStep> instructionSteps= new ArrayList<>();

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<InstructionStep> getInstructionSteps() {
        return instructionSteps;
    }

    public void setInstructionSteps(List<InstructionStep> instructionSteps) {
        this.instructionSteps = instructionSteps;
    }
}
