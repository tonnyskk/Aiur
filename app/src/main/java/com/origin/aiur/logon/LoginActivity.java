package com.origin.aiur.logon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.BaseModel;
import com.origin.aiur.R;
import com.origin.aiur.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView loginButton;
    private TextView registerButton;

    enum Actions {
        user_login
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (TextView) findViewById(R.id.loginLogon);
        registerButton = (TextView) findViewById(R.id.loginRegister);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecuteSuccessful(String action) {
        MainActivity.startActivity(this);
        this.finish();
    }

    @Override
    public void onPostExecuteFailed(String action) {
        // invalid user or others
    }

    @Override
    public BaseModel getModel() {
        return new LoginModel(this);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.loginLogon:
                this.postSync(Actions.user_login.name());
                break;

            case R.id.loginRegister:
                RegisterActivity.startActivity(this);
                break;
        }
    }
}
