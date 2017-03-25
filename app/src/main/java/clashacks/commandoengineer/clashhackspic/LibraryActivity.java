package clashacks.commandoengineer.clashhackspic;

import android.animation.LayoutTransition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);

        LibraryAdapter adapter = new LibraryAdapter(this);

        mRecyclerView.setAdapter(adapter);

    }
}
