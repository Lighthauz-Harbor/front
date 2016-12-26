package com.example.dvidr_000.lighthauzproject;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private ProgressDialog pDialog;
    private ProgressBar pb;
    private ImageView profilePic;
    private Button submit;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private RelativeLayout layout;

    private String msg;
    private String newName;
    private String newUsername;
    private String newPic;
    private String newBio;
    private String newPassword;
    private String newPasswordConf;
    private String oldPic;
    private Long newDob;
    private Long oldDob;

    String emailStr;
    String bioStr;
    String nameStr;

    private EditText name;
    private EditText confirm;
    private EditText dob;
    private EditText email;
    private EditText password;
    private EditText bio;

    private SessionManager sessionManager;
    private HashMap<String,String> user;
    private String idStr;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        getActivity().setTitle("Edit Profile");

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pb = (ProgressBar) v.findViewById(R.id.pBarEditProfile);
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        idStr = user.get(SessionManager.KEY_ID);
        msg="";
        newDob=null;
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        name = (EditText) v.findViewById(R.id.etEditProfileName);
        confirm = (EditText) v.findViewById(R.id.etEditProfileConfirmPass);
        dob = (EditText) v.findViewById(R.id.etEditProfileDate);
        dob.setInputType(InputType.TYPE_NULL);
        email = (EditText) v.findViewById(R.id.etEditProfileEmail);
        bio = (EditText) v.findViewById(R.id.etEditProfileAboutMe);
        password = (EditText) v.findViewById(R.id.etEditProfilePass);
        submit = (Button) v.findViewById(R.id.btnSaveEditProfile);
        layout = (RelativeLayout) v.findViewById(R.id.edit_profile_layout);

        setDateField();

        profilePic = (ImageView) v.findViewById(R.id.ic_edit_profile);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    datePickerDialog.show();
                v.clearFocus();
            }
        });

        submit.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(validate()){
                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();

                }else {
                    if (checkChanges()){
                        updateProfile();
                    }
                }
            }

        });

        requestProfile();

        return v;
    }

    protected void setDateField(){
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDob = newDate.getTimeInMillis();
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    protected boolean validate(){
        boolean fail=false;
        msg="";
        newUsername = email.getText().toString().trim();
        newPassword = password.getText().toString().trim();
        newName = name.getText().toString().trim();
        newPasswordConf = confirm.getText().toString().trim();
        newBio = bio.getText().toString().trim();

        if(dob.getText().toString().isEmpty()){
            fail=true;
            msg="Please enter date of birth";
        }

        if(newName.isEmpty()){
            fail=true;
            msg="Please enter name";
        }

        if(newBio.isEmpty()){
            fail=true;
            msg="Please enter bio";
        }

        if (!newPassword.isEmpty()){
            if(newPassword.length()<8 && newPassword.length()>12)
            {
                fail=true;
                msg="Invalid Password";
            }else if(!newPassword.equals(newPasswordConf)){
                fail=true;
                msg="Passwords don't match";
            }
        }

        if (newUsername.isEmpty()) {
            fail =true;
            msg="Please enter email";
        } else {
            fail= !android.util.Patterns.EMAIL_ADDRESS.matcher(newUsername).matches();
            if(fail)msg="Invalid email";
        }

        return fail;
    }

    public boolean checkChanges(){
        boolean change = false;

        if (!newName.equals(nameStr)){
            change = true;
        }
        else if (!newUsername.equals(emailStr)){
            change = true;
        }
        else if (!newBio.equals(bioStr)) {
            change = true;
        }
        else if (newPic!=null) {
            change = true;
        }
        else if (!newPassword.isEmpty()){
            change = true;
        }
        else if (newDob!=null){
            change = true;
        }

        return change;
    }

    public void requestProfile(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/users/get/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url + idStr ,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(response.toString());

                        try{
                            nameStr = response.getString("name");
                            emailStr = response.getString("username");
                            bioStr = response.getString("bio");
                            oldPic = response.getString("profilePic");
                            oldDob = response.getLong("dateOfBirth");

                            name.setText(nameStr);
                            email.setText(emailStr);
                            bio.setText(bioStr);
                            dob.setText(dateFormatter.format(oldDob));
                            MyAdapter.imageLoader(oldPic,profilePic);

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

    protected void updateProfile(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        final String URL = "https://lighthauz.herokuapp.com/api/users/update/" + idStr;
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("name", newName);
        params.put("oldUsername", user.get(SessionManager.KEY_USERNAME));
        params.put("username", newUsername);
        if (newDob!=null)params.put("dateOfBirth", newDob.toString());
        else params.put("dateOfBirth", oldDob.toString());
        params.put("bio", newBio);
        if (newPic!=null)params.put("profilePic", newBio);
        params.put("password", newPassword);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String feedback;

                        try {
                            feedback = response.getString("message");
                            Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        // add the request object to the queue to be executed
        MySingleton.getInstance(getContext()).addToRequestQueue(req,tag_json);
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "Error occured. Please try again.", Toast.LENGTH_SHORT).show();
            }
            else {
                try{
                    final String path = IdeaInfoFragment.getImagePath(getContext(),data.getData());

                    AsyncTask upload = new AsyncTask() {
                        JSONObject obj=null;
                        @Override
                        protected void onPreExecute() {
                            pDialog.setMessage("Uploading image...");
                            pDialog.show();
                        }

                        @Override
                        protected Object doInBackground(Object[] objects) {
                            Map config = new HashMap();
                            config.put("cloud_name", "lighthauz-harbor");
                            Cloudinary cloudinary = new Cloudinary(config);
                            Map result=null;
                            try{
                                result = cloudinary.uploader().unsignedUpload(path,"ac9xvojb", ObjectUtils.emptyMap());
                                obj = new JSONObject(result);
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            try{
                                if (!obj.isNull("secure_url")){
                                    newPic = obj.getString("secure_url");
                                    pDialog.dismiss();
                                    profilePic.setImageURI(data.getData());
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };
                    upload.execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
