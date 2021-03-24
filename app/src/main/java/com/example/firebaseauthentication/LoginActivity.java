package com.example.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button loginButton;
    private TextView signup, forgetPassword;
    private FirebaseAuth firebaseAuth;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signup = findViewById(R.id.signup_link);
        forgetPassword = findViewById(R.id.forget_password);
        checkBox = findViewById(R.id.remember_me);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                
                if(emailValue.isEmpty()){
                    email.setError("Please insert email");
                    email.requestFocus();
                }else if(passwordValue.isEmpty()){
                    password.setError("Please insert password");
                }else if( emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fill the blank", Toast.LENGTH_SHORT).show();
                }else if(!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                            }else{
                                if(checkBox.isChecked()){
                                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("remember", "true");
                                    editor.apply();
                                }else{
                                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("remember", "false");
                                    editor.apply();
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Something error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetEmail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ? ");
                passwordResetDialog.setMessage("Enter your email");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mailValue = resetEmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mailValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(a);
            }
        });

    }


    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}