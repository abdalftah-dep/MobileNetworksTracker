package com.blogspot.droidcrib.mobilenetworkstracker.loaders;

import android.content.Context;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;

import java.util.List;

/**
 * Created by Andrey on 18.06.2016.
 */
public class PinpointLoader extends DatabaseLoader {

    private DatabaseManager mDatabaseManager;
    private long mTrackId;
    private double mLatSw;
    private double mLonSw;
    private double mLatNe;
    private double mLonNe;

    public PinpointLoader(Context context, DatabaseManager databaseManager, long trackId, double latSw,
                          double lonSw, double latNe, double lonNe) {
        super(context);
        mDatabaseManager = databaseManager;
        mTrackId = trackId;
        mLatSw = latSw;
        mLonSw = lonSw;
        mLatNe = latNe;
        mLonNe = lonNe;
    }

    @Override
    public List<?> loadList() {
        return mDatabaseManager.queryPinPointsForTrackMapVisibleArea(mTrackId,
                mLatSw, mLonSw, mLatNe, mLonNe);
    }
}
