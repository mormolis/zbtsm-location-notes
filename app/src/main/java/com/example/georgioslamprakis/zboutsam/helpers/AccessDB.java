package com.example.georgioslamprakis.zboutsam.helpers;

import android.util.Log;

import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.database.daos.CategoryDao;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Category;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.ArrayList;
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
    private static CategoryDao categoryDao = app.getDB().categoryDao();
    private static ExecutorService executor = Executors.newFixedThreadPool(2);

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

    public static boolean isNoteIdInDb(final int id){
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

    public static int findCategoryIdByCategoryTitle(final String title){
        int id = -1;
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return categoryDao.findIdByCategoryTitle(title);
            }
        };
        Future<Integer> future = executor.submit(callable);
        try{
            id = future.get();
        }catch(Exception e){
            Log.e("AccessingDB", e.toString());
        }
        return id;
    }

    public static String findCategoryTitleById(final int categoryId){
        String categoryTitle = "Uncategorised";
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                 return categoryDao.findCategoryFromId(categoryId).get(0).getTitle();
            }
        };
        Future<String> future = executor.submit(callable);
        try{
            categoryTitle = future.get();
        }catch(Exception e){
            Log.e("AccessingDB", "Getting Category from id" + e.toString());
        }
        return categoryTitle;
    }

    public static List<String> getAllCategories(){
        Callable<List<Category>> callable = new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {
                return categoryDao.getAllCategories();
            }
        };

        Future<List<Category>> categoryListFuture = executor.submit(callable);
        List<Category> categoryList;
        List<String> categoryListTitles = new ArrayList<>();
        try{
            categoryList = categoryListFuture.get();
            for (Category category : categoryList){
                categoryListTitles.add(category.getTitle());
            }
            return categoryListTitles;
        }catch (Exception e){
            Log.e("getAllCategoriesAccsDB", e.toString());
        }
        return null;
    }

    public static void deleteCategoryByTitle(final String categoryTitle){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                categoryDao.deleteByTitle(categoryTitle);

            }
        });
    }




}
