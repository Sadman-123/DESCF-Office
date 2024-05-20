package com.example.snake_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    Button rescubtn,releasebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rescubtn = findViewById(R.id.rescubtn);
        releasebtn = findViewById(R.id.releasebtn);
        rescubtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, rescue.class);
            startActivity(intent);
        });
        releasebtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, release.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.change_userskey:
                Intent intent = new Intent(this, change_key.class);
                startActivity(intent);
                return true;
            case R.id.logoutt:
                fAuth.signOut();
                Intent intent1 = new Intent(this, login.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}