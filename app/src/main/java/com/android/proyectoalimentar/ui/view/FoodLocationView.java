package com.android.proyectoalimentar.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodLocationView extends FrameLayout {

    private Donation foodLocation;
    @BindView(R.id.background) View background;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.opening_time) TextView openingTime;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.find_donation) View findDonation;
    @BindView(R.id.time_container) View timeContainer;

    private OnFoodLocationClickListener onFoodLocationClickListener;

    public interface OnFoodLocationClickListener {

        void onFoodLocationClicked(Donation foodLocation);
    }

    public FoodLocationView(Context context, Donation foodLocation,
                            OnFoodLocationClickListener onFoodLocationClickListener) {
        super(context);
        init();
        setDonation(foodLocation);
        this.onFoodLocationClickListener = onFoodLocationClickListener;
    }

    public FoodLocationView(Context context) {
        super(context);
        init();
    }

    public FoodLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodLocationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FoodLocationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.food_location_detail, this);
        ButterKnife.bind(this, view);
    }

    public void setDonation(Donation donation) {
        this.foodLocation = donation;

        openingTime.setText(String.format("%s - %s",
                stringFromDate(donation.getPickupTimeFrom()),
                stringFromDate(donation.getPickupTimeTo())));
        setFoodLocation(donation.getDonator());
    }

    public void setFoodLocation(FoodLocation foodLocation) {
        // TODO: Update hardcoded texts.
        name.setText(foodLocation.getName());
        distance.setText("");
        address.setText(foodLocation.getAddress());
        description.setText(foodLocation.getDescription());
    }

    private String stringFromDate(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour();
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
        if (onFoodLocationClickListener != null) {
            onFoodLocationClickListener.onFoodLocationClicked(foodLocation);
        }
    }

    public void hideDonationButton() {
        findDonation.setVisibility(GONE);
    }

    public void setDonationButtonVisible(boolean donationButtonAvailable) {
        findDonation.setVisibility(donationButtonAvailable ? VISIBLE : GONE);
        timeContainer.setVisibility(donationButtonAvailable ? VISIBLE : GONE);
    }

}
