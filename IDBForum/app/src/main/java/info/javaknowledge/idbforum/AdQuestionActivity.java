package info.javaknowledge.idbforum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.javaknowledge.idbforum.model.Answer;
import info.javaknowledge.idbforum.util.UserSessionManager;

public class AdQuestionActivity extends AppCompatActivity {
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    ImageView imageView;
    private Bitmap bitmap;
    private String UPLOAD_URL = "http://www.javaknowledge.info/idb_forum/addQuestion.php";
    private String URL_TO_TAG = "http://www.javaknowledge.info/idb_forum/getTag.php";
    private String URL_TO_QTITLE = "http://www.javaknowledge.info/idb_forum/getQTitle.php";
    EditText quesDesc;
    Button save;
    AutoCompleteTextView textView;
    NachoTextView cvTag;
    ArrayList<String> selectedTagArray;
    String newTagArray;
    private Toolbar toolbar;
    List<String> tagList, quesList;
    ArrayAdapter<String> qadapter;
    ArrayAdapter<String> tagadapter;
    // User Session Manager Class
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_question);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Question");
        // User Session Manager
        session = new UserSessionManager(getApplicationContext());
        quesList = new ArrayList<>();
        fetchQTitle();
        qadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, quesList);

        textView = (AutoCompleteTextView)
                findViewById(R.id.question_title);
        textView.setAdapter(qadapter);
        imageView = (ImageView)findViewById(R.id.imageView1);
        quesDesc = (EditText) findViewById(R.id.add_question_body);
        save = (Button) findViewById(R.id.btnSave);
        selectedTagArray = new ArrayList<>();
        tagList = new ArrayList<>();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        fetchTag();
        cvTag = (NachoTextView)findViewById(R.id.nacho_text_view);
        //String[] suggestions = new String[]{"android", "java", "spring", "hibernate", "sql", "jquery"};
        tagadapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tagList);
        cvTag.setAdapter(tagadapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTagArray.clear();
                // Iterate over all of the chips in the NachoTextView
                for (Chip chip : cvTag.getAllChips()) {
                    // Do something with the text of each chip
                    CharSequence text = chip.getText();
                    // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
                    Object data = chip.getData();
                    //Toast.makeText(AdQuestionActivity.this, data.toString(), Toast.LENGTH_LONG).show();
                    selectedTagArray.add(chip.getData().toString());
                }
                Gson gson=new Gson();
                newTagArray=gson.toJson(selectedTagArray); // dataarray is list aaray
                if(!searchQTitle(textView.getText().toString(), quesList)){

                    if(!session.isUserLoggedIn()){
                        displayDilog();
                    }
                    else{
                        if(session.getUserRole().equalsIgnoreCase("administrator")||
                                session.getUserRole().equalsIgnoreCase("pa")||
                                session.getUserRole().equalsIgnoreCase("pc")||
                                session.getUserRole().equalsIgnoreCase("instructors")||
                                session.getUserRole().equalsIgnoreCase("trainees")){
                            if(validateFields()){
                                addQuestion();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "Permission denied!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else
                    Toast.makeText(AdQuestionActivity.this, "This question already exists!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void displayDilog(){
        new AlertDialog.Builder(AdQuestionActivity.this).setTitle("Please Login")
                .setMessage("Do you want to login now?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(AdQuestionActivity.this, LoginActivity.class);
                        startActivity(i);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void addQuestion(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //Showing toast message of the response
                        Toast.makeText(AdQuestionActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        //Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(AdQuestionActivity.this, "Upload Error! "+volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = "";
                if(bitmap!=null)
                image = getStringImage(bitmap);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                //StringEntity
                params.put("image", image);
//                try {
//                    params.put("qbody", URLEncoder.encode(quesDesc.getText().toString(), "UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                params.put("qbody", quesDesc.getText().toString());
                params.put("qtitle", textView.getText().toString());
                params.put("tagarray",newTagArray);
                params.put("uid", session.getUserName().toString());
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

    private void fetchTag() {
        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);
        // appending offset to url
        String url = URL_TO_TAG;
        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        tagList.clear();
                        if (response.length() > 0 && response != null) {
                            // looping through json and adding to event list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject json_data = response.getJSONObject(i);
                                    tagList.add(json_data.getString("tag_name"));

                                } catch (JSONException e) {
                                    Log.e("Error", "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            tagadapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        //swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Server Error: " + error.getCause());
                if (error.getClass().getName().equals("com.android.volley.ParseError")){
                    Toast.makeText(AdQuestionActivity.this, "No tag yet", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(AdQuestionActivity.this, "No Internet Connection or Service Unavailable Right Now"
                            + error.getMessage(), Toast.LENGTH_SHORT).show();

                // stopping swipe refresh
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void fetchQTitle() {
        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);
        // appending offset to url
        String url = URL_TO_QTITLE;
        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        quesList.clear();
                        if (response.length() > 0 && response != null) {
                            // looping through json and adding to event list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject json_data = response.getJSONObject(i);
                                    quesList.add(json_data.getString("ques_title"));

                                } catch (JSONException e) {
                                    Log.e("Error", "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            qadapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        //swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Server Error: " + error.getCause());
                if (error.getClass().getName().equals("com.android.volley.ParseError")){
                    Toast.makeText(AdQuestionActivity.this, "No tag yet", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(AdQuestionActivity.this, "No Internet Connection or Service Unavailable Right Now"
                            + error.getMessage(), Toast.LENGTH_SHORT).show();

                // stopping swipe refresh
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private boolean searchQTitle(String searchStr, List<String> aList)
    {
        boolean found = false;
        Iterator<String> iter = aList.iterator();
        String curItem="";

        while ( iter .hasNext() == true )
        {
            curItem =(String) iter .next();
            if (curItem.equalsIgnoreCase(searchStr)  ) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean validateFields()
    {
        boolean valid = true;

        if (textView.getText().toString().trim().length() == 0)
        {
            textView.setError("Please enter question title");
            valid = false;
        }
        else
        {
            textView.setError(null);
        }

        if(quesDesc.getText().toString().trim().length()==0)
        {
            quesDesc.setError("Please enter your password");
            valid = false;
        }
        else
        {
            quesDesc.setError(null);
        }
        if(selectedTagArray.size()==0){
            Toast.makeText(AdQuestionActivity.this, "Please select tag", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
