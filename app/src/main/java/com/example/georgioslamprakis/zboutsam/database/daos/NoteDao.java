package com.example.georgioslamprakis.zboutsam.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.List;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note... notes);

    @Delete
    void delete(Note... notes);

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE categoryId=:categoryId")
    List<Note> findNotesFromCategoryId(final int categoryId);
}

