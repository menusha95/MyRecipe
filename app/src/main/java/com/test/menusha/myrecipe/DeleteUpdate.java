package com.test.menusha.myrecipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;



/**
 * Created by menusha on 7/23/17.
 */

public class DeleteUpdate extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    RecipeData data;
    FirebaseDatabase fDatabase;
    DatabaseReference recRef;
    String id;


    ArrayList<HashMap<String, String>> recipeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_update_recipe);

        recipeList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        fDatabase = FirebaseDatabase.getInstance();
        int in = 1;
        recRef = fDatabase.getReference().child("Recipe");
        id = recRef.push().getKey();
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(DeleteUpdate.this,"Recipes downloading to list!",Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpGet sh = new HttpGet();
            //Url to access the API
            String url = "https://myrecipe-d4383.firebaseio.com/.json?print=pretty&format=export&download=myrecipe-d4383-export.json&auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MDA5ODIxOTMsImV4cCI6MTUwMDk4NTc5MywiYWRtaW4iOnRydWUsInYiOjB9.IR_Jbqe0hDWRmrjhUbkLEknpe1oob-uRG1uvC84J9-E";

            String jsonStr = sh.service(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject contacts = jsonObj.getJSONObject("Recipe");
                    for (int i = 0; i < jsonObj.length() ; i++) {
                        System.out.println("view--->"+jsonObj);
                    }

                    for(Iterator<String> iter = contacts.keys(); iter.hasNext();) {
                        String key = iter.next();
                        //default unique key is accessed by objects using push method
                        JSONObject c = contacts.getJSONObject(key);
                        String recTitle = c.getString("recTitle");
                        String recServings = c.getString("recServings");
                        String recDescription = c.getString("recDescription");

                        System.out.println("display--->"+key + " "+recTitle+" "+recServings+" ");

                        //adding to the listview
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("recTitle", recTitle);
                        contact.put("recServings", recServings);
                        contact.put("recDescription", recDescription);

                        //adding recipes to list
                        recipeList.add(contact);


                        System.out.println("views--->"+contact);
                    }
                    //error handling to show message if server call doesnt work
                } catch (final JSONException e) {
                    Log.e(TAG, "error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Renew the API key",
                                Toast.LENGTH_SHORT).show();
                     }
                  });
             }
            return null;
         }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(DeleteUpdate.this, recipeList,
                    R.layout.list_item, new String[]{ "recTitle","recServings","recDescription"},
                    new int[]{R.id.titles, R.id.servings, R.id.desc});
            lv.setAdapter(adapter);
        }
    }
}