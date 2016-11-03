package com.yashtawade.foodforthought.models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private String image;
    private int readyInMinutes;
    private int usedIngredientCount;
    private int missedIngredientCount;
    private int likes;
    private Instruction instruction;
    private List<Ingredient> missedIngredients = new ArrayList<>();
    private List<Ingredient> usedIngredients = new ArrayList<>();
    private List<Ingredient> extendedIngredients = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public List<Ingredient> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(List<Ingredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public List<Ingredient> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(List<Ingredient> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public List<String> getInstructionContent(){
        List<String> instructionContent = new ArrayList<>();
        List<InstructionStep> instructionSteps = instruction.getInstructionSteps();
        for(InstructionStep instep : instructionSteps){
            for(Step step : instep.getSteps()){
                for(String s : step.getStep().split(".")){
                    String each = s.trim();
                    if(!each.equals("")){
                        instructionContent.add(each);
                    }
                }
            }
        }

        return instructionContent;
    }

}
