package clashacks.commandoengineer.clashhackspic;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SelectAndUploadActivity extends AppCompatActivity {
    private static final String TAG = SelectAndUploadActivity.class.getSimpleName();
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_select_upload);

        mStorageReference = FirebaseStorage.getInstance().getReference();

    }

    private void uploadFile(String filePath, String fileName){
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference riversRef = mStorageReference.child("images/" + fileName);
        //TODO: Show a progress bar
        Log.e(TAG, "Uploading...");
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Log.e(TAG, "Uploaded.");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SelectAndUploadActivity.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
