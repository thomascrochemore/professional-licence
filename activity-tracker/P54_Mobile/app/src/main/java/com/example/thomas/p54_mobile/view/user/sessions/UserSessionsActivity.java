package com.example.thomas.p54_mobile.view.user.sessions;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Session;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.service.SessionService;
import com.example.thomas.p54_mobile.view.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 12/01/2018.
 */

public class UserSessionsActivity extends BaseActivity
{
    Long id;
    Thread t;
    private NetworkUIRunnable<List<Session> > initRunnable = new NetworkUIRunnable<List<Session> >()
    {
        @Override public List<Session> executeNetwork(BaseActivity activity) throws HttpException
        {
            // réception des infos
            Bundle donnees = getIntent().getExtras();
            String id_string = donnees.getString("@string/id");
            id = Long.parseLong(id_string);
            return SessionService.findByUser(id);
        }
        @Override public void onSuccess(BaseActivity activity, List<Session> sessions)
        {
            for (int i = 0; i < sessions.size(); i++)
            {
                Date date = sessions.get(i).getDate();
                String acti = sessions.get(i).getActivity().getName();
                HashMap<String, String> sess = new HashMap<>();
                sess.put("date", date.toString());
                sess.put("activity", acti);
                sessionsList.add(sess);
            }
            // listview
            ListAdapter adapter = new SimpleAdapter(UserSessionsActivity.this, sessionsList,
                    R.layout.list_user_sessions, new String[]{ "date", "activity"},
                    new int[]{ R.id.dateListUserSessions, R.id.activityListUserSessions });
            lv.setAdapter(adapter);
        }
    };

    private ListView lv;
    ArrayList<HashMap<String, String>> sessionsList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sessions);
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listUserSessions);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listUserSessions);
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listUserSessions);
    }
}
