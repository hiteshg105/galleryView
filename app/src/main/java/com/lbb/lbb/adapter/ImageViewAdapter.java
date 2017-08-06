package com.lbb.lbb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lbb.lbb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hitesh on 8/4/17.
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {

    private final Context context;

    public ImageViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_home, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        switch (position) {
            case 0:
                Glide.with(context).load("http://res.cloudinary.com/hiteshg105/image/upload/image_2" + ".jpg").into(holder.image);
                holder.text.setText("Chocolate Ice Cream");
                break;
            case 1:
                Glide.with(context).load("http://res.cloudinary.com/hiteshg105/image/upload/image_3" + ".jpg").into(holder.image);
                holder.text.setText("Vanilla Ice Cream");
                break;

            case 2:
                Glide.with(context).load("http://res.cloudinary.com/hiteshg105/image/upload/image_2" + ".jpg").into(holder.image);
                holder.text.setText("Strawberry Ice Cream");
                break;
            case 3:
                Glide.with(context).load("http://res.cloudinary.com/hiteshg105/image/upload/image_4" + ".jpg").into(holder.image);
                holder.text.setText("Butter Scotch Ice Cream");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.text_image)
        TextView text;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
