package com.app.leon.moshtarak.BaseItems;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomTab;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.activities.BaseInfoActivity;
import com.app.leon.moshtarak.activities.ContactUsActivity;
import com.app.leon.moshtarak.activities.HomeActivity;
import com.app.leon.moshtarak.activities.RecoveryCodeActivity;
import com.app.leon.moshtarak.activities.RegisterAccountActivity;
import com.app.leon.moshtarak.activities.SessionActivity;
import com.app.leon.moshtarak.adapters.NavigationCustomAdapter;
import com.app.leon.moshtarak.databinding.BaseActivityBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    DrawerLayout drawer;
    RecyclerView recyclerView;
    NavigationCustomAdapter adapter;
    List<NavigationCustomAdapter.DrawerItem> dataList;
    BaseActivityBinding binding;
    SharedPreference sharedPreference;
    View.OnClickListener onImageSwitcherClickListener = new View.OnClickListener() {
        @SuppressLint("NewApi")
        @Override
        public void onClick(View v) {
            sharedPreference.putTheme(!sharedPreference.getTheme());
            if (sharedPreference.getTheme()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    };

    @SuppressLint({"NewApi", "RtlHardcoded", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        binding = BaseActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        MyApplication.isHome = false;
        MyApplication.doubleBackToExitPressedOnce = false;
        initializeBase();
        setSupportActionBar(toolbar);//TODO
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(view1 -> drawer.openDrawer(Gravity.START));
        initialize();
    }

    protected abstract void initialize();

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!MyApplication.isHome) {
                super.onBackPressed();
            } else {
                if (MyApplication.doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                }
                MyApplication.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> MyApplication.doubleBackToExitPressedOnce = false, 2000);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    void setOnDrawerItemClick() {
        recyclerView.addOnItemTouchListener(
                new NavigationCustomAdapter.RecyclerItemClickListener(MyApplication.getContext(),
                        recyclerView, new NavigationCustomAdapter.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        drawer.closeDrawer(GravityCompat.START);

                        MyApplication.position = position;
                        if (position == 0) {
                            Intent intent = new Intent(MyApplication.getContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (position == 1) {
                            Intent intent = new Intent(MyApplication.getContext(), RegisterAccountActivity.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            new CustomTab(getString(R.string.abfa_site), MyApplication.getContext());
                        } else if (position == 3) {
                            Intent intent = new Intent(MyApplication.getContext(), BaseInfoActivity.class);
                            startActivity(intent);
                        } else if (position == 4) {
                            Intent intent = new Intent(MyApplication.getContext(), SessionActivity.class);
                            startActivity(intent);
                        } else if (position == 5) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("bazaar://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/details?id=" + getPackageName())));
                            }
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
//                } catch (android.content.ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
//                }
                        } else if (position == 6) {
                            Intent intent = new Intent(MyApplication.getContext(), RecoveryCodeActivity.class);
                            startActivity(intent);
                        } else if (position == 7) {
                            Intent intent = new Intent(MyApplication.getContext(), ContactUsActivity.class);
                            startActivity(intent);
                        } else if (position == 8) {
                            finishAffinity();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void initializeBase() {
        sharedPreference = new SharedPreference(this);
        toolbar = findViewById(R.id.toolbar);
        drawer = binding.drawerLayout;
        recyclerView = binding.recyclerView;
        dataList = new ArrayList<>();
        fillDrawerListView();
        setOnDrawerItemClick();
        initializeImageViewSwitch();
    }

    @SuppressLint("NewApi")
    private void initializeImageViewSwitch() {
        if (sharedPreference.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        binding.imageViewSwitch.setOnClickListener(onImageSwitcherClickListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void fillDrawerListView() {
        dataList = NavigationCustomAdapter.DrawerItem.createItemList(
                getResources().getStringArray(R.array.menu), getResources().obtainTypedArray(
                        R.array.icons));
        if (sharedPreference.checkIsNotEmpty())
            dataList.get(1).setItemName(getString(R.string.change_account_));
        adapter = new NavigationCustomAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        recyclerView.setNestedScrollingEnabled(true);
    }
}

