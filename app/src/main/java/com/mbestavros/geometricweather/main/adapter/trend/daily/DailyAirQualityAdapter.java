package com.mbestavros.geometricweather.main.adapter.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.GeoActivity;
import com.mbestavros.geometricweather.basic.model.weather.AirQuality;
import com.mbestavros.geometricweather.basic.model.weather.Daily;
import com.mbestavros.geometricweather.basic.model.weather.Weather;
import com.mbestavros.geometricweather.ui.widget.trend.TrendRecyclerView;
import com.mbestavros.geometricweather.ui.widget.trend.chart.PolylineAndHistogramView;
import com.mbestavros.geometricweather.ui.widget.trend.item.DailyTrendItemView;
import com.mbestavros.geometricweather.utils.manager.ThemeManager;

/**
 * Daily air quality adapter.
 * */

public class DailyAirQualityAdapter extends AbsDailyTrendAdapter<DailyAirQualityAdapter.ViewHolder> {

    private Weather weather;
    private TimeZone timeZone;
    private ThemeManager themeManager;
    private int highestIndex;
    private int size;

    class ViewHolder extends RecyclerView.ViewHolder {

        private DailyTrendItemView dailyItem;
        private PolylineAndHistogramView polylineAndHistogramView;

        ViewHolder(View itemView) {
            super(itemView);
            dailyItem = itemView.findViewById(R.id.item_trend_daily);
            dailyItem.setParent(getTrendParent());

            polylineAndHistogramView = new PolylineAndHistogramView(itemView.getContext());
            dailyItem.setChartItemView(polylineAndHistogramView);
        }

        @SuppressLint({"SetTextI18n, InflateParams", "DefaultLocale"})
        void onBindView(int position) {
            Context context = itemView.getContext();
            Daily daily = weather.getDailyForecast().get(position);

            if (daily.isToday(timeZone)) {
                dailyItem.setWeekText(context.getString(R.string.today));
            } else {
                dailyItem.setWeekText(daily.getWeek(context));
            }

            dailyItem.setDateText(daily.getShortDate(context));

            dailyItem.setTextColor(
                    themeManager.getTextContentColor(context),
                    themeManager.getTextSubtitleColor(context)
            );

            Integer index = daily.getAirQuality().getAqiIndex();
            polylineAndHistogramView.setData(
                    null, null,
                    null, null,
                    null, null,
                    (float) (index == null ? 0 : index),
                    String.format("%d", index == null ? 0 : index),
                    (float) highestIndex,
                    0f
            );
            polylineAndHistogramView.setLineColors(
                    daily.getAirQuality().getAqiColor(context),
                    daily.getAirQuality().getAqiColor(context),
                    themeManager.getLineColor(context)
            );
            int[] themeColors = themeManager.getWeatherThemeColors();
            polylineAndHistogramView.setShadowColors(
                    themeColors[1], themeColors[2], themeManager.isLightTheme());
            polylineAndHistogramView.setTextColors(
                    themeManager.getTextContentColor(context),
                    themeManager.getTextSubtitleColor(context)
            );
            polylineAndHistogramView.setHistogramAlpha(themeManager.isLightTheme() ? 1f : 0.5f);

            dailyItem.setOnClickListener(v -> onItemClicked(getAdapterPosition()));
        }
    }

    @SuppressLint("SimpleDateFormat")
    public DailyAirQualityAdapter(GeoActivity activity, TrendRecyclerView parent,
                                  String formattedId, @NonNull Weather weather, @NonNull TimeZone timeZone) {
        super(activity, parent, formattedId);

        this.weather = weather;
        this.timeZone = timeZone;
        this.themeManager = ThemeManager.getInstance(activity);

        highestIndex = Integer.MIN_VALUE;
        boolean valid = false;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i --) {
            Integer index = weather.getDailyForecast().get(i).getAirQuality().getAqiIndex();
            if (index != null && index > highestIndex) {
                highestIndex = index;
            }
            if ((index != null && index != 0) || valid) {
                valid = true;
                size ++;
            }
        }
        if (highestIndex == 0) {
            highestIndex = AirQuality.AQI_INDEX_5;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_1,
                        String.valueOf(AirQuality.AQI_INDEX_1),
                        activity.getString(R.string.aqi_1),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_3,
                        String.valueOf(AirQuality.AQI_INDEX_3),
                        activity.getString(R.string.aqi_3),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_5,
                        String.valueOf(AirQuality.AQI_INDEX_5),
                        activity.getString(R.string.aqi_5),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        parent.setLineColor(themeManager.getLineColor(activity));
        parent.setData(keyLineList, highestIndex, 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend_daily, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }
}