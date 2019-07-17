package com.example.thomas.p54_mobile.view.user.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.Property;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.ActivityService;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 16/01/2018.
 */

public class SeeActivityActivity extends BaseActivity
{
    Long id = Long.MIN_VALUE;
    Thread t;
    private NetworkUIRunnable<List<Property>> initRunnable2 = new NetworkUIRunnable<List<Property>>()
    {
        @Override public List<Property> executeNetwork(BaseActivity activity) throws HttpException
        {
            // textview
            Bundle donnees = getIntent().getExtras();
            String id_string = donnees.getString("@string/id");
            id = Long.parseLong(id_string);
            ((TextView) activity.findViewById(R.id.textViewSeeActivityName)).setText(ActivityService.findOne(id).getName());
            return null;
        }
        @Override public void onSuccess(BaseActivity activity, List<Property> props)
        {
            // nothing
        }
    };
    private NetworkUIRunnable<List<Property>> initRunnable = new NetworkUIRunnable<List<Property>>()
    {
        @Override public List<Property> executeNetwork(BaseActivity activity) throws HttpException
        {
            // listView
            Bundle donnees = getIntent().getExtras();
            String id_string = donnees.getString("@string/id");
            id = Long.parseLong(id_string);
            return ActivityService.findPropertiesOfActivity(id);
        }
        @Override public void onSuccess(BaseActivity activity, List<Property> props)
        {
            for (int i = 0; i < props.size(); i++)
            {
                String name = props.get(i).getName();
                String value_type = props.get(i).getValueType();
                HashMap<String, String> acti = new HashMap<>();
                acti.put("name", name);
                acti.put("value_type", value_type);
                activitiesPropertiesList.add(acti);
            }

            ListAdapter adapter = new SimpleAdapter(SeeActivityActivity.this, activitiesPropertiesList,
                    R.layout.list_activity_properties, new String[]{ "name", "value_type"},
                    new int[]{R.id.textViewActivityPropertiesName, R.id.textViewActivityPropertiesValueType});
            lv.setAdapter(adapter);
        }
    };

    private ListView lv;
    ArrayList<HashMap<String, String>> activitiesPropertiesList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_activity);
        activitiesPropertiesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvSeeActivityProperties);
        initRunnable.setActivity(this);
        initRunnable2.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        t = new Thread(initRunnable);
        t.start();
        t = new Thread(initRunnable2);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
    }

    public void backHome(View v)
    {
        this.pushActivity(AccountActivity.class);
    }
}
