package com.test.menusha.myrecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by menusha on 7/14/17.
 */

public class Recipe extends AppCompatActivity {

    int rep;
    DatabaseReference recRef;
    RecipeData data;
    LayoutInflater inflater1;
    EditText recTitle, recServings, recDescrip;
    TextView view;
    ListView bottomList;
    Button btnAdd,btnDelUpd;
    FirebaseDatabase fDatabase;
    ArrayList<RecipeData> recipeList;
    RecipeAdapter recAdapter;
    String id;
    String descrip;
    String serving;
    String recipe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_adder);

        fDatabase = FirebaseDatabase.getInstance();
        recRef = fDatabase.getReference().child("Recipe");
        id = recRef.push().getKey();

        recTitle = (EditText) findViewById(R.id.rec_Title);
        recServings = (EditText) findViewById(R.id.rec_Servings);
        recDescrip = (EditText) findViewById(R.id.rec_Description);
        view = (TextView) findViewById(R.id.viewText);
        btnAdd = (Button) findViewById(R.id.recButton);
        btnDelUpd=(Button)findViewById(R.id.rec_Delete_Update);
        bottomList = (ListView) findViewById(R.id.bottom_list);

        //3 fields of recipe added to the database and the textviews are set to null to add another recipe
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recipe = recTitle.getText().toString().trim();
                    if (TextUtils.isEmpty(recipe)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                    } else {
                        serving = recServings.getText().toString().trim();
                        descrip = recDescrip.getText().toString().trim();
                        data = new RecipeData(recRef.push().getKey().concat("dsds"), recipe, serving, descrip);
                        recRef.child(data.getId()).setValue(data);
                        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                        recTitle.setText("");
                        recServings.setText("");
                        recDescrip.setText("");

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //view the lists of recipes
        btnDelUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Recipe.this,DeleteUpdate.class);
                startActivity(intent1);
            }
        });



        recipeList = new ArrayList<>();

        recAdapter = new RecipeAdapter(Recipe.this, recipeList);
        bottomList.setAdapter(recAdapter);

        recRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RecipeData datam = dataSnapshot.getValue(RecipeData.class);
                recipeList.add(datam);
                recAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bottomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int tmp, long l) {


                final View view2 = inflater1.from(getApplicationContext()).inflate(R.layout.custom_alert, null);
                rep = tmp;

                final EditText updateRecipe, updateServing,updateDescription;
                updateRecipe = (EditText) view2.findViewById(R.id.update_Recipe);
                updateServing = (EditText) view2.findViewById(R.id.update_Serving);
                updateDescription = (EditText) view2.findViewById(R.id.update_Descrip);


                final AlertDialog.Builder builder = new AlertDialog.Builder(Recipe.this).setView(view2);
                final AlertDialog alert = builder.create();

                view2.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RecipeData tempData = new RecipeData(recipeList.get(rep).getId(), updateRecipe.getText().toString(), updateServing.getText().toString(), updateDescription.getText().toString());
                        recRef.child(recipeList.get(rep).getId()).setValue(tempData);
                        recipeList.remove(rep);
                        recipeList.add(rep, tempData);
                        recAdapter.notifyDataSetChanged();
                    }
                });

                view2.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rep == -1) {
                            Toast.makeText(getApplicationContext(), "There is no data to delete", Toast.LENGTH_SHORT).show();
                        } else {
                            recRef.child(recipeList.get(rep).getId()).removeValue();
                            recipeList.remove(rep);
                            recAdapter.notifyDataSetChanged();
                            alert.cancel();
                            rep = -1;
                        }
                    }
                });
                updateRecipe.setText(recipeList.get(rep).getRecTitle());
                updateServing.setText(recipeList.get(rep).getRecServings());
                updateDescription.setText(recipeList.get(rep).getRecDescription());

                try {
                    alert.show();
                } catch (Exception e) {
                    Log.d("show", "onItemClick: " + e);
                }
                return;
            }
        });

        recipeUpdate();
    }

    //update a particular recipe
    public void recipeUpdate() {
        recRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    data = dataSnapshot1.getValue(RecipeData.class);
                    recipeList.add(data);
                }
                recAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
