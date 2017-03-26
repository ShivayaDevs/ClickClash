package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int IMAGE_PICK_CODE = 2;

    RecyclerView mRecyclerView;
    DatabaseReference mDbReference;
    StorageReference mStorageReference;
    LibraryAdapter adapter;

    SharedPreferences prefs;
    String user, mImageName;

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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
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
                if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0) {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    return;
                }
                try {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map == null)
                        return;
                    for (Map.Entry<String, Object> Entry : map.entrySet()) {
                        imageNameList.add(Entry.getValue().toString());
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            File file = storeImage(imageBitmap);

            Log.i(TAG, file.getPath() + mImageName);

            Intent i = new Intent(this, SelectAndUploadActivity.class);
            i.putExtra("FILE_PATH", file.getPath());
            i.putExtra("FILE_NAME", mImageName);
            startActivity(i);
        }
    }

    private File storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return pictureFile;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return null;
        }
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
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
        }
        else if(item.getItemId() == R.id.action_upload) {
//            Intent intent = new Intent(HomeActivity.this, );
//            startActivity(intent);
        }
        return true;
    }

}
