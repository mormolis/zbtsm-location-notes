package com.example.georgioslamprakis.zboutsam.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.database.daos.CategoryDao;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Category;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */
@Database(entities = {Category.class, Note.class}, version = 1)
public abstract class ZboutsamDB extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract CategoryDao categoryDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE product "
                    + " ADD COLUMN price INTEGER");

            // enable flag to force update products
            ZbtsmApp.get().setForceUpdate(true);
        }
    };

}


