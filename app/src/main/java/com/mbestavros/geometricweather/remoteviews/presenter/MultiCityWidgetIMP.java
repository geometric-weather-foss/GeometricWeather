package com.mbestavros.geometricweather.remoteviews.presenter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.core.content.ContextCompat;

import java.util.List;

import com.mbestavros.geometricweather.GeometricWeather;
import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.background.receiver.widget.WidgetMultiCityProvider;
import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.basic.model.option.unit.TemperatureUnit;
import com.mbestavros.geometricweather.basic.model.weather.Temperature;
import com.mbestavros.geometricweather.basic.model.weather.Weather;
import com.mbestavros.geometricweather.resource.ResourceHelper;
import com.mbestavros.geometricweather.resource.provider.ResourceProvider;
import com.mbestavros.geometricweather.resource.provider.ResourcesProviderFactory;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.utils.manager.TimeManager;

public class MultiCityWidgetIMP extends AbstractRemoteViewsPresenter {

    public static void updateWidgetView(Context context, List<Location> locationList) {
        WidgetConfig config = getWidgetConfig(
                context,
                context.getString(R.string.sp_widget_multi_city)
        );

        RemoteViews views = getRemoteViews(
                context, locationList,
                config.cardStyle, config.cardAlpha, config.textColor, config.textSize
        );

        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, WidgetMultiCityProvider.class),
                views
        );
    }

    public static RemoteViews getRemoteViews(Context context,
                                             List<Location> locationList,
                                             String cardStyle, int cardAlpha,
                                             String textColor, int textSize) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_multi_city_horizontal);

        Location location = locationList.get(0);
        Weather weather = location.getWeather();
        boolean dayTime = TimeManager.isDaylight(location);

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        SettingsOptionManager settings = SettingsOptionManager.getInstance(context);
        TemperatureUnit temperatureUnit = settings.getTemperatureUnit();
        boolean minimalIcon = settings.isWidgetMinimalIconEnabled();
        boolean touchToRefresh = settings.isWidgetClickToRefreshEnabled();

        WidgetColor color = new WidgetColor(context, dayTime, cardStyle, textColor);

        int textColorInt;
        if (color.darkText) {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextDark);
        } else {
            textColorInt = ContextCompat.getColor(context, R.color.colorTextLight);
        }

        // city 1.
        views.setViewVisibility(R.id.widget_multi_city_horizontal_weather_1, View.VISIBLE);
        if (weather != null) {
            views.setTextViewText(
                    R.id.widget_multi_city_horizontal_title_1,
                    location.getCityName(context)
            );

            views.setImageViewUri(
                    R.id.widget_multi_city_horizontal_icon_1,
                    ResourceHelper.getWidgetNotificationIconUri(
                            provider,
                            dayTime
                                    ? weather.getDailyForecast().get(0).day().getWeatherCode()
                                    : weather.getDailyForecast().get(0).night().getWeatherCode(),
                            dayTime,
                            minimalIcon,
                            color.darkText
                    )
            );

            views.setTextViewText(
                    R.id.widget_multi_city_horizontal_content_1,
                    Temperature.getTrendTemperature(
                            context,
                            weather.getDailyForecast().get(0).night().getTemperature().getTemperature(),
                            weather.getDailyForecast().get(0).day().getTemperature().getTemperature(),
                            temperatureUnit
                    )
            );
        }
        setOnClickPendingIntent(context, views, location,
                R.id.widget_multi_city_horizontal_weather_1, 0, touchToRefresh);

        // city 2.
        if (locationList.size() >= 2) {
            location = locationList.get(1);
            weather = location.getWeather();
            dayTime = TimeManager.isDaylight(location);

            views.setViewVisibility(R.id.widget_multi_city_horizontal_weather_2, View.VISIBLE);
            if (weather != null) {
                views.setTextViewText(
                        R.id.widget_multi_city_horizontal_title_2,
                        location.getCityName(context)
                );

                views.setImageViewUri(
                        R.id.widget_multi_city_horizontal_icon_2,
                        ResourceHelper.getWidgetNotificationIconUri(
                                provider,
                                dayTime
                                        ? weather.getDailyForecast().get(0).day().getWeatherCode()
                                        : weather.getDailyForecast().get(0).night().getWeatherCode(),
                                dayTime,
                                minimalIcon,
                                color.darkText
                        )
                );

                views.setTextViewText(
                        R.id.widget_multi_city_horizontal_content_2,
                        Temperature.getTrendTemperature(
                                context,
                                weather.getDailyForecast().get(0).night().getTemperature().getTemperature(),
                                weather.getDailyForecast().get(0).day().getTemperature().getTemperature(),
                                temperatureUnit
                        )
                );
            }
            setOnClickPendingIntent(context, views, location,
                    R.id.widget_multi_city_horizontal_weather_2, 1,  touchToRefresh);
        } else {
            views.setViewVisibility(R.id.widget_multi_city_horizontal_weather_2, View.GONE);
        }

        // city 3.
        if (locationList.size() >= 3) {
            location = locationList.get(2);
            weather = location.getWeather();
            dayTime = TimeManager.isDaylight(location);

            views.setViewVisibility(R.id.widget_multi_city_horizontal_weather_3, View.VISIBLE);
            if (weather != null) {
                views.setTextViewText(
                        R.id.widget_multi_city_horizontal_title_3,
                        location.getCityName(context)
                );

                views.setImageViewUri(
                        R.id.widget_multi_city_horizontal_icon_3,
                        ResourceHelper.getWidgetNotificationIconUri(
                                provider,
                                dayTime
                                        ? weather.getDailyForecast().get(0).day().getWeatherCode()
                                        : weather.getDailyForecast().get(0).night().getWeatherCode(),
                                dayTime,
                                minimalIcon,
                                color.darkText
                        )
                );

                views.setTextViewText(
                        R.id.widget_multi_city_horizontal_content_3,
                        Temperature.getTrendTemperature(
                                context,
                                weather.getDailyForecast().get(0).night().getTemperature().getTemperature(),
                                weather.getDailyForecast().get(0).day().getTemperature().getTemperature(),
                                temperatureUnit
                        )
                );
            }
            setOnClickPendingIntent(context, views, location,
                    R.id.widget_multi_city_horizontal_weather_3, 2, touchToRefresh);
        } else {
            views.setViewVisibility(R.id.widget_multi_city_horizontal_weather_3, View.GONE);
        }

        views.setTextColor(R.id.widget_multi_city_horizontal_title_1, textColorInt);
        views.setTextColor(R.id.widget_multi_city_horizontal_title_2, textColorInt);
        views.setTextColor(R.id.widget_multi_city_horizontal_title_3, textColorInt);
        views.setTextColor(R.id.widget_multi_city_horizontal_content_1, textColorInt);
        views.setTextColor(R.id.widget_multi_city_horizontal_content_2, textColorInt);
        views.setTextColor(R.id.widget_multi_city_horizontal_content_3, textColorInt);

        if (textSize != 100) {
            float titleSize = context.getResources().getDimensionPixelSize(R.dimen.widget_title_text_size)
                    * textSize / 100f;
            float contentSize = context.getResources().getDimensionPixelSize(R.dimen.widget_content_text_size)
                    * textSize / 100f;

            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_title_1, TypedValue.COMPLEX_UNIT_PX, titleSize);
            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_title_2, TypedValue.COMPLEX_UNIT_PX, titleSize);
            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_title_3, TypedValue.COMPLEX_UNIT_PX, titleSize);
            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_content_1, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_content_2, TypedValue.COMPLEX_UNIT_PX, contentSize);
            views.setTextViewTextSize(R.id.widget_multi_city_horizontal_content_3, TypedValue.COMPLEX_UNIT_PX, contentSize);
        }

        if (color.showCard) {
            views.setImageViewResource(
                    R.id.widget_multi_city_horizontal_card,
                    getCardBackgroundId(context, color.darkCard, cardAlpha)
            );
            views.setViewVisibility(R.id.widget_multi_city_horizontal_card, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_multi_city_horizontal_card, View.GONE);
        }

        return views;
    }

    public static boolean isEnable(Context context) {
        int[] widgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(
                        new ComponentName(context, WidgetMultiCityProvider.class)
                );
        return widgetIds != null && widgetIds.length > 0;
    }

    private static void setOnClickPendingIntent(Context context, RemoteViews views, Location location,
                                                @IdRes int resId, @IntRange(from = 0, to = 2) int index,
                                                boolean touchToRefresh) {
        // weather.
        if (touchToRefresh) {
            views.setOnClickPendingIntent(
                    resId,
                    getRefreshPendingIntent(
                            context,
                            GeometricWeather.WIDGET_MULTI_CITY_PENDING_INTENT_CODE_REFRESH_1 + 2 * index
                    )
            );
        } else {
            views.setOnClickPendingIntent(
                    resId,
                    getWeatherPendingIntent(
                            context,
                            location,
                            GeometricWeather.WIDGET_MULTI_CITY_PENDING_INTENT_CODE_WEATHER_1 + 2 * index
                    )
            );
        }
    }
}
