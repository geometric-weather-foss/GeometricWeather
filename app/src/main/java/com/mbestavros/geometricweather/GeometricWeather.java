package com.mbestavros.geometricweather;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.mbestavros.geometricweather.basic.GeoActivity;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.weather.TLSCompactHelper;
import com.mbestavros.geometricweather.utils.LanguageUtils;
import com.mbestavros.geometricweather.utils.manager.TimeManager;

/**
 * Geometric weather application class.
 * */

public class GeometricWeather extends MultiDexApplication {

    private static GeometricWeather instance;
    public static GeometricWeather getInstance() {
        return instance;
    }

    private Set<GeoActivity> activitySet;

    private OkHttpClient okHttpClient;
    private GsonConverterFactory gsonConverterFactory;
    private RxJava2CallAdapterFactory rxJava2CallAdapterFactory;

    public static final String NOTIFICATION_CHANNEL_ID_NORMALLY = "normally";
    public static final String NOTIFICATION_CHANNEL_ID_ALERT = "alert";
    public static final String NOTIFICATION_CHANNEL_ID_FORECAST = "forecast";
    public static final String NOTIFICATION_CHANNEL_ID_LOCATION = "location";
    public static final String NOTIFICATION_CHANNEL_ID_BACKGROUND = "background";

    public static final int NOTIFICATION_ID_NORMALLY = 1;
    public static final int NOTIFICATION_ID_TODAY_FORECAST = 2;
    public static final int NOTIFICATION_ID_TOMORROW_FORECAST = 3;
    public static final int NOTIFICATION_ID_LOCATION = 4;
    public static final int NOTIFICATION_ID_RUNNING_IN_BACKGROUND = 5;
    public static final int NOTIFICATION_ID_UPDATING_NORMALLY = 6;
    public static final int NOTIFICATION_ID_UPDATING_TODAY_FORECAST= 7;
    public static final int NOTIFICATION_ID_UPDATING_TOMORROW_FORECAST= 8;
    public static final int NOTIFICATION_ID_UPDATING_AWAKE = 9;
    public static final int NOTIFICATION_ID_ALERT_MIN = 1000;
    public static final int NOTIFICATION_ID_ALERT_MAX = 1999;
    public static final int NOTIFICATION_ID_ALERT_GROUP = 2000;
    public static final int NOTIFICATION_ID_PRECIPITATION = 3000;

    // day.
    public static final int WIDGET_DAY_PENDING_INTENT_CODE_WEATHER = 11;
    public static final int WIDGET_DAY_PENDING_INTENT_CODE_REFRESH = 12;
    public static final int WIDGET_DAY_PENDING_INTENT_CODE_CALENDAR = 13;
    // week.
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_WEATHER = 21;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_1 = 211;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_2 = 212;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_3 = 213;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_4 = 214;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_5 = 215;
    public static final int WIDGET_WEEK_PENDING_INTENT_CODE_REFRESH = 22;
    // day + week.
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_WEATHER = 31;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_1 = 311;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_2 = 312;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_3 = 313;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_4 = 314;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_5 = 315;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_REFRESH = 32;
    public static final int WIDGET_DAY_WEEK_PENDING_INTENT_CODE_CALENDAR = 33;
    // clock + day (vertical).
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_WEATHER = 41;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_REFRESH = 42;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_LIGHT = 43;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_NORMAL = 44;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_BLACK = 45;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_1_LIGHT = 46;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_2_LIGHT = 47;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_1_NORMAL = 48;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_2_NORMAL = 49;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_1_BLACK = 50;
    public static final int WIDGET_CLOCK_DAY_VERTICAL_PENDING_INTENT_CODE_CLOCK_2_BLACK = 51;
    // clock + day (horizontal).
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_WEATHER = 61;
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_REFRESH = 62;
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_CALENDAR = 63;
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_CLOCK_LIGHT = 64;
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_CLOCK_NORMAL = 65;
    public static final int WIDGET_CLOCK_DAY_HORIZONTAL_PENDING_INTENT_CODE_CLOCK_BLACK = 66;
    // clock + day + details.
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_WEATHER = 71;
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_REFRESH = 72;
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CALENDAR = 73;
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_LIGHT = 74;
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_NORMAL = 75;
    public static final int WIDGET_CLOCK_DAY_DETAILS_PENDING_INTENT_CODE_CLOCK_BLACK = 76;
    // clock + day + week.
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_WEATHER = 81;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_1 = 821;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_2 = 822;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_3 = 823;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_4 = 824;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_5 = 825;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_REFRESH = 82;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_CALENDAR = 83;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_CLOCK_LIGHT = 84;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_CLOCK_NORMAL = 85;
    public static final int WIDGET_CLOCK_DAY_WEEK_PENDING_INTENT_CODE_CLOCK_BLACK = 86;

