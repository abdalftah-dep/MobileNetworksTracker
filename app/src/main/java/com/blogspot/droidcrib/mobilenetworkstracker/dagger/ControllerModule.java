package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import android.app.Application;

import com.blogspot.droidcrib.mobilenetworkstracker.controller.DatabaseManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.PositioningManager;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackingManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    TrackingManager providesTrackManager(Application application){
        return new TrackingManager(application);
    }

    @Provides
    @Singleton
    PositioningManager providesPositioningManager(Application application){
        return new PositioningManager(application);
    }

    @Provides
    @Singleton
    DatabaseManager providesDatabaseManager(){
        return new DatabaseManager();
    }

    @Provides
    @Singleton
    GsonBuilder providesGsonBuilder(){
        return new GsonBuilder();
    }

    @Provides
    @Singleton
    Gson providesGson(){
        return new Gson();
    }



}
