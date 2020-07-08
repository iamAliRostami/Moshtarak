package com.app.leon.moshtarak.BaseItems;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.Adapters.NavigationCustomAdapter;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.SharedPreference;
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
                binding.imageViewSwitch.setImageDrawable(ContextCompat.getDrawable(
                        MyApplication.getContext(), R.drawable.night_drawer));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                binding.imageViewSwitch.setImageDrawable(ContextCompat.getDrawable(
                        MyApplication.getContext(), R.drawable.day_drawer));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    };

    protected abstract void initialize();

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint({"NewApi", "RtlHardcoded", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        binding = BaseActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initializeBase();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    void setOnDrawerItemClick() {
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
            binding.imageViewSwitch.setImageDrawable(ContextCompat.getDrawable(MyApplication.getContext()
                    , R.drawable.night_drawer)
            );
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
//            binding.imageViewSwitch.setImageDrawable(getDrawable(R.drawable.day_drawer));//TODO
            binding.imageViewSwitch.setImageDrawable(ContextCompat.getDrawable(MyApplication.getContext()
                    , R.drawable.day_drawer)
            );
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        binding.imageViewSwitch.setOnClickListener(onImageSwitcherClickListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList = null;
        adapter = null;
    }

    void fillDrawerListView() {
        dataList = NavigationCustomAdapter.DrawerItem.createItemList(
                getResources().getStringArray(R.array.menu), getResources().obtainTypedArray(R.array.icons));
        if (sharedPreference.checkIsNotEmpty())
            dataList.get(5).setItemName(getString(R.string.change_account));
        adapter = new NavigationCustomAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

//    public void setItemsColor(ViewGroup viewTree, int selected) {
//        Stack<ViewGroup> stackOfViewGroup = new Stack<>();
//        stackOfViewGroup.push(viewTree);
//        while (!stackOfViewGroup.isEmpty()) {
//            ViewGroup tree = stackOfViewGroup.pop();
//            for (int i = 0; i < tree.getChildCount() - 1; i++) {
//                View child = tree.getChildAt(i);
//                if (child instanceof ViewGroup) {
//                    stackOfViewGroup.push((ViewGroup) child);
//                } else if (child instanceof TextView) {
//                    if (child.getId() == R.id.textViewTitle) {
//                        ((TextView) child).setTextColor(getResources().getColor(R.color.gray2));
//                    }
//                }
//            }
//        }
//        View view = drawerList.getChildAt(selected);
//        TextView textView = view.findViewById(R.id.textViewTitle);
//        textView.setTextColor(getResources().getColor(R.color.white));
//    }
}

