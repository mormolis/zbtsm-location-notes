package com.example.georgioslamprakis.zboutsam.helpers;

import android.util.Log;

import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by georgioslamprakis on 18/03/2018.
 */

public class AccessDB {

    private static ZbtsmApp app = ZbtsmApp.get();
    private static NoteDao noteDao = app.getDB().noteDao();
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static List<Note> returnAllNotes(){
        Callable<List<Note>> getAllNotesFromDb = new Callable<List<Note>>() {
            @Override
            public List<Note> call() {
                return noteDao.getAllNotes();
            }
        };

        Future<List<Note>> futureListPopulatedFromDB = executor.submit(getAllNotesFromDb);
        List<Note> updatedNoteList;

        try {
            updatedNoteList = futureListPopulatedFromDB.get();
            return updatedNoteList;
        }catch(InterruptedException e){
            Log.e("On Activity resume", "InterruptedException fired : " + e.toString());
        }catch(ExecutionException e){
            Log.e("On Activity resume", "ExecutionException fired : " + e.toString());
        }

        return null;
    }

    public static Note returnNoteByID(int id){
        final int finalId = id;
         Callable<Note> getNoteById = new Callable<Note>() {
            @Override
            public Note call() {
                return noteDao.getNoteById(finalId);
            }
        };
         Future<Note> futureNote = executor.submit(getNoteById);
         Note note;
         try{
             note = futureNote.get();
             return note;
         }catch(InterruptedException e){
             Log.e("On returnNoteByID", "InterruptedException fired : " + e.toString());
         }catch(ExecutionException e){
             Log.e("On returnNoteByID", "ExecutionException fired : " + e.toString());
         }
         return null;
    }

    public static boolean isIdInDb(final int id){
        Callable<Integer> countIdsFromDb = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return noteDao.isOneOrZero(id);
            }
        };

        Future<Integer> futureCoundTheId = executor.submit(countIdsFromDb);
        try{
            return futureCoundTheId.get() == 1;
        }catch(InterruptedException e){
            Log.e("On isInTheDB", "InterruptedException fired : " + e.toString());
        }catch(ExecutionException e){
            Log.e("On isInTheDB", "ExecutionException fired : " + e.toString());
        }

        return false;
    }


    public static void updateNote(final Note note){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }

    public static void deleteNote(final int noteId){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteById(noteId);
            }
        });
    }




}