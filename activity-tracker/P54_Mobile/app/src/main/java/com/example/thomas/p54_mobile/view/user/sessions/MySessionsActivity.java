package com.example.thomas.p54_mobile.view.user.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Session;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.SessionService;
import com.example.thomas.p54_mobile.view.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 11/01/2018.
 */

public class MySessionsActivity extends BaseActivity
{
    Thread t;
    List<Session> theSessions; // utils: stockage de session
    private NetworkUIRunnable<List<Session>> initRunnable = new NetworkUIRunnable<List<Session>>()
    {
        @Override public List<Session> executeNetwork(BaseActivity activity) throws HttpException
        {
            return SessionService.findMySessions(); // récupération des sessions de l'user actuel
        }
        @Override public void onSuccess(BaseActivity activity, List<Session> sessions)
        {
            // récupération des données
            for (int i = 0; i < sessions.size(); i++)
            {
                Date date = sessions.get(i).getDate();
                String acti = sessions.get(i).getActivity().getName();
                HashMap<String, String> sess = new HashMap<>();
                sess.put("date", date.toString());
                sess.put("activity", acti);
                sessionsList.add(sess);
            }
            // transmission des données à la liste d'éléments lv
            ListAdapter adapter = new SimpleAdapter(MySessionsActivity.this, sessionsList,
                    R.layout.list_all_sessions, new String[]{ "date", "activity" },
                    new int[]{ R.id.dateListSessions, R.id.activityListSessions});
            lv.setAdapter(adapter);
            // export
            theSessions = sessions;
        }
    };

    // utils: list view dans le layout
    private ListView lv;
    ArrayList<HashMap<String, String>> sessionsList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
        sessionsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
    }

    public void myClickHandlerSee(View v)
    {
        // layout cliqué
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        // récupération de l'id de la session cliquée
        Long id = Long.MIN_VALUE;
        for (int i = 0; i < lv.getChildCount(); i++)
            if(lv.getChildAt(i) == vwParentRow)
                id = theSessions.get(i).getId();
        // envoi des données
        Intent newIntent = new Intent(this, SeeSessionActivity.class);
        newIntent.putExtra("@string/id", ""+id);
        startActivity(newIntent);
    }
}
