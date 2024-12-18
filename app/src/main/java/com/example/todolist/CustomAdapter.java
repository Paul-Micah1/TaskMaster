package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> items;

    public CustomAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView itemText = convertView.findViewById(R.id.item_text);
        Button removeButton = convertView.findViewById(R.id.remove_button);
        Button updateButton = convertView.findViewById(R.id.update_button); // Update Button

        itemText.setText(items.get(position));

        removeButton.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
        });

        updateButton.setOnClickListener(v -> {
            EditText input = new EditText(context);
            input.setText(items.get(position));

            new AlertDialog.Builder(context)
                    .setTitle("Update Item")
                    .setView(input)
                    .setPositiveButton("Update", (dialog, which) -> {
                        String updatedText = input.getText().toString();
                        if (!updatedText.isEmpty()) {
                            items.set(position, updatedText);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Text cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }
}