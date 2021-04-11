package com.mbestavros.geometricweather.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.TimeZone;

import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.basic.model.option.provider.WeatherSource;
import com.mbestavros.geometricweather.db.DatabaseHelper;
import com.mbestavros.geometricweather.location.service.AndroidLocationService;
import com.mbestavros.geometricweather.location.service.LocationService;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.utils.NetworkUtils;
import com.mbestavros.geometricweather.weather.service.AccuWeatherService;
import com.mbestavros.geometricweather.weather.service.MfWeatherService;
import com.mbestavros.geometricweather.weather.service.WeatherService;

/**
 * Location helper.
 * */

public class LocationHelper {

    @NonNull private final LocationService locationService;
    @NonNull private final WeatherService mfWeather;
    @NonNull private final WeatherService accuWeather;

    public LocationHelper(Context context) {
        switch (SettingsOptionManager.getInstance(context).getLocationProvider()) {
            default: // NATIVE
                locationService = new AndroidLocationService(context);
                break;
        }

        accuWeather = new AccuWeatherService();
        mfWeather = new MfWeatherService();
    }

    public void requestLocation(Context context, Location location, boolean background,
                                @NonNull OnRequestLocationListener l) {
        if (locationService.getPermissions().length != 0) {
            // if needs any location permission.
            if (!NetworkUtils.isAvailable(context)
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                l.requestLocationFailed(location);
                return;
            }
            if (background) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    l.requestLocationFailed(location);
                    return;
                }
            }
        }

        // 1. get location by location service.
        // 2. get available location by weather service.

        locationService.requestLocation(
                context,
                result -> {
                    if (result == null) {
                        l.requestLocationFailed(location);
                        return;
                    }

                    location.updateLocationResult(
                            result.latitude, result.longitude, TimeZone.getDefault(),
                            result.country, result.province, result.city, result.district,
                            result.inChina
                    );

                    requestAvailableWeatherLocation(context, location, l);
                }
        );
    }

    private void requestAvailableWeatherLocation(Context context,
                                                 @NonNull Location location,
                                                 @NonNull OnRequestLocationListener l) {
        switch (SettingsOptionManager.getInstance(context).getWeatherSource()) {
            case ACCU:
                location.setWeatherSource(WeatherSource.ACCU);
                accuWeather.requestLocation(context, location, new AccuLocationCallback(context, location, l));
                break;
            case MF:
                location.setWeatherSource(WeatherSource.MF);
                mfWeather.requestLocation(context, location, new MfLocationCallback(context, location, l));
                break;
        }
    }

    public void cancel() {
        locationService.cancel();
        accuWeather.cancel();
        mfWeather.cancel();
    }

    public String[] getPermissions(boolean background) {
        String[] permissions = locationService.getPermissions();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || permissions.length == 0) {
            return permissions;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            String[] qPermissions = new String[permissions.length + 1];
            System.arraycopy(permissions, 0, qPermissions, 0, permissions.length);
            qPermissions[qPermissions.length - 1] = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
            return qPermissions;
        }

        if (background) {
            return new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {
            return permissions;
        }
    }

    // interface.

    public interface OnRequestLocationListener {
        void requestLocationSuccess(Location requestLocation);
        void requestLocationFailed(Location requestLocation);
    }
}

class AccuLocationCallback implements WeatherService.RequestLocationCallback {

    private Context context;
    private Location location;
    private LocationHelper.OnRequestLocationListener listener;

    AccuLocationCallback(Context context, Location location,
                         @NonNull LocationHelper.OnRequestLocationListener l) {
        this.context = context;
        this.location = location;
        this.listener = l;
    }

    @Override
    public void requestLocationSuccess(String query, List<Location> locationList) {
        if (locationList.size() > 0) {
            Location location = locationList.get(0).setCurrentPosition();
            DatabaseHelper.getInstance(context).writeLocation(location);
            listener.requestLocationSuccess(location);
        } else {
            requestLocationFailed(query);
        }
    }

    @Override
    public void requestLocationFailed(String query) {
        listener.requestLocationFailed(location);
    }
}

class MfLocationCallback implements WeatherService.RequestLocationCallback {

    private Context context;
    private Location location;
    private LocationHelper.OnRequestLocationListener listener;

    MfLocationCallback(Context context, Location location,
                         @NonNull LocationHelper.OnRequestLocationListener l) {
        this.context = context;
        this.location = location;
        this.listener = l;
    }

    @Override
    public void requestLocationSuccess(String query, List<Location> locationList) {
        if (locationList.size() > 0) {
            Location location = locationList.get(0).setCurrentPosition();
            DatabaseHelper.getInstance(context).writeLocation(location);
            listener.requestLocationSuccess(location);
        } else {
            requestLocationFailed(query);
        }
    }

    @Override
    public void requestLocationFailed(String query) {
        listener.requestLocationFailed(location);
    }
}

class ChineseCityLocationCallback implements WeatherService.RequestLocationCallback {

    private Context context;
    private Location location;
    private LocationHelper.OnRequestLocationListener listener;

    ChineseCityLocationCallback(Context context, Location location,
                                @NonNull LocationHelper.OnRequestLocationListener l) {
        this.context = context;
        this.location = location;
        this.listener = l;
    }

    @Override
    public void requestLocationSuccess(String query, List<Location> locationList) {
        if (locationList.size() > 0) {
            Location location = locationList.get(0).setCurrentPosition();
            DatabaseHelper.getInstance(context).writeLocation(location);
            listener.requestLocationSuccess(location);
        } else {
            requestLocationFailed(query);
        }
    }

    @Override
    public void requestLocationFailed(String query) {
        listener.requestLocationFailed(location);
    }
}
