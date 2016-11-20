package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by whjin on 16-11-11.
 */
public class Utility {
    private static final String TAG = "Utility";
    //return province data from http, and sava data to db
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB db, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (null!=allProvince && allProvince.length>0) {
                for (String p : allProvince) {
                    String[] array = p.split(":");
                    Province province = new Province();
                    province.setProvinceCode(array[0].replaceAll("\"", ""));
                    province.setProvinceName(array[1].replaceAll("\"", ""));
                    Log.d(TAG, "province code =============" + province.getProvinceCode());
                    Log.d(TAG, "province name =============" + province.getProvinceName());
                    db.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //return city data from http, and save data to db
    public synchronized static boolean handleCityResponse(CoolWeatherDB db, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (null!=allCity && allCity.length>0) {
                for (String c : allCity) {
                    String[] array = c.split(":");
                    City city = new City();
                    city.setCityCode(array[0].replaceAll("\"", ""));
                    city.setCityName(array[1].replaceAll("\"", ""));
                    city.setProvinceId(provinceId);
                    db.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //return county data from http, and save data to db
    public synchronized static boolean handleCountyResponse(CoolWeatherDB db, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounty = response.split(",");
            if (null!=allCounty && allCounty.length>0) {
                for (String cc : allCounty) {
                    String[] array = cc.split(":");
                    County county = new County();
                    county.setCountyCode(array[0].replaceAll("\"", ""));
                    county.setCountyName(array[1].replaceAll("\"", ""));
                    county.setCityId(cityId);
                    db.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    //parse json data, and save to db
    public static void handleWeatherResponse(Context context, String response) {
        try {
            /*
            * {"weatherinfo":
            *     {"city":"", "cityid":"", "temp1":"", "temp2":"", "weather":"", "ptime":""}
            * }
            *
            * */
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //save weatherinfo to db
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                       String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", "weatherCode");
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
