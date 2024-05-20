package com.example.snake_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.adminemail);
        password= findViewById(R.id.adminpass);
        login= findViewById(R.id.adminloginbtn);
        if(fAuth.getCurrentUser() != null){
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1= email.getText().toString();
                String password1= password.getText().toString();
                fAuth.signInWithEmailAndPassword(email1, password1). addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(login.this, "Login Successful! ❤ ", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}