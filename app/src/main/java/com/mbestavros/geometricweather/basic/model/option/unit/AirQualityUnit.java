package com.mbestavros.geometricweather.basic.model.option.unit;

import android.content.Context;
import android.text.BidiFormatter;

import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.utils.DisplayUtils;

public enum AirQualityUnit {

    MUGPCUM("mugpcum", 0, 1f);

    private String unitId;
    private int unitArrayIndex;
    private float unitFactor; // actual air quality = quality(μg/m³) * factor.

    AirQualityUnit(String id, int arrayIndex, float factor) {
        unitId = id;
        unitArrayIndex = arrayIndex;
        unitFactor = factor;
    }

    public String getUnitId() {
        return unitId;
    }

    public float getDensity(float mugpcum) {
        return mugpcum * unitFactor;
    }

    public String getDensityText(Context context, float mugpcum) {
        if (DisplayUtils.isRtl(context)) {
            return BidiFormatter.getInstance().unicodeWrap(
                    UnitUtils.formatFloat(mugpcum * unitFactor, 1)
            ) + context.getResources().getStringArray(R.array.air_quality_units)[unitArrayIndex];
        } else {
            return UnitUtils.formatFloat(mugpcum * unitFactor, 1)
                    + context.getResources().getStringArray(R.array.air_quality_units)[unitArrayIndex];
        }
    }
}
