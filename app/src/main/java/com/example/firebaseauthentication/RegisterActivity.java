package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button singUpBtn;
    private TextView login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        singUpBtn = findViewById(R.id.signup);
        login = findViewById(R.id.login_link);

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                if(emailValue.isEmpty()){
                    email.setText("Please insert email");
                    email.requestFocus();
                }else if(passwordValue.isEmpty()){
                    password.setText("Please insert password");
                    password.requestFocus();
                }
                else if(emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fill the blank", Toast.LENGTH_SHORT).show();
                }else if(!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Something error occured", Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Error occuured", Toast.LENGTH_SHORT).show();
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}