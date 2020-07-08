package com.app.leon.moshtarak.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.Activities.BaseInfoActivity;
import com.app.leon.moshtarak.Activities.ContactUsActivity;
import com.app.leon.moshtarak.Activities.HomeActivity;
import com.app.leon.moshtarak.Activities.RecoveryCodeActivity;
import com.app.leon.moshtarak.Activities.SessionActivity;
import com.app.leon.moshtarak.Activities.SignAccountActivity;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomTab;

import java.util.ArrayList;
import java.util.List;

public class NavigationCustomAdapter extends
        RecyclerView.Adapter<NavigationCustomAdapter.DrawerItemHolder> {
    public Context context;
    private List<DrawerItem> drawerItemList;

    public NavigationCustomAdapter(Context context, List<DrawerItem> listItems) {
        this.context = context;
        this.drawerItemList = listItems;
    }

    @NonNull
    @Override
    public DrawerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View drawerView = inflater.inflate(R.layout.item_navigation, parent, false);
        return new DrawerItemHolder(drawerView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerItemHolder holder, int position) {
        DrawerItem drawerItem = drawerItemList.get(position);
        if (position == 8) {
            holder.textViewTitle.setTextColor(context.getResources().getColor(R.color.red2));
        } else if (position == MyApplication.position) {
            holder.textViewTitle.setTextColor(context.getResources().getColor(R.color.white));
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red2));
        }
        holder.imageViewIcon.setImageDrawable(drawerItem.drawable);
        holder.textViewTitle.setText(drawerItem.ItemName);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    public static class DrawerItem {
        String ItemName;
        Drawable drawable;


        DrawerItem(String itemName, Drawable drawable) {
            this.ItemName = itemName;
            this.drawable = drawable;
        }

        public static ArrayList<DrawerItem> createItemList(String[] menu, TypedArray drawable) {
            ArrayList<DrawerItem> drawerItems = new ArrayList<>();
            int numItem = menu.length;
            for (int i = 0; i < numItem; i++) {
                drawerItems.add(new DrawerItem(menu[i], drawable.getDrawable(i)));
            }
            return drawerItems;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public List<NavigationCustomAdapter.DrawerItem> convertStringToList(String[] items,
                                                                            TypedArray imgs) {
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
    }

    public class DrawerItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewIcon;
        LinearLayout linearLayout;

        public DrawerItemHolder(View viewItem) {
            super(viewItem);
            this.textViewTitle = viewItem.findViewById(R.id.textViewTitle);
            this.imageViewIcon = viewItem.findViewById(R.id.imageViewIcon);
            this.linearLayout = viewItem.findViewById(R.id.linearLayoutBackground);
            viewItem.setOnClickListener(view -> {
                int position = getAdapterPosition();
                MyApplication.position = position;
                if (position == 0) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else if (position == 1) {
                    new CustomTab(context.getString(R.string.abfa_site), context);
                } else if (position == 2) {
                    Intent intent = new Intent(context, BaseInfoActivity.class);
                    context.startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(context, SessionActivity.class);
                    context.startActivity(intent);
                } else if (position == 4) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("bazaar://details?id=" + context.getPackageName())));
                    } catch (android.content.ActivityNotFoundException e) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/details?id=" + context.getPackageName())));
                    }
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
//                } catch (android.content.ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
//                }
                } else if (position == 5) {
                    Intent intent = new Intent(context, SignAccountActivity.class);
                    context.startActivity(intent);
                } else if (position == 6) {
                    Intent intent = new Intent(context, RecoveryCodeActivity.class);
                    context.startActivity(intent);
                } else if (position == 7) {
                    Intent intent = new Intent(context, ContactUsActivity.class);
                    context.startActivity(intent);
                } else if (position == 8) {
                    ((Activity) context).finishAffinity();
                }
            });
        }
    }
}
