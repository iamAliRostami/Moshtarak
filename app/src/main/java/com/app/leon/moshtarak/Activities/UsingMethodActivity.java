package com.app.leon.moshtarak.Activities;

import android.widget.ListView;

import com.app.leon.moshtarak.Adapters.LearningCustomAdapter;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsingMethodActivity extends BaseActivity {
    @BindView(R.id.listViewLearningUsing)
    ListView listViewLearningUsing;
    LearningCustomAdapter adapter;
    List<LearningCustomAdapter.DrawerItem> dataList;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.using_method_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        fillListViewLearningUsing();
    }

    void fillListViewLearningUsing() {
        dataList = new ArrayList<>();
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_bath), R.drawable.btn_read));
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_washing), R.drawable.btn_read));
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_wc), R.drawable.btn_read));
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_watering), R.drawable.btn_read));
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_pool), R.drawable.btn_read));
        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_planet), R.drawable.btn_read));
        adapter = new LearningCustomAdapter(this, R.layout.item_learning, dataList);
        listViewLearningUsing.setAdapter(adapter);
    }
}
