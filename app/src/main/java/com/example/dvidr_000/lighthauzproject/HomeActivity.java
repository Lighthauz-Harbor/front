package com.example.dvidr_000.lighthauzproject;


import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new NewsFeedFragment()).commit();
        pos = 1;

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                if(pos!=1) {
                                    NewsFeedFragment fragment1 = new NewsFeedFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment1).commit();
                                    pos=1;
                                }
                                break;
                            case R.id.nav_idea:
                                if(pos!=2) {
                                    IdeaListFragment fragment2 = new IdeaListFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment2).commit();
                                    pos=2;
                                }
                                break;
                            case R.id.nav_search:
                                if(pos!=3) {
                                    SearchFragment fragment3 = new SearchFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment3).commit();
                                    pos=3;
                                }
                                break;

                            case R.id.nav_connection:
                                if(pos!=4) {
                                    ConnectionFragment fragment4 = new ConnectionFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home, fragment4).commit();
                                    pos=4;
                                }
                                break;
                        }
                        return false;
                    }
                });
    }


}