package com.blogspot.droidcrib.mobilenetworkstracker.application;

import android.app.Application;

import com.blogspot.droidcrib.mobilenetworkstracker.dagger.ApplicationModule;
import com.blogspot.droidcrib.mobilenetworkstracker.dagger.BaseComponent;
import com.blogspot.droidcrib.mobilenetworkstracker.dagger.DaggerBaseComponent;
import com.blogspot.droidcrib.mobilenetworkstracker.dagger.TelephonyModule;

/**
 * Created by Andrey on 04.06.2016.
 */
public class MobileNetworksTrackerApp extends Application {

    private  BaseComponent mBaseComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mBaseComponent = DaggerBaseComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .telephonyModule(new TelephonyModule())
                .build();
    }

    public  BaseComponent getBaseComponent(){
        return mBaseComponent;
    }
}
