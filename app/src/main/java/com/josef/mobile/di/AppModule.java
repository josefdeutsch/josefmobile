package com.josef.mobile.di;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.R;
import com.josef.mobile.data.AppDataManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.firebase.Firebase;
import com.josef.mobile.data.firebase.Firedatabase;
import com.josef.mobile.data.local.db.AppDataBase;
import com.josef.mobile.data.local.db.AppDbHelper;
import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.prefs.AppPreferencesHelper;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.data.remote.Endpoints;
import com.josef.mobile.utils.AppConstants;
import com.josef.mobile.utils.AppUtilManager;
import com.josef.mobile.utils.UtilManager;
import com.josef.mobile.utils.common.AppCommonUtils;
import com.josef.mobile.utils.common.CommonUtils;
import com.josef.mobile.utils.net.AppNetworkUtils;
import com.josef.mobile.utils.net.NetworkUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {


    @Provides
    @Singleton
    AppDataBase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper, Context context) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    Firebase provideFirebase(Firedatabase firedatabase) {
        return firedatabase;
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "my_db.db";
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }


    @Provides
    @Singleton
    Endpoints provideMainApi(Retrofit retrofit) {
        return retrofit.create(Endpoints.class);
    }

    @Provides
    @Singleton
    UtilManager provideUtil(AppUtilManager utilManager) {
        return utilManager;
    }

    @Provides
    @Singleton
    CommonUtils provideCommonUtil(AppCommonUtils appCommonUtils) {
        return appCommonUtils;
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtil(AppNetworkUtils appNetworkUtils) {
        return appNetworkUtils;
    }


    @Provides
    @Singleton
    FirebaseDatabase provideFiredatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL3)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30l, TimeUnit.SECONDS)
                        .readTimeout(30l, TimeUnit.SECONDS).build())
                .build();
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }


    @Singleton
    @Provides
    static GoogleSignInClient provideGoogleClient(Application application) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        return GoogleSignIn.getClient(application, gso);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }


    @Singleton
    @Provides
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }


}