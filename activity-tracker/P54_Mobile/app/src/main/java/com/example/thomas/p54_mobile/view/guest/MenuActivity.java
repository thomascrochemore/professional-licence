package com.example.thomas.p54_mobile.view.guest;

import android.os.Bundle;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.fragments.guest.StartFragment;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;

/**
 * Created by thomas on 15/12/2017.
 */

public class MenuActivity extends BaseActivity
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        // RestService.setBasurl("http://192.168.0.46:8080"); // api locale
        RestService.setBasurl("http://arch208.iutrs.unistra.fr:8080/api"); // api serveur
        super.onCreate(savedInstanceState);
        navigate(R.id.guestRelativeLayout,new StartFragment());
        setContentView(R.layout.activity_menu);
    }
}