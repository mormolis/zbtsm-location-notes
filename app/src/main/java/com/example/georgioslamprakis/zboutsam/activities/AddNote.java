package com.example.georgioslamprakis.zboutsam.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.georgioslamprakis.zboutsam.ZbtsmApp;

import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.database.daos.CategoryDao;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Category;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;
import com.example.georgioslamprakis.zboutsam.helpers.AccessDB;

import java.util.ArrayList;
import java.util.List;

public class AddNote extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
//TODO: fix category spinner
    private final List<Category> categoryArray =  new ArrayList<>();
    private final List<String> spinnerArray =  new ArrayList<>();
    private ZbtsmApp app = ZbtsmApp.get();
    private NoteDao noteDao = app.getDB().noteDao();
    private CategoryDao categoryDao = app.get().getDB().categoryDao();

    private EditText titleTextField;
    private EditText textField;
    private String title;
    private String text;
    private String category;
    private Note note;

    private boolean isNewNote = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        populateSpinner();

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            value = b.getInt("id");
        }
        if(AccessDB.isIdInDb(value)){
            note = AccessDB.returnNoteByID(value);
            getAllFields();
            titleTextField.setText(note.getTitle());
            textField.setText(note.getText());
            isNewNote = false;
        }
        Log.i("check-value-passed", Integer.toString(value));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (isNewNote){
            insertItem();
        } else {
            updateNoteinDb();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("on back pressed", noteDao.getAllNotes().toString());
            }
        }).start();
    }

    private void populateSpinner(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                categoryArray.addAll(categoryDao.getAllCategories());
                for (Category category:categoryArray)
                    spinnerArray.add(category.getTitle());
            }
        }).start();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        Spinner spinner = findViewById(R.id.spinnerCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getAllFields(){
        titleTextField = findViewById(R.id.editTextTitle);
        textField = findViewById(R.id.editTextNote);
        title = titleTextField.getText().toString();
        text = textField.getText().toString();
        //TODO:need to retrieve category
    }

    private void updateNoteinDb(){
        getAllFields();
        note.setText(text);
        note.setTitle(title);
        AccessDB.updateNote(note);
    }


    private void insertItem(){
        final Note newNote = new Note();
        getAllFields();
        newNote.setTitle(title);
        newNote.setText(text);
        //!newNote.getText().isEmpty() || !newNote.getTitle().isEmpty()
        if (true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (category==null){
                        newNote.setCategoryId(categoryDao.findIdByCategoryTitle("Uncategorised"));

                    }
                    else{
                        newNote.setCategoryId(categoryDao.findIdByCategoryTitle(category));
                    }
                    noteDao.insert(newNote);

                }
            }).start();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), category, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(), "helloo", Toast.LENGTH_SHORT).show();

    }
}
