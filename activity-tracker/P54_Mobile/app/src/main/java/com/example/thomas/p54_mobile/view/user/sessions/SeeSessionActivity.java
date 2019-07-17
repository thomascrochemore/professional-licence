package com.example.thomas.p54_mobile.view.user.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.Session;
import com.example.thomas.p54_mobile.model.SessionWithProperties;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.SessionService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;

/**
 * Created by thomas on 15/01/2018.
 */

public class SeeSessionActivity extends BaseActivity
{
    Long id = Long.MIN_VALUE;
    Thread t;
    private NetworkUIRunnable<Session> initRunnable = new NetworkUIRunnable<Session>()
    {
        @Override public Session executeNetwork(BaseActivity activity) throws HttpException
        {
            Bundle donnees = getIntent().getExtras();
            String id_string = donnees.getString("@string/id");
            id = Long.parseLong(id_string);
            return SessionService.findOne(id);
        }
        @Override public void onSuccess(BaseActivity activity, Session session)
        {
            // ((TextView) activity.findViewById(R.id.activityListSeeSessions)).setText(session.getActivity().getName());
            ((TextView) activity.findViewById(R.id.dateListSeeSessions)).setText(session.getDate().toString());
        }
    };

    private ClickNetworkUIListener<Session> onSubmitDelete = new ClickNetworkUIListener<Session>()
    {
        @Override
        public Session executeNetwork(BaseActivity activity, View view) throws HttpException
        {
            SessionService.deleteSession(id);
            return new Session();
        }
        @Override
        public void onSuccess(BaseActivity activity, Session result, View view)
        {
            // ((TextView) activity.findViewById(R.id.dateListSeeSessions)).setText("SUPPRIME");
            // activity.pushActivity(AccountActivity.class);
        }
    };

    private ClickNetworkUIListener<SessionWithProperties> onSubmitUpdate = new ClickNetworkUIListener<SessionWithProperties>()
    {
        @Override public SessionWithProperties executeNetwork(BaseActivity activity, View view) throws HttpException
        {
            return SessionService.findOne(id);
        }
        @Override public void onSuccess(BaseActivity activity, SessionWithProperties result, View view)
        {
            Intent newIntent = new Intent(SeeSessionActivity.this, ModifySessionActivity.class);
            newIntent.putExtra("@string/id", ""+id);
            startActivity(newIntent);
        }
    };

    @Override public void onDestroy()
    {
        super.onDestroy();
        this.pushActivity(AccountActivity.class);
    }

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_session);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        t = new Thread(initRunnable);
        t.start();
        this.findViewById(R.id.trashListSeeSessionsDelete).setOnClickListener(onSubmitDelete);
        this.findViewById(R.id.buttonListSeeSessionsUpdate).setOnClickListener(onSubmitUpdate);
    }

    @Override public void onPause()
    {
        super.onPause();
        this.findViewById(R.id.trashListSeeSessionsDelete).setOnClickListener(null);
        this.findViewById(R.id.buttonListSeeSessionsUpdate).setOnClickListener(null);
    }

    public void backHome(View v)
    {
        this.pushActivity(AccountActivity.class);
    }
}
