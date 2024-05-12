package com.application.yogaandmeditation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SessionViewActivity extends AppCompatActivity {
    private static final String LOG_TAG = SessionViewActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mSessions;



    private RecyclerView mRecyclerView;
    private ArrayList<OnlineSession> mSessionList;
    private SessionAdapter mAdapter;

    private int gridNumber = 1;
    private int showingCards = 3;
    private boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_view);
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

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mSessionList = new ArrayList<>();

        mAdapter = new SessionAdapter(this, mSessionList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN) && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                        && !isLoaded
                ) {
                    isLoaded = true;
                    showingCards += 5;
                    loadData();
                    isLoaded = false;
                }
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        mSessions =mFirestore.collection("Sessions");

        loadData();
    }

    private void loadData() {
        mSessionList.clear();
        mSessions.limit(showingCards).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                OnlineSession onlineSession = document.toObject(OnlineSession.class);
                mSessionList.add(onlineSession);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.session_view_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_new_session_button){
            if (!user.isAnonymous()) {
                Log.d(LOG_TAG, "Add new session button clicked");
                Intent intent = new Intent(this, NewSessionActivity.class);
                intent.putExtra("SECRET_KEY", 99);
                startActivity(intent);
                return true;
            }
        } else if (item.getItemId() == R.id.my_sessions_button) {
            if (!user.isAnonymous()) {
                Log.d(LOG_TAG, "My sessions button clicked");
                Intent intent = new Intent(this, ListMySessionsActivity.class);
                intent.putExtra("SECRET_KEY", 99);
                startActivity(intent);
                return true;
            }
        } else if (item.getItemId() == R.id.log_out_button) {
            Log.d(LOG_TAG, "Log out button clicked");
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}