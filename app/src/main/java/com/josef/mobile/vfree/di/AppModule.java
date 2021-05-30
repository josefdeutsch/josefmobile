package com.josef.mobile.vfree.di;

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
import com.josef.mobile.vfree.data.AppDataManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.data.ads.AdsRequest;
import com.josef.mobile.vfree.data.ads.AppAdsRequest;
import com.josef.mobile.vfree.data.firebase.Firebase;
import com.josef.mobile.vfree.data.firebase.Firedatabase;
import com.josef.mobile.vfree.data.local.db.AppDataBase;
import com.josef.mobile.vfree.data.local.db.AppDbHelper;
import com.josef.mobile.vfree.data.local.db.DbHelper;
import com.josef.mobile.vfree.data.local.prefs.AppPreferencesHelper;
import com.josef.mobile.vfree.data.local.prefs.PreferencesHelper;
import com.josef.mobile.vfree.data.remote.Endpoints;
import com.josef.mobile.vfree.utils.AppConstants;
import com.josef.mobile.vfree.utils.AppUtilManager;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.vfree.utils.common.AppCommonUtils;
import com.josef.mobile.vfree.utils.common.CommonUtils;
import com.josef.mobile.vfree.utils.dialog.auth.AppAuthDialog;
import com.josef.mobile.vfree.utils.dialog.auth.AuthDialog;
import com.josef.mobile.vfree.utils.dialog.main.AppMainDialog;
import com.josef.mobile.vfree.utils.dialog.main.MainDialog;
import com.josef.mobile.vfree.utils.net.AppNetworkUtils;
import com.josef.mobile.vfree.utils.net.NetworkUtils;
import com.josef.mobile.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class AppModule {

    @Provides
    @Singleton
    static  DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    static  AppDataBase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }


    @Provides
    @Singleton
    static DbHelper provideDbHelper(AppDbHelper appDbHelper, Context context) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    static  Firebase provideFirebase(Firedatabase firedatabase) {
        return firedatabase;
    }

    @Provides
    @Singleton
    static FirebaseDatabase provideFiredatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    static PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Singleton
    @Provides
    static  FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }


    @Provides
    @Singleton
    static Endpoints provideMainApi(Retrofit retrofit) {
        return retrofit.create(Endpoints.class);
    }


    @Provides
    @Singleton
    static UtilManager provideUtil(AppUtilManager utilManager) {
        return utilManager;
    }

    @Provides
    @Singleton
    static MainDialog provideDialogUtil(AppMainDialog mainDialogUtils) {
        return mainDialogUtils;
    }

    @Provides
    @Singleton
    static AuthDialog provideAuthDialogUtil(AppAuthDialog appAuthDialog) {
        return appAuthDialog;
    }

    @Provides
    @Singleton
    static NetworkUtils provideNetworkUtil(AppNetworkUtils appNetworkUtils) {
        return appNetworkUtils;
    }

    @Provides
    @Singleton
    static CommonUtils provideCommonUtil(AppCommonUtils commonUtils) {
        return commonUtils;
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30l, TimeUnit.SECONDS)
                        .readTimeout(30l, TimeUnit.SECONDS).build())
                .build();
    }

    @Provides
    @DatabaseInfo
    static String provideDatabaseName() {
        return "my_db.db";
    }

    @Provides
    @PreferenceInfo
    static String providePreferenceName() {
        return "josef_pref";
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
        return GoogleSignIn.getClient(application, gso);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }

    @Singleton
    @Provides
    static AdsRequest provideRequestAd(AppAdsRequest appAdsRequest) {
        return appAdsRequest;
    }


    @Provides
    @Singleton
    static Context provideContext(Application application) {
        return application;
    }
}