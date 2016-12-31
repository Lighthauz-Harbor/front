package com.example.dvidr_000.lighthauzproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment implements MyAdapter.ItemClickCallback{
    private RecyclerView recView;
    public MyAdapter adapter;
    private TextView notice;
    private ProgressBar pb;
    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private List<Comment> comments;
    private String idStr;
    private EditText commentBox;
    private String commentStr;
    private ImageButton btnComment;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        getComment();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment,container,false);

        getActivity().setTitle("Comments");
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();

        comments = new ArrayList<>();

        try {
            idStr = getArguments().getString("IDEA_ID");
        }
        catch (Exception e){
            idStr = getActivity().getIntent().getStringExtra("IDEA_ID");
        }

        notice = (TextView) v.findViewById(R.id.CommentNotice);
        pb = (ProgressBar) v.findViewById(R.id.pBarComment);
        recView = (RecyclerView) v.findViewById(R.id.rec_list_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        recView.setLayoutManager(layoutManager);

        commentBox = (EditText) v.findViewById(R.id.etComment);
        btnComment = (ImageButton) v.findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    comment();
                }
            }
        });

        adapter = new MyAdapter("COMMENT_LIST", getActivity(),comments);
        adapter.setItemClickCallback(this);
        return v;
    }

    @Override
    public void onItemClick(int p, View view) {
        switch (view.getId()){
            case R.id.ic_list_comment:
            case R.id.tv_title_comment:
                Bundle args = new Bundle();
                args.putString("USER_ID",comments.get(p).getId());

                ViewProfileFragment fragment2 = new ViewProfileFragment();
                fragment2.setArguments(args);

                FragmentTransaction tr2 = getActivity().getSupportFragmentManager().beginTransaction();
                tr2.replace(R.id.fragment_container_detail,fragment2);
                tr2.addToBackStack(null);
                tr2.commit();
                break;
        }

    }

    public void getComment(){
        notice.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/comment/list/"+idStr;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());
                        JSONArray myArray;
                        comments.clear();
                        if (response.isNull("fail")) {
                            try {
                                myArray = response.getJSONArray("list");
                                if (myArray.length()==0){
                                    notice.setVisibility(View.VISIBLE);
                                }
                                else {
                                    for (int i = myArray.length()-1; i >=0 ; i--) {
                                        JSONObject author = ((JSONObject) myArray.get(i)).getJSONObject("author");
                                        JSONObject commentObj = ((JSONObject) myArray.get(i)).getJSONObject("comment");
                                        String userId = author.getString("id");
                                        String name = author.getString("name");
                                        String profilePic = author.getString("profilePic");
                                        String commentText = commentObj.getString("text");
                                        Long timestamp = commentObj.getLong("timestamp");

                                        Comment newComment = new Comment(userId, name, profilePic,commentText,timestamp);
                                        comments.add(newComment);
                                    }
                                    recView.setAdapter(adapter);
                                }

                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        else {
                            try {
                                String msg = response.getString("fail");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e("MYAPP", "unexpected JSON exception", e);
                            }
                        }
                        pb.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pb.setVisibility(View.GONE);
            }
        });

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

    public boolean validate(){
        commentStr = commentBox.getText().toString().trim();
        if (commentStr.isEmpty()){
            return false;
        }
        else return true;
    }

    public void comment(){
        btnComment.setEnabled(false);
        // Tag used to cancel the request
        String tag_json = "json_object_req";
        String url = "http://lighthauz.herokuapp.com/api/comment";

        HashMap<String,String> params = new HashMap<>();
        params.put("userId",user.get(SessionManager.KEY_ID));
        params.put("ideaId",idStr);
        params.put("comment",commentStr);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String msg = response.getString("fail");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }
                        btnComment.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                btnComment.setEnabled(true);
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                if (mStatusCode== HttpURLConnection.HTTP_OK){
                    Log.d(TAG,"Success");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentBox.getText().clear();
                            //close keyboard
                            ((InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
                            onResume();
                        }
                    });

                }
                return super.parseNetworkResponse(response);
            }
        };

        // Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

}
