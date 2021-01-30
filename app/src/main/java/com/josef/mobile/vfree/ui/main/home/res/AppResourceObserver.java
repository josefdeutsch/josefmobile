package com.josef.mobile.vfree.ui.main.home.res;

import android.content.Context;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.home.model.Profile;
import com.josef.mobile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppResourceObserver implements ResourceObserver {

    private final Context context;

    @Inject
    public AppResourceObserver(Context context) {
        this.context = context;
    }

    public ArrayList<Profile> initiateProfilesData() {

        ArrayList<Profile> profiles = new ArrayList<>();
        List<String> url = Arrays.asList(context.getResources().getStringArray(R.array.profile_url_supplier));
        List<String> article = Arrays.asList(context.getResources().getStringArray(R.array.profile_text_supplier));

        for (int i = 0; i <= url.size() - 1; i++) {
            Profile profile = new Profile();
            profile.setUrl(url.get(i));
            profile.setArticle(article.get(i));
            profiles.add(profile);
        }

        return profiles;
    }

    private Flowable<List<Profile>> getFlowablesProfiles(ArrayList<Profile> profiles) {
        return Flowable.fromIterable(profiles)
                .toList()
                .toFlowable();
    }

    public Flowable<Resource<List<Profile>>> getAllProfiles() {

        return getFlowablesProfiles(initiateProfilesData())
                .onErrorReturn((Function<Throwable, ArrayList<Profile>>) throwable -> {
                    Profile container = new Profile();
                    container.setId(-1);
                    container.setException(throwable.getMessage());
                    ArrayList<Profile> containers = new ArrayList<>();
                    containers.add(container);
                    return containers;
                })
                .map((Function<List<Profile>, Resource<List<Profile>>>) posts -> {
                    if (posts.size() > 0) {
                        if (posts.get(0).getId() == -1) {
                            return Resource.error(posts.get(0).getException(), null);
                        }
                    }
                    return Resource.success(posts);
                })
                .subscribeOn(Schedulers.io());
    }

}
