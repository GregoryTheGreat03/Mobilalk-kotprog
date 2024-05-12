package com.application.yogaandmeditation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListMySessionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = SessionViewActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mSessions;
    private RecyclerView mRecyclerView;
    private ArrayList<OnlineSession> mSessionList;
    private MySessionAdapter mAdapter;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_my_sessions);
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
        mRecyclerView = findViewById(R.id.myRecycleView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mSessionList = new ArrayList<>();

        mAdapter = new MySessionAdapter(this, mSessionList);

        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mSessions =mFirestore.collection("Sessions");

        loadData();
    }

    private void loadData() {
        mSessionList.clear();
        mSessions.whereEqualTo("creatorId", user.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                OnlineSession onlineSession = document.toObject(OnlineSession.class);
                onlineSession.setId(document.getId());
                mSessionList.add(onlineSession);
            }

            mAdapter.notifyDataSetChanged();
        });
    }


    public void updateSession(OnlineSession session){
        Intent intent = new Intent(this, SessionUpdateActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        intent.putExtra("SESSION_ID", session._getId());
        startActivity(intent);
    }

    public void deleteSession(OnlineSession session){
        DocumentReference ref = mSessions.document(session._getId());
        ref.delete().addOnSuccessListener(success -> {
            Log.d(LOG_TAG, "Item deleted sucesfully.");
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Session "+ session._getId() + " can't be deleted.", Toast.LENGTH_LONG).show();
        });

        loadData();
    }
}