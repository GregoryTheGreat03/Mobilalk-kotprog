package com.application.yogaandmeditation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    TextView registrationTW;
    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bundle b = getIntent().getExtras();
        // b.getInt("SECRET_KEY");
        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secretKey != 99){
            finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        registrationTW = findViewById(R.id.textView);
        usernameET = findViewById(R.id.editTextUsername);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordAgainET = findViewById(R.id.editTextPasswordAgain);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.drop);
        registrationTW.startAnimation(animation);

        usernameET.setText(preferences.getString("userName", ""));
        emailET.setText(preferences.getString("email", ""));

        mAuth = FirebaseAuth.getInstance();
    }

    private void startSessionView() {
        Intent intent = new Intent(this, SessionViewActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
    }

    public void register(View view) {
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (username == null || username.length() == 0 || password == null || password.length() == 0
            || email == null || email.length() == 0 || passwordAgain == null || passwordAgain.length() == 0){
            return;
        }

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "A két jelszó nem egyezik!");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + username + ", " + email + ", " + password + ", " + passwordAgain);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "User created successfully");
                    startSessionView();
                } else {
                    Log.d(LOG_TAG, "Failed to create user");
                    Toast.makeText(RegisterActivity.this, "Failed to create user: " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", usernameET.getText().toString());
        editor.apply();
    }
}