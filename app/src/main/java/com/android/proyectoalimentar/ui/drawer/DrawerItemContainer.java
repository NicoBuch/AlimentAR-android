package com.android.proyectoalimentar.ui.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class DrawerItemContainer {

    private final DrawerActivity drawerActivity;
    private final DrawerItem drawerItem;
    private final Fragment fragment;

    public DrawerItemContainer(DrawerActivity drawerActivity, DrawerItem drawerItem) {
        this.drawerActivity = drawerActivity;
        this.drawerItem = drawerItem;
        View drawerItemView = drawerActivity.findViewById(drawerItem.getLayoutRes());
        this.fragment = drawerItem.createFragment();

        drawerItemView.setOnClickListener(v -> drawerActivity.openDrawerItem(drawerItem, new Bundle()));
    }

    public Fragment getFragment() {
        return fragment;
    }

}
