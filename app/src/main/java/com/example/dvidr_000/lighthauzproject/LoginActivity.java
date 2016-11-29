package com.example.dvidr_000.lighthauzproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {



    String msg="";
    int loginIndex=0;
    Integer imgRes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ArrayList<Idea> ideas = (ArrayList) Idea.getIdeas();
        ArrayList<User> users = (ArrayList) User.getUsers();

        User newUser;
        newUser = new User("admin@admin.com","admin","Admin","01/01/1970","0123456789");
        users.add(newUser);

        newUser = new User("david@lighthauz.com","david","David Long","01/01/1970","0123456789");
        newUser.setOccupation("Student");
        newUser.setInterest("Food & Beverage");
        imgRes = R.drawable.man1;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("bryan@lighthauz.com","bryan","Bryan Tyler","01/01/1970","0123456789");
        newUser.setOccupation("Entrepreneur");
        newUser.setInterest("Restaurant");
        imgRes = R.drawable.man2;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        newUser = new User("andrew@lighthauz.com","andrew","Andrew Jason","01/01/1970","0123456789");
        newUser.setOccupation("Programmer");
        newUser.setInterest("Tech");
        imgRes = R.drawable.man3;
        newUser.setProfilePic(imgRes);
        users.add(newUser);

        Idea newIdea = new Idea("asd","Some category","Some description\n2\n3\n4asddsadasadadadadasadsad",new Date(),2,"Some background","Some problem","Some solution","VP","CS","CR","CH","KA","KR","KP","COST","RS","Some strength","Some weakness","Some opportunities","Some threats");
        ideas.add(newIdea);

        users.get(0).getIdea().add(ideas.size()-1);

        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);
        final Button login = (Button) findViewById(R.id.btnLogin);


        registerLink.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }

        });

        login.setOnClickListener (new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(validate()){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                }else{

                    Intent login = new Intent(LoginActivity.this,HomeActivity.class);
                    login.putExtra("LOGIN_INDEX",loginIndex);
                    LoginActivity.this.startActivity(login);
                    finish();
                }
            }

        });

    }

    public boolean validate(){

        ArrayList<User> users = (ArrayList) User.getUsers();

        boolean fail=false;
        String email = ((EditText) findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

        if (email.isEmpty()) {
            fail =true;
            msg="Please enter email";
            return fail;
        } else {
            fail= !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if(fail)msg="Invalid email";
        }

        if (password.isEmpty()){
            fail=true;
            msg="Please enter password";
            return fail;
        } else if(password.length()<8 && password.length()>12) {
            fail = true;
            msg = "Invalid Password";
            return fail;
        }

        int index=0;
        fail=true;
        do{
            if(email.equals(users.get(index).getEmail())){
                if(password.equals(users.get(index).getPassword())){
                    fail=false;
                    loginIndex=index;
                }
                else{
                    msg="Wrong password";
                }
            }else {
                msg="Email isn't registered";
            }

            index++;
        }while(index<users.size());



       return fail;
    }





}
