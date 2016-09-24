package com.android.proyectoalimentar.ui.map;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.FoodLocation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodLocationView extends FrameLayout {

    private FoodLocation foodLocation;
    @BindView(R.id.background) View background;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.opening_time) TextView openingTime;
    @BindView(R.id.description) TextView description;

    public FoodLocationView(Context context, FoodLocation foodLocation) {
        super(context);
        View view = inflate(context, R.layout.food_location_detail, this);
        ButterKnife.bind(this, view);
        setFoodLocation(foodLocation);
    }

    public void setFoodLocation(FoodLocation foodLocation) {
        this.foodLocation = foodLocation;
        // TODO: Update hardcoded texts.
        name.setText(foodLocation.getName());
        distance.setText("0.8km");
        address.setText(foodLocation.getAddress());
        openingTime.setText("09:00 - 22:00");
        description.setText(foodLocation.getDescription());
    }

    public void setHeight(float height) {
        ViewGroup.LayoutParams params = background.getLayoutParams();
        params.height = convertDpToPixel(height);
        background.setLayoutParams(params);
        invalidate();
    }

    private int convertDpToPixel(float dp) {
        Resources resources = AlimentarApp.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    @OnClick(R.id.find_donation)
    void onFindDonationClicked() {
        // TODO
    }

}
