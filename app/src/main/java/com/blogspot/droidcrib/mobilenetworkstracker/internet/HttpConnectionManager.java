package com.blogspot.droidcrib.mobilenetworkstracker.internet;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpConnectionManager {

    public static final String TAG = "mobilenetworkstracker";

    /**
     * Gets bytes from URL and returns them as byte array
     */
    public byte[] getUrlBytes(String urlInput) throws IOException {

        // Create URL and open HTTP connection
        URL url = new URL(urlInput);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // Create streams
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            // Get content size to be fetched
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            // Save stream to byte array
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Reads input stream and returns it as byte array
     * @param in InputStream
     * @return
     * @throws IOException
     */
     private byte[] getStreamBytes(InputStream in) throws IOException {
        try {
            // Create streams
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // Get content size to be fetched
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            // Save stream to byte array
            return out.toByteArray();
        } finally {
        }
    }

    /**
     * Converts bytes array data to String
     */
    public String getStringFromBytes(String url) throws IOException {
        return new String(getUrlBytes(url));
    }

    /**
     * Sends JSON object to server using POST method
     * @param jsonObject object to send
     * @param postUrl server URL
     * @throws IOException
     */
    public String postJsonObject(JSONObject jsonObject, String postUrl) throws IOException {
        URL url = new URL(postUrl);
        String serverResponse = new String();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            OutputStream outputStream;
            outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes("UTF-8"));
            outputStream.close();
            // read the response
            InputStream in = new BufferedInputStream(connection.getInputStream());
            serverResponse = new String(getStreamBytes(in));
            Log.d(TAG, "POST RESPONCE: " + serverResponse);
        } finally {
            connection.disconnect();
        }
        return serverResponse;
    }


}
