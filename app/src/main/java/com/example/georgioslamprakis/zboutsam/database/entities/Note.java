package com.example.georgioslamprakis.zboutsam.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.georgioslamprakis.zboutsam.database.entities.helpers.ZbtsmLocation;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by georgioslamprakis on 10/03/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = CASCADE))
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoryId")
    private int categoryId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "zbtsmLocation")
    private ZbtsmLocation zbtsmLocation;

    public ZbtsmLocation getZbtsmLocation() {
        return zbtsmLocation;
    }

    public void setZbtsmLocation(ZbtsmLocation zbtsmLocation) {
        this.zbtsmLocation = zbtsmLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSummary(){
        return text.length() > 50 ? text.substring(0, 49) + " ..." : text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (id != note.id) return false;
        if (categoryId != note.categoryId) return false;
        if (title != null ? !title.equals(note.title) : note.title != null) return false;
        if (text != null ? !text.equals(note.text) : note.text != null) return false;
        return zbtsmLocation != null ? zbtsmLocation.equals(note.zbtsmLocation) : note.zbtsmLocation == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + categoryId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (zbtsmLocation != null ? zbtsmLocation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", zbtsmLocation=" + zbtsmLocation +
                '}';
    }
}
