package com.example.thomas.p54_mobile.fragments.guest;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.thomas.p54_mobile.R;
import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.network.ClickNetworkUIListener;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.model.UserRegister;
import com.example.thomas.p54_mobile.service.UserService;
import com.example.thomas.p54_mobile.view.BaseActivity;
import com.example.thomas.p54_mobile.view.guest.MenuActivity;
import com.example.thomas.p54_mobile.view.user.account.AccountActivity;

public class SignUpFragment extends Fragment
{
    private ClickNetworkUIListener<User> onSubmit = new ClickNetworkUIListener<User>()
    {
        @Override
        public User executeNetwork(BaseActivity activity, View view) throws HttpException
        {
            String login =((EditText) activity.findViewById(R.id.signUpTextLogin)).getText().toString();
            String firstname = ((EditText) activity.findViewById(R.id.signUpTextFirstname)).getText().toString();
            String lastname = ((EditText) activity.findViewById(R.id.signUpTextLastname)).getText().toString();
            String password = ((EditText) activity.findViewById(R.id.signUpTextPasswd)).getText().toString();
            String confPwd = ((EditText) activity.findViewById(R.id.signUpTextPasswdConf)).getText().toString();
            UserRegister register = new UserRegister(login,firstname,lastname,password,confPwd);
            return UserService.register(register);
        }
        @Override
        public void onSuccess(BaseActivity activity, User result, View view)
        {
            activity.navigate(R.id.guestRelativeLayout,new SignInFragment());
            activity.pushActivity(AccountActivity.class);
        }
    };

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        onSubmit.setActivity((MenuActivity) getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_guest_signup,container,false);
    }

    @Override public void onResume()
    {
        super.onResume();
        MenuActivity activity = (MenuActivity) getActivity();
        activity.bindNavigate(activity.findViewById(R.id.signUpButtonCancel),R.id.guestRelativeLayout,new StartFragment());
        activity.findViewById(R.id.signUpButtonSignUp).setOnClickListener(onSubmit);
    }

    @Override public void onPause()
    {
        super.onPause();
        MenuActivity activity = (MenuActivity) getActivity();
        activity.unbind(activity.findViewById(R.id.signUpButtonCancel));
        activity.findViewById(R.id.signUpButtonSignUp).setOnClickListener(null);
    }
}
