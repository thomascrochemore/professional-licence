package com.example.thomas.p54_mobile.network;

import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.view.BaseActivity;

/**
 * Created by couty on 17/12/2017.
 */

public abstract class NetworkUIRunnable<T> implements Runnable
{
    private BaseActivity activity;

    public NetworkUIRunnable() { }

    public NetworkUIRunnable(BaseActivity activity){
        this.activity = activity;
    }

    public abstract T executeNetwork(BaseActivity activity) throws HttpException;

    public abstract void onSuccess(BaseActivity activity, T result);

    public void onHttpError(BaseActivity activity, HttpException e)
    {
        activity.showError(e.getError());
    }

    public void onThrowable(BaseActivity activity, Throwable e)
    {
        e.printStackTrace();
        activity.showError(e.getMessage());
    }

    public void run()
    {
        try{
            final T result = executeNetwork(activity);
            activity.runOnUiThread(new Runnable()
            {
                T res;
                {
                    res = result;
                }
                @Override
                public void run() {
                    onSuccess(activity,result);
                }
            });
        }
        catch (final HttpException e)
        {
            activity.runOnUiThread(new Runnable()
            {
                HttpException exception;
                {
                    exception = e;
                }
                @Override
                public void run() {
                    onHttpError(activity,exception);
                }
            });
        }
        catch (final Throwable e)
        {
            System.out.println(activity);
            activity.runOnUiThread(new Runnable()
            {
                Throwable throwable;
                {
                    throwable = e;
                }
                @Override
                public void run() {
                    onThrowable(activity,throwable);
                }
            });
        }
    }

    public void setActivity(BaseActivity activity){
        this.activity = activity;
    }
}