    // text.
    public static final int WIDGET_TEXT_PENDING_INTENT_CODE_WEATHER = 91;
    public static final int WIDGET_TEXT_PENDING_INTENT_CODE_REFRESH = 92;
    public static final int WIDGET_TEXT_PENDING_INTENT_CODE_CALENDAR = 93;
    // trend daily.
    public static final int WIDGET_TREND_DAILY_PENDING_INTENT_CODE_WEATHER = 101;
    public static final int WIDGET_TREND_DAILY_PENDING_INTENT_CODE_REFRESH = 102;
    // trend hourly.
    public static final int WIDGET_TREND_HOURLY_PENDING_INTENT_CODE_WEATHER = 111;
    public static final int WIDGET_TREND_HOURLY_PENDING_INTENT_CODE_REFRESH = 112;
    // multi city.
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_WEATHER_1 = 121;
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_REFRESH_1 = 122;
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_WEATHER_2 = 123;
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_REFRESH_2 = 124;
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_WEATHER_3 = 125;
    public static final int WIDGET_MULTI_CITY_PENDING_INTENT_CODE_REFRESH_3 = 126;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();

        String processName = getProcessName();
        if (processName != null && processName.equals(getPackageName())) {
            resetDayNightMode();
        }
    }

    private void initialize() {
        instance = this;
        activitySet = new HashSet<>();

        okHttpClient = TLSCompactHelper.getClientBuilder().build();
        gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create());
                // new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create());
        rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();

        LanguageUtils.setLanguage(
                this, SettingsOptionManager.getInstance(this).getLanguage().getLocale());
    }

    public void addActivity(GeoActivity a) {
        activitySet.add(a);
    }

    public void removeActivity(GeoActivity a) {
        activitySet.remove(a);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public GsonConverterFactory getGsonConverterFactory() {
        return gsonConverterFactory;
    }

    public RxJava2CallAdapterFactory getRxJava2CallAdapterFactory() {
        return rxJava2CallAdapterFactory;
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNotificationChannelName(Context c, String channelId) {
        switch (channelId) {
            case NOTIFICATION_CHANNEL_ID_ALERT:
                return c.getString(R.string.geometric_weather) + " " + c.getString(R.string.action_alert);

            case NOTIFICATION_CHANNEL_ID_FORECAST:
                return c.getString(R.string.geometric_weather) + " " + c.getString(R.string.forecast);

            case NOTIFICATION_CHANNEL_ID_LOCATION:
                return c.getString(R.string.geometric_weather) + " " + c.getString(R.string.feedback_request_location);

            case NOTIFICATION_CHANNEL_ID_BACKGROUND:
                return c.getString(R.string.geometric_weather) + " " + c.getString(R.string.background_information);

            default:
                return c.getString(R.string.geometric_weather);
        }
    }

    public void resetDayNightMode() {
        switch (SettingsOptionManager.getInstance(this).getDarkMode()) {
            case AUTO:
                AppCompatDelegate.setDefaultNightMode(
                        TimeManager.getInstance(this).isDayTime()
                                ? AppCompatDelegate.MODE_NIGHT_NO
                                : AppCompatDelegate.MODE_NIGHT_YES
                );
                break;

            case LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    public void recreateAllActivities() {
        for (Activity a : activitySet) {
            a.recreate();
        }
    }
}
