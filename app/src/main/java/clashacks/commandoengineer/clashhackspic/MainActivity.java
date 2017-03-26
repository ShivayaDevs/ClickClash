package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText mUsername;
    Button mLogin;
    private DatabaseReference myDbRefUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbRefUsers = FirebaseDatabase.getInstance().getReference("users");

        //getting shared preferences
        final SharedPreferences pref= getSharedPreferences("pref",MODE_PRIVATE);
        String username = pref.getString("username","");
//        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        if(!username.equals("") && !username.isEmpty()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        mUsername = (EditText)findViewById(R.id.input_username);
        mLogin = (Button)findViewById(R.id.bt_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();

                if(username == ""){
                        Toast.makeText(MainActivity.this, "Empty username!", Toast.LENGTH_SHORT).show();
                }else{
                    //storing shared preferences
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",username);
                    editor.apply();

                    myDbRefUsers.child(username).setValue(0);

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }

               // startActivity(new Intent(MainActivity.this, AddImage.class));
            }
        });
    }
}
