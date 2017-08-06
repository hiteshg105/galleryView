package com.lbb.lbb.dagger.module;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.lbb.lbb.util.presenter.cloudinary.HomePageContract;
import com.lbb.lbb.util.presenter.gallery.GalleryPageContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hitesh on 8/5/17.
 */

@Module
public class PresenterModule {

    @NonNull
    private HomePageContract.View    homePageContractView;

    @NonNull
    private GalleryPageContract.View galleryContractView;

    @NonNull
    private Activity                 activity;

    public PresenterModule(@NonNull HomePageContract.View view) {
        this.homePageContractView = view;
    }

    public PresenterModule(@NonNull Activity activity, @NonNull GalleryPageContract.View view) {
        this.activity = activity;
        this.galleryContractView = view;
    }

    @Provides
    HomePageContract.View providesHomePageContractView() {
        return homePageContractView;
    }

    @Provides
    GalleryPageContract.View providesGalleryPageContractView() {
        return galleryContractView;
    }

    @Provides
    Activity providesActivity() {
        return activity;
    }

}
