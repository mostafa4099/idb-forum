package info.javaknowledge.idbforum;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dmallcott.dismissibleimageview.DismissibleImageView;

//import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import info.javaknowledge.idbforum.adapter.AnswerAdapter;
import info.javaknowledge.idbforum.model.Answer;
import info.javaknowledge.idbforum.model.Question;
import info.javaknowledge.idbforum.model.Tag;
import info.javaknowledge.idbforum.util.UserSessionManager;

public class QuestionActivity extends AppCompatActivity {
    TextView title, upVote, quesDate, quesDesc, userId, ansDesc;
    ImageView imgup, imgdown;
    DismissibleImageView questionImage;
    boolean isImageFitToScreen;
    Button saveButton;
    String qid;
    private AnswerAdapter adapter;
    private ArrayList<Answer> ansList;
    private NonScrollListView listView;
    private String ANSWER_URL ="http://www.javaknowledge.info/idb_forum/addAnswer.php";
    private String URL_TO_ANS = "http://www.javaknowledge.info/idb_forum/getAnswer.php";
    private Toolbar toolbar;
    private LinearLayout imageHolder;
    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // User Session Manager
        session = new UserSessionManager(getApplicationContext());

        title = (TextView) findViewById(R.id.tvtitle);
        upVote = (TextView) findViewById(R.id.tvupvote);
        quesDate = (TextView) findViewById(R.id.tvtime);
        quesDesc = (TextView) findViewById(R.id.questionBody);
        ansDesc = (TextView) findViewById(R.id.add_answer_body);
        //ansDesc.setShowSoftInputOnFocus(false);
        userId = (TextView) findViewById(R.id.tvuser);
        imgup = (ImageView) findViewById(R.id.imgup);
        imgdown = (ImageView) findViewById(R.id.imgdown);
        saveButton = (Button) findViewById(R.id.btnSave);
        questionImage = (DismissibleImageView) findViewById(R.id.questionImage);
        imageHolder = (LinearLayout) findViewById(R.id.imageHolder);

        listView = (NonScrollListView) findViewById(R.id.answer_list);
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
        ansList = new ArrayList<>();
        adapter = new AnswerAdapter(this, ansList);
        listView.setAdapter(adapter);
        //setListViewHeightBasedOnChildren(listView);

        Bundle args = this.getIntent().getExtras();
        String titletext = args.getString("title");
        actionBar.setTitle(titletext);
        String upvote = args.getString("upvote");
        String dat = args.getString("date");
        String desc = args.getString("desc");
        String id = args.getString("id");
        qid = args.getString("qid");
        String image_url = args.getString("image_url");
        //Toast.makeText(QuestionActivity.this, image_url , Toast.LENGTH_LONG).show();
        ArrayList<Tag> tagList = (ArrayList<Tag>)this.getIntent().getSerializableExtra("taglist");
        if(tagList!=null) {
            //Toast.makeText(this, tagList.get(0).getTag_name(), Toast.LENGTH_SHORT).show();
            LinearLayout tagLayout = (LinearLayout) findViewById(R.id.tag_layout);
            for (int i = 0; i <tagList.size() ; i++) {
                final TextView btnTag = new TextView(this);
                btnTag.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                btnTag.setText(tagList.get(i).getTag_name());
                btnTag.setId(i+1);
                //btnTag.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
                btnTag.setBackgroundResource(R.drawable.layout_bg);
                btnTag.setTextColor(Color.parseColor(tagList.get(i).getTag_color()));
                tagLayout.addView(btnTag);
                int paddingDp = 5;
                float density = this.getResources().getDisplayMetrics().density;
                int paddingPixel = (int)(paddingDp * density+ 0.5f);
                btnTag.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
                setMargins(btnTag, 0, 0, 10, 0);

                btnTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(QuestionActivity.this,
                                btnTag.getText().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        title.setText(titletext);
        upVote.setText(upvote);
        quesDate.setText(dat);
        //StringEntity entity = new StringEntity(desc, "UTF-8");
        //entity.setContentEncoding("UTF-8");
        //byte[] buf = entity.getBytes("UTF-8");
        quesDesc.setText(desc);
        userId.setText(id);
        if(!image_url.equals("null")){
            imageHolder.setVisibility(LinearLayout.VISIBLE);
            Glide.with(this).load(image_url).into(questionImage);
        }
        else{
            imageHolder.setVisibility(LinearLayout.GONE);
        }

        imgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestionActivity.this,
                        "The favorite list would appear on clicking this icon",
                        Toast.LENGTH_LONG).show();
            }
        });

//        questionImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;
//                    questionImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    questionImage.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    questionImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                    questionImage.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//            }
//        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            addAnswer();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                            "Permission denied!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fetchAnswer();

    }
    private void displayDilog(){
        new AlertDialog.Builder(QuestionActivity.this).setTitle("Please Login")
                .setMessage("Do you want to login now?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(QuestionActivity.this, LoginActivity.class);
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
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void addAnswer(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Posting Answer...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ANSWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //Showing toast message of the response
                        Toast.makeText(QuestionActivity.this, s , Toast.LENGTH_LONG).show();
                        ansDesc.setText("");
                        fetchAnswer();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        //Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(QuestionActivity.this, "Upload Error! "+volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("abody", ansDesc.getText().toString());
                params.put("qid", qid);
                params.put("uid", session.getUserName().toString());

                //returning parameters
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }


    private void fetchAnswer() {
        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);
        // appending offset to url
        String url = URL_TO_ANS+"?qid="+qid;
        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        ansList.clear();
                        if (response.length() > 0 && response != null) {
                            // looping through json and adding to event list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject json_data = response.getJSONObject(i);
                                    Answer e = new Answer();
                                    e.setAns_id(json_data.getString("ans_id"));
                                    e.setAns_desc(json_data.getString("ans_desc"));
                                    e.setUp_vote(json_data.getString("up_vote"));
                                    e.setUser_id(json_data.getString("user_id"));
                                    e.setAns_time(json_data.getString("ans_time"));

                                    ansList.add(e);

                                } catch (JSONException e) {
                                    Log.e("Error", "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.notifyDataSetChanged();
                            //setListViewHeightBasedOnChildren(listView);
                        }

                        // stopping swipe refresh
                        //swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Server Error: " + error.getCause());
                if (error.getClass().getName().equals("com.android.volley.ParseError")){
                    Toast.makeText(QuestionActivity.this, "No answer yet", Toast.LENGTH_SHORT).show();
                }else
                Toast.makeText(QuestionActivity.this, "No Internet Connection or Service Unavailable Right Now"
                        + error.getMessage(), Toast.LENGTH_SHORT).show();

                // stopping swipe refresh
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            //if (i == 0)
                view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            Toast.makeText(QuestionActivity.this,view.getMeasuredHeight()+"", Toast.LENGTH_SHORT).show();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount()- 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
    }
    public boolean validateFields()
    {
        boolean valid = true;

        if (ansDesc.getText().toString().trim().length() == 0)
        {
            ansDesc.setError("Please enter your answer");
            valid = false;
        }
        else
        {
            ansDesc.setError(null);
        }

        return valid;
    }

}
