package com.android.proyectoalimentar.ui.map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.ui.view.FoodLocationView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends PagerAdapter {

    private List<Donation> foodLocations;
    private List<FoodLocationView> foodLocationViews;
    private FoodLocationView.OnFoodLocationClickListener onFoodLocationClickListener;
    private FoodLocationView.OnFoodLocationClickListener onTargetLocationClickListener;
    private Context context;
    private boolean donationButtonAvailable;
    private boolean targetButtonAvailable;

    private int targetDonationId;

    public LocationAdapter(Context context,
                           FoodLocationView.OnFoodLocationClickListener onFoodLocationClickListener,
                           FoodLocationView.OnFoodLocationClickListener onTargetLocationClickListener) {
        this.context = context;
        this.onFoodLocationClickListener = onFoodLocationClickListener;
        this.onTargetLocationClickListener = onTargetLocationClickListener;
        foodLocations = new ArrayList<>();
        foodLocationViews = new ArrayList<>();
    }

    public void setFoodLocations(List<Donation> foodLocations) {
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
                foodLocationView.setDonation(foodLocations.get(i));
                foodLocationView.setDonationButtonVisible(donationButtonAvailable);
                foodLocationView.setTargetDonationButtonVisible(targetButtonAvailable);
                foodLocationView.setVisibility(View.VISIBLE);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Donation foodLocation = foodLocations.get(position);
        FoodLocationView foodLocationView =
                new FoodLocationView(context, foodLocation, onFoodLocationClickListener, onTargetLocationClickListener);
        foodLocationView.setDonationButtonVisible(donationButtonAvailable);
        foodLocationView.setTargetDonationButtonVisible(targetButtonAvailable);
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

    public int getLocationPosition(Donation foodLocation) {
        for (int i = 0; i < foodLocations.size(); i++) {
            if (foodLocation.equals(foodLocations.get(i))) {
                return i;
            }
        }
        return 0;
    }

    public int getFoodDonatorPosition(FoodLocation foodLocation){
        for (int i = 0; i < foodLocations.size(); i++) {
            if (foodLocation.equals(foodLocations.get(i).getDonator())) {
                return i;
            }
        }
        return 0;
    }

    public Donation getFoodLocationAt(int position) {
        if (position < 0 || position >= foodLocations.size()) {
            return null;
        }
        return foodLocations.get(position);
    }

    public void setDonationButtonAvailable(boolean donationButtonAvailable) {
        this.donationButtonAvailable = donationButtonAvailable;
        for (FoodLocationView foodLocationView : foodLocationViews) {
            if (foodLocationView != null) {
                foodLocationView.setDonationButtonVisible(donationButtonAvailable);
            }
        }
    }

    public void setTargetDonationButtonAvailable(boolean targetDonationButtonAvailable){
        this.targetButtonAvailable = targetDonationButtonAvailable;
        for (FoodLocationView foodLocationView : foodLocationViews) {
            if (foodLocationView != null) {
                foodLocationView.setDonationButtonVisible(targetButtonAvailable);
            }
        }
    }

    public void setTargetDonationId(int targetDonationId) {
        this.targetDonationId = targetDonationId;
    }
}
