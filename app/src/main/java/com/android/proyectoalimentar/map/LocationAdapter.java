package com.android.proyectoalimentar.map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.android.proyectoalimentar.model.FoodLocation;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends PagerAdapter {

    private List<FoodLocation> foodLocations;
    private List<FoodLocationView> foodLocationViews;
    private Context context;

    public LocationAdapter(Context context) {
        this.context = context;
        foodLocations = new ArrayList<>();
        foodLocationViews = new ArrayList<>();
    }

    public void setFoodLocations(List<FoodLocation> foodLocations) {
        int minSize = Math.min(this.foodLocations.size(), foodLocations.size());
        for (int i = minSize; i < this.foodLocations.size(); i++) {
            FoodLocationView foodLocationView = foodLocationViews.get(i);
            if (foodLocationView != null) {
                foodLocationView.setVisibility(View.GONE);
            }
        }
        for (int i = foodLocationViews.size(); i < foodLocations.size(); i++) {
            foodLocationViews.add(null);
        }
        this.foodLocations = foodLocations;
        for (int i = 0; i < foodLocations.size(); i++) {
            FoodLocationView foodLocationView = foodLocationViews.get(i);
            if (foodLocationView != null) {
                foodLocationView.setFoodLocation(foodLocations.get(i));
                foodLocationView.setVisibility(View.VISIBLE);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        FoodLocation foodLocation = foodLocations.get(position);
        FoodLocationView foodLocationView = new FoodLocationView(context, foodLocation);
        collection.addView(foodLocationView);
        foodLocationViews.set(position, foodLocationView);
        return foodLocationView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        ((View) view).setVisibility(View.GONE);
        collection.removeView((View) view);
        foodLocationViews.set(position, null);
    }

    @Override
    public int getCount() {
        return foodLocations.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public FoodLocationView getViewAt(int position) {
        if (position >= foodLocationViews.size()) {
            return null;
        }
        return foodLocationViews.get(position);
    }

    public int getLocationPosition(FoodLocation foodLocation) {
        for (int i = 0; i < foodLocations.size(); i++) {
            if (foodLocation.equals(foodLocations.get(i))) {
                return i;
            }
        }
        return 0;
    }

    public FoodLocation getFoodLocationAt(int position) {
        if (position < 0 || position >= foodLocations.size()) {
            return null;
        }
        return foodLocations.get(position);
    }

}
