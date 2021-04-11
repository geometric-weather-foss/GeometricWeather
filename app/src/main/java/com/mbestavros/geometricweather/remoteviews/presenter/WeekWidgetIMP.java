package com.mbestavros.geometricweather.remoteviews.presenter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import androidx.core.content.ContextCompat;

import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.mbestavros.geometricweather.GeometricWeather;
import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.background.receiver.widget.WidgetWeekProvider;
import com.mbestavros.geometricweather.basic.model.option.WidgetWeekIconMode;
import com.mbestavros.geometricweather.basic.model.option.unit.TemperatureUnit;
import com.mbestavros.geometricweather.basic.model.weather.Temperature;
import com.mbestavros.geometricweather.basic.model.weather.Weather;
import com.mbestavros.geometricweather.remoteviews.WidgetUtils;
import com.mbestavros.geometricweather.resource.ResourceHelper;
import com.mbestavros.geometricweather.resource.provider.ResourceProvider;
import com.mbestavros.geometricweather.resource.provider.ResourcesProviderFactory;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.utils.manager.TimeManager;

public class WeekWidgetIMP extends AbstractRemoteViewsPresenter {

    public static void updateWidgetView(Context context, Location location) {
        WidgetConfig config = getWidgetConfig(
                context,
                context.getString(R.string.sp_widget_week_setting)
        );

        RemoteViews views = getRemoteViews(context, location, config.viewStyle, config.cardStyle,
                config.cardAlpha, config.textColor, config.textSize);

        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, WidgetWeekProvider.class),
                views
        );
    }

    public static RemoteViews getRemoteViews(Context context, Location location, String viewStyle,
                                             String cardStyle, int cardAlpha, String textColor, int textSize) {
        RemoteViews views;
        switch (viewStyle) {
            case "3_days":
                views = new RemoteViews(context.getPackageName(), R.layout.widget_week_3);
                break;

            default: // 5_days
                views = new RemoteViews(context.getPackageName(), R.layout.widget_week);
                break;
        }

        Weather weather = location.getWeather();
        if (weather == null) {
            return views;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        boolean dayTime = TimeManager.isDaylight(location);

        SettingsOptionManager settings = SettingsOptionManager.getInstance(context);
        TemperatureUnit temperatureUnit = settings.getTemperatureUnit();
        WidgetWeekIconMode weekIconMode = settings.getWidgetWeekIconMode();
        boolean minimalIcon = settings.isWidgetMinimalIconEnabled();
        boolean touchToRefresh = settings.isWidgetClickToRefreshEnabled();

        WidgetColor color = new WidgetColor(context, dayTime, cardStyle, textColor);

        // get text color.
        int textColorInt;
        if (color.darkText) {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextDark);
        } else {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextLight);
        }

        // weather view.
        views.setTextViewText(
                R.id.widget_week_week_1,
                WidgetUtils.getDailyWeek(context, weather, 0));
        views.setTextViewText(
                R.id.widget_week_week_2,
                WidgetUtils.getDailyWeek(context, weather, 1));
        views.setTextViewText(
                R.id.widget_week_week_3,
                WidgetUtils.getDailyWeek(context, weather, 2));
        views.setTextViewText(
                R.id.widget_week_week_4,
                WidgetUtils.getDailyWeek(context, weather, 3));
        views.setTextViewText(
                R.id.widget_week_week_5,
                WidgetUtils.getDailyWeek(context, weather, 4));

        views.setTextViewText(
                R.id.widget_week_temp,
                weather.getCurrent().getTemperature().getShortTemperature(context, temperatureUnit));
        views.setTextViewText(
                R.id.widget_week_temp_1,
                getTemp(context, weather, 0, temperatureUnit));
        views.setTextViewText(
                R.id.widget_week_temp_2,
                getTemp(context, weather, 1, temperatureUnit));
        views.setTextViewText(
                R.id.widget_week_temp_3,
                getTemp(context, weather, 2, temperatureUnit));
        views.setTextViewText(
                R.id.widget_week_temp_4,
                getTemp(context, weather, 3, temperatureUnit));
        views.setTextViewText(
                R.id.widget_week_temp_5,
                getTemp(context, weather, 4, temperatureUnit));

        views.setImageViewUri(
                R.id.widget_week_icon,
                ResourceHelper.getWidgetNotificationIconUri(
                        provider,
                        weather.getCurrent().getWeatherCode(),
                        dayTime,
                        minimalIcon,
                        color.darkText
                )
        );
        boolean weekIconDaytime = isWeekIconDaytime(weekIconMode, dayTime);
        views.setImageViewUri(
                R.id.widget_week_icon_1,
                getIconDrawableUri(
                        provider, weather, weekIconDaytime, minimalIcon, color.darkText,
                        0)
        );
        views.setImageViewUri(
                R.id.widget_week_icon_2,
                getIconDrawableUri(
                        provider, weather, weekIconDaytime, minimalIcon, color.darkText,
                        1)
        );
        views.setImageViewUri(
                R.id.widget_week_icon_3,
                getIconDrawableUri(
                        provider, weather, weekIconDaytime, minimalIcon, color.darkText,
                        2)
        );
        views.setImageViewUri(
                R.id.widget_week_icon_4,
                getIconDrawableUri(
                        provider, weather, weekIconDaytime, minimalIcon, color.darkText,
                        3)
        );
        views.setImageViewUri(
                R.id.widget_week_icon_5,
                getIconDrawableUri(
                        provider, weather, weekIconDaytime, minimalIcon, color.darkText,
                        4)
        );

        // set text color.
        views.setTextColor(R.id.widget_week_week_1, textColorInt);
        views.setTextColor(R.id.widget_week_week_2, textColorInt);
        views.setTextColor(R.id.widget_week_week_3, textColorInt);
        views.setTextColor(R.id.widget_week_week_4, textColorInt);
        views.setTextColor(R.id.widget_week_week_5, textColorInt);
        views.setTextColor(R.id.widget_week_temp, textColorInt);
        views.setTextColor(R.id.widget_week_temp_1, textColorInt);
        views.setTextColor(R.id.widget_week_temp_2, textColorInt);
        views.setTextColor(R.id.widget_week_temp_3, textColorInt);
        views.setTextColor(R.id.widget_week_temp_4, textColorInt);
        views.setTextColor(R.id.widget_week_temp_5, textColorInt);

        // set text size.
        if (textSize != 100) {
            float contentSize = context.getResources().getDimensionPixelSize(R.dimen.widget_content_text_size)
                    * textSize / 100f;
            views.setTextViewTextSize(R.id.widget_week_week_1, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_week_2, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_week_3, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_week_4, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_week_5, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_temp_1, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_temp_2, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_temp_3, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_temp_4, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_week_temp_5, TypedValue.COMPLEX_UNIT_PX, contentSize);
        }

        // set card visibility.
        if (color.showCard) {
            views.setImageViewResource(
                    R.id.widget_week_card,
                    getCardBackgroundId(context, color.darkCard, cardAlpha)
            );
            views.setViewVisibility(R.id.widget_week_card, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_week_card, View.GONE);
        }

        // set intent.
        setOnClickPendingIntent(context, views, location, viewStyle, touchToRefresh);

        // commit.
        return views;
    }

    public static boolean isEnable(Context context) {
        int[] widgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, WidgetWeekProvider.class));
        return widgetIds != null && widgetIds.length > 0;
    }

    private static String getTemp(Context context, Weather weather, int index, TemperatureUnit unit) {
        return Temperature.getTrendTemperature(
                context,
                weather.getDailyForecast().get(index).night().getTemperature().getTemperature(),
                weather.getDailyForecast().get(index).day().getTemperature().getTemperature(),
                unit
        );
    }

    private static Uri getIconDrawableUri(ResourceProvider helper, Weather weather,
                                          boolean dayTime, boolean minimalIcon, boolean blackText,
                                          int index) {
        return ResourceHelper.getWidgetNotificationIconUri(
                helper,
                dayTime
                        ? weather.getDailyForecast().get(index).day().getWeatherCode()
                        : weather.getDailyForecast().get(index).night().getWeatherCode(),
                dayTime, minimalIcon, blackText
        );
    }

    private static void setOnClickPendingIntent(Context context, RemoteViews views, Location location,
                                                String viewType, boolean touchToRefresh) {
        // weather.
        if (touchToRefresh) {
            views.setOnClickPendingIntent(
                    R.id.widget_week_weather,
                    getRefreshPendingIntent(
                            context,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_REFRESH
                    )
            );
        } else {
            views.setOnClickPendingIntent(
                    R.id.widget_week_weather,
                    getWeatherPendingIntent(
                            context,
                            location,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_WEATHER
                    )
            );
        }

        // daily forecast.
        if (viewType.equals("3_days")) {
            views.setOnClickPendingIntent(
                    R.id.widget_week_icon_1,
                    getDailyForecastPendingIntent(
                            context,
                            location,
                            0,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_1
                    )
            );
            views.setOnClickPendingIntent(
                    R.id.widget_week_icon_2,
                    getDailyForecastPendingIntent(
                            context,
                            location,
                            1,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_2
                    )
            );
            views.setOnClickPendingIntent(
                    R.id.widget_week_icon_3,
                    getDailyForecastPendingIntent(
                            context,
                            location,
                            2,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_3
                    )
            );
            views.setOnClickPendingIntent(
                    R.id.widget_week_icon_4,
                    getDailyForecastPendingIntent(
                            context,
                            location,
                            3,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_4
                    )
            );
            views.setOnClickPendingIntent(
                    R.id.widget_week_icon_5,
                    getDailyForecastPendingIntent(
                            context,
                            location,
                            4,
                            GeometricWeather.WIDGET_WEEK_PENDING_INTENT_CODE_DAILY_FORECAST_5
                    )
            );
        }
    }
}
