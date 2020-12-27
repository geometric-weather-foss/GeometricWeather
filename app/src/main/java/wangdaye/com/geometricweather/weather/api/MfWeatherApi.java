package wangdaye.com.geometricweather.weather.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wangdaye.com.geometricweather.weather.json.mf.*;

import java.util.List;

/**
 * API Météo France
 */

public interface MfWeatherApi {

    @GET("places")
    Call<List<MfLocationResult>> callWeatherLocation(@Query("q") String q,
                                                     @Query("lat") double lat,
                                                     @Query("lon") double lon,
                                                     @Query("token") String token);

    @GET("places")
    Observable<List<MfLocationResult>> getWeatherLocation(@Query("q") String q,
                                                          @Query("lat") double lat,
                                                          @Query("lon") double lon,
                                                          @Query("token") String token);

    @GET("forecast")
    Observable<MfForecastResult> getForecast(@Query("lat") double lat,
                                             @Query("lon") double lon,
                                             @Query("lang") String lang,
                                             @Query("token") String token);

    @GET("forecast")
    Observable<MfForecastResult> getForecastInstants(@Query("lat") double lat,
                                                     @Query("lon") double lon,
                                                     @Query("lang") String lang,
                                                     @Query("instants") String instants,
                                                     @Query("token") String token);

    @GET("forecast")
    Observable<MfForecastResult> getForecastInseepp(@Query("id") int id,
                                                    @Query("lang") String lang,
                                                    @Query("token") String token);

    @GET("observation/gridded")
    Observable<MfCurrentResult> getCurrent(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("lang") String lang,
                                           @Query("token") String token);

    @GET("rain")
    Observable<MfRainResult> getRain(@Query("lat") double lat,
                                     @Query("lon") double lon,
                                     @Query("lang") String lang,
                                     @Query("token") String token);

    @GET("ephemeris")
    Observable<MfEphemerisResult> getEphemeris(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("lang") String lang,
                                               @Query("token") String token);

    @GET("warning/full")
    Observable<MfWarningsResult> getWarnings(@Query("domain") String domain,
                                             @Query("formatDate") String formatDate,
                                             @Query("token") String token);
}
