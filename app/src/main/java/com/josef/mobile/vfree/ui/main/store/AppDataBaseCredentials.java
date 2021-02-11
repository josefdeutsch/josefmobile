package com.josef.mobile.vfree.ui.main.store;

import androidx.lifecycle.LiveDataReactiveStreams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.josef.mobile.vfree.SessionManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.main.Resource;
import org.reactivestreams.Publisher;
import javax.inject.Inject;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class AppDataBaseCredentials implements Credentials {

    private static final String PROFILE = "profile";

    @NonNull
    private final SessionManager sessionManager;
    @NonNull
    private final DataManager dataManager;

    @Inject
    public AppDataBaseCredentials(@NonNull SessionManager sessionManager,
                                  @NonNull DataManager dataManager) {
        this.sessionManager = sessionManager;
        this.dataManager = dataManager;
    }

    @NonNull
    private Observable<User> getUserObservable(@NonNull MainActivity activity) {
        Publisher<AuthResource<User>> userPublisher
                = LiveDataReactiveStreams.toPublisher(activity, sessionManager.getAuthUser());
        return Observable.fromPublisher(userPublisher)
                .map(userAuthResource -> userAuthResource.data);
    }

    @NonNull
    private Observable<User> getDataStoreCredentials(@NonNull User user) {
        return Single.create(new SingleOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<User> emitter) throws Throwable {

                DatabaseReference databaseReference = dataManager.getFirebaseDataBase()
                        .getReference(PROFILE);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            user.setEmail(dataSnapshot.child(user.uid).getValue(User.class).getEmail());
                            user.setFname(dataSnapshot.child(user.uid).getValue(User.class).getFname());
                            user.setLname(dataSnapshot.child(user.uid).getValue(User.class).getLname());

                            emitter.onSuccess(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                });
            }
        }).toObservable();

    }

    @NonNull
    public Flowable<Resource<User>> observeDataStore(@NonNull MainActivity activity) {

        if (sessionManager == null) return null;
        return getUserObservable(activity).
                flatMap(user -> getDataStoreCredentials(user))
                .onErrorReturn(throwable -> {
                    User user = new User();
                    user.setId(-1);
                    return user;
                }).map((Function<User, Resource<User>>) user -> {
                    if (user.getId() == -1) {
                        return Resource.error(user.getThrowable().toString(), null);
                    }
                    return Resource.success(user);
                }).toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());


    }
}
