package com.lbb.lbb.util.presenter.gallery;

import android.app.Activity;
import android.support.v4.content.Loader;

import com.lbb.lbb.data.model.PhotoItem;
import com.lbb.lbb.util.PhotoGalleryAsyncLoader;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hitesh on 8/1/17.
 */

public class GalleryPresenter implements GalleryPageContract.Presenter {

    Activity                 context;
    GalleryPageContract.View view;

    @Inject
    public GalleryPresenter(Activity context, GalleryPageContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void getGalleryPhotos() {
        view.openCamera();
    }

    @Override
    public Loader<List<PhotoItem>> getAsyncLoader() {
        view.showProgressDialog("Loading Photos...");
        return new PhotoGalleryAsyncLoader(context);
    }

    @Override
    public void onLoadFinished(List<PhotoItem> data) {
        view.finishLoading(data);
        view.resolveEmptyText();
        view.cancelProgressDialog();
    }
}
