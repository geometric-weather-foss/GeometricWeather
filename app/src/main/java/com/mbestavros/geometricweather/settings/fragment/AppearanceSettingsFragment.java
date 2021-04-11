package com.mbestavros.geometricweather.settings.fragment;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.mbestavros.geometricweather.GeometricWeather;
import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.GeoActivity;
import com.mbestavros.geometricweather.basic.model.weather.Temperature;
import com.mbestavros.geometricweather.resource.provider.ResourcesProviderFactory;
import com.mbestavros.geometricweather.basic.model.option.utils.OptionMapper;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.settings.dialog.ProvidersPreviewerDialog;
import com.mbestavros.geometricweather.utils.SnackbarUtils;
import com.mbestavros.geometricweather.utils.helpter.IntentHelper;

/**
 * Appearance settings fragment.
 * */

public class AppearanceSettingsFragment extends AbstractSettingsFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference_appearance);

        // ui style.
        Preference uiStyle = findPreference(getString(R.string.key_ui_style));
        uiStyle.setSummary(getSettingsOptionManager().getUiStyle().getUIStyleName(getActivity()));
        uiStyle.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setUiStyle(OptionMapper.getUIStyle((String) newValue));
            preference.setSummary(getSettingsOptionManager().getUiStyle().getUIStyleName(getActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(),
                    getString(R.string.feedback_restart),
                    getString(R.string.restart),
                    v -> GeometricWeather.getInstance().recreateAllActivities()
            );
            return true;
        });

        // icon provider.
        initIconProviderPreference();

        // set card display preference in onStart().
        // set daily trend display preference in onStart().

        // horizontal lines in trend.
        findPreference(getString(R.string.key_trend_horizontal_line_switch)).setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setTrendHorizontalLinesEnabled((Boolean) newValue);
            return true;
        });

        // exchange day night temperature.
        Preference exchangeDayNightTemperature = findPreference(getString(R.string.key_exchange_day_night_temp_switch));
        exchangeDayNightTemperature.setSummary(
                Temperature.getTrendTemperature(
                        requireActivity(),
                        3,
                        7,
                        SettingsOptionManager.getInstance(requireActivity()).getTemperatureUnit()
                )
        );
        exchangeDayNightTemperature.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setExchangeDayNightTempEnabled((Boolean) newValue);
            preference.setSummary(
                    Temperature.getTrendTemperature(
                            requireActivity(),
                            3,
                            7,
                            SettingsOptionManager.getInstance(requireActivity()).getTemperatureUnit()
                    )
            );
            return true;
        });

        // sensor.
        findPreference(getString(R.string.key_gravity_sensor_switch)).setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setGravitySensorEnabled((Boolean) newValue);
            return true;
        });

        // list animation.
        findPreference(getString(R.string.key_list_animation_switch)).setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setListAnimationEnabled((Boolean) newValue);
            return true;
        });

        // item animation.
        findPreference(getString(R.string.key_item_animation_switch)).setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setItemAnimationEnabled((Boolean) newValue);
            return true;
        });

        // language.
        Preference language = findPreference(getString(R.string.key_language));
        language.setSummary(getSettingsOptionManager().getLanguage().getLanguageName(getActivity()));
        language.setOnPreferenceChangeListener((preference, newValue) -> {
            getSettingsOptionManager().setLanguage(OptionMapper.getLanguage((String) newValue));
            preference.setSummary(getSettingsOptionManager().getLanguage().getLanguageName(getActivity()));
            SnackbarUtils.showSnackbar(
                    (GeoActivity) requireActivity(),
                    getString(R.string.feedback_restart),
                    getString(R.string.restart),
                    v -> GeometricWeather.getInstance().recreateAllActivities()
            );
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // card display.
        Preference cardDisplay = findPreference(getString(R.string.key_card_display));
        cardDisplay.setSummary(OptionMapper.getCardDisplaySummary(
                getActivity(),
                SettingsOptionManager.getInstance(getActivity()).getCardDisplayList()
        ));
        cardDisplay.setOnPreferenceClickListener(preference -> {
            IntentHelper.startCardDisplayManageActivityForResult(requireActivity(), 0);
            return true;
        });

        // daily trend display.
        Preference dailyTrendDisplay = findPreference(getString(R.string.key_daily_trend_display));
        dailyTrendDisplay.setSummary(OptionMapper.getDailyTrendDisplaySummary(
                getActivity(),
                SettingsOptionManager.getInstance(getActivity()).getDailyTrendDisplayList()
        ));
        dailyTrendDisplay.setOnPreferenceClickListener(preference -> {
            IntentHelper.startDailyTrendDisplayManageActivityForResult(requireActivity(), 1);
            return true;
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // do nothing.
    }

    private void initIconProviderPreference() {
        Preference iconProvider = findPreference(getString(R.string.key_icon_provider));
        iconProvider.setSummary(ResourcesProviderFactory.getNewInstance().getProviderName());
        
        iconProvider.setOnPreferenceClickListener(preference -> {
            ProvidersPreviewerDialog dialog = new ProvidersPreviewerDialog();
            dialog.setOnIconProviderChangedListener(iconProvider1 -> {
                getSettingsOptionManager().setIconProvider(iconProvider1);
                PreferenceManager.getDefaultSharedPreferences(requireActivity())
                        .edit()
                        .putString(getString(R.string.key_icon_provider), iconProvider1)
                        .apply();
                initIconProviderPreference();
                SnackbarUtils.showSnackbar(
                        (GeoActivity) requireActivity(), 
                        getString(R.string.feedback_refresh_ui_after_refresh)
                );
            });
            dialog.show(getParentFragmentManager(), null);
            return true;
        });
    }
}