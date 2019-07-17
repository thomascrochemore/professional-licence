package com.example.thomas.p54_mobile.view.user.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.ActivityRequest;
import com.example.thomas.p54_mobile.model.Property;
import com.example.thomas.p54_mobile.model.PropertyRequest;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.service.ActivityService;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;
import com.example.thomas.p54_mobile.view.user.sessions.CreateSessionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 13/01/2018.
 */

public class CreateActivityActivity extends BaseActivity
{
    private ClickNetworkUIListener<Activity> onSubmit = new ClickNetworkUIListener<Activity>()
    {
        @Override
        public Activity executeNetwork(BaseActivity a, View view) throws HttpException
        {
            // récup data
            String nameActivity =((EditText) a.findViewById(R.id.editTextCreateActivityNomActivity)).getText().toString();
            // modele : request
            PropertyRequest propertyRequest = new PropertyRequest();
            propertyRequest.setValue_type("string");
            propertyRequest.setName("name");
            // list property
            List<PropertyRequest> properties = new ArrayList<>();
            properties.add(propertyRequest);
            // activity
            ActivityRequest activity = new ActivityRequest();
            activity.setName(nameActivity);
            activity.setPropertiesRequest(properties);
            // requete
            return ActivityService.createActivity(activity);
        }
        @Override
        public void onSuccess(BaseActivity activity, Activity result, View view)
        {
            activity.pushActivity(AllActivitiesActivity.class);
        }
    };

    private ListView lv;
    ArrayList<HashMap<String, String>> addProperty;
    ArrayAdapter<String> adapter;
    ArrayList<String> str1;

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);
        addProperty = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAddPropertiesActivity);
        onSubmit.setActivity(this);

        /*str1 = new ArrayList<>();
        str1.add("First Row");
        str1.add("Second Row");
        str1.add("Third Row");
        str1.add("Fourth Row");
        str1.add("Fifth Row");
        adapter = new ArrayAdapter(this, 0);
        lv.setAdapter(adapter);*/

    }

    @Override public void onResume()
    {
        super.onResume();
        this.findViewById(R.id.buttonCreateActivityActivity).setOnClickListener(onSubmit);
    }

    @Override public void onPause()
    {
        super.onPause();
        this.findViewById(R.id.buttonCreateActivityActivity).setOnClickListener(null);
    }

    public void addProperty(View v)
    {
        /*ArrayList<String> str1 = new ArrayList<String>();

        str1.add("First Row");
        str1.add("Second Row");
        str1.add("Third Row");
        str1.add("Fourth Row");
        str1.add("Fifth Row");

        ArrayAdapter<String> adapter= new ListAdapter(this,str1);
        list.setAdapter(adapter);
        then add your EditText text into str1 and then called adapter.notifyDataSetChanged(); like

        str1.add(edit_message.getText().toString());
        adapter.notifyDataSetChanged();*/
    }
}
