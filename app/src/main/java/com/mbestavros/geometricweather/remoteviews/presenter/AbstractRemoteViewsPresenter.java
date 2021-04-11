package com.mbestavros.geometricweather.remoteviews.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.basic.model.option.WidgetWeekIconMode;
import com.mbestavros.geometricweather.basic.model.option.unit.DistanceUnit;
import com.mbestavros.geometricweather.basic.model.option.unit.PrecipitationUnit;
import com.mbestavros.geometricweather.basic.model.option.unit.PressureUnit;
import com.mbestavros.geometricweather.basic.model.option.unit.ProbabilityUnit;
import com.mbestavros.geometricweather.basic.model.option.unit.RelativeHumidityUnit;
import com.mbestavros.geometricweather.basic.model.option.unit.TemperatureUnit;
import com.mbestavros.geometricweather.basic.model.weather.Base;
import com.mbestavros.geometricweather.basic.model.weather.Weather;
import com.mbestavros.geometricweather.remoteviews.WidgetUtils;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.utils.DisplayUtils;
import com.mbestavros.geometricweather.resource.ResourceUtils;
import com.mbestavros.geometricweather.utils.helpter.IntentHelper;
import com.mbestavros.geometricweather.utils.helpter.LunarHelper;

public abstract class AbstractRemoteViewsPresenter {

    private static final int SUBTITLE_DAILY_ITEM_LENGTH = 5;

    public static class WidgetConfig {
        public String viewStyle;
        public String cardStyle;
        public int cardAlpha;
        public String textColor;
        public int textSize;
        public boolean hideSubtitle;
        public String subtitleData;
        public String clockFont;
        public boolean hideLunar;
    }

    public static class WidgetColor {
        public boolean showCard;
        public boolean darkCard;
        public boolean darkText;

        public WidgetColor(Context context, boolean dayTime, String cardStyle, String textColor) {
            showCard = !cardStyle.equals("none");
            darkCard = cardStyle.equals("dark") || (cardStyle.equals("auto") && !dayTime);

            darkText = showCard
                    ? !darkCard // light card.
                    : textColor.equals("dark") || (textColor.equals("auto") && isLightWallpaper(context));
        }
    }

    public static WidgetConfig getWidgetConfig(Context context, String sharedPreferencesName) {
        WidgetConfig config = new WidgetConfig();

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                sharedPreferencesName,
                Context.MODE_PRIVATE
        );
        config.viewStyle = sharedPreferences.getString(
                context.getString(R.string.key_view_type),
                "rectangle"
        );
        config.cardStyle = sharedPreferences.getString(
                context.getString(R.string.key_card_style),
                "none"
        );
        config.cardAlpha = sharedPreferences.getInt(
                context.getString(R.string.key_card_alpha),
                100
        );
        config.textColor = sharedPreferences.getString(
                context.getString(R.string.key_text_color),
                "light"
        );
        config.textSize = sharedPreferences.getInt(
                context.getString(R.string.key_text_size),
                100
        );
        config.hideSubtitle = sharedPreferences.getBoolean(
                context.getString(R.string.key_hide_subtitle),
                false
        );
        config.subtitleData = sharedPreferences.getString(
                context.getString(R.string.key_subtitle_data),
                "time"
        );
        config.clockFont = sharedPreferences.getString(
                context.getString(R.string.key_clock_font),
                "light"
        );
        config.hideLunar = sharedPreferences.getBoolean(
                context.getString(R.string.key_hide_lunar),
                false
        );

