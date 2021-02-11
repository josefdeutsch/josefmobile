package com.josef.mobile.vfree.ui.main.archive.fire;

import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.vfree.SessionManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.archive.model.Archive;
import com.josef.mobile.vfree.ui.auth.AuthResource;
import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.main.archive.fire.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppFirebaseUpload implements FirebaseUpload {

    @NonNull
    private final DataManager dataManager;
    @NonNull
    private final SessionManager sessionManager;

    @Inject
    public AppFirebaseUpload(@NonNull DataManager dataManager,
                             @NonNull SessionManager sessionManager) {

        this.dataManager = dataManager;
        this.sessionManager = sessionManager;
    }

    @NonNull
    public Observable<DatabaseReference> synchronize(@NonNull MainActivity mainActivity) {
        if (sessionManager == null) return null;

        Observable<User> currentuser = getUserObservable(mainActivity);
        Observable<List<Archive>> archives = getListObservable();

        return Observable.zip(currentuser, archives, (user, archives1) -> AppFirebaseUpload.this.upload(user, archives1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Observable<List<Archive>> getListObservable() {
        return dataManager.getAllArchives().toObservable();
    }

    @NonNull
    private Observable<User> getUserObservable(@NonNull MainActivity mainActivity) {
        Publisher<AuthResource<User>> userPublisher
                = LiveDataReactiveStreams.toPublisher(mainActivity, sessionManager.getAuthUser());
        return Observable.fromPublisher(userPublisher)
                .map(userAuthResource -> userAuthResource.data);
    }

    @NonNull
    private DatabaseReference upload(@NonNull User user,
                                     @NonNull List<Archive> archives)
            throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray googlevideos = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray sources = new JSONArray();

        for (int i = 0; i <= archives.size() - 1; i++) {
            JSONObject sum = new JSONObject();
            sum.put(DESCRIPTION, _);
            JSONArray path = new JSONArray();
            path.put(archives.get(i).url);
            sum.put(SOURCES, path);
            sum.put(CARD, archives.get(i).png);
            sum.put(BACKGROUND, archives.get(i).png);
            sum.put(TITLE, archives.get(i).name);
            sum.put(STUDIO, archives.get(i).tag);
            sources.put(sum);
        }
        data.put(CATEGORY, VALUE);
        data.put(VIDEO, sources);
        googlevideos.put(data);
        jsonObject.put(GOOGLE_VIDEO, googlevideos);
        Data upload = new Data(jsonObject.toString(), _, _);
        DatabaseReference myRef = dataManager.getDataBaseRefChild_User();
        myRef.child(user.uid).setValue(upload);

        return myRef;
    }
}
