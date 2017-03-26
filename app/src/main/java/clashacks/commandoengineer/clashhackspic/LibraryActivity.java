package clashacks.commandoengineer.clashhackspic;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_library);

        mDbReference = FirebaseDatabase.getInstance().getReference("users");
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user  = prefs.getString("username", "");

        /*List<String> imageUrls = new ArrayList<String>();
        String completePath = Environment.getExternalStorageDirectory() + "/ClashHacksPic/";
        File dir = new File(completePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            Uri imageUri = Uri.fromFile(file);
            imageUrls.add(imageUri.toString());
        }*/

        adapter = new LibraryAdapter(this);
        mRecyclerView.setAdapter(adapter);

        downloadUserImages();

    }

    private void downloadUserImages(){
        Log.e(TAG, "username"+ user);
        DatabaseReference mPathReference = mDbReference.child(user);
        mPathReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> imageNameList = new ArrayList<String>();
                Log.e(TAG, dataSnapshot.toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String, Object> Entry: map.entrySet()) {
                    imageNameList.add(Entry.getKey());
                   StorageReference imageRef = mStorageReference.child("images/" + Entry.getKey());
                    Log.e(TAG, "started");
                   imageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                       @Override
                       public void onSuccess(byte[] bytes) {
                           Log.e(TAG, "Successfully downloaded bytes.");
                       }
                   });
                }
                adapter.setImageList(imageNameList);
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
        if(item.getItemId() == R.id.action_logout){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString("username", "");
            ed.commit();
            startActivity(new Intent(LibraryActivity.this, MainActivity.class));
            finish();
        }
        return true;
    }
}

/*

    private void downloadUserImages(){

        Log.e(TAG, "username"+ user);
        DatabaseReference mPathReference = mDbReference.child(user);
        mPathReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String, Object> Entry: map.entrySet()){
                    if(!Entry.getValue().toString().equals("1")){ // Not downloaded already
                        try {
                            downloadImage(Entry.getKey());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "datasnapshot cancelled");
            }
        });
    }

    private  downloadImage(final String filename) throws IOException{

        final File localFile = File.createTempFile(filename, ".jpg",
                new File(Environment.getExternalStorageDirectory() + "/ClashHacksPic/"));

        StorageReference pathReference = mStorageReference.child("images/" + filename);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);





//        pathReference.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//              /      public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Log.e(TAG, "File has been downloaded.");
//                        adapter.addImage(Uri.fromFile(localFile).toString());
//                        mDbReference.child(user).child(filename).setValue(1);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                 Handle failed download
//                 ...
//                Log.e(TAG, "FAILED");
//            }
//        });

    }
*/
