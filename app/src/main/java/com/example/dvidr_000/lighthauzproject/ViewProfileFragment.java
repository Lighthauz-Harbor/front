package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment {

    int id=0;

    private ProgressBar pb;
    private TextView name;
    private TextView about;
    private TextView email;
    private ImageView profilePic;
    private RelativeLayout layout;

    private String idStr;
    private String nameStr;
    private String aboutStr;
    private String emailStr;
    private String profilePicStr;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_profile, container, false);

        pb = (ProgressBar) v.findViewById(R.id.pBarViewProfile);
        name = (TextView) v.findViewById(R.id.tvUserProfileName);
        about = (TextView) v.findViewById(R.id.tvUserProfileAboutMeFIll);
        email = (TextView) v.findViewById(R.id.tvUserProfileEmailFill);
        profilePic = (ImageView) v.findViewById(R.id.ic_view_profile);

        layout = (RelativeLayout) v.findViewById(R.id.view_profile_layout);

        try {
            //id = getArguments().getInt("USER_ID");
            idStr = getArguments().getString("USER_ID");
        }
        catch (Exception e){
            //id = getActivity().getIntent().getIntExtra("USER_ID",0);
            idStr = getActivity().getIntent().getStringExtra("USER_ID");
        }


        getActivity().setTitle("User Profile");
        setHasOptionsMenu(true);

        request();



        // Inflate the layout for this fragment
        return v;
    }

    public void setDetails(){

        /*name.setText(user.getName());
        occupation.setText(user.getOccupation());
        interest.setText(user.getInterest());
        about.setText(user.getBio());
        email.setText(user.getEmail());
        profilePic.setImageResource(User.getUsers().get(id).getProfilePic());*/

        name.setText(nameStr);
        about.setText(aboutStr);
        email.setText(emailStr);
        MyAdapter.imageLoader(profilePicStr,profilePic);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuContact:
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));*/

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
                startActivity(Intent.createChooser(intent, "asd"));


                break;
            case R.id.menuAddUser:
                Toast.makeText(getActivity(),"User added",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void request(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/users/get/id/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url + idStr ,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            nameStr = response.getString("name");
                            emailStr = response.getString("username");
                            aboutStr = response.getString("bio");
                            profilePicStr = response.getString("profilePic");

                            setDetails();
                            pb.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);

                        }
                        catch (JSONException e){
                            Log.e("MYAPP", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        MySingleton.getInstance(getContext()).addToRequestQueue(req, tag_json);
    }

}
