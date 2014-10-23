package com.origin.aiur.activity.logon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.GroupDao;
import com.origin.aiur.dao.IdentityDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView regButton = null;

    private EditText userAccount;
    private EditText userNickName;
    private EditText userPassword;
    private EditText userPasswordConfirm;

    enum Actions {
        user_reg
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);


        userAccount = (EditText)findViewById(R.id.registerEmail);
        userNickName = (EditText)findViewById(R.id.registerNick);
        userPassword = (EditText)findViewById(R.id.registerPassword);
        userPasswordConfirm = (EditText)findViewById(R.id.registerConfirmPassword);

        regButton = (TextView)findViewById(R.id.btnRegister);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnRegister:
                if (isInputValid()) {
                    this.postSync(Actions.user_reg.name());
                }
                break;
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        // Update user identity data
        JSONObject userInfo = AppUtils.getJsonObject(response, "data");
        User loginUser = new User(userInfo);
        UserDao.getInstance().setCurrentUser(loginUser);

        MainActivity.startActivity(this);
        this.finish();
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    private boolean isInputValid() {
        String account = userAccount.getText().toString();
        String nick = userNickName.getText().toString();
        String pwd = userPassword.getText().toString();
        String pwdConfirm = userPasswordConfirm.getText().toString();

        if (AppUtils.isEmpty(account) || AppUtils.isEmpty(nick) || AppUtils.isEmpty(pwd) || AppUtils.isEmpty(pwdConfirm)) {
            showToastMessage(this.getString(R.string.no_input_value));
            return false;
        }
        if (!pwd.equals(pwdConfirm)) {
            showToastMessage(this.getString(R.string.pwd_not_consist));
            return false;
        }
        return true;
    }

    @Override
    protected String getPath(String action){
        String path = null;
        switch (Actions.valueOf(action)) {
            case user_reg:
                path = HttpUtils.user_reg;
                break;
        }
        return path;
    }

    @Override
    protected HashMap<String, String> getPostParam(String action) {
        HashMap<String, String> param = new HashMap<String, String>();
        switch (Actions.valueOf(action)) {
            case user_reg:
                param.put("loginName", userAccount.getText().toString());
                param.put("nickName", userNickName.getText().toString());
                param.put("password", AppUtils.encryptKey(userPassword.getText().toString()));
                break;
        }
        return param;
    }
}
