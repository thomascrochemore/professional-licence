package com.example.thomas.p54_mobile.view.user.sessions;

import android.os.Bundle;
import android.widget.ListView;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.SessionWithProperties;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.SessionService;
import com.example.thomas.p54_mobile.view.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by thomas on 18/01/2018.
 */

public class ModifySessionActivity extends BaseActivity
{
    Long id = Long.MIN_VALUE;
    Thread t;
    private NetworkUIRunnable<SessionWithProperties> initRunnable = new NetworkUIRunnable<SessionWithProperties>()
    {
        @Override public SessionWithProperties executeNetwork(BaseActivity activity) throws HttpException
        {
            // Date newDate = null;
            // réception des infos
            // Bundle donnees = getIntent().getExtras();
            // String id_string = donnees.getString("@string/id");
            // id = Long.parseLong(id_string);
            // SessionWithProperties s = SessionService.findOne(id);
            // s.setDate(newDate);
            return new SessionWithProperties();
        }
        @Override public void onSuccess(BaseActivity activity, SessionWithProperties session)
        {

        }
    };

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_session);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
    }
}
