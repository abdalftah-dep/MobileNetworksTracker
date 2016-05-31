package com.blogspot.droidcrib.mobilenetworkstracker.filesystem;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.blogspot.droidcrib.mobilenetworkstracker.model.PinPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Andrey on 11.01.2016.
 */
public class PointsToJSONSerializer {

    public static final String TAG = "mobilenetworkstracker";

    private final static String APP_PATH = Environment.getExternalStorageDirectory().toString();
    private final static String APP_DIRECTORY = "/MobileNetworksTracker/";
    private final static String APP_FILE_EXT = ".json";
    private static String SERIES_FILENAME;

    private Context mContext;

    /**
     * Public constructor
     */
    public PointsToJSONSerializer(Context context) {
        mContext = context;
        // Check if directory exists on SD card and create it if it's not exist
        File f = new File(Environment.getExternalStorageDirectory() + APP_DIRECTORY);
        if (!f.exists()) {
            Log.d(TAG, "Directory not exist, creating...");
            f.mkdirs();
        }
        SERIES_FILENAME = APP_PATH + APP_DIRECTORY + "series" + APP_FILE_EXT;
        // Check if file exists on SD card and create it if it's not exist
        File ff = new File(SERIES_FILENAME);
        if (!ff.exists()) {
            Log.d(TAG, "File not exist, creating...");
            try {
                ff.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves PinPoint objects to file as json objects
     * @param points points array list
     * @throws JSONException
     * @throws IOException
     */
    public void savePoints(ArrayList<PinPoint> points, String fileName) throws JSONException, IOException {
        // Build json array
        JSONArray jsonArray = new JSONArray();
        for (PinPoint p : points) jsonArray.put(p.toJson());
        // Saving file
        File file;
        FileOutputStream outputStream;
        try {
            file = new File(APP_PATH + APP_DIRECTORY + fileName + APP_FILE_EXT);
            outputStream = new FileOutputStream(file);
            outputStream.write(jsonArray.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends PinPoint object to file as JSON object
     *
     * @param pinPoint
     */
    public void appendPointToFile(PinPoint pinPoint) {
        try {
            JSONObject jso = pinPoint.toJson();
//            FileOutputStream fos = mContext.openFileOutput(SERIES_FILENAME, Context.MODE_APPEND);
            File fileName = new File(SERIES_FILENAME);
            FileOutputStream fos = new FileOutputStream(fileName, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(jso.toString());
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads data from file and saves as JSON array
     */
    public ArrayList<PinPoint> loadPoints() throws IOException, JSONException {
        Log.d(TAG, "Loading from file ...");
        ArrayList<PinPoint> pinPoints = new ArrayList<PinPoint>();
        BufferedReader reader = null;
        try {
            // Open and read file into StringBuilder
            File fileName = new File(SERIES_FILENAME);
            InputStream in = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
            // Line breaks are omitted and irrelevant
                jsonString.append(line);
                Log.d(TAG, "read line from file: " + line);
            }
            // Разбор JSON с использованием JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // Build array of PinPoint objects using JSONObject data
            for (int i = 0; i < array.length(); i++) {
                pinPoints.add(new PinPoint(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
             // Происходит при начале "с нуля"; не обращайте внимания
        } finally {
            if (reader != null)
                reader.close();
        }

        Log.d(TAG, "Objects loaded: " + pinPoints.size());
        return pinPoints;
    }


    /**
     * Deletes file with specified file name from directory specified in APP_DIRECTORY
     *
     * @param fileName file name to delete
     */
    public void deleteFile(String fileName) {

    }

    /**
     * Returns list of files in app's external storage directory
     * where .json files are saved
     */
    public void getFilesList() {
        File f = new File(APP_PATH + APP_DIRECTORY);
        File filesList[] = f.listFiles();
        Log.d(TAG, "Files in girectory: " + filesList.length);
        for (int i = 0; i < filesList.length; i++) {
            Log.d(TAG, "Filename: " + filesList[i].getName());
        }
    }


}
