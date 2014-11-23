package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.activity.group.GroupHelper;
import com.origin.aiur.dao.GroupUserDao;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    private EditText description;
    private CheckBox isPrepay;
    private View chargePrePayContainer;
    private List<Long> selectedUserList = new ArrayList<Long>();


    private enum Actions {
        send_group_charge
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_charge, container, false);

        userSelectList = (ListView) rootView.findViewById(R.id.groupTabChargeUserList);
        userAdapter = new SelectUserAdapter(getContext());
        userAdapter.setListener(this);
        userSelectList.setAdapter(userAdapter);

        chargeButton = (TextView) rootView.findViewById(R.id.btnConsumeChargeForGroup);
        chargeButton.setOnClickListener(this);

        description = (EditText) rootView.findViewById(R.id.chargeMoneyDesc);
        isPrepay = (CheckBox) rootView.findViewById(R.id.chargePrePayBox);
        chargePrePayContainer = rootView.findViewById(R.id.chargePrePayContainer);

        chargeAmount = (EditText) rootView.findViewById(R.id.chargeMoneyNumber);
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

        long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
        long userId = UserDao.getInstance().getUserId();

        if (ownerId == userId) {
            chargePrePayContainer.setVisibility(View.GONE);
        } else {
            chargePrePayContainer.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onResume");

        long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
        List<User> userList = GroupUserDao.getInstance().getGroupJoinedUserList(currentGroupId);
        refreshUserList(userList);
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
                closeBoard();

                long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
                long userId = UserDao.getInstance().getUserId();

                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("userId", userId);
                param.put("groupId", UserDao.getInstance().getCurrentGroup().getGroupId());
                param.put("description", description.getText().toString());
                param.put("userPrePay", isPrepay.isChecked());
                param.put("payByGroupOwner", ownerId == userId);
                param.put("money", Double.parseDouble(chargeAmount.getText().toString()));
                param.put("userList", selectedUserList);
                this.postSync(Actions.send_group_charge.name(), param);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        Long userId = (Long) compoundButton.getTag();
        if (checked) {
            if (!selectedUserList.contains(userId)) {
                selectedUserList.add(userId);
            }
        } else {
            if (selectedUserList.contains(userId)) {
                selectedUserList.remove(userId);
            }
        }
        userAdapter.updateCheckedList(selectedUserList);

        checkButtonEnable();
    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case send_group_charge:
                // Clear selected user
                selectedUserList.clear();
                description.setText("");
                chargeAmount.setText("");
                isPrepay.setChecked(false);

                long currentGroupId = UserDao.getInstance().getCurrentGroup().getGroupId();
                List<User> users = GroupUserDao.getInstance().getGroupJoinedUserList(currentGroupId);
                refreshUserList(users);

                long ownerId = UserDao.getInstance().getCurrentGroup().getOwnerUserId();
                long userId = UserDao.getInstance().getUserId();
                if (ownerId == userId) {
                    this.showToastMessage(this.getContext().getString(R.string.common_group_charge_res));
                } else {
                    this.showToastMessage(this.getContext().getString(R.string.common_group_charge_req));
                }

                // Go back to first tab
                GroupHelper.getInstance().notifyChangeTab(0);

                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {
        switch (Actions.valueOf(action)) {
        }

    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case send_group_charge:
                path = HttpUtils.buildPath(HttpUtils.send_group_charge, args);
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
            userAdapter.updateCheckedList(selectedUserList);
            userAdapter.setUserList(userList);
        }
    }
}
