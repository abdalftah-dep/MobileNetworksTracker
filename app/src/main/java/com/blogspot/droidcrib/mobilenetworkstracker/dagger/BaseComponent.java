package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;
import com.blogspot.droidcrib.mobilenetworkstracker.internet.PostGetIntentService;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.CustomPhoneStateListener;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.DataFragment;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.MainActivity;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.TrackListFragment;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.TrackMapFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrey on 04.06.2016.
 */

@Singleton
@Component (modules = {ApplicationModule.class, TelephonyModule.class, ControllerModule.class})
public interface BaseComponent {
    void inject(MainActivity mainActivity);
    void inject(DataFragment dataFragment);
    void inject(TrackMapFragment trackMapFragment);
    void inject(TrackListFragment trackListFragment);
    void inject(PostGetIntentService postGetIntentService);
    TelephonyInfo getTelephonyInfo();
    CustomPhoneStateListener getCustomPhoneStateListener();
    TrackManager getTrackManager();
    //TelephonyInfo getTelephonyInfo();
}
