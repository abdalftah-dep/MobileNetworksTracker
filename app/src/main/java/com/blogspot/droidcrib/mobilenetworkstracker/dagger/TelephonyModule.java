package com.blogspot.droidcrib.mobilenetworkstracker.dagger;

import android.app.Application;

import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrey on 04.06.2016.
 */

@Module
public class TelephonyModule {

    @Provides
    @Singleton
    TelephonyInfo providesTelephonyInfo(Application application){
        return new TelephonyInfo(application);
    }

}
