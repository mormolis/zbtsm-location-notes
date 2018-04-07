package com.example.georgioslamprakis.zboutsam.activities.adapters;

import android.content.Context;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by georgioslamprakis on 15/03/2018.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    private Context context;
    private List<Note> noteList;


    private Map<Integer, Integer> positionToID;

    public NoteAdapter(@NonNull Context context,  ArrayList<Note> list) {
        super(context, 0 , list);
        this.context = context;
        this.noteList = list;
        this.positionToID = new HashMap<>();
    }


    public Map<Integer, Integer> getPositionToID() {
        return positionToID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.notelist_item, parent,false);

        Note currentNote = noteList.get(position);

        positionToID.put(position, currentNote.getId());

        TextView title = listItem.findViewById(R.id.textView_Title);
        title.setText(currentNote.getTitle());

        TextView text = listItem.findViewById(R.id.textView_note);
        text.setText(currentNote.getSummary());

        TextView category = listItem.findViewById(R.id.textView_category);
        category.setText(Integer.toString(currentNote.getCategoryId()));

        return listItem;
    }

}
