package clashacks.commandoengineer.clashhackspic;

import android.animation.LayoutTransition;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);


        List<String> imageUrls = new ArrayList<String>();
        String completePath = Environment.getExternalStorageDirectory() + "/ClashHacksPic/" ;
        File dir = new File(completePath);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            Uri imageUri = Uri.fromFile(file);
            imageUrls.add(imageUri.toString());
        }
        LibraryAdapter adapter = new LibraryAdapter(this,imageUrls);
        mRecyclerView.setAdapter(adapter);

    }
}
