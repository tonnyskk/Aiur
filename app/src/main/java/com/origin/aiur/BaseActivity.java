package com.origin.aiur;

import android.support.v7.app.ActionBarActivity;

/**
 * Created by Administrator on 2014/9/23.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected final void postSync(final String action) {
        try {
            Thread.sleep(1000);
            onPostExecuteSuccessful(action);
        } catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecuteFailed(action);
        }
    }

    public abstract void onPostExecuteSuccessful(String action);
    public abstract void onPostExecuteFailed(String action);

    public abstract BaseModel getModel();
}
