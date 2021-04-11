package com.mbestavros.geometricweather.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import com.mbestavros.geometricweather.basic.model.option.provider.WeatherSource;
import com.mbestavros.geometricweather.db.entity.AlertEntity;
import com.mbestavros.geometricweather.db.entity.AlertEntityDao;
import com.mbestavros.geometricweather.db.entity.DaoSession;
import com.mbestavros.geometricweather.db.propertyConverter.WeatherSourceConverter;

public class AlertEntityController extends AbsEntityController<AlertEntity> {

    public AlertEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertAlertList(@NonNull String cityId, @NonNull WeatherSource source,
                                @NonNull List<AlertEntity> entityList) {
        deleteAlertList(cityId, source);
        getSession().getAlertEntityDao().insertInTx(entityList);
        getSession().clear();
    }

    // delete.

    public void deleteAlertList(@NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getAlertEntityDao().deleteInTx(searchLocationAlarmEntity(cityId, source));
        getSession().clear();
    }

    // search.

    public List<AlertEntity> searchLocationAlarmEntity(@NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getAlertEntityDao()
                        .queryBuilder()
                        .where(
                                AlertEntityDao.Properties.CityId.eq(cityId),
                                AlertEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
