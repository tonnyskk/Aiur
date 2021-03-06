package com.origin.aiur.activity.group.pager;

/**
 * Created by dongjia on 11/17/2014.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.origin.aiur.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private boolean isOwner;
    private String gpName;
    private Context context;
    private List<BaseFragment> pagerLists = new ArrayList<BaseFragment>();

    public SectionsPagerAdapter(FragmentManager fm, Context context, boolean isOwner) {
        super(fm);
        this.context = context;
        this.isOwner = isOwner;
        initPager();
    }

    @Override
    public Fragment getItem(int position) {
        return pagerLists.get(position);
    }

    @Override
    public int getCount() {
        return pagerLists.size();
    }

    public void initPager() {
        pagerLists.add(GroupIndexFragment.startFragment(context));
        pagerLists.add(GroupPrepayFragment.startFragment(context));
        pagerLists.add(GroupChargeFragment.startFragment(context));
        //pagerLists.add(GroupMessageFragment.startFragment(context));

        if (this.isOwner) {
           pagerLists.add(GroupManageFragment.startFragment(context));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerLists.get(position).getTitle();
    }
}
