package com.example.dvidr_000.lighthauzproject;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {

    String msg=" ";
    String emailStr;
    String passwordStr;
    String confirmStr;
    String dobStr;
    String nameStr;

    EditText name;
    EditText confirm;
    EditText dob;
    EditText email;
    EditText password ;

    Button submit;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        name = (EditText) findViewById(R.id.etYourname);
        confirm = (EditText) findViewById(R.id.etConfPassword);
        dob = (EditText) findViewById(R.id.etDate);
        dob.setInputType(InputType.TYPE_NULL);
        email = (EditText) findViewById(R.id.etEmailReg);
        password = (EditText) findViewById(R.id.etPasswordReg);
        submit = (Button) findViewById(R.id.btnRegister);

        setDateField();

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
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

            }else {
                request();
            }
            }

        });

    }

    protected void setDateField(){
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
    }


    protected boolean validate(){
        boolean fail=false;
        msg="";
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        nameStr = name.getText().toString().trim();
        confirmStr = confirm.getText().toString().trim();
        dobStr = dob.getText().toString().trim();


        if(dobStr.isEmpty()){
            fail=true;
            msg="Please enter date of birth";
        }


        if(nameStr.isEmpty()){
            fail=true;
            msg="Please enter name";
        }

        if (passwordStr.isEmpty()){
            fail=true;
            msg="Please enter password";
        } else if(passwordStr.length()<8 && passwordStr.length()>12)
        {
            fail=true;
            msg="Invalid Password";
        }else if(!passwordStr.equals(confirmStr)){
            fail=true;
            msg="Passwords don't match";
        }

        if (emailStr.isEmpty()) {
            fail =true;
            msg="Please enter email";
        } else {
            fail= !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
            if(fail)msg="Invalid email";
        }

        return fail;
    }

    protected void request(){
        // Tag used to cancel the request
        String tag_json = "json_object_req";

        final String URL = "https://lighthauz.herokuapp.com/api/users/create";
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("name", nameStr);
        params.put("username", emailStr);
        params.put("password", passwordStr);
        params.put("role", "user");
        params.put("dateOfBirth", dobStr);


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String feedback;

                        try {
                            if (response.isNull("message")){
                                feedback = response.getString("fail");
                            }
                            else {
                                feedback = response.getString("message");
                                Intent proceed = new Intent(RegisterActivity.this,VerificationActivity.class);
                                proceed.putExtra("username",emailStr);
                                startActivity(proceed);
                                finish();

                            }

                            Toast.makeText(RegisterActivity.this, feedback, Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req,tag_json);
    }

}