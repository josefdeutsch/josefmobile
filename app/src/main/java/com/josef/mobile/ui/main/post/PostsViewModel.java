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
import com.josef.mobile.models.Post;
import com.josef.mobile.net.main.MainApi;
import com.josef.mobile.ui.main.Resource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Post>>> posts;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");

    }

    // If we had a local database cache I would have created a "MainRepository" and injected that
    // using constructor injection.

    public LiveData<Resource<List<Post>>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((List<Post>) null));

            Echo echo = new Echo();
            Message message = echo.echo(new Message(), 3);
            Log.d(TAG, "observePosts: " + message.getMessage());

            Flowable<Message> flowable = Observable.just(message)
                    .subscribeOn(Schedulers.io())
                    .toFlowable(BackpressureStrategy.BUFFER);

            String myjson = "{\"elements\": [{\"text\":\"Obj1\"},{\"text\":\"Obj2\"}, {\"text\":\"Obj3\"}]}";

            Observable.just(myjson)
                    .map(jsonStr -> new StringReader(myjson))
                    .map(reader -> Json.createReader(reader).readObject())

                    .map(jobj -> jobj.getJsonArray("elements"))
                    .map(elements -> elements.toArray(new JsonObject[elements.size()]))
                    .flatMap(jsonObjects -> Observable.fromArray(jsonObjects))
                    .subscribe(
                            (jsonObject) -> Log.d(TAG, "observePosts: " + jsonObject.getString("text")),
                            throwable -> throwable.printStackTrace(),
                            () -> Log.d(TAG, "observePosts: " + "oncomplete"));

            final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams.fromPublisher(
                    //sessionManager.getAuthUser().getValue().data.getId()
                    mainApi.getPostsFromUser(1)
                            // instead of calling onError, do this
                            .onErrorReturn(new Function<Throwable, List<Post>>() {
                                @Override
                                public List<Post> apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: ", throwable);
                                    Post post = new Post();
                                    post.setId(-1);
                                    ArrayList<Post> posts = new ArrayList<>();
                                    posts.add(post);
                                    return posts;
                                }
                            })

                            .map(new Function<List<Post>, Resource<List<Post>>>() {
                                @Override
                                public Resource<List<Post>> apply(List<Post> posts) throws Exception {
                                    if (posts.size() > 0) {
                                        if (posts.get(0).getId() == -1) {
                                            return Resource.error("Something went wrong", null);
                                        }
                                    }
                                    return Resource.success(posts);
                                }
                            })
                            .subscribeOn(Schedulers.io()));

            posts.addSource(source, new Observer<Resource<List<Post>>>() {
                @Override
                public void onChanged(Resource<List<Post>> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }

}


