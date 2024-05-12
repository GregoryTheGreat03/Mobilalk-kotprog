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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SessionUpdateActivity extends AppCompatActivity {
    private static final String LOG_TAG = SessionViewActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mSessions;
    private NotificationHandler mNotificationHandler;
    private String sessionId;

    private TextView newSessionTW;
    private EditText sessionTitleET;
    private EditText sessionDescriptionET;
    private RadioGroup genderRG;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private RadioButton otherRB;
    private EditText sessionPriceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);
        sessionId = getIntent().getStringExtra("SESSION_ID");

        if (secretKey != 99 || sessionId == null){
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

        maleRB = findViewById(R.id.genderMaleRadioButton);
        femaleRB = findViewById(R.id.genderFemaleRadioButton);
        otherRB = findViewById(R.id.genderOtherRadioButton);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.drop);
        newSessionTW.startAnimation(animation);

        mFirestore = FirebaseFirestore.getInstance();
        mSessions = mFirestore.collection("Sessions");

        mSessions.document(sessionId).get().addOnSuccessListener(queryDocumentSnapshot -> {
            OnlineSession o = queryDocumentSnapshot.toObject(OnlineSession.class);
            if (o == null){
                finish();
            } else {
                sessionTitleET.setText(o.getTitle());
                sessionDescriptionET.setText(o.getDescription());
                sessionPriceET.setText(o.getPrice());
                if(o.getForGender().equals(maleRB.getText().toString())){
                    genderRG.check(R.id.genderMaleRadioButton);
                } else if(o.getForGender().equals(femaleRB.getText().toString())){
                    genderRG.check(R.id.genderFemaleRadioButton);
                } else if(o.getForGender().equals(otherRB.getText().toString())){
                    genderRG.check(R.id.genderOtherRadioButton);
                }
            }
        });

        mNotificationHandler = new NotificationHandler(this);
    }

    public void updateSession(View view) {
        String title = sessionTitleET.getText().toString();
        String desc = sessionDescriptionET.getText().toString();
        String price = sessionPriceET.getText().toString();
        int checkedId = genderRG.getCheckedRadioButtonId();
        RadioButton radioButton = genderRG.findViewById(checkedId);
        String gender = radioButton.getText().toString();

        if (!title.isEmpty()){
            mSessions.document(sessionId).update("title", title);
        }
        if (!desc.isEmpty()){
            mSessions.document(sessionId).update("description", desc);
        }
        if (!price.isEmpty()){
            mSessions.document(sessionId).update("price", price);
        }
        if (!gender.isEmpty()){
            mSessions.document(sessionId).update("forGender", gender);
        }

        mNotificationHandler.send("Frissült a(z) " + title + " című alkalmad: \n" + " - új ár: " + price + " Ft\n" + " - új nemhez való hozzárendelés: " + gender + "\n - új leírás: " + desc);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}