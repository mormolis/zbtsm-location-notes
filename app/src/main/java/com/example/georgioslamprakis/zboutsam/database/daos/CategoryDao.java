package com.example.georgioslamprakis.zboutsam.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.georgioslamprakis.zboutsam.database.entities.Category;

import java.util.List;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Update
    void update(Category... category);


    @Delete
    void delete(Category... category);


    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category WHERE id=:id")
    List<Category> findCategoryFromId(final int id);

    @Query("SELECT id FROM category WHERE title=:title")
    int findIdByCategoryTitle(String title);

    @Query("DELETE FROM category WHERE title = :title")
    void deleteByTitle(String title);



}