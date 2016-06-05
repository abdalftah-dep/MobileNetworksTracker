package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import com.blogspot.droidcrib.mobilenetworkstracker.telephony.CustomPhoneStateListener;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.DataFragment;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.MainActivity;
import com.blogspot.droidcrib.mobilenetworkstracker.ui.TrackMapFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrey on 04.06.2016.
 */

@Singleton
@Component (modules = {ApplicationModule.class, TelephonyModule.class})
public interface BaseComponent {
    void inject(MainActivity mainActivity);
    void inject(DataFragment dataFragment);
    void inject(TrackMapFragment trackMapFragment);
    TelephonyInfo getTelephonyInfo();
    CustomPhoneStateListener getCustomPhoneStateListener();
    //TelephonyInfo getTelephonyInfo();
}
