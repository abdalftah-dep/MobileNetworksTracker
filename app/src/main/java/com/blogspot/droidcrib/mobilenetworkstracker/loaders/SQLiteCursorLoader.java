package com.blogspot.droidcrib.mobilenetworkstracker.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by andrey on 12.02.2016.
 */
public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mCursor;

    public SQLiteCursorLoader(Context context) {
        super(context);
    }

    protected abstract Cursor loadCursor();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if(cursor != null){
            // check content window is filled
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        super.deliverResult(data);
        Cursor oldCursor = mCursor;
        mCursor = data;
        if (isStarted()){
            super.deliverResult(data);
        }
        if (oldCursor != null && oldCursor != data && !oldCursor.isClosed()){
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mCursor !=null){
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        // Try to cancel current task if possible
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
        if (data != null && !data.isClosed() && mCursor != null){
            mCursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        // to be sure loader is stoped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()){
            mCursor.close();
        }
        mCursor = null;
    }
}
