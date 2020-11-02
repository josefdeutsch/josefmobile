package com.josef.mobile.ui.main.archive.helpers.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.archive.model.Archive;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class FirebaseUploadHelper implements FirebaseUpload {

    private static final String TAG = "FirebaseUploadHelper";
    private final DataManager dataManager;
    private final UtilManager utilManager;
    private final SessionManager sessionManager;
    private final Context context;

    @Inject
    public FirebaseUploadHelper(DataManager dataManager, SessionManager sessionManager, Context context, UtilManager utilManager) {
        this.dataManager = dataManager;
        this.sessionManager = sessionManager;
        this.utilManager = utilManager;
        this.context = context;
    }

    public void synchronize(MainActivity mainActivity, CompositeDisposable compositeDisposable) {
        if (sessionManager == null) return;

        Publisher<AuthResource<User>> userPublisher = LiveDataReactiveStreams.toPublisher(mainActivity, sessionManager.getAuthUser());

        Observable<User> currentuser = Observable.fromPublisher(userPublisher)
                .map(userAuthResource -> userAuthResource.data);
        compositeDisposable.add(currentuser.subscribe());

        Observable<List<Archive>> archives = dataManager.getAllArchives().toObservable();
        compositeDisposable.add(archives.subscribe());

        compositeDisposable.add(
                Observable.zip(
                        currentuser,
                        archives,
                        new BiFunction<User, List<Archive>, DatabaseReference>() {
                            @NonNull
                            @Override
                            public DatabaseReference apply(@NonNull User user, @NonNull List<Archive> archives) throws Exception {
                                return upload(user, archives);
                            }
                        }

                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<DatabaseReference>() {

                            @Override
                            public void onNext(@NonNull DatabaseReference reference) {
                                Toast.makeText(context, "Success !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Toast.makeText(context, "Error !", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onError: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(context, "Completed!", Toast.LENGTH_SHORT).show();
                            }
                        }));

    }

    private DatabaseReference upload(User user, List<Archive> archives) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        JSONArray googlevideos = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray sources = new JSONArray();

        for (int i = 0; i <= archives.size() - 1; i++) {
            JSONObject sum = new JSONObject();
            sum.put("description", "LoremIpsum...");
            JSONArray path = new JSONArray();
            path.put(archives.get(i).url);
            sum.put("sources", path);
            sum.put("card", archives.get(i).png);
            sum.put("background", archives.get(i).png);
            sum.put("title", "material");
            sum.put("studio", "Google+");
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
