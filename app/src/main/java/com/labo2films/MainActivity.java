package com.labo2films;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>();
    RecyclerView recyclerView;
    FilmAdapter adapter;
    Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Films");


        recyclerView = findViewById(R.id.recycleView);
        categorySpinner = findViewById(R.id.spinnerCateg);


        listeFilms = loadFilmsFromFile();

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
            case R.id.action_quit:
                saveFilmsToFile(listeFilms);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Film> loadFilmsFromFile() {
        ArrayList<Film> films = new ArrayList<>();
        //InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.films);
        try {
            FileInputStream fis = openFileInput("films.txt");
            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 5) {
                        int num = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        int category = Integer.parseInt(parts[2]);
                        String language = parts[3];
                        int rating = Integer.parseInt(parts[4]);

                        Film film = new Film(num, title, category, language, rating);
                        films.add(film);
                    }
                }
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return films;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listFilms() {
        findViewById(R.id.logo).setVisibility(View.GONE);
        findViewById(R.id.linearSpinner).setVisibility(View.GONE);
        findViewById(R.id.recycleView).setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new FilmAdapter(listeFilms);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

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


    ActivityResultLauncher<Intent> getResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (result.getResultCode() == 1) {
                ArrayList<Film> listeFilmsAjoute = Objects.requireNonNull(data).getParcelableArrayListExtra("listeFilms");
                listeFilms.clear();
                listeFilms.addAll(listeFilmsAjoute);
                adapter.notifyDataSetChanged();
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

    private void saveFilmsToFile(ArrayList<Film> films) {
        try {
            FileOutputStream fos = openFileOutput("films.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (Film film : films) {
                String filmData = film.getNum() + ";" + film.getTitre() + ";" + film.getCodeCateg() + ";" + film.getLangue() + ";" + film.getCote();
                osw.write(filmData + "\n");
            }
            osw.close();
            fos.close();
            Toast.makeText(this, "Les films ont été enregistrés avec succès.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getUniqueCategories(ArrayList<Film> films) {
        ArrayList<String> categories = new ArrayList<>();

        for (Film film : films) {
            if (!categories.contains(String.valueOf(film.getCodeCateg()))) {
                categories.add(String.valueOf(film.getCodeCateg()));
            }
        }
        return categories;
    }

    public void setCategorySpinner() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Selectionner le code de la categorie");
        categories.addAll(getUniqueCategories(listeFilms));
        categorySpinner = findViewById(R.id.spinnerCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {
                    String selectedCategory = categories.get(position);
                    Intent intent = new Intent(MainActivity.this, ListerParCategorieActivity.class);
                    intent.putExtra("selectedCategory", selectedCategory);
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