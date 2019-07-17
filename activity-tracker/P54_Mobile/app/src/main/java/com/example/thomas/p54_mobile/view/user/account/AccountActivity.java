package com.example.thomas.p54_mobile.view.user.account;

import com.example.thomas.p54_mobile.fragments.user.ModifyMyAccountFragment;
import com.example.thomas.p54_mobile.fragments.user.MyAccountFragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.guest.MenuActivity;
import com.example.thomas.p54_mobile.view.user.activities.AllActivitiesActivity;
import com.example.thomas.p54_mobile.view.user.sessions.MySessionsActivity;

public class AccountActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
        {
            super.onBackPressed();
            finish();
        }
    }

    @Override public void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) pushActivity(MenuActivity.class);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int idItem = item.getItemId();

        // menus de l'activité user connecté
        if (idItem == R.id.my_activities) // mes activités
            this.pushActivity(AllActivitiesActivity.class);
        else if (idItem == R.id.my_sessions) // mes sessions
            this.pushActivity(MySessionsActivity.class);
        // else if (idItem == R.id.create_activity) // créer activité
            // this.pushActivity(CreateActivityActivity.class);
        // else if (idItem == R.id.create_session) // créer session
            // this.pushActivity(CreateSessionActivity.class);
        else if (idItem == R.id.my_account) // mon compte
            navigate(R.id.userRelativeLayout,new MyAccountFragment());
        else if (idItem == R.id.maj_my_account) // modifier mon compte
            navigate(R.id.userRelativeLayout,new ModifyMyAccountFragment());
        else if (idItem == R.id.all_users) // mon compte
            this.pushActivity(AllUsersActivity.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
