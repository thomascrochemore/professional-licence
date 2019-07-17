package com.example.thomas.p54_mobile.view.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.sessions.UserSessionsActivity;

/**
 * Created by thomas on 12/01/2018.
 */

public class UserAccountActivity extends BaseActivity
{
    Long id = Long.MIN_VALUE;
    Thread t;
    private NetworkUIRunnable<User> initRunnable = new NetworkUIRunnable<User>()
    {
        @Override public User executeNetwork(BaseActivity activity) throws HttpException
        {
            // réception des infos
            Bundle donnees = getIntent().getExtras();
            String id_string = donnees.getString("@string/id");
            id = Long.parseLong(id_string);
            return RestService.get("/member/user/" + id,User.class);
        }
        @Override public void onSuccess(BaseActivity activity, User user)
        {
            ((TextView) activity.findViewById(R.id.editTextUserAccountLogin)).setText(user.getLogin());
            ((TextView) activity.findViewById(R.id.editTextUserAccountFirstname)).setText(user.getFirstname());
            ((TextView) activity.findViewById(R.id.editTextUserAccountLastname)).setText(user.getLastname());
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
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

    public void goSessions(View v)
    {
        // Toast.makeText(getApplicationContext(), " " + id, Toast.LENGTH_LONG).show();
        Intent newIntent = new Intent(this, UserSessionsActivity.class);
        newIntent.putExtra("@string/id", ""+id);
        startActivity(newIntent);
    }

    public void backHome(View v)
    {
        this.pushActivity(AccountActivity.class);
    }
}
