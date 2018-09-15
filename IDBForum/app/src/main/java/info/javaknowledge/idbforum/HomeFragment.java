package info.javaknowledge.idbforum;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.javaknowledge.idbforum.adapter.MyDividerItemDecoration;
import info.javaknowledge.idbforum.adapter.QuestionAdapter;
import info.javaknowledge.idbforum.adapter.QuestionAdapter2;
import info.javaknowledge.idbforum.model.Question;
import info.javaknowledge.idbforum.model.Tag;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, QuestionAdapter2.QuestionsAdapterListener{
    View rootView;
    private RecyclerView listView;
    private QuestionAdapter2 adapter;
    private ArrayList<Question> quesList;
    private String URL_TO_QUES = "http://www.javaknowledge.info/idb_forum/getquestion.php";
    //private String URL_TO_QUES = "http://192.168.1.67/idb_forum/getquestion.php";
    private String TAG = Question.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    SearchView quesSearch;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        listView = (RecyclerView) rootView.findViewById(R.id.question_list);
        quesSearch = (SearchView) rootView.findViewById(R.id.quesSearchView);
        quesSearch.setOnQueryTextListener(this);
        quesList = new ArrayList<>();
        adapter = new QuestionAdapter2(getActivity(), quesList, this);
        //listView.setAdapter(adapter);
        //new
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
//        listView.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 0));
        listView.setAdapter(adapter);
        //end
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchQuestion();
                                    }
                                }
        );

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Question item = (Question) listView.getItemAtPosition(position);
//                //QuestionActivity nextFrag = new QuestionActivity();
//                Bundle args = new Bundle();
//                args.putString("title", item.getQues_title());
//                args.putString("qid", String.valueOf(item.getQues_id()));
//                args.putString("upvote", String.valueOf(item.getUp_vote()));
//                args.putString("date", item.getQues_date());
//                args.putString("desc", item.getQues_desc());
//                args.putString("image_url", item.getImage_url());
//                args.putString("id", String.valueOf(item.getUser_id()));
//                args.putSerializable("taglist", (ArrayList<Tag>)item.getTags());
//                //args.putString("tag", item.getEvt_contact());
////                nextFrag.setArguments(args);
////
////                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                FragmentTransaction transaction = fragmentManager.beginTransaction();
////                transaction.replace(R.id.fragone, nextFrag, "evt_details");
////                transaction.addToBackStack(null);
////                transaction.commit();
//                Intent intent = new Intent(getActivity(), QuestionActivity.class);
//                intent.putExtras(args);
//                startActivity(intent);
//
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), AdQuestionActivity.class);
               //intent.setFlags()
                startActivity(intent);
            }
        });



        return rootView;
    }
    @Override
    public void onRefresh() {
        fetchQuestion();
    }


    private void fetchQuestion() {
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        // appending offset to url
        String url = URL_TO_QUES;
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
                                    Question e = new Question();
                                    e.setQues_id(Integer.parseInt(json_data.getString("ques_id")));
                                    e.setQues_title(json_data.getString("ques_title"));
                                    e.setQues_desc(json_data.getString("ques_desc"));
                                    e.setUp_vote(Integer.parseInt(json_data.getString("up_vote")));
                                    e.setComment_count(Integer.parseInt(json_data.getString("comment_count")));
                                    e.setQues_date(json_data.getString("ques_date"));
                                    e.setUser_id(json_data.getString("user_id"));
                                    e.setImage_url(json_data.getString("image_url"));
                                    //Log.d("value---", e.getComment_count()+"");

                                    JSONArray tag_data = json_data.getJSONArray("tags");
                                    if(tag_data.length()>0){

                                        List<Tag> tagList = new ArrayList<Tag>();
                                        tagList.clear();
                                        for (int j = 0; j <tag_data.length() ; j++) {
                                            Tag tag = new Tag();
                                            JSONObject tag_json_data = tag_data.getJSONObject(j);
                                            tag.setTag_id(tag_json_data.getString("tag_id"));
                                            tag.setTag_name(tag_json_data.getString("tag_name"));
                                            tag.setTag_color(tag_json_data.getString("tag_color"));
                                            tagList.add(tag);
                                        }
                                        e.setTags(tagList);
                                    }
                                    else{
                                        List<Tag> tagList = new ArrayList<Tag>();
                                        tagList.clear();
                                        Tag tag = new Tag("0", "no tag", "#ffffff");
                                        tagList.add(tag);
                                        e.setTags(tagList);
                                    }


                                    quesList.add(e);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getCause());
                if (error.getClass().getName().equals("com.android.volley.ParseError")){
                    Toast.makeText(getActivity(), "No question yet", Toast.LENGTH_SHORT).show();
                }else
                Toast.makeText(getActivity(), "No Internet Connection or Service Unavailable Right Now"
                        + error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }};

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        HomeFragment.this.adapter.getFilter().filter(newText.toString());
        return false;
    }
    @Override
    public void onQuestionSelected(Question item) {
        //Toast.makeText(getActivity(), "Selected: " + contact.getQues_title() + ", " + contact.getQues_desc(), Toast.LENGTH_LONG).show();
        //Question item = (Question) listView.getItemAtPosition(position);
                //QuestionActivity nextFrag = new QuestionActivity();
                Bundle args = new Bundle();
                args.putString("title", item.getQues_title());
                args.putString("qid", String.valueOf(item.getQues_id()));
                args.putString("upvote", String.valueOf(item.getUp_vote()));
                args.putString("date", item.getQues_date());
                args.putString("desc", item.getQues_desc());
                args.putString("image_url", item.getImage_url());
                args.putString("id", String.valueOf(item.getUser_id()));
                args.putSerializable("taglist", (ArrayList<Tag>)item.getTags());
                //args.putString("tag", item.getEvt_contact());
//                nextFrag.setArguments(args);
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragone, nextFrag, "evt_details");
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtras(args);
                startActivity(intent);
    }
}
