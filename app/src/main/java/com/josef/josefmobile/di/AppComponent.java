package com.josef.josefmobile.di;

import android.app.Application;

import com.josef.josefmobile.BaseApplication;
import com.josef.josefmobile.SessionManager;
import com.josef.josefmobile.data.DataManager;
import com.josef.josefmobile.utils.UtilManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivitysBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager sessionManager();

    DataManager dataManager();

    UtilManager commonUtils();


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
