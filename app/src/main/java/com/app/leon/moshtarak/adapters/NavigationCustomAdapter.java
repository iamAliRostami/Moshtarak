package com.app.leon.moshtarak.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationCustomAdapter extends
        RecyclerView.Adapter<NavigationCustomAdapter.DrawerItemHolder> {
    public Context context;
    private final List<DrawerItem> drawerItemList;

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


        public void setItemName(String itemName) {
            ItemName = itemName;
        }
    }

    static class DrawerItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewIcon;
        LinearLayout linearLayout;

        public DrawerItemHolder(View viewItem) {
            super(viewItem);
            this.textViewTitle = viewItem.findViewById(R.id.textViewTitle);
            this.imageViewIcon = viewItem.findViewById(R.id.imageViewIcon);
            this.linearLayout = viewItem.findViewById(R.id.linearLayoutBackground);
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private final OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,
                                         OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && mListener != null) {
                                mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onLongItemClick(View view, int position);
        }
    }
}
