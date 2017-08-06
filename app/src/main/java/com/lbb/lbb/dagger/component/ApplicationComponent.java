package com.lbb.lbb.dagger.component;

/**
 * Created by hitesh on 8/1/17.
 */

import android.app.Application;

import com.lbb.lbb.dagger.module.ApplicationModule;
import com.lbb.lbb.dagger.module.PresenterModule;
import com.lbb.lbb.ui.cloudinaryphotos.MainActivity;
import com.lbb.lbb.ui.galleryphotos.PhotoListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class, PresenterModule.class })
public interface ApplicationComponent {
    //Application
    void inject(Application app);

    void inject(MainActivity activity);

    void inject(PhotoListFragment fragment);
}
