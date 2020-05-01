package com.josef.mobile.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteRepository {
    private static final String TAG = "FavouriteRepository";
    private FavouriteDao noteDao;
    private LiveData<List<Favourite>> allNotes;

    public FavouriteRepository(Application application) {
        FavouriteDatabase database = FavouriteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();

    }

    public void insert(Favourite note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Favourite note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Favourite note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Favourite>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private FavouriteDao noteDao;

        private InsertNoteAsyncTask(FavouriteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Favourite... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private FavouriteDao noteDao;

        private UpdateNoteAsyncTask(FavouriteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Favourite... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Favourite, Void, Void> {
        private FavouriteDao noteDao;

        private DeleteNoteAsyncTask(FavouriteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Favourite... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouriteDao noteDao;

        private DeleteAllNotesAsyncTask(FavouriteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
