package com.test.menusha.myrecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by menusha on 7/14/17.
 */

public class Recipe extends AppCompatActivity {


    EditText recTitle,recServings,recDescrip;
    TextView view;
    ListView bottomList;
    Button btnAdd;
    FirebaseDatabase fDatabase;
    ArrayList<RecipeData> dataArrayList;
    RecipeAdapter recAdapter;
    String key;
    int temp;
    DatabaseReference recRef;
    RecipeData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_adder);

        recTitle=(EditText)findViewById(R.id.rec_Title);
        recServings=(EditText)findViewById(R.id.rec_Servings);
        recDescrip=(EditText)findViewById(R.id.rec_Description);
        view= (TextView)findViewById(R.id.viewText);
        btnAdd=(Button)findViewById(R.id.recButton);
        bottomList=(ListView)findViewById(R.id.bottom_list);

        fDatabase = FirebaseDatabase.getInstance();
        recRef = fDatabase.getReference().child("Recipe");
        key = recRef.push().getKey();


        //final String getTitle = recTitle.getText().toString();
        final String getServing = recServings.getText().toString();
        final String getDescrip = recDescrip.getText().toString();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setText(recTitle.getText().toString());
            }
        });
    }
}
