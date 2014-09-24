package com.origin.aiur.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014/9/23.
 */
public class UserGroup implements Parcelable{
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
