package com.mbestavros.geometricweather.ui.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import com.mbestavros.geometricweather.R;
import com.mbestavros.geometricweather.basic.GeoActivity;
import com.mbestavros.geometricweather.basic.model.location.Location;
import com.mbestavros.geometricweather.db.DatabaseHelper;
import com.mbestavros.geometricweather.main.MainListDecoration;
import com.mbestavros.geometricweather.ui.adapter.location.LocationAdapter;
import com.mbestavros.geometricweather.ui.adapter.location.LocationTouchCallback;
import com.mbestavros.geometricweather.utils.SnackbarUtils;
import com.mbestavros.geometricweather.utils.helpter.IntentHelper;
import com.mbestavros.geometricweather.utils.manager.ShortcutsManager;
import com.mbestavros.geometricweather.utils.manager.ThemeManager;

public class LocationManageFragment extends Fragment
        implements LocationTouchCallback.OnLocationListChangedListener {

    private CardView cardView;
    private AppCompatImageView searchIcon;
    private TextView searchTitle;
    private AppCompatImageButton currentLocationButton;
    private RecyclerView recyclerView;

    private LocationAdapter adapter;
    private MainListDecoration decoration;
    private int searchRequestCode;
    private int providerSettingsRequestCode;

    private ValueAnimator colorAnimator;

    private boolean drawerMode = false;

    private @Nullable LocationManageCallback locationListChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_manage, container, false);
        initWidget(view);
        return view;
    }

    private List<Location> readLocationList() {
        List<Location> locationList = DatabaseHelper.getInstance(requireActivity()).readLocationList();
        for (Location l : locationList) {
            l.setWeather(DatabaseHelper.getInstance(requireActivity()).readWeather(l));
        }
        return locationList;
    }

    private void initWidget(View view) {
        AppBarLayout appBar = view.findViewById(R.id.fragment_location_manage_appBar);
        ViewCompat.setOnApplyWindowInsetsListener(appBar, (v, insets) -> {
            v.setPadding(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    drawerMode ? 0 : insets.getSystemWindowInsetRight(),
                    0
            );
            return insets;
        });

        this.cardView = view.findViewById(R.id.fragment_location_manage_searchBar);
        cardView.setOnClickListener(v ->
                IntentHelper.startSearchActivityForResult(requireActivity(), cardView, searchRequestCode));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setTransitionName(getString(R.string.transition_activity_search_bar));
        }

        this.searchIcon = view.findViewById(R.id.fragment_location_manage_searchIcon);
        this.searchTitle = view.findViewById(R.id.fragment_location_manage_title);

        this.currentLocationButton = view.findViewById(R.id.fragment_location_manage_currentLocationButton);
        currentLocationButton.setOnClickListener(v -> {
            DatabaseHelper.getInstance(requireActivity()).writeLocation(Location.buildLocal());
            addLocation();
        });

        List<Location> locationList = readLocationList();
        adapter = new LocationAdapter(
                requireActivity(),
                locationList,
                (v, formattedId) -> {
                    if (locationListChangedListener != null) {
                        locationListChangedListener.onSelectedLocation(formattedId);
                    }
                }
        );
        this.recyclerView = view.findViewById(R.id.fragment_location_manage_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL, false));
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView, (v, insets) -> {
            v.setPadding(
                    insets.getSystemWindowInsetLeft(),
                    0,
                    drawerMode ? 0 : insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom()
            );
            return insets;
        });

        new ItemTouchHelper(
                new LocationTouchCallback(
                        (GeoActivity) requireActivity(),
                        adapter,
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.START | ItemTouchHelper.END,
                        this
                )
        ).attachToRecyclerView(recyclerView);

        setThemeStyle();
        onLocationListChanged(locationList, false, false);
    }

    private void setThemeStyle() {
        ThemeManager themeManager = ThemeManager.getInstance(requireActivity());

        ImageViewCompat.setImageTintList(
                searchIcon,
                ColorStateList.valueOf(themeManager.getTextContentColor(requireActivity()))
        );
        ImageViewCompat.setImageTintList(
                currentLocationButton,
                ColorStateList.valueOf(themeManager.getTextContentColor(requireActivity()))
        );
        searchTitle.setTextColor(
                ColorStateList.valueOf(themeManager.getTextSubtitleColor(requireActivity())));

        // background.
        if (colorAnimator != null) {
            colorAnimator.cancel();
            colorAnimator = null;
        }

        int oldColor = Color.TRANSPARENT;
        Drawable background = recyclerView.getBackground();
        if (background instanceof ColorDrawable) {
            oldColor = ((ColorDrawable) background).getColor();
        }
        int newColor = themeManager.getRootColor(requireActivity());

        if (newColor != oldColor) {
            colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor, newColor);
            colorAnimator.addUpdateListener(animation -> {
                cardView.setCardBackgroundColor((Integer) animation.getAnimatedValue());
                recyclerView.setBackgroundColor((Integer) animation.getAnimatedValue());
            });
            colorAnimator.setDuration(450);
            colorAnimator.start();
        } else {
            cardView.setCardBackgroundColor(newColor);
            recyclerView.setBackgroundColor(newColor);
        }

        if (decoration != null) {
            recyclerView.removeItemDecoration(decoration);
            decoration = null;
        }
        decoration = new MainListDecoration(requireActivity());
        recyclerView.addItemDecoration(decoration);
    }

    public void setRequestCodes(int searchRequestCode, int providerSettingsRequestCode) {
        this.searchRequestCode = searchRequestCode;
        this.providerSettingsRequestCode = providerSettingsRequestCode;
    }

    public void updateView(List<Location> newList) {
        adapter.update(newList);
        setThemeStyle();
    }

    public void addLocation() {
        resetLocationList();
        SnackbarUtils.showSnackbar(
                (GeoActivity) requireActivity(), getString(R.string.feedback_collect_succeed));
    }

    public void resetLocationList() {
        List<Location> list = readLocationList();
        adapter.update(list);
        onLocationListChanged(list, false, true);
    }

    private void onLocationListChanged(List<Location> list, boolean updateShortcuts, boolean notifyOutside) {
        setCurrentLocationButtonEnabled(list);
        if (updateShortcuts && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutsManager.refreshShortcutsInNewThread(requireActivity(), list);
        }
        if (notifyOutside && locationListChangedListener != null) {
            locationListChangedListener.onLocationListChanged();
        }
    }

    private void setCurrentLocationButtonEnabled(List<Location> list) {
        boolean hasCurrentLocation = false;
        for (int i = 0; i < list.size(); i ++) {
            if (list.get(i).isCurrentPosition()) {
                hasCurrentLocation = true;
                break;
            }
        }
        currentLocationButton.setEnabled(!hasCurrentLocation);
        currentLocationButton.setAlpha(hasCurrentLocation ? 0.5f : 1);
    }

    public void setDrawerMode(boolean drawerMode) {
        this.drawerMode = drawerMode;
    }

    // interface.

    public interface LocationManageCallback {
        void onSelectedLocation(@NonNull String formattedId);
        void onLocationListChanged();
    }

    public void setOnLocationListChangedListener(LocationManageCallback l) {
        this.locationListChangedListener = l;
    }

    // on location list changed listener.

    @Override
    public void onLocationSequenceChanged(List<Location> locationList) {
        DatabaseHelper.getInstance(requireActivity()).writeLocationList(locationList);
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onLocationInserted(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).writeLocation(location);
        if (location.getWeather() != null) {
            DatabaseHelper.getInstance(requireActivity()).writeWeather(location, location.getWeather());
        }
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onLocationRemoved(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).deleteLocation(location);
        DatabaseHelper.getInstance(requireActivity()).deleteWeather(location);
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onLocationChanged(List<Location> locationList, Location location) {
        DatabaseHelper.getInstance(requireActivity()).writeLocation(location);
        onLocationListChanged(locationList, true, true);
    }

    @Override
    public void onSelectProviderActivityStarted() {
        IntentHelper.startSelectProviderActivityForResult(
                requireActivity(), providerSettingsRequestCode);
    }
}
