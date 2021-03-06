package com.example.georgioslamprakis.zboutsam.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.example.georgioslamprakis.zboutsam.ZbtsmApp;
import com.example.georgioslamprakis.zboutsam.database.daos.CategoryDao;
import com.example.georgioslamprakis.zboutsam.database.daos.NoteDao;
import com.example.georgioslamprakis.zboutsam.database.entities.Category;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;
import com.example.georgioslamprakis.zboutsam.database.entities.helpers.Converters;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */
//TODO: set export schema to true and create a json file representiong the schema
@Database(entities = {Category.class, Note.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ZboutsamDB extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract CategoryDao categoryDao();



}


