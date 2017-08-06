
package com.lbb.lbb.adapter;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lbb.lbb.R;
import com.lbb.lbb.data.model.PhotoItem;
import com.lbb.lbb.ui.galleryphotos.PhotoListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private PhotoListFragment  fragment;
    private int                resourceId;
    List<PhotoItem>            items;
    private SparseBooleanArray mSparseBooleanArray;

    public PhotoAdapter(PhotoListFragment fragment, int resourceId, List<PhotoItem> items, boolean useList) {
        super();
        this.fragment = fragment;
        this.resourceId = resourceId;
        this.items = items;
        mSparseBooleanArray = new SparseBooleanArray();
    }

    public ArrayList<PhotoItem> getCheckedItems() {
        ArrayList<PhotoItem> mTempArry = new ArrayList<PhotoItem>();

        for (int i = 0; i < items.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(items.get(i));
            }
        }

        return mTempArry;
    }

    public void resetSelectedItems() {
        mSparseBooleanArray = new SparseBooleanArray();
        notifyDataSetChanged();

    }

    public boolean isSelectionModeOn() {

        for (int i = 0; i < items.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PhotoItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final PhotoItem photoItem = getItem(position);
        View viewToUse;
        LayoutInflater mInflater = (LayoutInflater) fragment.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            viewToUse = mInflater.inflate(resourceId, null);
            holder.photoImageView = (ImageView) viewToUse.findViewById(R.id.item_image);
            holder.selectedLinearLayout = (LinearLayout) viewToUse.findViewById(R.id.selected_layout);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        if (mSparseBooleanArray!=null &&mSparseBooleanArray.get(position)) {
            holder.selectedLinearLayout.setVisibility(View.VISIBLE);
        } else {
            holder.selectedLinearLayout.setVisibility(View.GONE);

        }
        holder.selectedLinearLayout.setTag(position);

        holder.photoImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fragment.updateDoneButton();
                mSparseBooleanArray.put((Integer) holder.selectedLinearLayout.getTag(), true);
                holder.selectedLinearLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectionModeOn()) {

                    if (mSparseBooleanArray.get((Integer) holder.selectedLinearLayout.getTag())) {
                        mSparseBooleanArray.put((Integer) holder.selectedLinearLayout.getTag(), false);
                        holder.selectedLinearLayout.setVisibility(View.GONE);
                    } else {
                        mSparseBooleanArray.put((Integer) holder.selectedLinearLayout.getTag(), true);
                        holder.selectedLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
                fragment.updateDoneButton();
            }
        });
        Glide.with(fragment.getContext()).load(new File(photoItem.getThumbnailUri().getPath())).into(holder.photoImageView);
        return viewToUse;
    }

    private static class ViewHolder {
        ImageView    photoImageView;
        LinearLayout selectedLinearLayout;
    }

    public SparseBooleanArray getmSparseBooleanArray() {
        return mSparseBooleanArray;
    }

}
