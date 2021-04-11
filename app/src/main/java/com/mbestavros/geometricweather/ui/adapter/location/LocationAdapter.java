package com.mbestavros.geometricweather.ui.adapter.location;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turingtechnologies.materialscrollbar.ICustomAdapter;

import java.util.ArrayList;
import java.util.List;

import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.basic.model.option.provider.WeatherSource;
import com.mbestavros.geometricweather.basic.model.option.unit.TemperatureUnit;
import com.mbestavros.geometricweather.databinding.ItemLocationBinding;
import com.mbestavros.geometricweather.resource.provider.ResourceProvider;
import com.mbestavros.geometricweather.resource.provider.ResourcesProviderFactory;
import com.mbestavros.geometricweather.settings.SettingsOptionManager;
import com.mbestavros.geometricweather.utils.manager.ThemeManager;

/**
 * Location adapter.
 * */

public class LocationAdapter extends ListAdapter<LocationModel, LocationHolder>
        implements ICustomAdapter {

    private Context context;
    private OnLocationItemClickListener listener = null;

    private @NonNull ThemeManager themeManager;
    private @NonNull ResourceProvider resourceProvider;
    private @NonNull WeatherSource defaultSource;
    private @NonNull TemperatureUnit temperatureUnit;

    public LocationAdapter(Context context, List<Location> locationList, OnLocationItemClickListener l) {
        super(new DiffUtil.ItemCallback<LocationModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
                return oldItem.areContentsTheSame(newItem);
            }
        });
        this.context = context;
        this.themeManager = ThemeManager.getInstance(context);
        this.resourceProvider = ResourcesProviderFactory.getNewInstance();
        this.defaultSource = SettingsOptionManager.getInstance(context).getWeatherSource();
        this.temperatureUnit = SettingsOptionManager.getInstance(context).getTemperatureUnit();
        setOnLocationItemClickListener(l);

        update(locationList);
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationHolder(
                ItemLocationBinding.inflate(LayoutInflater.from(parent.getContext())),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        holder.onBindView(context, getItem(position), resourceProvider);
    }

    public void update(@NonNull List<Location> newList) {
        update(newList, null);
    }

    public void update(@NonNull List<Location> newList, @Nullable String forceUpdateId) {
        List<LocationModel> modelList = new ArrayList<>(newList.size());
        for (Location l : newList) {
            modelList.add(
                    new LocationModel(
                            context,
                            l,
                            temperatureUnit,
                            defaultSource,
                            themeManager.isLightTheme(),
                            l.getFormattedId().equals(forceUpdateId)
                    )
            );
        }
        submitList(modelList);
    }

    protected List<Location> moveItem(int from, int to) {
        List<LocationModel> modelList = new ArrayList<>(getCurrentList());
        modelList.add(to, modelList.remove(from));
        submitList(modelList);

        return getLocationList(modelList);
    }

    @ColorInt
    public int getItemSourceColor(int position) {
        if (0 <= position && position < getItemCount()) {
            return getItem(position).weatherSource.getSourceColor();
        } else {
            return Color.TRANSPARENT;
        }
    }

    protected List<Location> getLocationList() {
        return getLocationList(getCurrentList());
    }

    protected List<Location> getLocationList(List<LocationModel> modelList) {
        List<Location> locationList = new ArrayList<>(getItemCount());
        for (LocationModel m : modelList) {
            locationList.add(m.location);
        }
        return locationList;
    }

    // interface.

    public interface OnLocationItemClickListener {
        void onClick(View view, String formattedId);
    }

    private void setOnLocationItemClickListener(OnLocationItemClickListener l){
        this.listener = l;
    }

    // I custom adapter.

    @Override
    public String getCustomStringForElement(int element) {
        if (getItemCount() == 0) {
            return "";
        }
        return getItem(element).weatherSource.getSourceUrl();
    }
}