package com.example.georgioslamprakis.zboutsam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.activities.adapters.NoteAdapter;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotesList extends AppCompatActivity {

    private ZbtsmApp app;
    private NoteDao noteDao;
    private ListView listView;
    private ArrayList<Note> listNote;
    private NoteAdapter arrayAdapter;
    private Map<Integer, Integer> positionToId;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private Callable<List<Note>> accessDb = new Callable<List<Note>>() {
        @Override
        public List<Note> call() {
            return noteDao.getAllNotes();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = ZbtsmApp.get();
        noteDao = app.getDB().noteDao();
        listView = findViewById(R.id.listViewNotes);
        listNote = new ArrayList<>();
        arrayAdapter = new NoteAdapter(this, listNote);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigerActivity(AddNote.class);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionToId = arrayAdapter.getPositionToID();
                int idClicked = positionToId.get(position);

                Intent intent = new Intent(NotesList.this, AddNote.class);
                Bundle b = new Bundle();
                b.putInt("id", idClicked); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
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
