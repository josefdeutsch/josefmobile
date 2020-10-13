package com.josef.mobile.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
    private static final String TAG = "FavouriteViewModel";
    private FavouriteRepository repository;
    private LiveData<List<Favourite>> allNotes;
    private String string;
    private MutableLiveData<List<String>> metadata;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteRepository(application);
        allNotes = repository.getAllNotes();
        metadata = new MutableLiveData<>();
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
}

