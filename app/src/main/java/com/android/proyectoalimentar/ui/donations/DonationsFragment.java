package com.android.proyectoalimentar.ui.donations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.repository.DonationsRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationsFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.donations_list) RecyclerView donationsList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject DonationsRepository donationsRepository;

    private DonationsAdapter donationsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donations_fragment, container, false);
        ButterKnife.bind(this, view);
        AlimentarApp.inject(this);

        setupDrawer();
        setupDonationsList();
        setupSwipeRefresh();

        return view;
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> ((DrawerActivity) getActivity()).toggleDrawer());
    }

    private void setupSwipeRefresh(){
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshDonationList();
        });
    }

    private void setupDonationsList() {
        donationsAdapter = new DonationsAdapter(donationsRepository);
        donationsList.setAdapter(donationsAdapter);
        donationsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.post(()-> {
            swipeRefreshLayout.setRefreshing(true);
            refreshDonationList();
        });


    }

    private void refreshDonationList(){
        donationsRepository.getDonationsList(new RepoCallback<List<Donation>>() {
            @Override
            public void onSuccess(List<Donation> donations) {
                donationsAdapter.setDonations(donations);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), "Hubo un error al cargar las donaciones activas",
                        Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
