package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrey on 04.06.2016.
 */

@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return mApplication;
    }

}
