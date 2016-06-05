package com.blogspot.droidcrib.mobilenetworkstracker.telephony;

/**
 * Created by Andrey on 02.02.2016.
 */

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.ui.DataFragment;

/**
 * Monitoring changes in specific telephony states
 */
public class CustomPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "mobilenetworkstracker";


    public static int sSignalStrengths = 99;
    private PhoneStateListenerInterface mListenerInterface;

    public void setInterface(PhoneStateListenerInterface listenerInterface){
        mListenerInterface = listenerInterface;
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrengths) {
        super.onSignalStrengthsChanged(signalStrengths);
        // Get Signal Strength, dBm
        int rssi = 0;
        if (signalStrengths.getGsmSignalStrength() != 99) {
            rssi = signalStrengths.getGsmSignalStrength() * 2 - 113;
            sSignalStrengths = rssi;
        } else {
            rssi = signalStrengths.getGsmSignalStrength();
            sSignalStrengths = rssi;
        }

        Log.d(TAG, "CustomPhoneStateListener RSSI " + rssi);

        if(mListenerInterface != null) {
                    mListenerInterface.signalStrengthsChanged(rssi);
        }

    }
}
