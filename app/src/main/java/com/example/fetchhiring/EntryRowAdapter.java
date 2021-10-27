package com.example.fetchhiring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EntryRowAdapter extends ArrayAdapter<EntryRow> {
    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    int mResource;


    public EntryRowAdapter(@NonNull Context context, int resource, @NonNull List<EntryRow> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get column values for the row
        String listId = getItem(position).getListId();
        String name = getItem(position).getName();
        String id = getItem(position).getId();

        // create the EntryRow object with the information
        EntryRow row = new EntryRow(listId, name, id);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvlistId = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvName = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvid = (TextView) convertView.findViewById(R.id.textView3);

        tvName.setText(name);
        tvlistId.setText(listId);
        tvid.setText(id);

        return convertView;
    }
}
