package com.test.menusha.myrecipe;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private EditText userName,userPass,userEmail;
    private Button btnSubmit,btnLogin;
    private FirebaseAuth firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting firebase instance
        firebase = FirebaseAuth.getInstance();

        //initializing username and password fields
        userPass = (EditText) findViewById(R.id.passWord_id);
        userEmail = (EditText) findViewById(R.id.email_id);

        //initializing login and submit buttons
        btnSubmit = (Button) findViewById(R.id.submit_button_id);
        btnLogin = (Button) findViewById(R.id.login_button_id);


        //submit button task executed
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegistration(view);
            }
        });

        //login button task executed
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }
        });
    }

    //user registration with error handling
    public void userRegistration(View view){
        firebase.createUserWithEmailAndPassword(userEmail.getText().toString().trim(), userPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Registration Unsuccessful!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //user login with error handling
    public void userLogin(View view){
        firebase.signInWithEmailAndPassword(userEmail.getText().toString().trim(),userPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Recipe.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
