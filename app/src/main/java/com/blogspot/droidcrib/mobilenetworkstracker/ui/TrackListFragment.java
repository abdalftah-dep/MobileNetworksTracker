package com.blogspot.droidcrib.mobilenetworkstracker.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.loaders.TrackLoader;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrey on 08.02.2016.
 */



public class TrackListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<?>>

{

    private static final String TAG = "mobilenetworkstracker";
    private static final int REQUEST_NEW_TRACK = 0;

    public static TrackListFragment sTrackListFragment;
    private Callbacks mCallbacks;
    private long mTrackId;
    @Inject TrackingManager mTrackingManager;
    @Inject DatabaseManager mDatabaseManager;
    private ArrayList<Track> mTracks;


    public static TrackListFragment getInstance(){
        if(sTrackListFragment == null){
            sTrackListFragment = new TrackListFragment();
        }
        return sTrackListFragment;
    }

    @Override
    public Loader<List<?>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader() called");
        return new TrackLoader(getActivity(), mDatabaseManager);
    }

    @Override
    public void onLoadFinished(Loader<List<?>> loader, List<?> data) {

        Log.d(TAG, "onLoadFinished() called");
        List<Track> trackList = (List<Track>) data;
        Log.d(TAG, "onLoadFinished: Track list size = " + trackList.size());
        Log.d(TAG, "onLoadFinished: Track list tracks ");
                for(int i =0; i < trackList.size(); i++){
                    Log.d(TAG,
                            "Track ID: " + trackList.get(i).getId()
                            + " date " + trackList.get(i).startDate.toString()
                    );
                }


    }

    @Override
    public void onLoaderReset(Loader<List<?>> loader) {
        Log.d(TAG, "onLoaderReset() called");

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
        Log.d(TAG, "TrackListFragment.onResume() ");
        getLoaderManager().restartLoader(0, null, this);


        List<Track> allTracks = mDatabaseManager.queryAllTracks();
        ArrayAdapter<Track> trackArrayAdapter = new ArrayAdapter<Track>(getActivity(),
                android.R.layout.simple_list_item_1,
                allTracks);
//        trackArrayAdapter.addAll(allTracks);
        setListAdapter(trackArrayAdapter);
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
//                mTrackingManager.deleteTrack(mTrackId);
//                mTrackingManager.deletePinPointsForTrack(mTrackId);
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
//        return new TrackListCursorLoader(getActivity(), mTrackingManager);
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
//        private TrackingManager innerTrackManager;
//
//        public TrackListCursorLoader(Context context, TrackingManager trackManager) {
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
