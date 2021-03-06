package com.example.georgioslamprakis.zboutsam.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.georgioslamprakis.zboutsam.database.entities.helpers.ZbtsmLocation;
import com.example.georgioslamprakis.zboutsam.helpers.AccessDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class AddNote extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FusedLocationProviderClient mFusedLocationClient;
    private final int ZBTSM_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private final List<String> spinnerArray = new ArrayList<>();
    private ZbtsmApp app = ZbtsmApp.get();
    private NoteDao noteDao = app.getDB().noteDao();
    private CategoryDao categoryDao = app.getDB().categoryDao();
    private boolean locationPermissionGranded = false;

    private EditText titleTextField;
    private EditText textField;
    private String title;
    private String text;
    private String category;
    private Note note;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ZbtsmLocation zbtsmLocation;

    private boolean isNewNote = true;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        populateSpinner();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AddNote.this);
        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            value = b.getInt("id");
        }
        if (AccessDB.isNoteIdInDb(value)) {
            note = AccessDB.returnNoteByID(value);
            getAllFields();
            titleTextField.setText(note.getTitle());
            textField.setText(note.getText());
            spinner.setSelection(spinnerAdapter.getPosition(AccessDB.findCategoryTitleById(note.getCategoryId())));
            isNewNote = false;

            Log.i("NoteItem:", note.toString());

        } else {
            note = new Note();
        }
        Log.i("check-value-passed", Integer.toString(value));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isNewNote) {
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

    private void populateSpinner() {
        Callable<List<Category>> getAllCategoriesFromDB = new Callable<List<Category>>() {
            @Override
            public List<Category> call() {
                return categoryDao.getAllCategories();
            }
        };

        Future<List<Category>> futureListPopulatedFromDB = executor.submit(getAllCategoriesFromDB);

        try {
            List<Category> updatedNoteList = futureListPopulatedFromDB.get();
            for (Category category : updatedNoteList)
                spinnerArray.add(category.getTitle());
        } catch (Exception e) {
            Log.e("populatingSpinner", e.toString());
        }

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner = findViewById(R.id.spinnerCategory);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getAllFields() {
        titleTextField = findViewById(R.id.editTextTitle);
        textField = findViewById(R.id.editTextNote);
        title = titleTextField.getText().toString();
        text = textField.getText().toString();
        spinner = findViewById(R.id.spinnerCategory);
        category = spinner.getSelectedItem().toString();
        if (zbtsmLocation == null && note.getId() != -1){
            zbtsmLocation = AccessDB.getNotesLocation(note.getId());
        }
    }

    private void updateNoteInDb() {
        getAllFields();
        note.setText(text);
        note.setTitle(title);
        note.setZbtsmLocation(zbtsmLocation);
        int categoryId = AccessDB.findCategoryIdByCategoryTitle(category);
        if (categoryId != -1) {
            note.setCategoryId(categoryId);
        }
        AccessDB.updateNote(note);
    }


    private void insertItem() {
        final Note newNote = new Note();
        getAllFields();
        newNote.setTitle(title);
        newNote.setText(text);
        newNote.setZbtsmLocation(zbtsmLocation);
        if (!newNote.getText().isEmpty() || !newNote.getTitle().isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (category == null) {
                        newNote.setCategoryId(categoryDao.findIdByCategoryTitle("Uncategorised"));

                    } else {
                        newNote.setCategoryId(categoryDao.findIdByCategoryTitle(category));
                    }
                    noteDao.insert(newNote);

                }
            }).start();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //do nothing
        category = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), category, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(), "helloo", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.addLocationMenu:
                getAccessLocationPermission();
                return true;
            case R.id.showOnMapMenu:
                if (zbtsmLocation !=null && note.getZbtsmLocation() == null){
                    Toast.makeText(this, "Please save the note before trying to view the map", Toast.LENGTH_SHORT).show();
                }else if (note.getZbtsmLocation() != null){
                    Bundle b = new Bundle();
                    b.putInt("id", note.getId());
                    intent = new Intent(this, MapsActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "There is no location to view", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void getAccessLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You wont be able to store the location without giving permission", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "You wont be able to store the location without giving permission", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ZBTSM_PERMISSIONS_REQUEST_READ_LOCATION);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ZBTSM_PERMISSIONS_REQUEST_READ_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            locationPermissionGranded = true;
            if (zbtsmLocation == null) {
                Toast.makeText(this, "...trying to retrieve location", Toast.LENGTH_SHORT).show();
                getAndSaveLocation();
            } else {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                getAndSaveLocation();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AddNote.this);
                builder.setMessage("There is a location stored already. Would you like to over write existing location?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }

        }
    }


    private void getAndSaveLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(locationPermissionGranded){
                Task locationTask = mFusedLocationClient.getLastLocation();
                locationTask.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location location = (Location)task.getResult();
                            Log.i("getLocation - >", "onComplete Location Found");
                            if (location != null) {
                                Toast.makeText(AddNote.this , "Locations has been retrieved successfully", Toast.LENGTH_SHORT).show();
                                zbtsmLocation = new ZbtsmLocation( location.getLatitude(),  location.getLongitude());
                            } else {
                                Toast.makeText(AddNote.this , "Cannot retrieve location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i("getLocation - >", "onComplete Could NOT find Location");
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e("getLocationSecurityExc", e.getMessage());
        }
    }
}
