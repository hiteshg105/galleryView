package com.lbb.lbb.util.presenter.cloudinary;

import javax.inject.Inject;

/**
 * Created by hitesh on 8/5/17.
 */

public class HomePagePresenter implements HomePageContract.Presenter {

    HomePageContract.View view;

    @Inject
    public HomePagePresenter(HomePageContract.View view) {
        this.view = view;
    }

    public void openGallery() {
        view.showGalleryPhotos();
    }

    @Override
    public void getCloudinaryPhotos() {
        //Used if external call is made to fetch the urls of each image
        //And update view from here
    }
}