        return config;
    }

    public static boolean isLightWallpaper(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        try {
            WallpaperManager manager = WallpaperManager.getInstance(context);
            if (manager == null) {
                return false;
            }

            Drawable drawable = manager.getDrawable();
            if (!(drawable instanceof BitmapDrawable)) {
                return false;
            }

            return DisplayUtils.isLightColor(
                    DisplayUtils.bitmapToColorInt(((BitmapDrawable) drawable).getBitmap())
            );
        } catch (Exception ignore) {
            return false;
        }
    }

    @DrawableRes
    public static int getCardBackgroundId(Context context, boolean darkCard, int cardAlpha) {
        int resId;
        if (darkCard) {
            resId = ResourceUtils.getResId(
                    context,
                    "widget_card_dark_" + cardAlpha,
                    "drawable"
            );
            if (resId != 0) {
                return resId;
            }
            return R.drawable.widget_card_dark_100;
        } else {
            resId = ResourceUtils.getResId(
                    context,
                    "widget_card_light_" + cardAlpha,
                    "drawable"
            );
            if (resId != 0) {
                return resId;
            }
            return R.drawable.widget_card_light_100;
        }
    }

    public static boolean isWeekIconDaytime(WidgetWeekIconMode mode, boolean daytime) {
        switch (mode) {
            case DAY:
                return true;

            case NIGHT:
                return false;

            default:
                return daytime;
        }
    }

    public static PendingIntent getWeatherPendingIntent(Context context,
                                                        @Nullable Location location, int requestCode) {
        return PendingIntent.getActivity(
                context,
                requestCode,
                IntentHelper.buildMainActivityIntent(location),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDailyForecastPendingIntent(Context context,
                                                              @Nullable Location location, int index,
                                                              int requestCode) {
        return PendingIntent.getActivity(
                context,
                requestCode,
                IntentHelper.buildMainActivityShowDailyForecastIntent(location, index),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getRefreshPendingIntent(Context context, int requestCode) {
        return PendingIntent.getService(
                context,
                requestCode,
                IntentHelper.getAwakeForegroundUpdateServiceIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getAlarmPendingIntent(Context context, int requestCode) {
        return PendingIntent.getActivity(
                context,
                requestCode,
                new Intent(AlarmClock.ACTION_SHOW_ALARMS),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getCalendarPendingIntent(Context context, int requestCode) {
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, System.currentTimeMillis());
        return PendingIntent.getActivity(
                context,
                requestCode,
                new Intent(Intent.ACTION_VIEW).setData(builder.build()),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        return DisplayUtils.drawableToBitmap(drawable);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCustomSubtitle(Context context, @Nullable String subtitle,
                                           @NonNull Location location, @NonNull Weather weather) {
        if (TextUtils.isEmpty(subtitle)) {
            return "";
        }
        TemperatureUnit temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
        PrecipitationUnit precipitationUnit = SettingsOptionManager.getInstance(context).getPrecipitationUnit();
        PressureUnit pressureUnit = SettingsOptionManager.getInstance(context).getPressureUnit();
        DistanceUnit distanceUnit = SettingsOptionManager.getInstance(context).getDistanceUnit();

        subtitle = subtitle
                .replace("$cw$", weather.getCurrent().getWeatherText())
                .replace(
                        "$ct$",
                        weather.getCurrent()
                                .getTemperature()
                                .getTemperature(context, temperatureUnit) + ""
                ).replace(
                        "$ctd$",
                        weather.getCurrent()
                                .getTemperature()
                                .getShortTemperature(context, temperatureUnit) + ""
                ).replace(
                        "$at$",
                        weather.getCurrent()
                                .getTemperature()
                                .getRealFeelTemperature(context, temperatureUnit) + ""
                ).replace(
                        "$atd$",
                        weather.getCurrent()
                                .getTemperature()
                                .getShortRealFeeTemperature(context, temperatureUnit) + ""
                ).replace(
                        "$cpb$",
                        ProbabilityUnit.PERCENT.getProbabilityText(
                                context,
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent()
                                                .getPrecipitationProbability()
                                                .getTotal(),
                                        0
                                )
                        )
                ).replace(
                        "$cp$",
                        precipitationUnit.getPrecipitationText(
                                context,
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent()
                                                .getPrecipitation()
                                                .getTotal(),
                                        0
                                )
                        )
                ).replace(
                        "$cwd$",
                        weather.getCurrent().getWind().getLevel()
                                + " ("
                                + weather.getCurrent().getWind().getDirection()
                                + ")"
                ).replace("$cuv$", weather.getCurrent().getUV().getShortUVDescription())
                .replace(
                        "$ch$",
                        RelativeHumidityUnit.PERCENT.getRelativeHumidityText(
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent().getRelativeHumidity(),
                                        0
                                )
                        )
                ).replace(
                        "$cps$",
                        pressureUnit.getPressureText(
                                context,
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent().getPressure(),
                                        0
                                )
                        )
                ).replace(
                        "$cv$",
                        distanceUnit.getDistanceText(
                                context,
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent().getVisibility(),
                                        0
                                )
                        )
                ).replace(
                        "$cdp$",
                        temperatureUnit.getTemperatureText(
                                context,
                                WidgetUtils.getNonNullValue(
                                        weather.getCurrent().getDewPoint(),
                                        0
                                )
                        )
                ).replace("$l$", location.getCityName(context))
                .replace("$lat$", String.valueOf(location.getLatitude()))
                .replace("$lon$", String.valueOf(location.getLongitude()))
                .replace("$ut$", Base.getTime(context, weather.getBase().getUpdateDate()))
                .replace(
                        "$d$",
                        new SimpleDateFormat(context.getString(R.string.date_format_long)).format(new Date())
                ).replace(
                        "$lc$",
                        LunarHelper.getLunarDate(new Date())
                ).replace(
                        "$w$",
                        new SimpleDateFormat("EEEE").format(new Date())
                ).replace(
                        "$ws$",
                        new SimpleDateFormat("EEE").format(new Date())
                ).replace("$dd$", weather.getCurrent().getDailyForecast() + "")
                .replace("$hd$", weather.getCurrent().getHourlyForecast() + "")
                .replace("$enter$", "\n");
        subtitle = replaceAlerts(subtitle, weather);
        subtitle = replaceDaytimeWeatherSubtitle(subtitle, weather);
        subtitle = replaceNighttimeWeatherSubtitle(subtitle, weather);
        subtitle = replaceDaytimeTemperatureSubtitle(context, subtitle, weather, temperatureUnit);
        subtitle = replaceNighttimeTemperatureSubtitle(context, subtitle, weather, temperatureUnit);
        subtitle = replaceDaytimeDegreeTemperatureSubtitle(context, subtitle, weather, temperatureUnit);
        subtitle = replaceNighttimeDegreeTemperatureSubtitle(context, subtitle, weather, temperatureUnit);
        subtitle = replaceDaytimePrecipitationSubtitle(context, subtitle, weather);
        subtitle = replaceNighttimePrecipitationSubtitle(context, subtitle, weather);
        subtitle = replaceDaytimeWindSubtitle(subtitle, weather);
        subtitle = replaceNighttimeWindSubtitle(subtitle, weather);
        subtitle = replaceSunriseSubtitle(context, subtitle, weather);
        subtitle = replaceSunsetSubtitle(context, subtitle, weather);
        subtitle = replaceMoonriseSubtitle(context, subtitle, weather);
        subtitle = replaceMoonsetSubtitle(context, subtitle, weather);
        subtitle = replaceMoonPhaseSubtitle(context, subtitle, weather);
        return subtitle;
    }

    private static String replaceAlerts(@NonNull String subtitle, @NonNull Weather weather) {
        StringBuilder defaultBuilder = new StringBuilder();
        StringBuilder shortBuilder = new StringBuilder();
        for (int i = 0; i < weather.getAlertList().size(); i ++) {
            defaultBuilder.append(weather.getAlertList().get(i).getDescription())
                    .append(", ")
                    .append(
                            DateFormat.getDateTimeInstance(
                                    DateFormat.DEFAULT,
                                    DateFormat.SHORT
                            ).format(weather.getAlertList().get(i).getDate())
                    );
            shortBuilder.append(weather.getAlertList().get(i).getDescription());

            if (i != weather.getAlertList().size() - 1) {
                defaultBuilder.append("\n");
                shortBuilder.append("\n");
            }
        }
        return subtitle.replace("$al$", defaultBuilder.toString())
                .replace("$als$", shortBuilder.toString());
    }

    private static String replaceDaytimeWeatherSubtitle(@NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "dw$",
                    weather.getDailyForecast().get(i).day().getWeatherText()
            );
        }
        return subtitle;
    }

    private static String replaceNighttimeWeatherSubtitle(@NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "nw$",
                    weather.getDailyForecast().get(i).night().getWeatherText()
            );
        }
        return subtitle;
    }

    private static String replaceDaytimeTemperatureSubtitle(Context context, @NonNull String subtitle,
                                                            @NonNull Weather weather, TemperatureUnit unit) {

        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "dt$",
                    weather.getDailyForecast()
                            .get(i)
                            .day()
                            .getTemperature()
                            .getTemperature(context, unit) + ""
            );
        }
        return subtitle;
    }

    private static String replaceNighttimeTemperatureSubtitle(Context context, @NonNull String subtitle,
                                                              @NonNull Weather weather, TemperatureUnit unit) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "nt$",
                    weather.getDailyForecast()
                            .get(i)
                            .night()
                            .getTemperature()
                            .getTemperature(context, unit) + ""
            );
        }
        return subtitle;
    }

    private static String replaceDaytimeDegreeTemperatureSubtitle(Context context,
                                                                  @NonNull String subtitle,
                                                                  @NonNull Weather weather,
                                                                  TemperatureUnit unit) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "dtd$",
                    weather.getDailyForecast()
                            .get(i)
                            .day()
                            .getTemperature()
                            .getShortTemperature(context, unit) + ""
            );
        }
        return subtitle;
    }

    private static String replaceNighttimeDegreeTemperatureSubtitle(Context context,
                                                                    @NonNull String subtitle,
                                                                    @NonNull Weather weather,
                                                                    TemperatureUnit unit) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "ntd$",
                    weather.getDailyForecast()
                            .get(i)
                            .night()
                            .getTemperature()
                            .getShortTemperature(context, unit) + ""
            );
        }
        return subtitle;
    }

    private static String replaceDaytimePrecipitationSubtitle(Context context,
                                                              @NonNull String subtitle,
                                                              @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "dp$",
                    ProbabilityUnit.PERCENT.getProbabilityText(
                            context,
                            WidgetUtils.getNonNullValue(
                                    weather.getDailyForecast()
                                            .get(i)
                                            .day()
                                            .getPrecipitationProbability()
                                            .getTotal(),
                                    0
                            )
                    )
            );
        }
        return subtitle;
    }

    private static String replaceNighttimePrecipitationSubtitle(Context context,
                                                                @NonNull String subtitle,
                                                                @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "np$",
                    ProbabilityUnit.PERCENT.getProbabilityText(
                            context,
                            WidgetUtils.getNonNullValue(
                                    weather.getDailyForecast()
                                            .get(i)
                                            .night()
                                            .getPrecipitationProbability()
                                            .getTotal(),
                                    0
                            )
                    )
            );
        }
        return subtitle;
    }

    private static String replaceDaytimeWindSubtitle(@NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "dwd$",
                    weather.getDailyForecast().get(i).day().getWind().getLevel()
                            + " ("
                            + weather.getDailyForecast().get(i).day().getWind().getDirection()
                            + ")"
            );
        }
        return subtitle;
    }

    private static String replaceNighttimeWindSubtitle(@NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "nwd$",
                    weather.getDailyForecast().get(i).night().getWind().getLevel()
                            + " ("
                            + weather.getDailyForecast().get(i).night().getWind().getDirection()
                            + ")"
            );
        }
        return subtitle;
    }

    private static String replaceSunriseSubtitle(Context context, @NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "sr$",
                    weather.getDailyForecast().get(i).sun().getRiseTime(context) + ""
            );
        }
        return subtitle;
    }

    private static String replaceSunsetSubtitle(Context context, @NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "ss$",
                    weather.getDailyForecast().get(i).sun().getSetTime(context) + ""
            );
        }
        return subtitle;
    }

    private static String replaceMoonriseSubtitle(Context context, @NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "mr$",
                    weather.getDailyForecast().get(i).moon().getRiseTime(context) + ""
            );
        }
        return subtitle;
    }

    private static String replaceMoonsetSubtitle(Context context, @NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "ms$",
                    weather.getDailyForecast().get(i).moon().getSetTime(context) + ""
            );
        }
        return subtitle;
    }

    private static String replaceMoonPhaseSubtitle(Context context, @NonNull String subtitle, @NonNull Weather weather) {
        for (int i = 0; i < SUBTITLE_DAILY_ITEM_LENGTH; i ++) {
            subtitle = subtitle.replace(
                    "$" + i + "mp$",
                    weather.getDailyForecast().get(i).getMoonPhase().getMoonPhase(context) + ""
            );
        }
        return subtitle;
    }
}
