package com.example.dvidr_000.lighthauzproject;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment {

    int id=0;


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

        try {
            id = getArguments().getInt("USER_ID");
        }
        catch (Exception e){
            id = getActivity().getIntent().getIntExtra("USER_ID",0);
        }
        getActivity().setTitle("User Profile");
        setHasOptionsMenu(true);

        setDetails(v);

        // Inflate the layout for this fragment
        return v;
    }

    public void setDetails(View v){

        User user = User.getUsers().get(id);

        TextView name = (TextView) v.findViewById(R.id.tvUserProfileName);
        TextView occupation = (TextView) v.findViewById(R.id.tvUserProfileOccupationFill);
        TextView interest = (TextView) v.findViewById(R.id.tvUserProfileInterestFill);
        TextView about = (TextView) v.findViewById(R.id.tvUserProfileAboutMeFIll);
        TextView email = (TextView) v.findViewById(R.id.tvUserProfileEmailFill);
        ImageView img = (ImageView) v.findViewById(R.id.profilePicture);

        name.setText(user.getName());
        occupation.setText(user.getOccupation());
        interest.setText(user.getInterest());
        about.setText(user.getBio());
        email.setText(user.getEmail());
        img.setImageResource(User.getUsers().get(id).getProfilePic());

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

}
