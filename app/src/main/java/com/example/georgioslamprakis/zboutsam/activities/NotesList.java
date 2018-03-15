package com.example.georgioslamprakis.zboutsam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.activities.adapters.NoteAdapter;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotesList extends AppCompatActivity {

    ZbtsmApp app;
    NoteDao noteDao;
    ListView listView;
    ArrayList<Note> listNote;
    NoteAdapter arrayAdapter;
    ExecutorService executor = Executors.newFixedThreadPool(2);

    Callable<List<Note>> accessDb = new Callable<List<Note>>() {
        @Override
        public List<Note> call() {
            return noteDao.getAllNotes();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigerActivity(AddNote.class);
            }
        });

        app = ZbtsmApp.get();
        noteDao = app.getDB().noteDao();
        listView = findViewById(R.id.listViewNotes);
        listNote = new ArrayList<>();
        arrayAdapter = new NoteAdapter(this, listNote);

    }


    @Override
    protected void onResume(){
        super.onResume();
        Future<List<Note>> futureListPopulatedFromDB = executor.submit(accessDb);
        List<Note> updatedNoteList ;
        try{
          updatedNoteList = futureListPopulatedFromDB.get();
          listNote.clear();
          listNote.addAll(updatedNoteList);
        }catch(InterruptedException e){
            Log.e("On Activity resume", "InterruptedException fired : " + e.toString());
        }catch(ExecutionException e){
            Log.e("On Activity resume", "ExecutionException fired : " + e.toString());
        }
        listView.setAdapter(arrayAdapter);
    }

    private void trigerActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

}
