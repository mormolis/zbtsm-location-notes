package com.example.georgioslamprakis.zboutsam.activities.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.georgioslamprakis.zboutsam.R;
import com.example.georgioslamprakis.zboutsam.database.entities.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgioslamprakis on 15/03/2018.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    Context context;
    List<Note> noteList;

    public NoteAdapter(@NonNull Context context,  ArrayList<Note> list) {
        super(context, 0 , list);
        this.context = context;
        this.noteList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.notelist_item, parent,false);

        Note currentNote = noteList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.textView_Title);
        title.setText(currentNote.getTitle());

        TextView text = (TextView) listItem.findViewById(R.id.textView_note);
        text.setText(currentNote.getText());

        TextView category = (TextView) listItem.findViewById(R.id.textView_category);
        category.setText(Integer.toString(currentNote.getCategoryId()));

        return listItem;
    }

}
