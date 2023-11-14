package com.labo2films;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CategoryAdapterSpinner extends ArrayAdapter<Category> {

    List<Category> list;
    int mResource;
    Context context;

    public CategoryAdapterSpinner(@NonNull Context context, int resource, List<Category> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init(position, convertView, parent);
    }

    private View init(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        }
        TextView name = convertView.findViewById(R.id.text_spinner);

        if (getItem(position) != null) {
            name.setText(getItem(position).getName());
        }
        return convertView;
    }

}
