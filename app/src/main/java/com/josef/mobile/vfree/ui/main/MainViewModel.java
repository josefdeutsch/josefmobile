package com.josef.mobile.vfree.ui.main;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.base.BaseViewModel;
import com.josef.mobile.vfree.data.ads.OnAdsInstantiated;
import com.josef.mobile.vfree.ui.main.store.Credentials;

import java.util.Objects;

import javax.inject.Inject;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;

public final class MainViewModel extends BaseViewModel {

    @NonNull
    private final DataManager dataManager;
    @NonNull
    private final Credentials credentials;
    @Nullable
    private final MediatorLiveData<Resource<User>> dataStoreCredentials = new MediatorLiveData<>();

    @Inject
    public MainViewModel(
            @NonNull DataManager dataManager,
            @NonNull Credentials credentials
    ) {

        this.dataManager = dataManager;
        this.credentials = credentials;

    }

    @Nullable
    public MediatorLiveData<Resource<User>> getDataStoreCredentials() {
         return Objects.requireNonNull(dataStoreCredentials,
                "com.josef.mobile.vfree.ui.main.MainViewodel " +
                        "dataStoreCredentials must not be null" );
    }



    @NonNull
    public MediatorLiveData<Resource<User>> observeDataStoreCredentials(
            @NonNull MainActivity mainActivity) {

        LiveData<Resource<User>> source =
                LiveDataReactiveStreams.fromPublisher(credentials.observeDataStore(mainActivity));

        dataStoreCredentials.setValue(Resource.loading(null));

        dataStoreCredentials.addSource(source, listResource -> {
            dataStoreCredentials.setValue(listResource);
            dataStoreCredentials.removeSource(source);
        });
        return dataStoreCredentials;
    }

}
