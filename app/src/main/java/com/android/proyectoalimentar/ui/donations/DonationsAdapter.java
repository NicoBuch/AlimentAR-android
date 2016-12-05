package com.android.proyectoalimentar.ui.donations;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.repository.DonationsRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.ui.view.FoodLocationView;
import com.android.proyectoalimentar.ui.view.TimerView;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

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

        private Donation donation;

        public DonationsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void populateWithDonation(Donation donation) {
            this.donation = donation;

            foodLocationView.hideDonationButton();
            foodLocationView.setDonation(donation);

            timer.setTimeLeft(donation.getTimeLeft());
        }

        @OnClick(R.id.cancel)
        void cancelDonation() {
            donationsRepository.deactivateDonation(donation, new RepoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean value) {
                    removeDonation(donation);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(AlimentarApp.getContext(),R.string.cancel,Toast.LENGTH_SHORT);
                }
            });
        }
    }
}
