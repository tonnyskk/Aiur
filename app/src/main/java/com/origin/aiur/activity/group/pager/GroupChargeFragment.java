package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.dao.GropUserDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjia on 11/17/2014.
 */
public class GroupChargeFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private ListView userSelectList;
    private SelectUserAdapter userAdapter;
    private TextView chargeButton;
    private EditText chargeAmount;
    private List<Long> selectedUserList = new ArrayList<Long>();


    private enum Actions {
        load_group_user
    }

    public static GroupChargeFragment startFragment(Context context) {
        GroupChargeFragment instance = new GroupChargeFragment();
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onCreate");
        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        this.getSync(Actions.load_group_user.name(), currentGroupId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_charge, container, false);

        userSelectList = (ListView)rootView.findViewById(R.id.groupTabChargeUserList);
        userAdapter = new SelectUserAdapter(getContext());
        userAdapter.setListener(this);
        userSelectList.setAdapter(userAdapter);

        chargeButton = (TextView)rootView.findViewById(R.id.btnConsumeChargeForGroup);
        chargeButton.setOnClickListener(this);

        chargeAmount = (EditText)rootView.findViewById(R.id.chargeMoneyNumber);
        chargeAmount.addTextChangedListener(new TextWatcher() {
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
    public void onStart() {
        super.onStart();
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onStart");

        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        List<User> userList = GropUserDao.getInstance().getGroupUserList(currentGroupId);
        refreshUserList(userList);
    }

    @Override
    public void onResume() {
        super.onResume();
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onResume");

    }

    @Override
    public void onStop() {
        super.onStop();
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onStop");
    }

    @Override
    public String getTitle() {
        return this.context.getString(R.string.title_section_charge);
    }

    @Override
    public void performClick(View view) {
        switch (view.getId()) {
            case R.id.btnConsumeChargeForGroup:

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        Long userId = (Long)compoundButton.getTag();
        if (checked) {
            if (!selectedUserList.contains(userId)) {
                selectedUserList.add(userId);
            }
        } else {
            if (selectedUserList.contains(userId)) {
                selectedUserList.remove(userId);
            }
        }

        checkButtonEnable();
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case load_group_user:
                List<User> userList = GroupHelper.getInstance().getGroupUserList(response);
                refreshUserList(userList);
            break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
            case load_group_user:
                long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
                List<User> userList = GropUserDao.getInstance().getGroupUserList(currentGroupId);
                refreshUserList(userList);
                break;
        }

    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case load_group_user:
                path = HttpUtils.buildPath(HttpUtils.load_group_users, args);
                break;
        }
        return path;
    }

    private void checkButtonEnable() {
        if (chargeAmount == null || chargeButton == null) {
            return;
        }
        String money = chargeAmount.getText().toString();
        if (selectedUserList.size() > 0 && !AppUtils.isEmpty(money)) {
            chargeButton.setEnabled(true);
        } else {
            chargeButton.setEnabled(false);
        }
    }
    private void refreshUserList(List<User> userList) {
        if (userList != null && userAdapter != null) {
            userAdapter.setUserList(userList, selectedUserList);
        }
    }
}
