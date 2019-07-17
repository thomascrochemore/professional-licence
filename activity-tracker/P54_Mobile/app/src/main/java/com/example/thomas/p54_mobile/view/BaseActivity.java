package com.example.thomas.p54_mobile.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by couty on 16/12/2017.
 */

public class BaseActivity extends AppCompatActivity
{
    public void navigate(int layoutId,Fragment fragment)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.commit();
    }

    public void pushActivity(Class<? extends Activity> activityClass)
    {
        Intent newIntent = new Intent(this, activityClass);
        startActivity(newIntent);
    }

    public void bindNavigate(View view, final int layoutId, final Fragment fragment)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            int layout;
            Fragment frag;
            {
                layout = layoutId;
                frag = fragment;
            }
            @Override
            public void onClick(View view) {
                navigate(layout,frag);
            }
        });
    }

    public void bindPushActivity(View view,final Class<? extends Activity> activityClass)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            private Class<? extends Activity> aClass;
            {
                aClass = activityClass;
            }
            @Override
            public void onClick(View view) {
                pushActivity(activityClass);
            }
        });
    }

    public void unbind(View view){
        view.setOnClickListener(null);
    }

    public void showError(String error){
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
