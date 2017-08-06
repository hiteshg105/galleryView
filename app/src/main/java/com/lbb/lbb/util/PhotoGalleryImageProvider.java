package com.lbb.lbb.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.lbb.lbb.data.model.PhotoItem;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryImageProvider {


    public static List<PhotoItem> getAlbumThumbnails(Context context){

        final String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID};
        String orderBy = android.provider.MediaStore.Video.Media.DATE_TAKEN;
        Cursor thumbnailsCursor = context.getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                orderBy + " DESC");

        int thumbnailColumnIndex = thumbnailsCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        ArrayList<PhotoItem> result = new ArrayList<>(thumbnailsCursor.getCount());

        if (thumbnailsCursor.moveToFirst()) {
            do {
                int thumbnailImageID = thumbnailsCursor.getInt(thumbnailColumnIndex);
                String thumbnailPath = thumbnailsCursor.getString(thumbnailImageID);
                Uri thumbnailUri = Uri.parse(thumbnailPath);
                PhotoItem newItem = new PhotoItem(thumbnailUri);
                result.add(newItem);
            } while (thumbnailsCursor.moveToNext());
        }
        thumbnailsCursor.close();
        return result;
    }

}
