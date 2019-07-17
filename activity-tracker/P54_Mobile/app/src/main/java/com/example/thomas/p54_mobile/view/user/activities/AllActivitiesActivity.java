package com.example.thomas.p54_mobile.view.user.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.ActivityRequest;
import com.example.thomas.p54_mobile.model.Property;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.ActivityService;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;
import com.example.thomas.p54_mobile.view.user.account.UserAccountActivity;
import com.example.thomas.p54_mobile.view.user.sessions.ActivitySessionsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 11/01/2018.
 */

public class AllActivitiesActivity extends BaseActivity
{
    Thread t;
    Activity[] tabActivities; // utils: stockage d'activités
    private NetworkUIRunnable<Activity[]> initRunnable = new NetworkUIRunnable<Activity[]>()
    {
        @Override public Activity[] executeNetwork(BaseActivity activity) throws HttpException
        {
            return RestService.get("/member/activity",Activity[].class);
        }
        @Override public void onSuccess(BaseActivity activity, Activity[] activities)
        {
            for (int i = 0; i < activities.length; i++)
            {
                String name = activities[i].getName();
                HashMap<String, String> acti = new HashMap<>();
                acti.put("name", name);
                activitiesList.add(acti);
            }

            ListAdapter adapter = new SimpleAdapter(AllActivitiesActivity.this, activitiesList,
                    R.layout.list_all_activities, new String[]{ "name"},
                    new int[]{R.id.nameListActivities});
            lv.setAdapter(adapter);
            // export
            tabActivities = activities;
        }
    };

    // utils: list view dans le layout
    private ListView lv;
    ArrayList<HashMap<String, String>> activitiesList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        activitiesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        activitiesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
        activitiesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
    }

    // voir les sessions d'une activité
    public void myClickHandlerListActivitySessions(View v)
    {
        // layout cliqué
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        // récupération de l'id de l'activité
        Long id = Long.MIN_VALUE;
        for (int i = 0; i < lv.getChildCount(); i++)
            if(lv.getChildAt(i) == vwParentRow)
                id = tabActivities[i].getId();
        // envoi des données
        Intent newIntent = new Intent(this, ActivitySessionsActivity.class);
        newIntent.putExtra("@string/id", ""+id);
        startActivity(newIntent);
    }

    // voir une activité
    public void myClickHandlerListActivitySee(View v)
    {
        // layout cliqué
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        // récupération de l'id de l'activité
        Long id = Long.MIN_VALUE;
        for (int i = 0; i < lv.getChildCount(); i++)
            if(lv.getChildAt(i) == vwParentRow)
                id = tabActivities[i].getId();
        // envoi des données
        Intent newIntent = new Intent(this, SeeActivityActivity.class);
        newIntent.putExtra("@string/id", ""+id);
        startActivity(newIntent);
    }
}
