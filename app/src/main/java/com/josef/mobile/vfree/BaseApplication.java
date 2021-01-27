package com.josef.mobile.vfree;





import com.josef.mobile.vfree.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * http://joseph3d.com/wp-admin/
 * Benutzer: joseph
 * Passwort: %(d2cIVsYKJdVNTZDhd@BR8d
 **/
public class BaseApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
