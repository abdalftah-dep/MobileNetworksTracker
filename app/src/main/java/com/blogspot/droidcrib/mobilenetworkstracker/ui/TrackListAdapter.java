package com.blogspot.droidcrib.mobilenetworkstracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import java.util.List;

/**
 * Created by Andrey on 17.06.2016.
 */
public class TrackListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Track> mTrackList;
    private static LayoutInflater mLayoutInflater = null;

    public TrackListAdapter(Context context, List<Track> list) {
        mContext = context;
        mTrackList = list;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTrackList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTrackList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mLayoutInflater
                    .inflate(R.layout.list_item_track, null);
        }

        Track track = (Track) getItem(position);

        TextView titleTextView =
                (TextView)convertView.findViewById(R.id.track_list_item_idTextView);
        titleTextView.setText("Track ID : " + track.getId());
        TextView dateTextView =
                (TextView)convertView.findViewById(R.id.track_list_item_dateTextView);
        dateTextView.setText("Date: " + track.startDate);
        CheckBox checkbox =
                (CheckBox)convertView.findViewById(R.id.track_list_item_uploadedCheckBox);
        checkbox.setChecked(false);


        return convertView;
    }
}