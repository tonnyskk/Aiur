package com.origin.aiur.activity.logon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.activity.main.MainActivity;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity implements TextWatcher{

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


        userAccount = (EditText) findViewById(R.id.registerEmail);
        userAccount.addTextChangedListener(this);
        userNickName = (EditText) findViewById(R.id.registerNick);
        userNickName.addTextChangedListener(this);
        userPassword = (EditText) findViewById(R.id.registerPassword);
        userPassword.addTextChangedListener(this);
        userPasswordConfirm = (EditText) findViewById(R.id.registerConfirmPassword);
        userPasswordConfirm.addTextChangedListener(this);

        regButton = (TextView) findViewById(R.id.btnRegister);
        regButton.setOnClickListener(this);
    }

    @Override
    public void performClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnRegister:
                if (isInputValid()) {
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("loginName", userAccount.getText().toString());
                    param.put("nickName", userNickName.getText().toString());
                    param.put("password", AppUtils.encryptKey(userPassword.getText().toString()));
                    this.postSync(Actions.user_reg.name(), param);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        String account = userAccount.getText().toString();
        String nick = userNickName.getText().toString();
        String pwd = userPassword.getText().toString();
        String pwdConfirm = userPasswordConfirm.getText().toString();

        if (AppUtils.isEmpty(account) || AppUtils.isEmpty(nick) || AppUtils.isEmpty(pwd) || AppUtils.isEmpty(pwdConfirm)) {
            regButton.setEnabled(false);
        } else {
            regButton.setEnabled(true);
        }

        userAccount.setTextColor(getResources().getColor(R.color.commonTextColor));
    }

    @Override
    public void afterTextChanged(Editable editable) {
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
        if (!AppUtils.isValidEmail(userAccount.getText())) {
            userAccount.setFocusable(true);
            userAccount.setTextColor(getResources().getColor(R.color.commonErrorFiled));
            showToastMessage(this.getString(R.string.error_email_format));
            return false;
        }
        return true;
    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case user_reg:
                path = HttpUtils.user_reg;
                break;
        }
        return path;
    }
}
