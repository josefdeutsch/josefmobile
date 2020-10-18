package com.josef.mobile.ui.player;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.josef.mobile.SessionManager;
import com.josef.mobile.models.Player;
import com.josef.mobile.ui.main.Resource;

import javax.inject.Inject;

public class PlayerViewModel extends ViewModel {

    private static final String TAG = "PostsViewModel";

    SessionManager sessionManager;

    private MediatorLiveData<Resource<Player>> posts;

    @Inject
    public PlayerViewModel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
    }

 /**   public void perfomLiveData(String string) {
  Log.d(TAG, "attemptLogin: attempting to login.");
  sessionManager.select(observePosts(string));
  }

  public LiveData<Resource<Player>> observeAuthState() {
  return sessionManager.getAuthUser();
  }

  public LiveData<Resource<Player>> observePosts(String string) {
  Player player = new Player(string, 1);

        Flowable<Player> playerFlowable = Observable.just(player)
                .toFlowable(BackpressureStrategy.BUFFER);

        final LiveData<Resource<Player>> source = LiveDataReactiveStreams.fromPublisher(
                playerFlowable
                        .onErrorReturn(new Function<Throwable, Player>() {
                            @Override
                            public Player apply(Throwable throwable) throws Exception {
                                Log.e(TAG, "apply: " + throwable.toString());
                                Player object = new Player();
                                // object.setId(-1);
                                //ArrayList<Change> posts = new ArrayList<>();
                                //posts.add(object);
                                return object;
                            }
                        })
  .map(new Function<Player, Resource<Player>>() {
 @Override public Resource<Player> apply(Player change) throws Exception {
 // ALERTS
 return Resource.success(change);
 }
 })
  .subscribeOn(Schedulers.io()));

  return source;
  }**/
}
