package com.origin.aiur.activity.group.pager;

/**
 * Created by dongjia on 11/17/2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.origin.aiur.R;
import com.origin.aiur.utils.ALogger;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class BaseFragment extends Fragment {
    public BaseFragment(){
    }
    public abstract String getTitle();

}
