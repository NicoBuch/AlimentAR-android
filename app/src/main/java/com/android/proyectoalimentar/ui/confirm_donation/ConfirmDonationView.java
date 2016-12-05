package com.android.proyectoalimentar.ui.confirm_donation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.repository.DonationsRepository;
import com.android.proyectoalimentar.repository.RepoCallback;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDonationView extends FrameLayout {

    @Inject DonationsRepository donationsRepository;

    @BindView(R.id.confirm_donation_time)
    TextView confirmDonationTime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Donation foodLocation;
    private DonationConfirmationListener donationConfirmationListener;

    public interface DonationConfirmationListener {

        void onDonationConfirmed();

        void onDonationCanceled();
    }

    public ConfirmDonationView(Context context) {
        super(context);
        init();
    }

    public ConfirmDonationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConfirmDonationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ConfirmDonationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.confirm_donation_activity, this);
        ButterKnife.bind(this, view);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .networkModule(new NetworkModule())
                .build()
                .inject(this);

        setOnClickListener(v -> setVisibility(GONE));
        setPossibleTimeLeft();
    }

    public void setDonationConfirmationListener(
            DonationConfirmationListener donationConfirmationListener) {
        this.donationConfirmationListener = donationConfirmationListener;
    }

    public void setPossibleTimeLeft(){
        if(foodLocation != null) {
            confirmDonationTime.setText(String.valueOf(foodLocation.getPossibleTimeLeft()) + " MIN");
        }
    }

    public void setFoodLocation(Donation foodLocation) {
        this.foodLocation = foodLocation;
        setPossibleTimeLeft();
    }

    @OnClick(R.id.accept_donation)
    void onDonationConfirmed() {
        progressBar.setVisibility(VISIBLE);
        donationsRepository.createDonation(foodLocation.getId(),
                new RepoCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean created) {
                        progressBar.setVisibility(GONE);
                        if (created) {
                            if (donationConfirmationListener != null) {
                                donationConfirmationListener.onDonationConfirmed();
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressBar.setVisibility(GONE);
                        Toast.makeText(getContext(), "No se pudo activar la donacion",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.cancel_donation)
    void onDonationCanceled() {
        if (donationConfirmationListener != null) {
            donationConfirmationListener.onDonationCanceled();
        }
    }

}
