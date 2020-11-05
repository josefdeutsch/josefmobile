package com.josef.mobile.ui.auth.sign;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.ui.base.BaseViewModel;
import com.josef.mobile.ui.main.Resource;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class SignViewModel extends BaseViewModel {

    private static final String TAG = "SignViewModel";

    private final Context mContext;
    private final FirebaseAuth firebaseAuth;
    private final MediatorLiveData<Resource<Boolean>> containers = new MediatorLiveData<>();

    @Inject
    public SignViewModel(Context context, FirebaseAuth firebaseAuth) {
        this.mContext = context;
        this.firebaseAuth = firebaseAuth;
    }

    public MediatorLiveData<Resource<Boolean>> getContainers() {
        return containers;
    }

    public void createUserWithEmailandPassword(String email, String password) {

        LiveData<Resource<Boolean>> source =
                LiveDataReactiveStreams.fromPublisher(getFlowableResourceBoolean(email, password));

        containers.setValue(Resource.loading(null));

        containers.addSource(source, listResource -> {
            containers.setValue(listResource);
            containers.removeSource(source);
        });
    }

    private Flowable<Resource<Boolean>> getFlowableResourceBoolean(String email, String password) {
        Flowable<Resource<Boolean>> resourceFlowable =
                addOnCompletionListener(email, password)
                        .toFlowable()
                        .timeout(3000, TimeUnit.MILLISECONDS, Flowable.empty())
                        .onErrorReturn(throwable -> {
                            Log.e(TAG, "apply: " + throwable.toString());
                            Boolean completion = null;
                            return completion;
                        })
                        .map((Function<Boolean, Resource<Boolean>>) completion -> {
                            if (completion == null) {
                                return Resource.error("Error!", null);
                            }
                            return Resource.success(completion);
                        })

                        .subscribeOn(Schedulers.io());

        return resourceFlowable;
    }

    public Single<Boolean> addOnCompletionListener(String email, String password) {

        return Single.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        emitter.onSuccess(true);
                    } else {
                        emitter.onError(new RuntimeException("an error occured.."));
                    }
                }
            });
        });

    }

}
