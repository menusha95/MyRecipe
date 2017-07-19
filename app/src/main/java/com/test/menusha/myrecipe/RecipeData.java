package com.test.menusha.myrecipe;

public class RecipeData {

    String key;
    String recTitle;
    String recServings;
    String recDescription;

    public RecipeData(String key, String title, String servings, String description) {
        this.key = key;
        this.recTitle = title;
        this.recServings = servings;
        this.recDescription=description;
    }

    public RecipeData() {

    }

    public String getKey() {
        return key;
    }
    public String getRecTitle(){
        return getRecTitle();
    }

    public String getRecServings() {
        return getRecServings();
    }
    public String getRecDescription(){
        return getRecDescription();
    }
}
