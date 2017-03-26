package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";

    RecyclerView mRecyclerView;
    DatabaseReference mDbReference;
    StorageReference mStorageReference;
    LibraryAdapter adapter;

    SharedPreferences prefs;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setElevation(0);
        }

        mDbReference = FirebaseDatabase.getInstance().getReference("users");
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user = prefs.getString("username", "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        adapter = new LibraryAdapter(this);
        mRecyclerView.setAdapter(adapter);

        downloadUserImages();
    }

    private void downloadUserImages() {

        DatabaseReference mPathReference = mDbReference.child(user);
        mPathReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> imageNameList = new ArrayList<String>();
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (map == null)
                    return;
                for (Map.Entry<String, Object> Entry : map.entrySet()) {
                    imageNameList.add(Entry.getValue().toString());
                }
                adapter.setImageList(imageNameList);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "datasnapshot cancelled");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString("username", "");
            ed.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }

}
