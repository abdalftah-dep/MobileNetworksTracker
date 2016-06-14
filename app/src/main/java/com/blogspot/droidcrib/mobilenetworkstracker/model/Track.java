package com.blogspot.droidcrib.mobilenetworkstracker.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Andrey on 31.01.2016.
 */

@Table(name = "Tracks", id = "_id")
public class Track extends Model{

    @Column(name = "start_date")
    public Date startDate;

    public Track() {
    }

    @Override
    public String toString() {
        return startDate.toString();
    }
}
