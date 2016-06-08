package com.blogspot.droidcrib.mobilenetworkstracker.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;


import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;
import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.loaders.SQLiteCursorLoader;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import javax.inject.Inject;

/**
 * Created by Andrey on 08.02.2016.
 */



public class TrackListFragment

       extends ListFragment
//        implements LoaderCallbacks<Cursor>

{

    private static final String TAG = "mobilenetworkstracker";
    private static final int REQUEST_NEW_TRACK = 0;

    public static TrackListFragment sTrackListFragment;
    private Context mContext;
    private Callbacks mCallbacks;
    private long mTrackId;
    @Inject TrackManager mTrackManager;


    public static TrackListFragment getInstance(){
        if(sTrackListFragment == null){
            sTrackListFragment = new TrackListFragment();
        }
        return sTrackListFragment;
    }

    /**
     * Interface for host activity
     */

    public interface  Callbacks{
        void onTrackSelected(long trackId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);;
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MobileNetworksTrackerApp)getActivity().getApplication())
                .getBaseComponent().inject(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sTrackListFragment = this;
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallbacks.onTrackSelected(id);
    }


    /*
        Context menu methods
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_item_delete_track, menu);
        // Get long-pressed item id
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        mTrackId = info.id;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Get menu item position
        switch (item.getItemId()) {
            case R.id.menu_item_delete_track:
//                mTrackManager.deleteTrack(mTrackId);
//                mTrackManager.deletePinPointsForTrack(mTrackId);
//                getLoaderManager().restartLoader(0, null, this);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_NEW_TRACK == requestCode) {
            // restart loader to get new series
//            getLoaderManager().restartLoader(0, null, this);
        }
    }

//    @Override
//    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
//        return new TrackListCursorLoader(getActivity(), mTrackManager);
//    }
//
//    @Override
//    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
//        // create adapter which references this cursor
//        TrackCursorAdapter adapter = new TrackCursorAdapter(getActivity(), (TrackCursor)data);
//        setListAdapter(adapter);
//    }
//
//    @Override
//    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
//        // stop using cursor
//        setListAdapter(null);
//    }

//    /**
//     * Private adapter class
//     */
//    private static class TrackCursorAdapter extends CursorAdapter {
//
//        private TrackCursor mTrackCursor;
//
//        public TrackCursorAdapter(Context context, TrackCursor c) {
//            super(context, c, 0);
//            mTrackCursor = c;
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            // Use Inflater to get layout
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            // Get series for current line
//            Track track = mTrackCursor.getTrack();
//            // Create TextView for begin date
//            TextView startDateTextView = (TextView) view;
//            String cellText = context.getString(R.string.cell_text, track.getStartDate());
//            startDateTextView.setText(cellText);
//        }
//    }

//    /**
//     * Private CursorLoader class
//     */
//
//    private static class TrackListCursorLoader extends SQLiteCursorLoader{
//
//        private TrackManager innerTrackManager;
//
//        public TrackListCursorLoader(Context context, TrackManager trackManager) {
//            super(context);
//            innerTrackManager = trackManager;
//        }
//
//
//
//        @Override
//        protected Cursor loadCursor() {
//            // request on series list
//            return innerTrackManager.queryAllTracks();
//        }
//    }
}
