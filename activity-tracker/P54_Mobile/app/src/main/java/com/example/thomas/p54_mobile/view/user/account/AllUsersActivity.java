package com.example.thomas.p54_mobile.view.user.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.RestService;
import com.example.thomas.p54_mobile.view.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by thomas on 09/01/2018.
 */

public class AllUsersActivity extends BaseActivity
{
    Thread t;
    User[] theUsers;
    ListAdapter adapter;
    private NetworkUIRunnable<User[]> initRunnable = new NetworkUIRunnable<User[]>()
    {
        @Override public User[] executeNetwork(BaseActivity activity) throws HttpException
        {
            return RestService.get("/member/user",User[].class);
        }
        @Override public void onSuccess(BaseActivity activity, User[] users)
        {
            // récupération de données
            for (int i = 0; i < users.length; i++)
            {
                String login = "@" + users[i].getLogin();
                String firstname = users[i].getFirstname();
                String lastname = users[i].getLastname();
                String name = firstname + " " + lastname;
                HashMap<String, String> user = new HashMap<>();
                user.put("login", login);
                user.put("name", name);
                userList.add(user);
            }
            // affichage
            adapter = new SimpleAdapter(AllUsersActivity.this, userList,
                    R.layout.list_all_users, new String[]{ "login", "name"},
                    new int[]{R.id.prenomListUsers, R.id.nomListUsers});
            lv.setAdapter(adapter);
            // save
            theUsers = users;
        }
    };

    private ListView lv;
    ArrayList<HashMap<String, String>> userList;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        userList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        initRunnable.setActivity(this);
    }

    @Override public void onResume()
    {
        super.onResume();
        userList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
        userList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listAllUsers);
    }

    public void myClickHandler(View v)
    {
        /*for (int i = 0; i < lv.getChildCount(); i++)
            lv.getChildAt(i).setBackgroundColor(Color.WHITE);*/
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        // Button btnChild = (Button)vwParentRow.getChildAt(2);
        // vwParentRow.setBackgroundColor(Color.YELLOW);

        Long id = Long.MIN_VALUE;
        for (int i = 0; i < lv.getChildCount(); i++)
            if(lv.getChildAt(i) == vwParentRow)
                id = theUsers[i].getId();

        // envoi des données
        Intent newIntent = new Intent(this, UserAccountActivity.class);
        newIntent.putExtra("@string/id", ""+id);
        startActivity(newIntent);
    }
}
