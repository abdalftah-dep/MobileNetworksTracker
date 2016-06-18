package com.blogspot.droidcrib.mobilenetworkstracker.loaders;

import android.content.Context;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.List;

/**
 * Created by abulanov on 15.06.2016.
 */
public class TrackLoader extends DatabaseLoader {

    private DatabaseManager mDatabaseManager;

    public TrackLoader(Context context, DatabaseManager databaseManager) {
        super(context);
        mDatabaseManager = databaseManager;
    }

    @Override
    public List<?> loadList() {
        return mDatabaseManager.queryAllTracks();
    }
}
