package com.android.proyectoalimentar.ui.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.android.proyectoalimentar.R;
import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DrawerActivity extends AppCompatActivity {

    private static final DrawerItem DEFAULT_ITEM = DrawerItem.MAP;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    private Map<DrawerItem, DrawerItemContainer> drawerItems;
    private DrawerItem selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        ButterKnife.bind(this);

        drawerItems = new HashMap<>();
        Stream.of(DrawerItem.values())
                .forEach(item ->
                        drawerItems.put(item, new DrawerItemContainer(DrawerActivity.this, item)));
        openDrawerItem(DEFAULT_ITEM);
    }


    public void openDrawerItem(DrawerItem drawerItem) {
        selectedItem = drawerItem;
        hideDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, drawerItems.get(drawerItem).getFragment())
                .commit();
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else {
            showDrawer();
        }
    }

    private void showDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void hideDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedItem == DrawerItem.MAP) {
            super.onBackPressed();
        } else {
            openDrawerItem(DrawerItem.MAP);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
