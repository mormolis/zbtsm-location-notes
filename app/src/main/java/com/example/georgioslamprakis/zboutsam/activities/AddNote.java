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

import java.util.ArrayList;
import java.util.List;

public class AddNote extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
//TODO: fix category spinner
    final List<Category> categoryArray =  new ArrayList<>();
    final List<String> spinnerArray =  new ArrayList<>();
    ZbtsmApp app = ZbtsmApp.get();
    NoteDao noteDao = app.getDB().noteDao();
    CategoryDao categoryDao = app.get().getDB().categoryDao();

    EditText titleTextField;
    EditText textField;
    String title;
    String text;
    String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        populateSpinner();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        insertItem();
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
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
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

    private void insertItem(){
        getAllFields();
        final Note note = new Note();
        note.setTitle(title);
        note.setText(text);
        //!note.getText().isEmpty() || !note.getTitle().isEmpty()
        if (true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (category==null){
                        note.setCategoryId(categoryDao.findIdByCategoryTitle("Uncategorised"));

                    }
                    else{
                        note.setCategoryId(categoryDao.findIdByCategoryTitle(category));
                    }
                    noteDao.insert(note);

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
        Toast.makeText(adapterView.getContext(), "heloo", Toast.LENGTH_SHORT).show();

    }
}
