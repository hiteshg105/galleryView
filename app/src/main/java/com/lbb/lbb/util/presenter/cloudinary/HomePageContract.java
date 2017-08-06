package com.lbb.lbb.util.presenter.cloudinary;

/**
 * Created by hitesh on 8/5/17.
 */

public interface HomePageContract {

    interface View {

        void showGalleryPhotos();

        //As no network call on adapter as Glide will load each adapter's image
        //Not needed for now
        void showProgress();

        void hideProgress();

    }

    // Not needed for now
    interface Presenter {

        void getCloudinaryPhotos();
    }
}
