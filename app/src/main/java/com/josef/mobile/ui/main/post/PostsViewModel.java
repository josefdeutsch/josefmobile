package com.josef.mobile.ui.main.post;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.Echo;
import com.josef.mobile.Message;
import com.josef.mobile.SessionManager;
import com.josef.mobile.net.main.MainApi;
import com.josef.mobile.ui.main.Resource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<JsonObject>>> posts;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");

    }

    // If we had a local database cache I would have created a "MainRepository" and injected that
    // using constructor injection.

    public LiveData<Resource<List<JsonObject>>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((List<JsonObject>) null));

            Echo echo = new Echo();
            Message message = echo.echo(new Message(), 3);
            Log.d(TAG, "observePosts: " + message.getMessage());


            String myjson = "{\"elements\": [{\"text\":\"Obj1\"},{\"text\":\"Obj2\"}, {\"text\":\"Obj3\"}]}";

            Flowable<List<JsonObject>> flowable = Observable.just(myjson)
                    .map(jsonStr -> new StringReader(myjson))
                    .map(reader -> Json.createReader(reader).readObject())
                    .map(jobj -> jobj.getJsonArray("elements"))
                    .map(elements -> elements.toArray(new JsonObject[elements.size()]))
                    .flatMap(jsonObjects -> Observable.fromArray(jsonObjects))
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .toFlowable();


            final LiveData<Resource<List<JsonObject>>> source = LiveDataReactiveStreams.fromPublisher(
                    //sessionManager.getAuthUser().getValue().data.getId()
                    flowable
                            // instead of calling onError, do this
                            .onErrorReturn(new Function<Throwable, List<JsonObject>>() {
                                @Override
                                public List<JsonObject> apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: ", throwable);
                                    JsonObject object = (JsonObject) new Object();
                                    // object.setId(-1);
                                    ArrayList<JsonObject> posts = new ArrayList<>();
                                    posts.add(object);
                                    return posts;
                                }
                            })

                            .map(new Function<List<JsonObject>, Resource<List<JsonObject>>>() {
                                @Override
                                public Resource<List<JsonObject>> apply(List<JsonObject> posts) throws Exception {
                                    if (posts.size() > 0) {
                                        /**if (posts.get(0).getId() == -1) {
                                         return Resource.error("Something went wrong", null);
                                         }**/
                                    }
                                    return Resource.success(posts);
                                }
                            })
                            .subscribeOn(Schedulers.io()));

            posts.addSource(source, new Observer<Resource<List<JsonObject>>>() {
                @Override
                public void onChanged(Resource<List<JsonObject>> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }

}


