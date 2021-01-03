package com.josef.josefmobile.ui.main.archive.fire;

import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.josef.josefmobile.SessionManager;
import com.josef.josefmobile.data.DataManager;
import com.josef.josefmobile.data.local.db.model.Archive;
import com.josef.josefmobile.ui.auth.AuthResource;
import com.josef.josefmobile.ui.auth.model.User;
import com.josef.josefmobile.ui.main.MainActivity;
import com.josef.josefmobile.ui.main.archive.fire.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppFirebaseUpload implements FirebaseUpload {

    private final DataManager dataManager;
    private final SessionManager sessionManager;

    public Observable<DatabaseReference> synchronize(MainActivity mainActivity) {
        if (sessionManager == null) return null;
        Observable<User> currentuser = getUserObservable(mainActivity);
        Observable<List<Archive>> archives = getListObservable();
        return Observable.zip(currentuser, archives, (user, archives1) -> upload(user, archives1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Archive>> getListObservable() {
        return dataManager.getAllArchives().toObservable();
    }

    private Observable<User> getUserObservable(MainActivity mainActivity) {
        Publisher<AuthResource<User>> userPublisher
                = LiveDataReactiveStreams.toPublisher(mainActivity, sessionManager.getAuthUser());
        return Observable.fromPublisher(userPublisher)
                .map(userAuthResource -> userAuthResource.data);
    }

    @Inject
    public AppFirebaseUpload(DataManager dataManager, SessionManager sessionManager) {
        this.dataManager = dataManager;
        this.sessionManager = sessionManager;
    }

    private DatabaseReference upload(User user, List<Archive> archives) throws JSONException {

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
        DatabaseReference myRef = dataManager.getDatabasereference();
        myRef.child(user.uid).setValue(upload);

        return myRef;
    }
}
