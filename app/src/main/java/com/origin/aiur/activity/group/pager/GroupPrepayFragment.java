package com.origin.aiur.activity.group.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dongjia on 11/17/2014.
 */
public class GroupPrepayFragment extends BaseFragment {
    private Context context;
    private TextView btnPrepayForUser;
    private EditText prepayAmount;

    private enum Actions {
        send_user_prepay
    }

    public static GroupPrepayFragment startFragment(Context context) {
        GroupPrepayFragment instance = new GroupPrepayFragment();
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupPrepayFragment.class, "GroupPrepayFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_prepay, container, false);

        btnPrepayForUser = (TextView)rootView.findViewById(R.id.btnPrepayForUser);
        btnPrepayForUser.setOnClickListener(this);

        prepayAmount = (EditText)rootView.findViewById(R.id.prepayMoneyNumber);
        prepayAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkButtonEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALogger.log(ALogger.LogPriority.debug, GroupPrepayFragment.class, "GroupPrepayFragment@onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        ALogger.log(ALogger.LogPriority.debug, GroupPrepayFragment.class, "GroupPrepayFragment@onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        ALogger.log(ALogger.LogPriority.debug, GroupPrepayFragment.class, "GroupPrepayFragment@onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALogger.log(ALogger.LogPriority.debug, GroupPrepayFragment.class, "GroupPrepayFragment@onStop");
    }

    @Override
    public String getTitle() {
        return this.context.getString(R.string.title_section_prepay);
    }

    @Override
    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrepayForUser:
                closeBoard();

                long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
                long userId = UserDao.getInstance().getUserId();

                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("userId", userId);
                param.put("groupId", UserDao.getInstance().getCurrentGroup().getGroupId());
                param.put("payByGroupOwner", ownerId == userId);
                param.put("money", Double.parseDouble(prepayAmount.getText().toString()));
                this.postSync(Actions.send_user_prepay.name(), param);

                break;
        }
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case send_user_prepay:
                prepayAmount.setText("");

                long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
                long userId = UserDao.getInstance().getUserId();
                if (ownerId == userId) {
                    this.showToastMessage(this.getContext().getString(R.string.common_group_prepay_res));
                } else {
                    this.showToastMessage(this.getContext().getString(R.string.common_group_prepay_req));
                }

                // Go back to first tab
                GroupHelper.getInstance().notifyChangeTab(0);
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case send_user_prepay:
                path = HttpUtils.buildPath(HttpUtils.send_group_user_prepay, args);
                break;
        }
        return path;
    }


    private void checkButtonEnable() {
        if (prepayAmount == null || btnPrepayForUser == null) {
            return;
        }
        String money = prepayAmount.getText().toString();
        if ( !AppUtils.isEmpty(money)) {
            btnPrepayForUser.setEnabled(true);
        } else {
            btnPrepayForUser.setEnabled(false);
        }
    }
}
