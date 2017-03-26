package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

public class SelectAndUploadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SelectAndUploadActivity.class.getSimpleName();

    private StorageReference mStorageReference;
    private DatabaseReference myDbRefUsers;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private String myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_select_upload);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        myDbRefUsers = FirebaseDatabase.getInstance().getReference("users");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        findViewById(R.id.button_upload).setOnClickListener(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        setUsersList();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String myUser = prefs.getString("username", "0");
    }

    private void setUsersList() {
        final ArrayList<String> arrayList = new ArrayList<>();
        Log.e(TAG, "setting arraylist");
        myDbRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Datasnapshot's size:" + dataSnapshot.getChildrenCount());
                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();

                for (Map.Entry<String, Object> entry : users.entrySet()) {
                    arrayList.add(entry.getKey());
                    Log.e(TAG, "looped..." + arrayList.size());
                }
                mAdapter.setData(arrayList);
                findViewById(R.id.pbar).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    // UPLOAD function
    private void uploadFile(final String filePath, final String fileName) {

        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference mCallLogsReference = mStorage.getReferenceFromUrl("gs://clashhackspic.appspot.com")
                .child("images");
        StorageReference mFileReference = mCallLogsReference.child(fileName);
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        UploadTask uploadTask = mFileReference.putBytes(bytes);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloadUri = taskSnapshot.getDownloadUrl();
//            }
//        });
//

//        mFileReference.putBytes(bytes);



/*


        Uri file = Uri.fromFile(new File(filePath));
        final StorageReference riversRef = mStorageReference.child("images/" + fileName);
        //TODO: Show a progress bar
        Log.e(TAG, "Uploading...");
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Log.e(TAG, "Uploaded.");

                        //TODO: no benefit of deleting
                        HashMap<String, String> checked = mAdapter.getCheckedItems();
                        if(!checked.containsKey(myUser)){
                            Log.e(TAG, "isFileDeleted: " + new File(filePath).delete());
                        }

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e(TAG,"URI " + uri.toString()) ;
                            }
                        });

//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        Log.e(TAG, downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SelectAndUploadActivity.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                    }
                });*/
    }

    @Override
    public void onClick(View view) {
        Intent intent = this.getIntent();
        String filePath = intent.getStringExtra("FILE_PATH");
        String fileName = intent.getStringExtra("FILE_NAME");

        if (fileName != null && filePath != null) {
            fileName = fileName.substring(0, fileName.length() - 4);
            findViewById(R.id.pbar).setVisibility(View.VISIBLE);
            addEntriesToSelectedUsers(filePath, fileName);
            mRecyclerView.setVisibility(View.GONE);
//            uploadFile(filePath, fileName);
        } else {
            Log.e(TAG, "Filename " + fileName + "FilePath : " + filePath);
        }
    }

    public void addEntriesToSelectedUsers(String filepath, String filename) {

        Bitmap bm = BitmapFactory.decodeFile(filepath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


        HashMap<String, String> checked = mAdapter.getCheckedItems();
        for (HashMap.Entry<String, String> Entry : checked.entrySet()) {
            String user = Entry.getKey();
//            if(myUser != user){
            myDbRefUsers.child(user).child(filename).setValue(encodedImage)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                finish();
                                              }
                                          });
//            } else {
//                myDbRefUsers.child(user).child(filename).setValue(0);
//            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> mData = new ArrayList<>();

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            CheckBox mCheckBox;

            ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.textViewName);
                mCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user = mTextView.getText().toString();
                        setCheckedItem(user);
                        mCheckBox.setChecked(checked.containsKey(user));
                    }
                });
            }
        }

        public void setData(ArrayList<String> data) {
            mData = data;
            notifyDataSetChanged();
            Log.e(TAG, "data changed, size" + mData.size() + mData.toString());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_item_people, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mData.get(position));
            holder.mCheckBox.setChecked(checked.containsKey(mData.get(position)));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public HashMap<String, String> checked = new HashMap<String, String>();

        public void setCheckedItem(String user) {
            if (checked.containsKey(user)) {
                checked.remove(user);
            } else {
                checked.put(user, user);
            }
        }

        public HashMap<String, String> getCheckedItems() {
            return checked;
        }
    }

}
