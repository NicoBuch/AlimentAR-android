package com.android.proyectoalimentar.ui.donations;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.ui.view.FoodLocationView;
import com.android.proyectoalimentar.ui.view.TimerView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.inflate;

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.DonationsViewHolder> {

    private List<Donation> donations;

    public DonationsAdapter() {
        donations = new LinkedList<>();
    }

    @Override
    public DonationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DonationsViewHolder(inflate(parent.getContext(), R.layout.donation, null));
    }

    @Override
    public void onBindViewHolder(DonationsViewHolder holder, int position) {
        Donation donation = donations.get(position);
        holder.populateWithDonation(donation);
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
        notifyDataSetChanged();
    }

    static class DonationsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.food_location) FoodLocationView foodLocationView;
        @BindView(R.id.timer) TimerView timer;

        private Donation donation;

        public DonationsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void populateWithDonation(Donation donation) {
            this.donation = donation;

            foodLocationView.hideDonationButton();
            foodLocationView.setFoodLocation(FoodLocation.nullValue());

            timer.setTimeLeft(donation.getTimeLeft());
        }

        @OnClick(R.id.cancel)
        void cancelDonation() {
            // TODO
        }
    }
}
