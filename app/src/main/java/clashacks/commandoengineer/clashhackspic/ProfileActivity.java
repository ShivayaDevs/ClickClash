package clashacks.commandoengineer.clashhackspic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.kairos.*;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class ProfileActivity extends AppCompatActivity {

    ImageButton imgButton2, imgButton3, imgButton4, imgButton5;
    private static final int IMAGE_PICK_CODE_1 = 1;
    private static final int IMAGE_PICK_CODE_2 = 2;
    private static final int IMAGE_PICK_CODE_3 = 3;
    private static final int IMAGE_PICK_CODE_4 = 4;
    Kairos myKairos;
    KairosListener listener;
    String subjectId;
    String galleryId;
    String TAG = "KAIROS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imgButton3 = (ImageButton)findViewById(R.id.imageButton3);
        imgButton4 = (ImageButton)findViewById(R.id.imageButton4);
        imgButton5 = (ImageButton)findViewById(R.id.imageButton5);

        // instantiate a new kairos instance
        myKairos = new Kairos();

        // set authentication
        String app_id = "7ea73387";
        String api_key = "b653c6dc6b0356497dd2aa8197e6a21d";
        myKairos.setAuthentication(this, app_id, api_key);
        galleryId = "friends";

        listener = new KairosListener() {
            @Override
            public void onSuccess(String s) {

                Log.d("KAIROS", "success : "+ s );

            }

            @Override
            public void onFail(String s) {
                Log.d(TAG, "FAil : "+ s );

            }
        };

        //TODO : Take username from shared prefs and save it in subjectId

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        subjectId = prefs.getString("username", "");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(subjectId);



        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICK_CODE_1);
            }
        });
        imgButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICK_CODE_2);
            }
        });
        imgButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICK_CODE_3);
            }
        });
        imgButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICK_CODE_4);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_PICK_CODE_1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK_CODE_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mUri = data.getData();
            try {
                Bitmap profileBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                if (profileBmp != null) {
//                    mDpImageView.setImageDrawable(getRoundedBitmapDrawable(profileBmp));
                    imgButton2.setImageBitmap(profileBmp);
                    try {
                        myKairos.enroll(profileBmp, subjectId, galleryId, null, null, null, listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } catch (OutOfMemoryError e) {
                Toast.makeText(this, "Please select a smaller image!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error while choosing image: " + e);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == IMAGE_PICK_CODE_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mUri = data.getData();
            try {
                Bitmap profileBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                if (profileBmp != null) {
//                    mDpImageView.setImageDrawable(getRoundedBitmapDrawable(profileBmp));
                    imgButton3.setImageBitmap(profileBmp);
                    try {
                        myKairos.enroll(profileBmp, subjectId, galleryId, null, null, null, listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } catch (OutOfMemoryError e) {
                Toast.makeText(this, "Please select a smaller image!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error while choosing image: " + e);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == IMAGE_PICK_CODE_3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mUri = data.getData();
            try {
                Bitmap profileBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                if (profileBmp != null) {
//                    mDpImageView.setImageDrawable(getRoundedBitmapDrawable(profileBmp));
                    imgButton4.setImageBitmap(profileBmp);
                    try {
                        myKairos.enroll(profileBmp, subjectId, galleryId, null, null, null, listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } catch (OutOfMemoryError e) {
                Toast.makeText(this, "Please select a smaller image!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error while choosing image: " + e);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == IMAGE_PICK_CODE_4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mUri = data.getData();
            try {
                Bitmap profileBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                if (profileBmp != null) {
//                    mDpImageView.setImageDrawable(getRoundedBitmapDrawable(profileBmp));
                    imgButton5.setImageBitmap(profileBmp);
                    try {
                        myKairos.enroll(profileBmp, subjectId, galleryId, null, null, null, listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } catch (OutOfMemoryError e) {
                Toast.makeText(this, "Please select a smaller image!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error while choosing image: " + e);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
