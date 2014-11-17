package com.origin.aiur.activity.group.pager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.origin.aiur.BaseFragment;
import com.origin.aiur.R;
import com.origin.aiur.utils.ALogger;

import org.json.JSONObject;

/**
 * Created by dongjia on 11/17/2014.
 */
public class GroupChargeFragment extends BaseFragment {
    private Context context;

    public static GroupChargeFragment startFragment(Context context) {
        GroupChargeFragment instance = new GroupChargeFragment();
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onCreateView");
        View rootView = inflater.inflate(R.layout.group_tab_charge, container, false);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALogger.log(ALogger.LogPriority.debug, GroupChargeFragment.class, "GroupChargeFragment@onCreate");
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
    public void onPostExecuteSuccessful(String action, JSONObject response) {

    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public String getPath(String action, Object... args) {
        return null;
    }
}
