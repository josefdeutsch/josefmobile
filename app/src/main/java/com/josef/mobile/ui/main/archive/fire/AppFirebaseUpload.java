package com.josef.mobile.ui.main.archive.fire;

import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.archive.fire.model.Data;

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

    private static final String DESCRIPTION = "description";

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

    private static final String _ = "";
    private static final String SOURCES = "sources";
    private static final String CARD = "card";
    private static final String BACKGROUND = "background";
    private static final String TITLE = "title";
    private static final String STUDIO = "studio";
    private static final String CATEGORY = "category";
    private static final String VALUE = "Artwork";
    private static final String GOOGLE_VIDEO = "googlevideos";
    private static final String VIDEO = "videos";

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
            sum.put(TITLE, archives.get(i).name); //name
            sum.put(STUDIO, archives.get(i).tag); //tag
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
