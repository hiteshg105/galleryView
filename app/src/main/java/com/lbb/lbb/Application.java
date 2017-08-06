package com.lbb.lbb;

import com.lbb.lbb.dagger.component.ApplicationComponent;
import com.lbb.lbb.dagger.component.DaggerApplicationComponent;
import com.lbb.lbb.dagger.module.ApplicationModule;

/**
 * Created by hitesh on 8/1/17.
 */

public class Application extends android.app.Application{

    private static Application     instance;
    private ApplicationComponent applicationComponent;
    public static synchronized Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);
    }


}
