package com.blogspot.droidcrib.mobilenetworkstracker.telephony;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.R;

import javax.inject.Inject;

/**
 * Created by Andrey on 19.03.2016.
 * This class manages telephony functions
 * - starts and stops PhoneStateListener
 * - provides telephony data such as SignalStrengths, LAC, CI etc
 */
public class TelephonyInfo {

    private static final String TAG = "mobilenetworkstracker";

    private TelephonyManager mTelephonyManager;
    private CustomPhoneStateListener mCustomPhoneStateListener;
    private Context mContext;


    public TelephonyInfo(Context context, CustomPhoneStateListener customPhoneStateListener) {
        mContext = context;
        mCustomPhoneStateListener = customPhoneStateListener;

        // Provide system service to telephony manager
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Specify events to listen
        mTelephonyManager.listen(mCustomPhoneStateListener,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    /**
     * Provides network Operator name
     * @return
     */
    public String getNetworkOperator() {
        try {
            return mTelephonyManager.getNetworkOperatorName();
        } catch (NullPointerException e){
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /**
     * Provides Location Area
     * @return
     */
    public String getLac() {
        try {
            GsmCellLocation cellLocation = (GsmCellLocation) mTelephonyManager.getCellLocation();
            int lac = cellLocation.getLac() & 0xffff;
            return String.valueOf(lac);
        } catch (NullPointerException e) {
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /**
     * Provides Cell Identity
     * @return
     */
    public String getCi() {
        try {
            GsmCellLocation cellLocation = (GsmCellLocation) mTelephonyManager.getCellLocation();
            int ci = cellLocation.getCid() & 0xffff;
            return String.valueOf(ci);
        } catch (NullPointerException e) {
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /**
     * Provides mobile network type
     * @return
     */
    public String getNetworkType() {
        try{
            return readNetworkType(mTelephonyManager.getNetworkType());
        } catch (NullPointerException e) {
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /**
     * Provides mobile network type to return in JSON
     * @return
     */
    public String getNetworkTypeForJSON() {
        try {
            return readNetworkTypeForJSON(mTelephonyManager.getNetworkType());
        } catch (Exception e) {
            e.printStackTrace();
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /**
     * Provides network country information according to ISO
     * @return
     */
    public String getCountryIso() {

        try {
            return mTelephonyManager.getNetworkCountryIso();
        } catch (NullPointerException e) {
            return mContext.getResources().getString(R.string.unknown);
        }
    }

    /** TODO: swap with interface
     * Provides network signal strengths in dBm
     * @return
     */
    public int getSignalStrengths() {
        return CustomPhoneStateListener.sSignalStrengths;
    }

    public void stopListener() {
        // Unregister listener PhoneStateListener
        mTelephonyManager.listen(mCustomPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        mTelephonyManager = null;
        mCustomPhoneStateListener = null;
    }


    /**
     * Converts network type integer constant to text value
     * Used for UI update
     */
    private String readNetworkType(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "2G EDGE";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "2G GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G HSPA+";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G HSUPA";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G UMTS";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO rev. B";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDen";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown";
        }
        throw new RuntimeException("New type of network");
    }

    /**
     * Converts network type integer constant to text value
     * Used in JSON
     */
    private String readNetworkTypeForJSON(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO rev. B";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDen";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown";
        }
        throw new RuntimeException("New type of network");
    }
}
