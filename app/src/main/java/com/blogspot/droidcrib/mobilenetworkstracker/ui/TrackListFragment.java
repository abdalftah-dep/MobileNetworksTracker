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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
    private int mTrackPosition;
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

        TrackListAdapter adapter = new TrackListAdapter(getActivity(), trackList);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<?>> loader) {
        Log.d(TAG, "onLoaderReset() called");
        setListAdapter(null);
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
        refreshTrackList();
        registerForContextMenu(getListView());

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "Selected track.ID " + id);
        Log.d(TAG, "Selected track.POSITION " + position);
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
        mTrackPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Get menu item position
        switch (item.getItemId()) {
            case R.id.menu_item_delete_track:
                mDatabaseManager.deleteTrack(mTrackId);
                refreshTrackList();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void refreshTrackList(){
        getLoaderManager().restartLoader(0, null, this);
    }
}
