package com.lbb.lbb.util.presenter.gallery;

import android.support.v4.content.Loader;

import com.lbb.lbb.data.model.PhotoItem;

import java.util.List;

/**
 * Created by hitesh on 8/5/17.
 */

public class GalleryPageContract {

    public interface View {

        void cancelProgressDialog();

        void openCamera();

        void showProgressDialog(String message);

        void finishLoading(List<PhotoItem> data);

        void resolveEmptyText();
    }

    public interface Presenter {

        void getGalleryPhotos();

        Loader<List<PhotoItem>> getAsyncLoader();

        void onLoadFinished(List<PhotoItem> data);
    }
}
