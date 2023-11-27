package com.example.linkedin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    Button  sign_up_btn,log_in_btn;
    TextView forgotPassword;
    EditText email_input,password_input;

    String email,password;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        log_in_btn = findViewById(R.id.login_btn);
        sign_up_btn = findViewById(R.id.signup_btn);
        forgotPassword = findViewById(R.id.forgotPassword);
        email_input = findViewById(R.id.text_email1);
        password_input = findViewById(R.id.text_password1);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        forgotPassword.setOnClickListener(v -> {
            Intent p = new Intent(this, forgot_password.class);
            startActivity(p);
        });

        sign_up_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUp.class)));
        log_in_btn.setOnClickListener(v -> {

            email = email_input.getText().toString().trim();
            password = password_input.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FB_AUTH", "signInWithEmail:success");
                                Toast.makeText(LoginPage.this,"Signed in Successful",Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(LoginPage.this,logout.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FB_AUTH", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });


    }
}