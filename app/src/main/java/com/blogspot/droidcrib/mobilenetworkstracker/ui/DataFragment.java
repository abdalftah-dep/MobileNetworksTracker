package com.blogspot.droidcrib.mobilenetworkstracker.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.blogspot.droidcrib.mobilenetworkstracker.application.MobileNetworksTrackerApp;
import com.blogspot.droidcrib.mobilenetworkstracker.internet.UploadDataService;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.CustomPhoneStateListener;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.PhoneStateListenerInterface;
import com.blogspot.droidcrib.mobilenetworkstracker.telephony.TelephonyInfo;
import com.blogspot.droidcrib.mobilenetworkstracker.controller.TrackManager;
import com.blogspot.droidcrib.mobilenetworkstracker.R;
import com.blogspot.droidcrib.mobilenetworkstracker.model.Track;

import javax.inject.Inject;

/**
 * Created by abulanov on 10.02.2016.
 */
public class DataFragment extends Fragment implements PhoneStateListenerInterface {

    public static final String TAG = "mobilenetworkstracker";
    private static final String ARG_TRACK_ID = "TRACK_ID";

    private TextView mSignalStrenghtsTextView;
    private TextView mNetworkTypeTextView;
    private TextView mOperatorTextView;
    private TextView mLacTextView;
    private TextView mCiTextView;
    private TextView mCountryTextView;
    private EditText mJsonPostUrl;
    TableLayout mTableLayout;

    private Button mButtonJsonUpload;
    private Button mButtonStopService;
    private Button mTestDatabase;
    @Inject TrackManager mTrackManager;
    private Track mTrack;
    private long mTrackId;
    public static DataFragment sDataFragment;
    @Inject TelephonyInfo mTelephonyInfo;
    @Inject CustomPhoneStateListener mCustomPhoneStateListener;


    public static DataFragment getInstance() {
        if (sDataFragment == null) {
            sDataFragment = new DataFragment();
        }
        //sTrackListFragment.setArguments(args);
        return sDataFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MobileNetworksTrackerApp)getActivity().getApplication())
                .getBaseComponent().inject(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Create TrackManager
       // mTrackManager = TrackManager.get(getActivity());


        // Check Track identifier and get series object
        Bundle args = getArguments();
        if (args != null) {
            long trackId = args.getLong(ARG_TRACK_ID, -1);
            if (trackId != -1) {
                mTrack = mTrackManager.getTrack(trackId);
                mTrackId = trackId;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        mSignalStrenghtsTextView = (TextView) v.findViewById(R.id.tv_level);
        mNetworkTypeTextView = (TextView) v.findViewById(R.id.tv_type);
        mOperatorTextView = (TextView) v.findViewById(R.id.tv_operator);
        mLacTextView = (TextView) v.findViewById(R.id.tv_lac);
        mCiTextView = (TextView) v.findViewById(R.id.tv_ci);
        mCountryTextView = (TextView) v.findViewById(R.id.tv_country);
        mJsonPostUrl = (EditText) v.findViewById(R.id.json_post_url);
        mTableLayout = (TableLayout) v.findViewById(R.id.table_indicator);
        mTestDatabase = (Button) v.findViewById(R.id.test_database);
        mButtonJsonUpload = (Button) v.findViewById(R.id.json_upload);
        mButtonStopService = (Button) v.findViewById(R.id.stop_service);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        //set Interface for CustomPhoneStateListener
        mCustomPhoneStateListener.setInterface(DataFragment.this);

        updateUI();

        //TODO: Buttons and button click listeners must be removed after tests finished
        mButtonJsonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mJsonPostUrl.getText().toString();
                // Start service to upload data
                Intent uploadDataService = new Intent(getContext(), UploadDataService.class);
                uploadDataService.putExtra("ipAddress", ip);
                getContext().startService(uploadDataService);
            }
        });

        mButtonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getContext(), UploadDataService.class));
            }
        });

        mTestDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur = mTrackManager.queryPinPointsForTrackMapVisibleArea(
                        0, 49.951671429279884, 33.62251628190279, 49.965991259517104, 33.637965470552444
                );
//                Cursor cur = TrackManager.get(getActivity()).queryFirstPinPointForTrack(1);
                if (cur != null) {
                    int columns = cur.getColumnCount();
                    if (cur.moveToFirst()) {

                        String str = "";
                        // Read table PINPOINTS headers
                        for (int i = 0; i < columns; i++) {
                            str += cur.getColumnName(i) + " ";
                        }
                        Log.d(TAG, "" + str);
                        // Read table PINPOINTS query values
                        do {
                            str = "";
                            for (int i = 0; i < columns; i++) {
                                str += cur.getString(i) + " ";
                            }
                            str += "\n";
                            Log.d(TAG, "" + str);

                        } while (cur.moveToNext());
                    }
                }
            }
        });
    }


    /**
     * Updates UI with actual data
     */
    public void updateUI() {

        String operator = mTelephonyInfo.getNetworkOperator();
        String netType = mTelephonyInfo.getNetworkType();
        int sigStr = mTelephonyInfo.getSignalStrengths();
        String lac = mTelephonyInfo.getLac();
        String ci = mTelephonyInfo.getCi();
        String country = mTelephonyInfo.getCountryIso().toUpperCase();

        mOperatorTextView.setText(operator);
        mNetworkTypeTextView.setText(netType);
        mSignalStrenghtsTextView.setText(signalLevelExplain(sigStr) + " (" + sigStr + ")");
        mLacTextView.setText(lac);
        mCiTextView.setText(ci);
        mCountryTextView.setText(country);

        // Signal level indicator is represented by TableLayout with 64 rows
        int indicatorLevel = mTelephonyInfo.getSignalStrengths() * (-1) - 51;

        for (int i = 0; i < mTableLayout.getChildCount(); i++) {

            if (i <= indicatorLevel) {
                mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.color_blue_gray_100));
                if (i > 48 && i <= 63) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_red_idle));
                } else if (i > 38 && i <= 48) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_orange_idle));
                } else if (i > 28 && i <= 38) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_yellow_idle));
                } else if (i > 18 && i <= 28) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_light_green_idle));
                } else if (i <= 18) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_green_idle));
                }
            } else {
                if (i > 48 && i <= 63) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_red));
                } else if (i > 38 && i <= 48) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_orange));
                } else if (i > 28 && i <= 38) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_yellow));
                } else if (i > 18 && i <= 28) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_light_green));
                } else if (i <= 18) {
                    mTableLayout.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.legend_green));
                }
            }
        }
    }


    /**
     *  Provides human readable explanation for signal level value
     * @param rxLevel input signal level
     * @return signal level value meaning
     */
    private String signalLevelExplain(int rxLevel) {
        String description = "";

        if (rxLevel < -50 && rxLevel >= -70) {
            description = getResources().getString(R.string.excellent);
        } else if (rxLevel < -70 && rxLevel >= -80) {
            description = getResources().getString(R.string.good);
        } else if (rxLevel < -80 && rxLevel >= -90) {
            description = getResources().getString(R.string.sufficient);
        } else if (rxLevel < -90 && rxLevel >= -100) {
            description = getResources().getString(R.string.poor);
        } else if (rxLevel < -100 && rxLevel >= -115) {
            description = getResources().getString(R.string.bad);
        } else if (rxLevel == 99) {
            description = getResources().getString(R.string.no_signal);
        }

        return description;
    }

    @Override
    public void signalStrengthsChanged(int signalStrengths) {
        // Update UI on Signal Strengths changes
        if(this.isVisible()){
            updateUI();
        }
    }
}
