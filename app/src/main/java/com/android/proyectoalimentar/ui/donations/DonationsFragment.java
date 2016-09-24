package com.android.proyectoalimentar.ui.donations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationsFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.donations_list) RecyclerView donationsList;

    private DonationsAdapter donationsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donations_fragment, container, false);
        ButterKnife.bind(this, view);

        setupDrawer();
        setupDonationsList();

        AlimentarApp.inject(this);

        return view;
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> ((DrawerActivity) getActivity()).toggleDrawer());
    }

    private void setupDonationsList() {
        donationsAdapter = new DonationsAdapter();
        donationsList.setAdapter(donationsAdapter);
        donationsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // TODO: Get this from API
        List<Donation> donations = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            Donation donation = new Donation(DateTime.now().plusMinutes((int)(Math.random() * 60)));
            donations.add(donation);
        }
        donationsAdapter.setDonations(donations);
    }

}
