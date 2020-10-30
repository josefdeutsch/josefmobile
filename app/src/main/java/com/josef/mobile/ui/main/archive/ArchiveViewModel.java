package com.josef.mobile.ui.main.archive;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.josef.mobile.SessionManager;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.auth.AuthResource;
import com.josef.mobile.ui.auth.model.User;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.archive.model.Archive;
import com.josef.mobile.ui.main.archive.model.Data;
import com.josef.mobile.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ArchiveViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    private final DataManager dataManager;
    private final Util util;
    private final SessionManager sessionManager;
    private final Context context;

    private MediatorLiveData<Resource<List<Archive>>> posts;

    @Inject
    public ArchiveViewModel(Context context, SessionManager sessionManager, DataManager dataManager, Util util) {
        this.context = context;
        this.sessionManager = sessionManager;
        this.dataManager = dataManager;
        this.util = util;
    }

    public LiveData<Resource<List<Archive>>> observeArchive() {
        if (posts == null) posts = new MediatorLiveData<>();

        posts.setValue(Resource.loading(null));
        final LiveData<Resource<List<Archive>>> source = LiveDataReactiveStreams.fromPublisher(
                dataManager.getAllArchives()
                        .onErrorReturn(throwable -> {
                            Log.e(TAG, "apply: " + throwable.toString());
                            Archive archive = new Archive();
                            archive.id = -1l;
                            List<Archive> archives = new ArrayList<>();
                            archives.add(archive);
                            return archives;
                        })
                        .map((Function<List<Archive>, Resource<List<Archive>>>) archives -> {

                            if (archives.size() > 0) {
                                if (archives.get(0).id == -1l) {
                                    return Resource.error("Error!", null);
                                }
                            }

                            return Resource.success(archives);
                        })
                        .subscribeOn(Schedulers.io()));

        posts.addSource(source, listResource -> {
            posts.setValue(listResource);
            posts.removeSource(source);
        });

        return posts;
    }

    public void synchronize(MainActivity mainActivity) {
        Log.d(TAG, "synchronize: " + sessionManager);
        if (sessionManager == null) return;

        Log.d(TAG, "synchronize: ");
        Publisher<AuthResource<User>> userPublisher = LiveDataReactiveStreams.toPublisher(mainActivity, sessionManager.getAuthUser());
        Observable<User> currentuser = Observable.fromPublisher(userPublisher).map(userAuthResource -> userAuthResource.data);
        Observable<List<Archive>> archives = dataManager.getAllArchives().toObservable();
        Observable.zip(
                currentuser,
                archives,
                new BiFunction<User, List<Archive>, Boolean>() {
                    @NonNull
                    @Override
                    public Boolean apply(@NonNull User user, @NonNull List<Archive> archives) throws Exception {
                        return upload(user, archives);
                    }
                }

        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean s) {
                        Toast.makeText(context, "onNext " + s, Toast.LENGTH_SHORT).show();
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
                });
    }

    public void deleteArchivesPref(final Archive archive) {
        long id = archive.id;
        Log.d(TAG, "deleteArchivesPref: " + archive.id);
        HashMap<Integer, Boolean> map;
        Type sparseArrayType = new TypeToken<HashMap<Integer, Boolean>>() {
        }.getType();
        Gson gson = util.getGson();
        String stringmap = dataManager.getHashString();
        map = gson.fromJson(stringmap, sparseArrayType);
        map.replace((int) id, false);
        String string = gson.toJson(map);
        dataManager.setHashString(string);
    }

    public void deleteArchives(final Archive archive) {
        dataManager.deleteArchives(archive);
        observeArchive();
    }

    private Boolean upload(User user, List<Archive> archives) throws JSONException {

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
        Task<Void> resultTask = myRef.child(user.uid).setValue(upload);

        return resultTask.isSuccessful();
    }


}


