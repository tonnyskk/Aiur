package com.origin.aiur.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014/9/23.
 */
public class GroupEvent implements Parcelable{
    private String activityDesc;
    private long activityTimestamp;

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public long getActivityTimestamp() {
        return activityTimestamp;
    }

    public void setActivityTimestamp(long activityTimestamp) {
        this.activityTimestamp = activityTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
