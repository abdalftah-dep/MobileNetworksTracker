package com.blogspot.droidcrib.mobilenetworkstracker.ui;

/**
 * Created by abulanov on 16.02.2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "mobilenetworkstracker";

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DataFragment tab1 = DataFragment.getInstance();
                return tab1;
            case 1:
                TrackListFragment tab2 = TrackListFragment.getInstance();
                return tab2;
            case 2:
                TrackMapFragment tab3 = new TrackMapFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
