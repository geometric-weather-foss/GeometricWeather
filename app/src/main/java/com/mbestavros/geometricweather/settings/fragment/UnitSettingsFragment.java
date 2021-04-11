package com.mbestavros.geometricweather.settings.fragment;

import android.os.Bundle;

import androidx.preference.ListPreference;

import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.GeoActivity;
import com.mbestavros.geometricweather.basic.model.option.utils.OptionMapper;
import com.mbestavros.geometricweather.utils.SnackbarUtils;

/**
 * Unit settings fragment.
 * */

public class UnitSettingsFragment extends AbstractSettingsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference_unit);

        // temperature.
        ListPreference temperature = findPreference(getString(R.string.key_temperature_unit));
        temperature.setSummary(
                getSettingsOptionManager().getTemperatureUnit().getAbbreviation(requireActivity())
        );
        temperature.setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setTemperatureUnit(OptionMapper.getTemperatureUnit((String) newValue));
            temperature.setSummary(
                    getSettingsOptionManager().getTemperatureUnit().getAbbreviation(requireActivity())
            );
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(), getString(R.string.feedback_refresh_ui_after_refresh));
            return true;
        });

        // distance.
        ListPreference distance = findPreference(getString(R.string.key_distance_unit));
        distance.setSummary(getSettingsOptionManager().getDistanceUnit().getAbbreviation(requireActivity()));
        distance.setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setDistanceUnit(OptionMapper.getDistanceUnit((String) newValue));
            distance.setSummary(getSettingsOptionManager().getDistanceUnit().getAbbreviation(requireActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(), getString(R.string.feedback_refresh_ui_after_refresh));
            return true;
        });

        // precipitation.
        ListPreference precipitation = findPreference(getString(R.string.key_precipitation_unit));
        precipitation.setSummary(getSettingsOptionManager().getPrecipitationUnit().getAbbreviation(requireActivity()));
        precipitation.setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setPrecipitationUnit(OptionMapper.getPrecipitationUnit((String) newValue));
            precipitation.setSummary(getSettingsOptionManager().getPrecipitationUnit().getAbbreviation(requireActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(), getString(R.string.feedback_refresh_ui_after_refresh));
            return true;
        });

        // pressure.
        ListPreference pressure = findPreference(getString(R.string.key_pressure_unit));
        pressure.setSummary(getSettingsOptionManager().getPressureUnit().getAbbreviation(requireActivity()));
        pressure.setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setPressureUnit(OptionMapper.getPressureUnit((String) newValue));
            pressure.setSummary(getSettingsOptionManager().getPressureUnit().getAbbreviation(requireActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(), getString(R.string.feedback_refresh_ui_after_refresh));
            return true;
        });

        // speed.
        ListPreference speed = findPreference(getString(R.string.key_speed_unit));
        speed.setSummary(getSettingsOptionManager().getSpeedUnit().getAbbreviation(requireActivity()));
        speed.setOnPreferenceChangeListener((p, newValue) -> {
            getSettingsOptionManager().setSpeedUnit(OptionMapper.getSpeedUnit((String) newValue));
            speed.setSummary(getSettingsOptionManager().getSpeedUnit().getAbbreviation(requireActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(), getString(R.string.feedback_refresh_ui_after_refresh));
            return true;
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // do nothing.
    }
}