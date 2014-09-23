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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView regButton = null;

    enum Actions {
        reg_user, logon_user
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regButton = (TextView)findViewById(R.id.btnRegister);
        regButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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
        switch (Actions.valueOf(action)) {
            case logon_user:
                MainActivity.startActivity(this);
                this.finish();
                break;
            case reg_user:
                this.postSync(Actions.logon_user.name());
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public BaseModel getModel() {
        return new RegisterModel(this);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnRegister:
                this.postSync(Actions.reg_user.name());
                break;
        }
    }
}
