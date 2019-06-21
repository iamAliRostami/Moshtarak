package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.moshtarak.R;

import java.util.List;

public class LearningCustomAdapter extends ArrayAdapter<LearningCustomAdapter.DrawerItem> {
    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public LearningCustomAdapter(Context context, int layoutResourceID,
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        DrawerItemHolder drawerHolder;
        convertView = null;
        DrawerItem dItem;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        drawerHolder = new DrawerItemHolder();
        convertView = inflater.inflate(layoutResID, parent, false);
        drawerHolder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
        drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
        dItem = this.drawerItemList.get(position);
        drawerHolder.imageViewIcon.setImageDrawable(convertView.getResources().getDrawable(
                dItem.getImgResID()));
        drawerHolder.textViewTitle.setText(dItem.getItemName());
        convertView.setTag(drawerHolder);
        return convertView;
    }

    private static class DrawerItemHolder {
        TextView textViewTitle;
        ImageView imageViewIcon, imageViewSeperator;
    }

    public static class DrawerItem {
        String ItemName;
        int imgResID;

        public DrawerItem(String itemName, int imgResID) {
            super();
            ItemName = itemName;
            this.imgResID = imgResID;
        }

        private String getItemName() {
            return ItemName;
        }

        private void setItemName(String itemName) {
            ItemName = itemName;
        }

        private int getImgResID() {
            return imgResID;
        }

        private void setImgResID(int imgResID) {
            this.imgResID = imgResID;
        }

    }
}

