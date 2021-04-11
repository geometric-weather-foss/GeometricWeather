package com.mbestavros.geometricweather.ui.widget.insets;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mbestavros.geometricweather.utils.DisplayUtils;

public class FitSystemBarRecyclerView extends RecyclerView {

    private Rect windowInsets;
    private boolean adaptiveWidthEnabled = true;

    public FitSystemBarRecyclerView(@NonNull Context context) {
        super(context);
        ViewCompat.setOnApplyWindowInsetsListener(this, null);
    }

    public FitSystemBarRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setOnApplyWindowInsetsListener(this, null);
    }

    public FitSystemBarRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, null);
    }

    public void setAdaptiveWidthEnabled(boolean enabled) {
        this.adaptiveWidthEnabled = enabled;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
        super.setOnApplyWindowInsetsListener((v, insets) -> {
            Rect waterfull = Utils.getWaterfullInsets(insets);
            fitSystemWindows(
                    new Rect(
                            insets.getSystemWindowInsetLeft() + waterfull.left,
                            insets.getSystemWindowInsetTop() + waterfull.top,
                            insets.getSystemWindowInsetRight() + waterfull.right,
                            insets.getSystemWindowInsetBottom() + waterfull.bottom
                    )
            );
            return listener == null ? insets : listener.onApplyWindowInsets(v, insets);
        });
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        windowInsets = insets;
        requestLayout();
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = getMeasuredWidth();
        int adaptiveWidth = DisplayUtils.getTabletListAdaptiveWidth(getContext(), viewWidth);
        int paddingHorizontal = adaptiveWidthEnabled ? ((viewWidth - adaptiveWidth) / 2) : 0;
        setPadding(
                Math.max(paddingHorizontal, windowInsets.left),
                windowInsets.top,
                Math.max(paddingHorizontal, windowInsets.right),
                windowInsets.bottom
        );
    }

    public Rect getWindowInsets() {
        return windowInsets;
    }
}
