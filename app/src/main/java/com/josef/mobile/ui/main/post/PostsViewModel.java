package com.josef.mobile.ui.main.post;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.SessionManager;
import com.josef.mobile.models.Change;
import com.josef.mobile.net.main.MainApi;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<Change>> posts;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");

    }

    // If we had a local database cache I would have created a "MainRepository" and injected that
    // using constructor injection.

    public LiveData<Resource<Change>> observePosts() {
        if (posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((Change) null));

            /**  Echo echo = new Echo();
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
             .toFlowable();**/


            final LiveData<Resource<Change>> source = LiveDataReactiveStreams.fromPublisher(
                    //sessionManager.getAuthUser().getValue().data.getId()
                    // flowable
                    mainApi.getPostsFromUser()
                            // instead of calling onError, do this
                            .onErrorReturn(new Function<Throwable, Change>() {
                                @Override
                                public Change apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: ", throwable);
                                    Change object = new Change();
                                    // object.setId(-1);
                                    //ArrayList<Change> posts = new ArrayList<>();
                                    //posts.add(object);
                                    return object;
                                }
                            })

                            .map(new Function<Change, Resource<Change>>() {
                                @Override
                                public Resource<Change> apply(Change change) throws Exception {
                                    return Resource.success(change);
                                }
                            })
                            .subscribeOn(Schedulers.io()));

            posts.addSource(source, new Observer<Resource<Change>>() {
                @Override
                public void onChanged(Resource<Change> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }

}


