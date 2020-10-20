package com.josef.mobile.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.models.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Single;

public class FavouriteViewModel extends AndroidViewModel {
    private static final String TAG = "FavouriteViewModel";
    private final FavouriteRepository repository;
    private final LiveData<List<Favourite>> allNotes;
    private String string;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void insert(Favourite note) {
        repository.insert(note);
    }

    public void update(Favourite note) {
        repository.update(note);
    }

    public void delete(Favourite note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Favourite>> getAllNotes() {
        return allNotes;
    }

    // inject Sessionmanager to FLowable add Database

    public void onNetworkCall(String userId) {
        Publisher<List<Favourite>> list = LiveDataReactiveStreams.toPublisher(getApplication(), allNotes);
        Single<List<Favourite>> flowable = Single.fromPublisher(list);
        flowable.map(list1 -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();//
            final DatabaseReference myRef = database.getReference("users");
            myRef.child(userId).setValue(getData(list1));
            return list1;
        });
    }

    private Data getData(List<Favourite> list) throws JSONException {
        Data string = null;
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray googlevideos = new JSONArray();
            JSONObject data = new JSONObject();
            JSONArray sources = new JSONArray();
            for (int i = 0; i <= list.size() - 1; i++) {
                JSONObject sum = new JSONObject();
                sum.put("description", "LoremIpsum...");
                JSONArray path = new JSONArray();
                path.put(list.get(i).getTitle());
                sum.put("sources", path);
                sum.put("card", list.get(i).getDescription());
                sum.put("background", list.get(i).getDescription());
                sum.put("title", "material");
                sum.put("studio", "Google+");
                sources.put(sum);
            }
            data.put("category", "Google+");
            data.put("videos", sources);
            googlevideos.put(data);
            jsonObject.put("googlevideos", googlevideos);
            string = new Data(jsonObject.toString(), "", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }
}
