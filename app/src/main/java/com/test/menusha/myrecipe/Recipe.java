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

    int temp;
    DatabaseReference recRef;
    RecipeData data;
    LayoutInflater inflater1;
    EditText recTitle, recServings, recDescrip;
    TextView view;
    ListView bottomList;
    Button btnAdd,btnDelUpd;
    FirebaseDatabase fDatabase;
    ArrayList<RecipeData> dataArrayList;
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

        btnDelUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Recipe.this,DeleteUpdate.class);
                startActivity(intent1);
            }
        });



        dataArrayList = new ArrayList<>();

        recAdapter = new RecipeAdapter(Recipe.this, dataArrayList);
        bottomList.setAdapter(recAdapter);

        recRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RecipeData datam = dataSnapshot.getValue(RecipeData.class);
                dataArrayList.add(datam);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                final View v = inflater1.from(getApplicationContext()).inflate(R.layout.custom_alert, null);
                temp = i;

                final EditText updateRecipe, updateServing,updateDescription;
                updateRecipe = (EditText) v.findViewById(R.id.update_Recipe);
                updateServing = (EditText) v.findViewById(R.id.update_Serving);
                updateDescription = (EditText) v.findViewById(R.id.update_Descrip);


                final AlertDialog.Builder builder = new AlertDialog.Builder(Recipe.this).setView(v);
                final AlertDialog alert = builder.create();

                v.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RecipeData tempData = new RecipeData(dataArrayList.get(temp).getId(), updateRecipe.getText().toString(), updateServing.getText().toString(), updateDescription.getText().toString());
                        recRef.child(dataArrayList.get(temp).getId()).setValue(tempData);
                        dataArrayList.remove(temp);
                        dataArrayList.add(temp, tempData);
                        recAdapter.notifyDataSetChanged();
                    }
                });

                v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (temp == -1) {
                            Toast.makeText(getApplicationContext(), "There is no data to delete", Toast.LENGTH_SHORT).show();
                        } else {
                            recRef.child(dataArrayList.get(temp).getId()).removeValue();
                            dataArrayList.remove(temp);
                            recAdapter.notifyDataSetChanged();
                            alert.cancel();
                            temp = -1;
                        }
                    }
                });
                updateRecipe.setText(dataArrayList.get(temp).getRecTitle());
                updateServing.setText(dataArrayList.get(temp).getRecServings());
                updateDescription.setText(dataArrayList.get(temp).getRecTitle());

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

    public void recipeUpdate() {
        recRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    data = dataSnapshot1.getValue(RecipeData.class);
                    dataArrayList.add(data);
                }
                recAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
