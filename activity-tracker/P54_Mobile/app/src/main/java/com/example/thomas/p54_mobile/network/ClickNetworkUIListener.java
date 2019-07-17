package com.example.thomas.p54_mobile.network;

import android.view.View;

import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.view.BaseActivity;

/**
 * Created by couty on 17/12/2017.
 */

public abstract class ClickNetworkUIListener<T> implements View.OnClickListener
{
    private View view;
    private Thread t;
    private BaseActivity activity;

    private NetworkUIRunnable<T> runnable = new NetworkUIRunnable<T>()
    {
        @Override public T executeNetwork(BaseActivity activity) throws HttpException
        {
            return ClickNetworkUIListener.this.executeNetwork(activity,view);
        }

        @Override public void onSuccess(BaseActivity activity, T result)
        {
            ClickNetworkUIListener.this.onSuccess(activity,result,view);
        }

        @Override public void onHttpError(BaseActivity activity, HttpException e)
        {
            ClickNetworkUIListener.this.onHttpError(activity,e,view);
        }

        @Override public void onThrowable(BaseActivity activity, Throwable e)
        {
            ClickNetworkUIListener.this.onThrowable(activity,e,view);
        }
    };

    public ClickNetworkUIListener() { }

    public ClickNetworkUIListener(BaseActivity activity){
        setActivity(activity);
    }

    @Override public void onClick(View view) {
        this.view = view;
        t = new Thread(runnable);
        t.start();
    }

    public abstract T executeNetwork(BaseActivity activity, View view) throws HttpException;

    public abstract  void onSuccess(BaseActivity activity, T result, View view);

    public  void onHttpError(BaseActivity activity, HttpException e, View view)
    {
       activity.showError(e.getError());
    }

    public void onThrowable(BaseActivity activity, Throwable e, View view)
    {
        activity.showError(e.getMessage());
    }

    public void setActivity(BaseActivity activity)
    {
        this.activity = activity;
        runnable.setActivity(activity);
    }
}
