package com.android.proyectoalimentar.ui.drawer;

import android.support.v4.app.Fragment;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.ui.donations.DonationsFragment;
import com.android.proyectoalimentar.ui.map.MapFragment;
import com.android.proyectoalimentar.ui.terms_and_conditions.TermsAndConditionsFragment;

public enum DrawerItem {

    MAP(R.id.nav_item_map, MapFragment::new),
    DONATIONS(R.id.nav_item_donations, DonationsFragment::new),
    TERMS(R.id.nav_item_terms, TermsAndConditionsFragment::new);

    private final int layoutRes;
    private final FragmentFactory fragmentFactory;

    DrawerItem(int layoutRes, FragmentFactory fragmentFactory) {
        this.layoutRes = layoutRes;
        this.fragmentFactory = fragmentFactory;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public Fragment createFragment() {
        return fragmentFactory.createFragment();
    }
}
