package com.labo2films;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

public class ListerParCategorieActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>(), filmsFiltres = new ArrayList<>();
    RecyclerView recyclerView;
    FilmAdapter adapter;
    String selectedCategory = "";
    int countFrenchFilms = 0, countEnglishFilms = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister_par_categorie);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        selectedCategory = getIntent().getStringExtra("selectedCategory");
        listeFilms = getIntent().getParcelableArrayListExtra("listeFilms");
        filmsFiltres = getListeFilms();
        setTitle("Films De Categorie " + selectedCategory);

        recyclerView = findViewById(R.id.recycleView);
        listViewFilms();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backMain();
        }
        return super.onOptionsItemSelected(item);
    }


    public ArrayList<Film> getListeFilms() {
        ArrayList<Film> filmsFiltres = new ArrayList<>();
        for (Film film : listeFilms) {
            if (film.getCodeCateg() == Integer.parseInt(selectedCategory)) {
                filmsFiltres.add(film);
            }
        }
        return filmsFiltres;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listViewFilms() {
        if (adapter == null) {
            adapter = new FilmAdapter(ListerParCategorieActivity.this,filmsFiltres);
            recyclerView.setLayoutManager(new LinearLayoutManager(ListerParCategorieActivity.this));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void backMain(){
        for (Film film : filmsFiltres) {
            if (film.getLangue().equals("FR")) {
                countFrenchFilms++;
            } else if (film.getLangue().equals("AN")) {
                countEnglishFilms++;
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("countFrenchFilms", countFrenchFilms);
        resultIntent.putExtra("countEnglishFilms", countEnglishFilms);
        setResult(2, resultIntent);
        finish();
    }

}