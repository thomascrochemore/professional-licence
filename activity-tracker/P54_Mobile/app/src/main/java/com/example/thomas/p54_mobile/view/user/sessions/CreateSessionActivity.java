package com.example.thomas.p54_mobile.view.user.sessions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Activity;
import com.example.thomas.p54_mobile.model.ActivityRequest;
import com.example.thomas.p54_mobile.model.Property;
import com.example.thomas.p54_mobile.model.PropertyRequest;
import com.example.thomas.p54_mobile.model.Session;
import com.example.thomas.p54_mobile.model.SessionPropertyRequest;
import com.example.thomas.p54_mobile.model.SessionRequest;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.ActivityService;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.service.SessionService;
import com.example.thomas.p54_mobile.service.UserService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;
import com.example.thomas.p54_mobile.view.user.account.UserAccountActivity;
import com.example.thomas.p54_mobile.view.user.activities.AllActivitiesActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 13/01/2018.
 */

public class CreateSessionActivity extends BaseActivity
{
    Thread t;
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

            ListAdapter adapter = new SimpleAdapter(CreateSessionActivity.this, activitiesList,
                    R.layout.list_create_sessions, new String[]{ "name"},
                    new int[]{R.id.textViewCreateSessionActivity});
            lv.setAdapter(adapter);
        }
    };

    private ClickNetworkUIListener<Session> onSubmit = new ClickNetworkUIListener<Session>()
    {
        @Override
        public Session executeNetwork(BaseActivity a, View view) throws HttpException
        {
            // dpCreateSession
            int jour = 16; // ((DatePicker) a.findViewById(R.id.dpCreateSession)).getDayOfMonth();
            int mois = 1; // ((DatePicker) a.findViewById(R.id.dpCreateSession)).getMonth();
            int an = 2018; // ((DatePicker) a.findViewById(R.id.dpCreateSession)).getYear();
            Date d = new Date(jour, mois, an);
            Long id = Long.parseLong(""+1);

            // model
            SessionPropertyRequest prop = new SessionPropertyRequest();
            prop.setValue_string("string");
            prop.setValue_bool(null);
            prop.setValue_number(null);
            prop.setPropertyId(null);
            List<SessionPropertyRequest> properties = new ArrayList<>();
            properties.add(prop);

            // new session
            Activity activity = ActivityService.findOne(id);
            SessionRequest session = new SessionRequest();
            session.setProperties(properties);
            session.setDate(d);
            session.setActivityId(activity.getId());
            session.setActivity(activity);
            session.setUserId(UserService.identity().getId());
            session.setUser(UserService.identity());

            // requete
            return SessionService.createSession(session);
        }
        @Override
        public void onSuccess(BaseActivity activity, Session result, View view)
        {
            activity.pushActivity(AllActivitiesActivity.class);
        }
    };

    private ListView lv;
    ArrayList<HashMap<String, String>> activitiesList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        activitiesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listCreateSessionProperties);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        t = new Thread(initRunnable);
        t.start();
        this.findViewById(R.id.buttonCreateSession).setOnClickListener(onSubmit);
    }

    @Override public void onPause()
    {
        super.onPause();
        this.findViewById(R.id.buttonCreateSession).setOnClickListener(null);
    }

    public void myClickHandlerCreateSession(View v)
    {
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();

        int id = 0;
        for (int i = 0; i < lv.getChildCount(); i++)
            if(lv.getChildAt(i) == vwParentRow)
                id = i+1;

        // mettre le nombre dans un intent ?

        // envoi des données
        if(id > 0)
        {
            Toast.makeText(this, "essai", Toast.LENGTH_LONG);
            //Toast.makeText(getApplicationContext(), " " + id, Toast.LENGTH_LONG).show();
            Intent newIntent = new Intent(this, MySessionsActivity.class);
            // newIntent.putExtra("@string/id", ""+id);
            startActivity(newIntent);

        }
    }
}
