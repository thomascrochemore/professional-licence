package com.example.thomas.p54_mobile.fragments.guest;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.view.guest.MenuActivity;

public class StartFragment extends Fragment{

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_guest_start,container,false);
    }

    @Override public void onResume()
    {
        super.onResume();
        MenuActivity activity = (MenuActivity) getActivity();
        // ajout boutons inscription et connexion
        activity.bindNavigate(activity.findViewById(R.id.buttonSignIn),R.id.guestRelativeLayout,new SignInFragment());
        activity.bindNavigate(activity.findViewById(R.id.buttonSignUp),R.id.guestRelativeLayout,new SignUpFragment());
    }

    @Override public void onPause()
    {
        super.onPause();
        MenuActivity activity = (MenuActivity) getActivity();
        // ajout boutons inscription et connexion
        activity.unbind(activity.findViewById(R.id.buttonSignIn));
        activity.unbind(activity.findViewById(R.id.buttonSignUp));
    }
}
