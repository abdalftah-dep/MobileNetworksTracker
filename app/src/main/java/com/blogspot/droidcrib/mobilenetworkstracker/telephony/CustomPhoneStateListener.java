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

    public static CustomPhoneStateListener sCustomPhoneStateListener;
    public static int sSignalStrengths = 99;
    private PhoneStateListenerInterface listenerInterface;

    private CustomPhoneStateListener() {
        Log.d(TAG, "Empty constructor");
    }

    private CustomPhoneStateListener(PhoneStateListenerInterface listenerInterface) {
        this.listenerInterface = listenerInterface;
        Log.d(TAG, "Constructor with interface");
    }

    public static CustomPhoneStateListener get() {
        if (sCustomPhoneStateListener == null) {
            sCustomPhoneStateListener = new CustomPhoneStateListener();
        }
        return sCustomPhoneStateListener;
    }

    public static CustomPhoneStateListener get(PhoneStateListenerInterface phoneStateListenerInterface) {
        if (sCustomPhoneStateListener == null) {
            sCustomPhoneStateListener = new CustomPhoneStateListener(phoneStateListenerInterface);
        } else {
            sCustomPhoneStateListener.setInterface(phoneStateListenerInterface);
        }
        return sCustomPhoneStateListener;
    }

    private void setInterface(PhoneStateListenerInterface listenerInterface){
        this.listenerInterface = listenerInterface;
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

        CustomPhoneStateListener.this.listenerInterface.signalStrengthsChanged(rssi);

    }
}
