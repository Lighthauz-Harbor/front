package com.example.dvidr_000.lighthauzproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    String msg=" ";
    String email;
    String phone;
    String password;
    String confirm;
    String dob;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView submit = (TextView) findViewById(R.id.btnRegister);

        submit.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
            if(validate()){
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

            }else {
                User newUser = new User(email,password,name,dob,phone);
                User.getUsers().add(newUser);

                Intent proceed = new Intent(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.startActivity(proceed);
            }
            }

        });

    }

    protected boolean validate(){
        boolean fail=false;
        //EditText email = (EditText) findViewById(R.id.etEmailReg);
        email = ((EditText) findViewById(R.id.etEmailReg)).getText().toString();
        //EditText password = (EditText) findViewById(R.id.etPasswordReg);
        password = ((EditText) findViewById(R.id.etPasswordReg)).getText().toString();
        name = ((EditText) findViewById(R.id.etYourname)).getText().toString();
        confirm = ((EditText) findViewById(R.id.etConfPassword)).getText().toString();
        dob = ((EditText) findViewById(R.id.etDate)).getText().toString();
        phone = ((EditText) findViewById(R.id.etMobileNumber)).getText().toString();
        //EditText name = (EditText) findViewById(R.id.etYourname);
        //EditText confirm = (EditText) findViewById(R.id.etConfPassword);
        //EditText dob = (EditText) findViewById(R.id.etDate);
        //EditText phone = (EditText) findViewById(R.id.etMobileNumber);

        if (email.isEmpty()) {
            fail =true;
            msg="Please enter email";
        } else {
            fail= !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if(fail)msg="Invalid email";
        }

        if (password.isEmpty()){
            fail=true;
            msg="Please enter password";
        } else if(password.length()<8 && password.length()>12)
        {
            fail=true;
            msg="Invalid Password";
        }else if(!password.equals(confirm)){
            fail=true;
            msg="Passwords don't match";
        }

        if(name.isEmpty()){
            fail=true;
            msg="Please enter name";
        }

        if(dob.isEmpty()){
            fail=true;
            msg="Please enter date of birth";
        }
        if(phone.isEmpty()){
            fail=true;
            msg="Please enter phone number";
        }

        return fail;
    }

}