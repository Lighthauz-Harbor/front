package com.example.dvidr_000.lighthauzproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {



    String msg="";
    int loginIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User newUser = new User("admin@admin.com","admin","Admin","01/01/1970","0123456789");
        User.users.add(newUser);

        Idea newIdea = new Idea("tes","Idea",2,"idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea","idea");

        Idea.ideas.add(newIdea);

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
                    login.putExtra("loginIndex",loginIndex);
                    LoginActivity.this.startActivity(login);
                }
            }

        });

    }

    public boolean validate(){
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
            if(email.equals(User.users.get(index).getEmail())){
                if(password.equals(User.users.get(index).getPassword())){
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
        }while(index<User.users.size());



       return fail;
    }





}
