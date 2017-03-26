package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText mUsername;
    Button mLogin;
    private DatabaseReference myDbRefUsers;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("TAG", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbRefUsers = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        //getting shared preferences
//        final SharedPreferences pref= getSharedPreferences("pref",MODE_PRIVATE);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("username", "");
//        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        if (!username.equals("") && !username.isEmpty()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        mUsername = (EditText) findViewById(R.id.input_username);
        mLogin = (Button) findViewById(R.id.bt_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mUsername.getText().toString();

                if (username == "") {
                    Toast.makeText(MainActivity.this, "Empty username!", Toast.LENGTH_SHORT).show();
                } else {
                    //storing shared preferences
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", username);
                    editor.apply();

                    myDbRefUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                myDbRefUsers.child(username).setValue(0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }

                // startActivity(new Intent(MainActivity.this, AddImage.class));
            }
        });
    }
}
