package wangdaye.com.geometricweather.weather.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wangdaye.com.geometricweather.weather.json.accu.AccuLocationResult;
import wangdaye.com.geometricweather.weather.json.owm.OWMOneCallResult;

/**
 * Accu api.
 */

public interface OpenWeatherMapApi {

    @GET("data/2.5/onecall.json")
    Observable<List<OWMOneCallResult>> oneCall(@Query("lat") String lat,
                                                            @Query("lon") String lon,
                                                            @Query("units") String units,
                                                            @Query("appid") String appid);
    @GET("data/2.5/weather.json")
    Call<List<AccuLocationResult>> locationWeather(@Query("q") String city_name,
                                           @Query("appid") String appid);
}
