package com.android.proyectoalimentar.location_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.FoodLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class FoodLocationDetailView extends FrameLayout {

    private static final String MAPS_URL_FORMAT = "http://maps.google.com/maps?daddr=%f,%f";

    private TextView title;
    private TextView address;
    private TextView description;
    private View close;
    private View seeDirections;

    public FoodLocationDetailView(Context context) {
        super(context);
        init();
    }

    public FoodLocationDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodLocationDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FoodLocationDetailView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.food_location_detail, this);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        int padding = (int) getResources().getDimension(R.dimen.standard_margin);
        setPadding(padding, padding * 2, padding, padding);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setUi(root);
    }

    private void setUi(View root) {
        title = (TextView) root.findViewById(R.id.location_name);
        address = (TextView) root.findViewById(R.id.location_address);
        description = (TextView) root.findViewById(R.id.location_description);
        seeDirections = root.findViewById(R.id.see_directions);
        close = root.findViewById(R.id.close);
        close.setOnClickListener(v -> hide());
    }

    public void setFoodLocation(final FoodLocation foodLocation) {
        title.setText(foodLocation.getName());
        description.setText(foodLocation.getDescription());
        address.setText(foodLocation.getAddress());
        seeDirections.setOnClickListener(v -> openGoogleMapsDirections(foodLocation));
    }

    public void show() {
        setVisibility(VISIBLE);
        float height = ((ViewGroup)getParent()).getHeight();
        setTranslationY(height);
        animate().translationY(0);
    }

    public void hide() {
        float height = getHeight();
        animate().translationY(height);
    }

    private void openGoogleMapsDirections(FoodLocation foodLocation) {
        LatLng location = foodLocation.getLocation();
        String linkToOpen = String.format(
                Locale.ENGLISH, MAPS_URL_FORMAT, location.latitude, location.longitude);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(linkToOpen));
        getContext().startActivity(intent);
    }

}
