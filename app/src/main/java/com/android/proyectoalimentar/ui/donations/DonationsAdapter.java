package com.android.proyectoalimentar.ui.donations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.repository.DonationsRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;
import com.android.proyectoalimentar.ui.drawer.DrawerItem;
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
    DonationsRepository donationsRepository;


    public DonationsAdapter(DonationsRepository donationsRepository) {
        donations = new LinkedList<>();
        this.donationsRepository = donationsRepository;
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

    public void removeDonation(Donation donation){
        donations.remove(donation);
        notifyDataSetChanged();
    }

    class DonationsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.food_location) FoodLocationView foodLocationView;
        @BindView(R.id.timer) TimerView timer;
        @BindView(R.id.progressBar) ProgressBar progressBar;
        @BindView(R.id.destination_text)
        TextView destinationText;


        private Context context;
        private Donation donation;

        public DonationsViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        void populateWithDonation(Donation donation) {
            this.donation = donation;

            foodLocationView.hideDonationButton();
            foodLocationView.setDonation(donation);

            timer.setTimeLeft(donation.getTimeLeft());
            if(donation.getFridge() != null && donation.getFridge().getAddress() != null){
                destinationText.setText(donation.getFridge().getAddress());
            }
        }

        @OnClick(R.id.selector_map_layout)
        void selectFridge(){
            startSelectorMap();
        }

        /**
         * Start the map fragment but to select a fridge.
         */
        private void startSelectorMap(){
            if(context instanceof DrawerActivity){
                Bundle bundle = new Bundle();
                bundle.putInt(Configuration.DONATION_ID, donation.getId());
                ((DrawerActivity) context).openDrawerItem(DrawerItem.MAP, bundle);
            }
        }

        @OnClick(R.id.cancel)
        void cancelDonation() {
            progressBar.setVisibility(View.VISIBLE);
            donationsRepository.deactivateDonation(donation, new RepoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean deactivated) {
                    progressBar.setVisibility(View.GONE);
                    if(deactivated) {
                        removeDonation(donation);
                    }
                    onError(null);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(AlimentarApp.getContext(),R.string.cancel,Toast.LENGTH_SHORT);
                }
            });
        }
    }
}
