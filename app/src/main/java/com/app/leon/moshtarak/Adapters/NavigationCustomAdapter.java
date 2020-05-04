package com.app.leon.moshtarak.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationCustomAdapter extends ArrayAdapter<NavigationCustomAdapter.DrawerItem> {
    private Context context;
    private List<DrawerItem> drawerItemList;
    private int layoutResID;

    public NavigationCustomAdapter(Context context, int layoutResourceID,
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        DrawerItemHolder drawerHolder;
        convertView = null;
        DrawerItem dItem;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//        FontManager fontManager = new FontManager(context);
        if (position == 0) {
            drawerHolder = new DrawerItemHolder();
            convertView = inflater.inflate(R.layout.item_navigation_, parent, false);
            drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            dItem = this.drawerItemList.get(position);
//            drawerHolder.imageViewIcon.setImageDrawable(convertView.getResources().getDrawable(
//                    dItem.getImgResID()));
            drawerHolder.imageViewIcon.setImageDrawable(dItem.drawable);
        } else {
            drawerHolder = new DrawerItemHolder();
            convertView = inflater.inflate(layoutResID, parent, false);
            drawerHolder.textViewTitle = convertView
                    .findViewById(R.id.textViewTitle);
            if (position == 9) {
                drawerHolder.textViewTitle.setTextColor(context.getResources().getColor(R.color.red2));
            }
            drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            dItem = this.drawerItemList.get(position);
            drawerHolder.imageViewIcon.setImageDrawable(dItem.drawable);
            drawerHolder.textViewTitle.setText(dItem.getItemName());
//            fontManager.setFont(drawerHolder.textViewTitle);
        }
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
        Drawable drawable;

        public DrawerItem() {
            super();
        }

        DrawerItem(String itemName, int imgResID) {
            super();
            this.ItemName = itemName;
            this.imgResID = imgResID;
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

        public List<NavigationCustomAdapter.DrawerItem> convertStringToList(String[] items, TypedArray imgs) {
            List<NavigationCustomAdapter.DrawerItem> drawerItems = new ArrayList<>();

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

        int getImgResID() {
            return imgResID;
        }

        public void setImgResID(int imgResID) {
            this.imgResID = imgResID;
        }

    }
}
