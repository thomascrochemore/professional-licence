package com.example.thomas.p54_mobile.fragments.user;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.fragments.guest.SignInFragment;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.model.UserModify;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.network.NetworkUIRunnable;
import com.example.thomas.p54_mobile.service.UserService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;
import com.example.thomas.p54_mobile.view.user.activities.AllActivitiesActivity;

public class ModifyMyAccountFragment extends Fragment
{
    Thread t;
    private NetworkUIRunnable<User> initRunnable = new NetworkUIRunnable<User>()
    {
        @Override public User executeNetwork(BaseActivity activity) throws HttpException
        {
            return UserService.identity();
        }
        @Override public void onSuccess(BaseActivity activity, User user)
        {
            ((EditText) activity.findViewById(R.id.editTextModifyAccountFirstname)).setText(user.getFirstname());
            ((EditText) activity.findViewById(R.id.editTextModifyAccountLastname)).setText(user.getLastname());
            activity.findViewById(R.id.editTextModifyAccountPasswdConf).setEnabled(false);
            final EditText e = activity.findViewById(R.id.editTextModifyAccountPasswdConf);
            ((EditText) activity.findViewById(R.id.editTextModifyAccountPasswd)).addTextChangedListener(new TextWatcher()
            {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    e.setEnabled(true);
                }
            });
        }
    };

    private ClickNetworkUIListener<User> onSubmit = new ClickNetworkUIListener<User>()
    {
        @Override
        public User executeNetwork(BaseActivity activity, View view) throws HttpException
        {
            String login =((EditText) activity.findViewById(R.id.editTextModifyAccountLogin)).getText().toString();
            String firstname = ((EditText) activity.findViewById(R.id.editTextModifyAccountFirstname)).getText().toString();
            String lastname = ((EditText) activity.findViewById(R.id.editTextModifyAccountLastname)).getText().toString();
            String password = ((EditText) activity.findViewById(R.id.editTextModifyAccountPasswd)).getText().toString();
            String confPwd = ((EditText) activity.findViewById(R.id.editTextModifyAccountPasswdConf)).getText().toString();
            UserModify modify = new UserModify(login,firstname,lastname,password,confPwd);
            return UserService.modifyUser(modify);
        }
        @Override
        public void onSuccess(BaseActivity activity, User modify, View view)
        {
            Toast.makeText((AccountActivity)activity, modify.getLogin() + " : votre nouveau prénom est ["
                    + modify.getFirstname() + "] et votre nouveau nom est [" + modify.getLastname() + "]", Toast.LENGTH_LONG);
            // activity.navigate(R.id.guestRelativeLayout,new MyAccountFragment());
        }
    };

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        onSubmit.setActivity((AccountActivity) getActivity());
        initRunnable.setActivity((BaseActivity) getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_modify_account,container,false);
    }

    @Override public void onResume()
    {
        super.onResume();
        AccountActivity activity = (AccountActivity) getActivity();
        activity.findViewById(R.id.buttonModifyAccount).setOnClickListener(onSubmit);
        t = new Thread(initRunnable);
        t.start();
    }

    @Override public void onPause()
    {
        super.onPause();
        AccountActivity activity = (AccountActivity) getActivity();
        activity.findViewById(R.id.buttonModifyAccount).setOnClickListener(null);
    }
}
