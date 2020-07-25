package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationCustomAdapterOld extends ArrayAdapter<NavigationCustomAdapterOld.DrawerItem> {
    private Context context;
    private List<DrawerItem> drawerItemList;
    private int layoutResID;

    public NavigationCustomAdapterOld(Context context, int layoutResourceID,
                                      List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public DrawerItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(DrawerItem item) {
        return super.getPosition(item);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        DrawerItemHolder drawerHolder;
        convertView = null;
        DrawerItem dItem;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        drawerHolder = new DrawerItemHolder();
        convertView = inflater.inflate(layoutResID, parent, false);
        drawerHolder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
        drawerHolder.linearLayout = convertView.findViewById(R.id.linearLayoutBackground);
        if (position == 8) {
            drawerHolder.textViewTitle.setTextColor(context.getResources().getColor(R.color.red2));
        } else if (position == MyApplication.position) {
            drawerHolder.textViewTitle.setTextColor(context.getResources().getColor(R.color.white));
            drawerHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red2));
        }
        drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        dItem = this.drawerItemList.get(position);
        drawerHolder.imageViewIcon.setImageDrawable(dItem.drawable);
        drawerHolder.textViewTitle.setText(dItem.getItemName());
        convertView.setTag(drawerHolder);
        return convertView;
    }

    private static class DrawerItemHolder {
        TextView textViewTitle;
        ImageView imageViewIcon;
        LinearLayout linearLayout;
    }

    public static class DrawerItem {
        String ItemName;
        Drawable drawable;

        public DrawerItem() {
            super();
        }

        DrawerItem(String itemName, Drawable drawable) {
            super();
            this.ItemName = itemName;
            this.drawable = drawable;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public List<NavigationCustomAdapterOld.DrawerItem> convertStringToList(String[] items,
                                                                               TypedArray imgs) {
            List<NavigationCustomAdapterOld.DrawerItem> drawerItems = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                drawerItems.add(new DrawerItem(items[i], imgs.getDrawable(i)));
            }
            return drawerItems;
        }

        String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }
    }
}
