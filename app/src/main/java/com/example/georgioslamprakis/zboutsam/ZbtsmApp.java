package com.example.georgioslamprakis.zboutsam;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.georgioslamprakis.zboutsam.database.ZboutsamDB;

import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.List;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */

public class ZbtsmApp extends Application {
    public static ZbtsmApp INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";
    private ZboutsamDB database;
    private static final String KEY_FORCE_UPDATE = "force_update";
    private static final String PREFERENCES = "ZBOUTSAM.preferences";


    public static ZbtsmApp get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        Log.i("from application class", "IRHIXA BREEE!");

        database = Room.databaseBuilder(getApplicationContext(), ZboutsamDB.class, DATABASE_NAME)
                .build();

        INSTANCE = this;


    }

    public ZboutsamDB getDB() {
        return database;
    }

    public boolean isForceUpdate() {
        return getSP().getBoolean(KEY_FORCE_UPDATE, true);
    }

    public void setForceUpdate(boolean force) {
        SharedPreferences.Editor edit = getSP().edit();
        edit.putBoolean(KEY_FORCE_UPDATE, force);
        edit.apply();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }
}
