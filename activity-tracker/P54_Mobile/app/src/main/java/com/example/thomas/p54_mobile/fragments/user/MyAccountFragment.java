package com.example.thomas.p54_mobile.fragments.user;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.UserService;
import com.example.thomas.p54_mobile.view.BaseActivity;

public class MyAccountFragment extends Fragment
{
    Thread t;
    private NetworkUIRunnable<User> initRunnable = new NetworkUIRunnable<User>()
    {
        @Override public User executeNetwork(BaseActivity activity) throws HttpException
        {
            return UserService.identity();
        }
        @Override public void onSuccess(BaseActivity activity, User user)
        {
            ((TextView) activity.findViewById(R.id.editTextMyAccountLogin)).setText(user.getLogin());
            ((TextView) activity.findViewById(R.id.editTextMyAccountFirstname)).setText(user.getFirstname());
            ((TextView) activity.findViewById(R.id.editTextMyAccountLastname)).setText(user.getLastname());
        }
    };

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initRunnable.setActivity((BaseActivity) getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_user_account,container,false);
    }

    @Override public void onResume()
    {
        super.onResume();
        // ajout bouton update
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
    }
}
