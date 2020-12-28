package wangdaye.com.geometricweather.weather;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import wangdaye.com.geometricweather.basic.model.location.Location;
import wangdaye.com.geometricweather.basic.model.option.provider.WeatherSource;
import wangdaye.com.geometricweather.basic.model.weather.Weather;
import wangdaye.com.geometricweather.db.DatabaseHelper;
import wangdaye.com.geometricweather.settings.SettingsOptionManager;
import wangdaye.com.geometricweather.weather.observer.BaseObserver;
import wangdaye.com.geometricweather.weather.observer.ObserverContainer;
import wangdaye.com.geometricweather.weather.service.AccuWeatherService;
import wangdaye.com.geometricweather.weather.service.MfWeatherService;
import wangdaye.com.geometricweather.weather.service.WeatherService;

/**
 * Weather helper.
 * */

public class WeatherHelper {

    @Nullable private WeatherService weatherService;

    @Nullable private WeatherService[] searchServices;
    private CompositeDisposable compositeDisposable;

    public WeatherHelper() {
        weatherService = null;
        searchServices = null;
        compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    private static WeatherService getWeatherService(WeatherSource source) {
        switch (source) {
            case MF:
                return new MfWeatherService();

            default: // ACCU.
                return new AccuWeatherService();
        }
    }

    public void requestWeather(Context c, Location location, @NonNull final OnRequestWeatherListener l) {
        weatherService = getWeatherService(location.getWeatherSource());
        weatherService.requestWeather(c, location, new WeatherService.RequestWeatherCallback() {

            @Override
            public void requestWeatherSuccess(@NonNull Location requestLocation) {
                Weather weather = requestLocation.getWeather();
                if (weather != null) {
                    DatabaseHelper.getInstance(c).writeWeather(requestLocation, weather);
                    if (weather.getYesterday() == null) {
                        weather.setYesterday(
                                DatabaseHelper.getInstance(c).readHistory(requestLocation, weather));
                    }
                    l.requestWeatherSuccess(requestLocation);
                } else {
                    requestWeatherFailed(requestLocation);
                }
            }

            @Override
            public void requestWeatherFailed(@NonNull Location requestLocation) {
                requestLocation.setWeather(DatabaseHelper.getInstance(c).readWeather(requestLocation));
                l.requestWeatherFailed(requestLocation);
            }
        });
    }

    public void requestLocation(Context context, String query, @NonNull final OnRequestLocationListener l) {
        Observable<List<Location>> searchList = Observable.create(emitter ->
                emitter.onNext(getWeatherService(SettingsOptionManager.getInstance(context).getWeatherSource()).requestLocation(context, query)));

        searchList.compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(compositeDisposable, new BaseObserver<List<Location>>() {
                    @Override
                    public void onSucceed(List<Location> locationList) {
                        if (locationList != null && locationList.size() != 0) {
                            l.requestLocationSuccess(query, locationList);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        l.requestLocationFailed(query);
                    }
                }));
    }

    public void cancel() {
        if (weatherService != null) {
            weatherService.cancel();
        }
        if (searchServices != null) {
            for (WeatherService s : searchServices) {
                if (s != null) {
                    s.cancel();
                }
            }
        }
        compositeDisposable.clear();
    }

    // interface.

    public interface OnRequestWeatherListener {
        void requestWeatherSuccess(@NonNull Location requestLocation);
        void requestWeatherFailed(@NonNull Location requestLocation);
    }

    public interface OnRequestLocationListener {
        void requestLocationSuccess(String query, List<Location> locationList);
        void requestLocationFailed(String query);
    }
}
