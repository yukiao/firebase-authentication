package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button signOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.signout);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String checkbox = preferences.getString("remember", "false");

        if(!checkbox.equals("true")){
            if(isTaskRoot()) {
                firebaseAuth.signOut();
                editor.putString("remember", "false");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                editor.putString("remember", "false");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}