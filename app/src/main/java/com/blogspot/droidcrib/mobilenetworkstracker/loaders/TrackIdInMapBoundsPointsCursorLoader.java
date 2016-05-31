package com.blogspot.droidcrib.mobilenetworkstracker.loaders;

import android.content.Context;
import android.database.Cursor;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;

/**
 * Created by Andrey on 03.04.2016.
 */
public class TrackIdInMapBoundsPointsCursorLoader extends SQLiteCursorLoader {

    private long mTrackId;
    double mLatSw;
    double mLonSw;
    double mLatNe;
    double mLonNe;

    public TrackIdInMapBoundsPointsCursorLoader(Context context, long trackId, double latSw,
                                                double lonSw, double latNe, double lonNe) {
        super(context);
        mTrackId = trackId;
        mLatSw = latSw;
        mLonSw = lonSw;
        mLatNe = latNe;
        mLonNe = lonNe;
    }

    @Override
    protected Cursor loadCursor() {
        return TrackManager.get(getContext()).queryPinPointsForTrackMapVisibleArea(mTrackId, mLatSw,
                mLonSw, mLatNe, mLonNe);
    }
}