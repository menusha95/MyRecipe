package com.test.menusha.myrecipe;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText userName,userPass,userEmail;
    private Button btnSubmit,btnLogin;
    private FirebaseAuth firebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = FirebaseAuth.getInstance();


        userPass = (EditText) findViewById(R.id.passWord_id);
        userPass.requestFocus(2);

        userEmail = (EditText) findViewById(R.id.email_id);


        btnSubmit = (Button) findViewById(R.id.submit_button_id);
        btnLogin = (Button) findViewById(R.id.login_button_id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegistration(view);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }
        });
    }

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
