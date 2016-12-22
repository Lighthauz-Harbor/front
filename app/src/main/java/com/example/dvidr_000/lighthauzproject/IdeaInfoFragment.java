package com.example.dvidr_000.lighthauzproject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdeaInfoFragment extends Fragment implements View.OnClickListener{

    private EditText title;
    private Spinner category;
    private EditText description;
    private ImageButton img;
    private ProgressDialog pDialog;
    private String imgStr="";
    private List<String> categoryList;
    private ArrayAdapter<String> categoryAdapter;
    private String categoryDefault;
    private Bundle ideaBundle;
    private String content;

    public IdeaInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_idea_info, container, false);

        getActivity().setTitle("Idea Information");
        content = getActivity().getIntent().getStringExtra("EXTRA_CONTENT");
        ideaBundle = getArguments();

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        title = (EditText) v.findViewById(R.id.etIdeaInfoBusinessNameFill);
        description = (EditText) v.findViewById(R.id.etIdeaInfoDescriptionFill);
        img = (ImageButton) v.findViewById(R.id.ic_idea_info);
        img.setOnClickListener(this);
        Button nextBtn = (Button) v.findViewById(R.id.btnNextIdeaInfo);
        nextBtn.setOnClickListener(this);

        categoryList = new ArrayList<>();
        categoryDefault = "Select category";
        categoryList.add(categoryDefault);
        requestCategoryList();
        category = (Spinner) v.findViewById(R.id.etIdeaInforBusinessCategoryFill);
        categoryAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNextIdeaInfo:
                if (validate()){
                    if (ideaBundle==null){
                        ideaBundle = new Bundle();
                    }

                    ideaBundle.putString("TITLE",title.getText().toString());
                    ideaBundle.putString("CATEGORY",category.getSelectedItem().toString());
                    ideaBundle.putString("DESCRIPTION",description.getText().toString());

                    if (!imgStr.isEmpty()){
                        ideaBundle.putString("PIC",imgStr);
                    }

                    BackgroundFragment fragment = new BackgroundFragment();
                    fragment.setArguments(ideaBundle);

                    FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                    tr.replace(R.id.fragment_container_detail,fragment);
                    tr.addToBackStack(null);
                    tr.commit();
                }
                else {
                    Toast.makeText(getContext(), R.string.EmptyField, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ic_idea_info:
                pickImage();
                break;

        }

    }

    public void setDetails(){
        title.setText(ideaBundle.getString("TITLE"));
        description.setText(ideaBundle.getString("DESCRIPTION"));
        String categoryStr = ideaBundle.getString("CATEGORY");
        for (int i=0;i<categoryList.size();i++){
            if (categoryList.get(i).equals(categoryStr))category.setSelection(i);
        }
        imgStr = ideaBundle.getString("PIC");
        MyAdapter.imageLoader(imgStr,img);
    }

    public boolean validate(){
        if (title.getText().toString().isEmpty()){
            title.requestFocus();
            return false;
        }
        else if (category.getSelectedItem().equals(categoryDefault)){
            category.performClick();
            return false;
        }
        else if (description.getText().toString().isEmpty()){
            description.requestFocus();
            return false;
        }
        return true;
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
                    final String path = getImagePath(getContext(),data.getData());

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
                                    imgStr = obj.getString("secure_url");
                                    pDialog.dismiss();
                                    img.setImageURI(data.getData());
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

    public static String getImagePath(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public void requestCategoryList(){

        // Tag used to cancel the request
        String tag_json = "json_object_req";

        String url = "http://lighthauz.herokuapp.com/api/category/list";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray myArray;
                        VolleyLog.d(response.toString());
                        try{
                            myArray = response.getJSONArray("list");
                            for(int i=0;i<myArray.length();i++){
                                categoryList.add(myArray.get(i).toString());
                            }
                            categoryAdapter.notifyDataSetChanged();
                            if (content.equals("EDIT_IDEA")){
                                setDetails();
                            }
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
