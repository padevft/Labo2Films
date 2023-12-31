package com.labo2films;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private final ArrayList<Film> films;

    public FilmAdapter(ArrayList<Film> films) {
        this.films = films;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = films.get(position);
        holder.textTitle.setText(film.getTitre());
        holder.num.setText(String.valueOf(film.getNum()));
        holder.code.setText(String.valueOf(film.getCodeCateg()));
        holder.lang.setText(String.valueOf(film.getLangue()));
        holder.cote.setText(String.valueOf(film.getCote()));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, num, code, lang, cote;

        public FilmViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.title);
            num = itemView.findViewById(R.id.num);
            code = itemView.findViewById(R.id.categ);
            lang = itemView.findViewById(R.id.lang);
            cote = itemView.findViewById(R.id.cote);
        }
    }
}

