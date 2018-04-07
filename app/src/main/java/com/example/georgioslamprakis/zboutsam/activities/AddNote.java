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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddNote extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
    private Spinner spinner;

    private boolean isNewNote = true;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);



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
            updateNoteInDb();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("on back pressed", noteDao.getAllNotes().toString());
            }
        }).start();
    }

    private void populateSpinner(){
        Callable<List<Category>> getAllCategoriesFromDB = new Callable<List<Category>>() {
            @Override
            public List<Category> call(){
                return categoryDao.getAllCategories();
            }
        };

        Future<List<Category>> futureListPopulatedFromDB = executor.submit(getAllCategoriesFromDB);

        try{
            List<Category> updatedNoteList = futureListPopulatedFromDB.get();
            for (Category category:updatedNoteList)
                spinnerArray.add(category.getTitle());
        } catch (Exception e){
            Log.e("populatingSpinner", e.toString());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner = findViewById(R.id.spinnerCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getAllFields(){
        titleTextField = findViewById(R.id.editTextTitle);
        textField = findViewById(R.id.editTextNote);
        title = titleTextField.getText().toString();
        text = textField.getText().toString();
        spinner = findViewById(R.id.spinnerCategory);
        category = spinner.getSelectedItem().toString();
    }

    private void updateNoteInDb(){
        getAllFields();
        note.setText(text);
        note.setTitle(title);
        int categoryId = AccessDB.findCategoryIdByCategoryTitle(category);
        if (categoryId != -1){
            note.setCategoryId(categoryId);
        }
        AccessDB.updateNote(note);
    }


    private void insertItem(){
        final Note newNote = new Note();
        getAllFields();
        newNote.setTitle(title);
        newNote.setText(text);
        if (!newNote.getText().isEmpty() || !newNote.getTitle().isEmpty()){
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
