package com.app.leon.moshtarak.BaseItems;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.leon.moshtarak.Activities.BaseInfoActivity;
import com.app.leon.moshtarak.Activities.ContactDeveloperActivity;
import com.app.leon.moshtarak.Activities.HomeActivity;
import com.app.leon.moshtarak.Activities.SignAccountActivity;
import com.app.leon.moshtarak.Adapters.NavigationCustomAdapter;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomTab;
import com.app.leon.moshtarak.Utils.FontManager;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final String packageName = "com.app.leon.moshtarak";
    private final String url = "https://www.abfaesfahan.ir";
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.left_drawer)
    ListView drawerList;
    Typeface typeface;
    NavigationCustomAdapter adapter;
    List<NavigationCustomAdapter.DrawerItem> dataList;

    protected abstract UiElementInActivity getUiElementsInActivity();

    protected abstract void initialize();

    @SuppressLint({"NewApi", "RtlHardcoded", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        UiElementInActivity uiElementInActivity = getUiElementsInActivity();
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        setContentView(uiElementInActivity.getContentViewId());
        initializeBase();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(view -> drawer.openDrawer(Gravity.START));
        initialize();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    void setOnDrawerItemClick() {
        drawerList.setOnItemClickListener((adpterView, view, position, id) -> {
            setItemsColor(drawer, position);
            if (position != 0) {
                for (int i = 0; i < drawerList.getChildCount(); i++) {
                    if (position == i) {
                        drawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.red2));
                    } else {
                        drawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }
            if (position == 1) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                new CustomTab(url, BaseActivity.this);
            } else if (position == 3) {
                Intent intent = new Intent(getApplicationContext(), BaseInfoActivity.class);
                startActivity(intent);
            } else if (position == 4) {
            } else if (position == 5) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            } else if (position == 6) {
                Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
                startActivity(intent);
            } else if (position == 7) {
            } else if (position == 8) {
                Intent intent = new Intent(getApplicationContext(), ContactDeveloperActivity.class);
                startActivity(intent);
            } else if (position == 9) {
                finishAffinity();
            }
            drawer.closeDrawer(GravityCompat.START);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact) {
//            SharedPreferences appPrefs = getSharedPreferences("com.app.leon.moshtarak.user_preferences", MODE_PRIVATE);
//            SharedPreferences.Editor prefsEditor = appPrefs.edit();
//            prefsEditor.putString("file_number", "");
//            prefsEditor.putString("account_number", "");
//            prefsEditor.apply();
//            Toast.makeText(getApplicationContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setMenuBackground() {
        getLayoutInflater().setFactory((name, context, attrs) -> {
            if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                try {
                    // Ask our inflater to create the view
                    LayoutInflater f = getLayoutInflater();
                    final View view = f.createView(name, null, attrs);
                    // Kind of apply our own background
                    new Handler().post(() -> view.setBackgroundResource(R.color.black));
                    return view;
                } catch (InflateException | ClassNotFoundException ignored) {
                }
            }
            return null;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeBase() {
        initializeTypeface();
        ButterKnife.bind(this);
        dataList = new ArrayList<>();
        fillDrawerListView();
        setOnDrawerItemClick();
        FontManager fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(this.drawer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList = null;
        adapter = null;
        typeface = null;
    }

    void fillDrawerListView() {
        dataList.add(new NavigationCustomAdapter.DrawerItem("", R.drawable.img_menu_logo));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.home), R.drawable.img_home));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.portal_connect), R.drawable.img_connect_to_portal));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.base_info), R.drawable.img_profile));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.transaction), R.drawable.img_transactions));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.update), R.drawable.img_update));
        SharedPreference sharedPreference = new SharedPreference(this);
        if (sharedPreference.checkIsNotEmpty())
            dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.change_account), R.drawable.img_registration));
        else
            dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.account), R.drawable.img_registration));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.recovery_code), R.drawable.img_recovery_code));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.connect_developer), R.drawable.img_contact_us));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.exit), R.drawable.img_exit));
        adapter = new NavigationCustomAdapter(this, R.layout.item_navigation, dataList);
        drawerList.setAdapter(adapter);
    }

    void initializeTypeface() {
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan_3.ttf");
    }

    public void setItemsColor(ViewGroup viewTree, int selected) {
        Stack<ViewGroup> stackOfViewGroup = new Stack<>();
        stackOfViewGroup.push(viewTree);
        while (!stackOfViewGroup.isEmpty()) {
            ViewGroup tree = stackOfViewGroup.pop();
            for (int i = 0; i < tree.getChildCount(); i++) {
                View child = tree.getChildAt(i);
                if (child instanceof ViewGroup) {
                    stackOfViewGroup.push((ViewGroup) child);
                } else if (child instanceof TextView) {
                    if (child.getId() == R.id.textViewTitle) {
                        ((TextView) child).setTextColor(getResources().getColor(R.color.gray2));
                    }
                }
            }
        }
        View view = drawerList.getChildAt(selected);
        TextView textView = view.findViewById(R.id.textViewTitle);
        textView.setTextColor(getResources().getColor(R.color.white));
    }
}

