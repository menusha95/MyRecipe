package com.test.menusha.myrecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class RecipeAdapter extends BaseAdapter {

    TextView recipeTitle, recipeServings, recipeDescription;
    Context context;
    ArrayList<RecipeData> recData;
    LayoutInflater inflater;

    public RecipeAdapter(Context context, ArrayList<RecipeData> data) {
        this.context = context;
        this.recData = data;
    }

    @Override
    public int getCount() {
        return recData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.from(context).inflate(R.layout.custom_list,viewGroup,false);

        recipeTitle = (TextView) view.findViewById(R.id.readname);
        recipeServings = (TextView) view.findViewById(R.id.readage);
        recipeDescription = (TextView) view.findViewById(R.id.desclist);


        recipeTitle.setText(recipeTitle.getText()+" "+recData.get(i).getRecTitle());
        recipeServings.setText(recipeServings.getText()+" "+ recData.get(i).getRecServings());
        recipeDescription.setText(recipeDescription.getText()+""+ recData.get(i).getRecDescription());

        return view;
    }
}
