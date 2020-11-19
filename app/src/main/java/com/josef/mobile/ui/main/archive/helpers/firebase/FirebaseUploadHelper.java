package com.josef.mobile.ui.main.archive.helpers.firebase;

import android.util.Log;

import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.archive.model.Data;
import com.josef.mobile.utils.UtilManager;

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
public class FirebaseUploadHelper implements FirebaseUpload {

    private static final String TAG = "FirebaseUploadHelper";
    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final SessionManager sessionManager;

    @Inject
    public FirebaseUploadHelper(DataManager dataManager, SessionManager sessionManager, UtilManager utilManager) {
        this.dataManager = dataManager;
        this.sessionManager = sessionManager;
        this.utilManager = utilManager;
    }

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

    private DatabaseReference upload(User user, List<Archive> archives) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        JSONArray googlevideos = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray sources = new JSONArray();

        for (int i = 0; i <= archives.size() - 1; i++) {
            JSONObject sum = new JSONObject();
            sum.put("description", "");
            JSONArray path = new JSONArray();
            path.put(archives.get(i).url);
            sum.put("sources", path);
            sum.put("card", archives.get(i).png);
            sum.put("background", archives.get(i).png);

            sum.put("title", archives.get(i).name); //name
            sum.put("studio", archives.get(i).tag); //tag

            sources.put(sum);
        }

        data.put("category", "Google+");
        data.put("videos", sources);
        googlevideos.put(data);
        jsonObject.put("googlevideos", googlevideos);
        Data upload = new Data(jsonObject.toString(), "", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();//
        final DatabaseReference myRef = database.getReference("users");

        Log.d(TAG, "upload: " + user.getUid());
        myRef.child(user.uid).setValue(upload);

        return myRef;
    }
}
