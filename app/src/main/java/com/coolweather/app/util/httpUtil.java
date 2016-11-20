package com.coolweather.app.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by whjin on 16-11-11.
 */
public class httpUtil {
    private static final String TAG = "httpUtil";
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        Log.d(TAG, "address =================" + address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    Log.d(TAG, "start get Inputstream =============");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    Log.d(TAG, "reader ============"  + reader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine()) != null) {
                        Log.d(TAG, "line ==================" + line);
                        response.append(line);
                    }

                    Log.d(TAG, "response ==============" + response);

                    if (null != listener) {
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e) {
                    if (null != listener) {
                        listener.onError(e);
                    }
                }finally {
                    if (null != connection) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
