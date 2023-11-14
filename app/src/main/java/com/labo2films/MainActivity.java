package com.labo2films;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>();
    RecyclerView recyclerView;
    FilmAdapter adapter;
    Spinner categorySpinner;

    FilmDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Films");


        recyclerView = findViewById(R.id.recycleView);
        categorySpinner = findViewById(R.id.spinnerCateg);

        db = new FilmDbHelper(this);
        listeFilms = db.getAllFilms();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                System.out.println(listeFilms);
                listFilms();
                return true;
            case R.id.action_add:
                launchAddFilm();
                return true;
            case R.id.action_list_categ:
                launchListCategFilm();
                return true;
            case R.id.action_delete:
                launchDeleteFilm();
                return true;
            case R.id.action_categs:
                launchCategories();
                return true;
            case R.id.action_add_categ:
                launchAddCategory();
                return true;
            case R.id.action_quit:
                saveFilmsToBD(listeFilms);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void notifyAdapter() {
        if (adapter == null) {
            listFilms();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listFilms() {
        findViewById(R.id.logo).setVisibility(View.GONE);
        findViewById(R.id.linearSpinner).setVisibility(View.GONE);
        findViewById(R.id.recycleView).setVisibility(View.VISIBLE);
        adapter = new FilmAdapter(MainActivity.this, listeFilms);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);

    }

    private void launchAddFilm() {
        Intent intent = new Intent(getApplicationContext(), AjouterActivity.class);
        intent.putParcelableArrayListExtra("listeFilms", listeFilms);
        getResult.launch(intent);
    }

    private void launchDeleteFilm() {
        Intent intent = new Intent(getApplicationContext(), SupprimerActivity.class);
        intent.putParcelableArrayListExtra("listeFilms", listeFilms);
        getResult.launch(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void launchListCategFilm() {
        findViewById(R.id.logo).setVisibility(View.GONE);
        findViewById(R.id.linearSpinner).setVisibility(View.VISIBLE);
        findViewById(R.id.recycleView).setVisibility(View.GONE);
        setCategorySpinner();
    }


    private void launchCategories() {
        Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(intent);
    }

    private void launchAddCategory() {
        Intent intent = new Intent(getApplicationContext(), AjouterCategorieActivity.class);
        startActivity(intent);
    }


    ActivityResultLauncher<Intent> getResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (result.getResultCode() == 1) {
                ArrayList<Film> listeFilmsAjoute = Objects.requireNonNull(data).getParcelableArrayListExtra("listeFilms");
                listeFilms.clear();
                listeFilms.addAll(listeFilmsAjoute);
                notifyAdapter();
            }
            if (result.getResultCode() == 2) {
                int countFrenchFilms = data.getIntExtra("countFrenchFilms", 0);
                int countEnglishFilms = data.getIntExtra("countEnglishFilms", 0);
                findViewById(R.id.logo).setVisibility(View.VISIBLE);
                findViewById(R.id.linearSpinner).setVisibility(View.GONE);
                findViewById(R.id.recycleView).setVisibility(View.GONE);
                findViewById(R.id.resultTextView).setVisibility(View.VISIBLE);
                TextView resultTextView = findViewById(R.id.resultTextView);
                resultTextView.setText("Films français : " + countFrenchFilms + ", Films anglais : " + countEnglishFilms);
            }
        }
    });


    private void saveFilmsToBD(ArrayList<Film> films) {
        db.addFilmList(films);
        Toast.makeText(this, "Les films ont été enregistrés avec succès.", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void setCategorySpinner() {
        categorySpinner = findViewById(R.id.spinnerCateg);
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(0,"Selectionnez une categorie"));
        categories.addAll(db.getAllCategories());
        CategoryAdapterSpinner adapter = new CategoryAdapterSpinner(this, R.layout.simple_spinner_white, categories);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {
                    Category selectedCategory = (Category) parentView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, ListerParCategorieActivity.class);
                    intent.putExtra("selectedCategory", String.valueOf(selectedCategory.getCode()));
                    intent.putParcelableArrayListExtra("listeFilms", listeFilms);
                    getResult.launch(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(MainActivity.this, "Aucun code selectionne", Toast.LENGTH_SHORT).show();
            }
        });
    }

}