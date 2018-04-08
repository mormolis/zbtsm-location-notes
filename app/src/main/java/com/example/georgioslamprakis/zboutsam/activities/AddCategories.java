package com.example.georgioslamprakis.zboutsam.activities;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.database.daos.CategoryDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Category;
import com.example.georgioslamprakis.zboutsam.helpers.AccessDB;

import java.util.ArrayList;
import java.util.List;

public class AddCategories extends ZbtsmActivity {
    EditText editText;
    Button button;
    ListView listView;
    CategoryDao categoryDao;
    final List<String> titles =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);;

        categoryDao = ZbtsmApp.get().getDB().categoryDao();
        setContentView(R.layout.activity_add_category);
        editText = findViewById(R.id.editTextAddCategory);
        button = findViewById(R.id.buttonAddCategory);
        listView = findViewById(R.id.listViewCategory);


        arrayAdapter.addAll(AccessDB.getAllCategories());
        listView.setAdapter(arrayAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AccessDB.deleteCategoryByTitle(arrayAdapter.getItem(position));
                                arrayAdapter.clear();
                                arrayAdapter.addAll(AccessDB.getAllCategories());
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AddCategories.this);
                builder.setMessage("Delete this category? All Notes assosiated with this category will be removed as well.").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryTitleSelectedFromList = listView.getItemAtPosition(position).toString();

                Intent intent = new Intent(AddCategories.this, NotesList.class);
                Bundle b = new Bundle();

                b.putInt("id", AccessDB.findCategoryIdByCategoryTitle(categoryTitleSelectedFromList));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        button.setOnClickListener( new View.OnClickListener() {
            String newCategory;
            @Override
            public void onClick(View v) {
                newCategory = editText.getText().toString();
                if (!newCategory.equals("") && !titles.contains(newCategory)){
                    insertItem(newCategory);
                } else {
                    displayToastMessage(newCategory);
                }
                editText.setText("");

            }
        });
    }


    private void insertItem(final String categoryTitle){
        final Category category = new Category();
        category.setTitle(categoryTitle);
        titles.add(categoryTitle);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("insertItem Thread", "I am in!!!!!!!!");
                categoryDao.insert(category);
            }
        }).start();
    }

    private void displayToastMessage(String categoryTitle){
        Context context = getApplicationContext();
        CharSequence text;
        text = categoryTitle.equals("") ? "you cannot give empty category" : categoryTitle+ " already exists!";
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
