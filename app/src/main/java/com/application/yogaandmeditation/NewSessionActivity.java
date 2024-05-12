package com.application.yogaandmeditation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewSessionActivity extends AppCompatActivity {

    private static final String LOG_TAG = SessionViewActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mSessions;


    private TextView newSessionTW;
    private EditText sessionTitleET;
    private EditText sessionDescriptionET;
    private RadioGroup genderRG;
    private EditText sessionPriceET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_session);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secretKey != 99){
            finish();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Log.d(LOG_TAG, "User authenticated!");
        } else {
            Log.d(LOG_TAG, "User unauthenticated!");
            finish();
        }

        newSessionTW = findViewById(R.id.newSessionTextView);
        sessionTitleET = findViewById(R.id.sessionTitleEditText);
        sessionDescriptionET = findViewById(R.id.sessionDescriptionEditText);
        genderRG = findViewById(R.id.genderRadioGroup);
        sessionPriceET = findViewById(R.id.sessionPriceEditText);

        genderRG.check(R.id.genderOtherRadioButton);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.drop);
        newSessionTW.startAnimation(animation);

        mFirestore = FirebaseFirestore.getInstance();
        mSessions = mFirestore.collection("Sessions");
    }

    public void createNewSession(View view) {
        String title = sessionTitleET.getText().toString();
        String desc = sessionDescriptionET.getText().toString();
        String price = sessionPriceET.getText().toString();
        int checkedId = genderRG.getCheckedRadioButtonId();
        RadioButton radioButton = genderRG.findViewById(checkedId);
        String gender = radioButton.getText().toString();

        OnlineSession newSession = new OnlineSession(user.getEmail(), user.getUid(), title, desc, gender, price, 0);
        mSessions.add(newSession);
        finish();
    }
}