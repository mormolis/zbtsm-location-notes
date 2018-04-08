package com.example.georgioslamprakis.zboutsam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.georgioslamprakis.zboutsam.R;

/**
 * Created by georgioslamprakis on 08/04/2018.
 */

public class ZbtsmActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.allNotesMenu:
                intent = new Intent(this, NotesList.class);
                startActivity(intent);
                return true;
            case R.id.categoriesMenu:
                intent = new Intent(this, AddCategories.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
