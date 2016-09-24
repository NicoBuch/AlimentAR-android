package com.android.proyectoalimentar.ui.map;

import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.ui.view.FoodLocationView;

public class HeightModifierOnPageChangedListener implements ViewPager.OnPageChangeListener {

    private static final int MIN_HEIGHT = 120;
    private static final int HEIGHT_DIFF = 40;
    private final LocationAdapter locationAdapter;

    private int lastPosition;
    private float lastOffset;

    public HeightModifierOnPageChangedListener(
            ViewPager viewPager,
            LocationAdapter locationAdapter) {
        this.locationAdapter = locationAdapter;
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentPosition;
        int previousPosition;
        float realOffset;
        boolean goingLeft = lastPosition + lastOffset < position + positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            previousPosition = position + 1;
            currentPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            previousPosition = position;
            currentPosition = position + 1;
            realOffset = positionOffset;
        }

        FoodLocationView currentView = locationAdapter.getViewAt(currentPosition);
        if (currentView != null) {
            currentView.setHeight(MIN_HEIGHT + HEIGHT_DIFF * realOffset);
        }

        FoodLocationView lastView = locationAdapter.getViewAt(previousPosition);
        if (lastView != null) {
            lastView.setHeight(MIN_HEIGHT + HEIGHT_DIFF * (1 - realOffset));
        }

        lastPosition = position;
        lastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private int convertDpToPixel(float dp) {
        Resources resources = AlimentarApp.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

}
