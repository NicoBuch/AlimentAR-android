package com.android.proyectoalimentar.map;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.proyectoalimentar.AlimentARApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.FoodLocation;

public class FoodLocationView extends FrameLayout {

    private FoodLocation foodLocation;
    private View background;

    public FoodLocationView(Context context, FoodLocation foodLocation) {
        super(context);
        View view = inflate(context, R.layout.food_location_detail, this);
        background = view.findViewById(R.id.background);
        setFoodLocation(foodLocation);
    }

    public void setFoodLocation(FoodLocation foodLocation) {
        this.foodLocation = foodLocation;
    }

    public void setHeight(float height) {
        ViewGroup.LayoutParams params = background.getLayoutParams();
        params.height = convertDpToPixel(height);
        background.setLayoutParams(params);
        invalidate();
    }

    private int convertDpToPixel(float dp) {
        Resources resources = AlimentARApp.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

}
