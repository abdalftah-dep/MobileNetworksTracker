package com.blogspot.droidcrib.mobilenetworkstracker.model;

import java.util.Date;

/**
 * Created by Andrey on 31.01.2016.
 */
public class Track {

    private Date mStartDate;
    private long mId;

    public Track() {
        mId = -1;
        mStartDate = new Date();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }
}
