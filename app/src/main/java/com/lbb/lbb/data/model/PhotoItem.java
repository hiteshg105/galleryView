package com.lbb.lbb.data.model;

import android.net.Uri;

public class PhotoItem {

    private Uri thumbnailUri;
//    private Uri fullImageUri;

    public PhotoItem(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
//        this.fullImageUri = fullImageUri;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

//    public Uri getFullImageUri() {
//        return fullImageUri;
//    }
//
//    public void setFullImageUri(Uri fullImageUri) {
//        this.fullImageUri = fullImageUri;
//    }
}
