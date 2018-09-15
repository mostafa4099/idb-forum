package info.javaknowledge.idbforum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import info.javaknowledge.idbforum.util.UserSessionManager;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText mName, mPassword;
    private Button mLogin, mRegistration, mForgot;
    private ProgressBar progressBar;
    private static final String LOGIN_URL = "http://www.javaknowledge.info/idb_forum/login.php";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASS = "pass";
    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        // User Session Manager
        session = new UserSessionManager(getApplicationContext());
        if(session.isUserLoggedIn()){
            finish();
            // After logout redirect user to Login Activity
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            startActivity(i);
        }

        mName = (EditText) findViewById(R.id.login_name);
        mPassword = (EditText) findViewById(R.id.login_password);

        mLogin = (Button) findViewById(R.id.login_login);
        mRegistration = (Button) findViewById(R.id.login_signup);
        mForgot = (Button) findViewById(R.id.btn_forgot);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    progressBar.setVisibility(View.VISIBLE);
                    doLogin();
                }
            }
        });
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doLogin(){
        final String em = mName.getText().toString().trim();
        final String pss = mPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(response.equalsIgnoreCase("administrator")||
                                response.equalsIgnoreCase("pa")||
                                response.equalsIgnoreCase("pc")||
                                response.equalsIgnoreCase("instructors")||
                                response.equalsIgnoreCase("trainees")){
                            session.createUserLoginSession(response, em);

                            finish();
                            // After logout redirect user to Login Activity
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            // Closing all the Activities
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Staring Login Activity
                            startActivity(i);
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Login error!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"Login Error!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAME, em);
                params.put(KEY_PASS, pss);
                return params;
            }

        };
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public boolean validateFields()
    {
        boolean valid = true;

        if (mName.getText().toString().trim().length() == 0)
        {
            mName.setError("Please enter your name");
            valid = false;
        }
        else
        {
            mName.setError(null);
        }

        if(mPassword.getText().toString().trim().length()==0)
        {
            mPassword.setError("Please enter your password");
            valid = false;
        }
        else
        {
            mPassword.setError(null);
        }

        return valid;
    }
}
