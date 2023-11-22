package com.example.linkedin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText userName, inputEmail, inputPhoneNumber, inputPassword, confirmPassword;
    Button sign_btn;
    ImageView imageView;
    Spinner spinner;
    String userID;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.text_username);
        inputEmail = findViewById(R.id.text_email);
        inputPhoneNumber = findViewById(R.id.phoneNumber);
        inputPassword = findViewById(R.id.text_Password);
        confirmPassword = findViewById(R.id.text_confirmPassword);
        sign_btn = findViewById(R.id.signup_btn1);
        imageView =  findViewById(R.id.imageView);
        spinner = findViewById(R.id.items_selection);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedValue = spinner.getSelectedItem().toString();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String phoneNumber = String.valueOf(inputPhoneNumber.getText());


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);

                                Map<String,Object> user = new HashMap<>();
                                user.put("email",email);
                                user.put("password",password);
                                user.put("phoneNumber",phoneNumber);
                                user.put("selectedValue",selectedValue);
                                documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: user profile is created for" + userID)).addOnFailureListener(e -> Log.d(TAG,"onFailure " + e ));

                                startActivity(new Intent(getApplicationContext(), forgot_password.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
          }
      });
    }
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

          if (resultCode == RESULT_OK){
              if (requestCode == PICK_IMAGE_REQUEST){
                  imageView.setImageURI(data.getData());
            }
        }
    }
}