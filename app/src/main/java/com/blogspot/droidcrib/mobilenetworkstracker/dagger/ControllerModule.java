package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import android.app.Application;
import android.content.Context;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrey on 05.06.2016.
 */

@Module
public class ControllerModule {

    @Provides
    @Singleton
    TrackManager providesTrackManager(Application application){
        return new TrackManager(application);
    }

}
