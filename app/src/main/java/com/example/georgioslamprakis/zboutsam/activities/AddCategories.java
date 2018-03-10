package com.example.georgioslamprakis.zboutsam.activities;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.georgioslamprakis.zboutsam.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCategories extends AppCompatActivity {

    List<String> categories;
    EditText editText;
    Button button;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        categories = populateCategoriesListFromDB();
        editText = (EditText) findViewById(R.id.editTextAddCategory);
        button = (Button) findViewById(R.id.buttonAddCategory);
        listView = (ListView) findViewById(R.id.listViewCategory);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(arrayAdapter);

        button.setOnClickListener( new View.OnClickListener() {
            String newCategory;
            @Override
            public void onClick(View v) {
                newCategory = editText.getText().toString();
                editText.setText("");
                if (!newCategory.equals("")) arrayAdapter.add(newCategory);
                insertItem(newCategory);
            }
        });
    }

    private List<String> populateCategoriesListFromDB(){
        //TODO:update from db
        return new ArrayList<>(Arrays.asList("cat1", "cat2", "cat3"));
    }

    private void insertItem(String categoryTitle){
        //TODO: store category to db
    }

}
