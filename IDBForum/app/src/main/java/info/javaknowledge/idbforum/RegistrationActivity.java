package info.javaknowledge.idbforum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import info.javaknowledge.idbforum.util.MyBounceInterpolator;

public class RegistrationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioGroup rg;
    private RadioButton rb_con;
    private RadioButton rb_ins;
    private RadioButton rb_tra;
    private LinearLayout traineeLayout, instructorIdLayout;
    private CircleImageView mProfileImage;
    private Uri resultUri;
    private EditText mNameField, mPassField, mFullName, mEmailField, mInsId, mTranieeId;
    private Spinner mselectSubject, mselectTsp, mselectBatch;
    private Button mConfirm;
    private boolean isInstructor, isTrainee;
    private String REG_URL = "http://www.javaknowledge.info/idb_forum/registration.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registration");


        rg = (RadioGroup) findViewById(R.id.rg);
        rb_con = (RadioButton) findViewById(R.id.rb_con);
        rb_ins = (RadioButton) findViewById(R.id.rb_ins);
        rb_tra = (RadioButton) findViewById(R.id.rb_tra);
        rb_con.setChecked(true);
        mNameField = (EditText) findViewById(R.id.name);
        mPassField = (EditText) findViewById(R.id.password);
        mFullName = (EditText) findViewById(R.id.fname);
        mEmailField = (EditText) findViewById(R.id.login_email);
        mInsId = (EditText) findViewById(R.id.ins_id);
        mTranieeId = (EditText) findViewById(R.id.trainee_id);
        mselectSubject = (Spinner)findViewById(R.id.subject);
        mselectTsp = (Spinner)findViewById(R.id.tspname);
        mselectBatch = (Spinner)findViewById(R.id.batchId);
        mConfirm = (Button) findViewById(R.id.confirm);

        traineeLayout = (LinearLayout)findViewById(R.id.traineeIdLayout);
        instructorIdLayout = (LinearLayout)findViewById(R.id.instructorIdLayout);
        mProfileImage = (CircleImageView) findViewById(R.id.profileImage);
//        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(700);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 5);
        myAnim.setInterpolator(interpolator);
        mProfileImage.startAnimation(myAnim);



        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb=(RadioButton)findViewById(i);
                if(rb.getText().toString().equals("Consultant")){
                    traineeLayout.setVisibility(LinearLayout.GONE);
                    instructorIdLayout.setVisibility(LinearLayout.GONE);
                    //Toast.makeText(RegistrationActivity.this,"con",Toast.LENGTH_SHORT).show();
                    isInstructor = false;
                    isTrainee = false;
                }
                else if(rb.getText().toString().equals("Instructor")){
                    //traineeLayout.setVisibility(LinearLayout.GONE);
                    //instructorIdLayout.setVisibility(LinearLayout.VISIBLE);
                    slideDown(traineeLayout);
                    slideUp(instructorIdLayout);
                    //Toast.makeText(RegistrationActivity.this,"ins",Toast.LENGTH_SHORT).show();
                    isInstructor = true;
                    isTrainee = false;
                }
                else{
                    //traineeLayout.setVisibility(LinearLayout.VISIBLE);
                    //instructorIdLayout.setVisibility(LinearLayout.GONE);
                    slideDown(instructorIdLayout);
                    slideUp(traineeLayout);
                    //Toast.makeText(RegistrationActivity.this,"tra",Toast.LENGTH_SHORT).show();
                    isInstructor = false;
                    isTrainee = true;
                }
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    //Toast.makeText(RegistrationActivity.this,"ok",Toast.LENGTH_SHORT).show();
                    registration();
                }
            }
        });
    }
    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void registration(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Registering...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //Showing toast message of the response
                        Toast.makeText(RegistrationActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        //Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(RegistrationActivity.this, "Upload Error! "+volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                Bitmap bitmap = null;
                if(resultUri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String image = "";
                if(bitmap!=null)
                    image = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                //StringEntity
                String tag="consultant";
                params.put("image", image);
                params.put("uname", mNameField.getText().toString());
                params.put("pass", mPassField.getText().toString());
                params.put("fname", mFullName.getText().toString());
                params.put("email", mEmailField.getText().toString());
                params.put("subject", mselectSubject.getSelectedItem().toString());
                if(isInstructor){
                    params.put("iid", mInsId.getText().toString());
                    params.put("tspname", mselectTsp.getSelectedItem().toString());
                    tag="instructor";
                }
                if(isTrainee){
                    params.put("tid", mTranieeId.getText().toString());
                    params.put("batchname", mselectBatch.getSelectedItem().toString());
                    tag="trainee";
                }
                params.put("tag", tag);
                params.put("Content-Type", "application/json; charset=utf-8");

                //returning parameters
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);
            //resultUri = imageUri;
            //mProfileImage.setImageURI(resultUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.getUri();
                mProfileImage.setImageURI(resultUri);
                mProfileImage.setAnimation(null);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    public boolean validateFields()
    {
        boolean valid = true;
        if(mNameField.getText().toString().trim().length()==0)
        {
            mNameField.setError("Please enter your name");
            valid = false;
        }
        else
        {
            mNameField.setError(null);
        }
        if(mPassField.getText().toString().trim().length()==0)
        {
            mPassField.setError("Please enter your password");
            valid = false;
        }
        else
        {
            mPassField.setError(null);
        }
        if(mFullName.getText().toString().trim().length()==0)
        {
            mFullName.setError("Please enter your full name");
            valid = false;
        }
        else
        {
            mFullName.setError(null);
        }
        if (mEmailField.getText().toString().trim().length() == 0)
        {
            mEmailField.setError("Please enter your email");
            valid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mEmailField.getText().toString().trim()).matches())
        {
            mEmailField.setError("Please enter a valid email");
            valid = false;
        }
        else
        {
            mEmailField.setError(null);
        }
        if(isInstructor){
            if(mInsId.getText().toString().trim().length()==0)
            {
                mInsId.setError("Please enter your instructor id");
                valid = false;
            }
            else
            {
                mInsId.setError(null);
            }
        }
        if(isTrainee){
            if(mTranieeId.getText().toString().trim().length()==0)
            {
                mTranieeId.setError("Please enter your trainee id");
                valid = false;
            }
            else
            {
                mTranieeId.setError(null);
            }
        }       return valid;
    }
}
