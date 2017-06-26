package com.android.proyectoalimentar.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
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
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodLocationView extends FrameLayout {

    private static final Uri IMAGE_PLACEHOLDER_URI = new Uri.Builder()
            .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
            .path(String.valueOf(R.drawable.ic_profile_placeholder))
            .build();

    private Donation foodLocation;
    @BindView(R.id.background) View background;
    @BindView(R.id.profile_image) SimpleDraweeView profileImage;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.opening_time) TextView openingTime;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.description_layout) View descriptionLayout;
    @BindView(R.id.find_donation) View findDonation;
    @BindView(R.id.time_container) View timeContainer;
    @BindView(R.id.select_fridge) TextView selectFridge;

    private OnFoodLocationClickListener onFoodLocationClickListener;
    private OnFoodLocationClickListener onTargetLocationClickListener;

    public interface OnFoodLocationClickListener {

        void onFoodLocationClicked(Donation foodLocation);
    }

    public FoodLocationView(Context context, Donation foodLocation,
                            OnFoodLocationClickListener onFoodLocationClickListener,
                            OnFoodLocationClickListener onTargetLocationClickListener) {
        super(context);
        init();
        setDonation(foodLocation);
        this.onFoodLocationClickListener = onFoodLocationClickListener;
        this.onTargetLocationClickListener = onTargetLocationClickListener;
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
        if(donation.getDescription() == null || donation.getDescription().isEmpty()){
            descriptionLayout.setVisibility(View.GONE);
        }else{
            descriptionLayout.setVisibility(View.VISIBLE);
            description.setText(donation.getDescription());
        }
        setFoodLocation(donation.getDonator());
        forceLayout();
        requestLayout();
    }

    public void setFoodLocation(FoodLocation foodLocation) {
        name.setText(foodLocation.getName());
        distance.setText("");
        address.setText(foodLocation.getAddress());
        if(foodLocation.getAvatar() != null){
            profileImage.setImageURI(foodLocation.getAvatar().getThumb());
        }else{
            profileImage.setImageURI(IMAGE_PLACEHOLDER_URI);
        }
        forceLayout();
        requestLayout();
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

    @OnClick(R.id.select_fridge)
    void onSelectFridgeClicked(){
        if (onTargetLocationClickListener != null) {
            onTargetLocationClickListener.onFoodLocationClicked(foodLocation);
        }
    }

    public void hideDonationButton() {
        findDonation.setVisibility(GONE);
    }

    public void setDonationButtonVisible(boolean donationButtonAvailable) {
        findDonation.setVisibility(donationButtonAvailable ? VISIBLE : GONE);
        timeContainer.setVisibility(donationButtonAvailable ? VISIBLE : GONE);
    }

    public void setTargetDonationButtonVisible(boolean donationButtonAvailable) {
        selectFridge.setVisibility(donationButtonAvailable ? VISIBLE : GONE);
    }

}
