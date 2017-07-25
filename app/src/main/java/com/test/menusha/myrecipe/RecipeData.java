package com.test.menusha.myrecipe;

public class RecipeData {

    String id;
    String recTitle;
    String recServings;
    String recDescription;

    public RecipeData(String id, String title, String servings, String description) {
        this.id = id;
        this.recTitle = title;
        this.recServings = servings;
        this.recDescription=description;
    }

    public RecipeData() {

    }
    public String getId() {
        return id;
    }
    public String getRecTitle(){
        return recTitle;
    }
    public String getRecServings() {
        return recServings;
    }
    public String getRecDescription(){
        return recDescription;
    }
}
