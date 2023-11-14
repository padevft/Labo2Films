package com.labo2films;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AjouterActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>();
    EditText num, title, cote;
    Spinner categorySpinner;

    String categ = "";

    RadioGroup lang;
    Button save;
    FilmDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Ajouter un film");

        db = new FilmDbHelper(this);

        Intent intent = getIntent();
        listeFilms = intent.getParcelableArrayListExtra("listeFilms");

        initView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        num = findViewById(R.id.editTextNum);
        title = findViewById(R.id.editTextTitre);
        categorySpinner = findViewById(R.id.spinnerCateg);
        cote = findViewById(R.id.editTextCote);
        lang = findViewById(R.id.radioGroup);
        save = findViewById(R.id.save);

        setCategorySpinner();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numText = num.getText().toString();
                String titleText = title.getText().toString();
                String categText = categ;
                int selectedRadioId = lang.getCheckedRadioButtonId();
                String coteText = cote.getText().toString();

                if (!numText.equals("") && !titleText.equals("") && !categText.equals("") && !coteText.equals("") && selectedRadioId != -1) {
                    if (!FilmUti.numeroExisteDansListe(Integer.parseInt(numText), listeFilms)) {
                        RadioButton selectedRadio = findViewById(selectedRadioId);
                        String langue = selectedRadio.getText().toString();
                        Film film = new Film(Integer.parseInt(numText), titleText, Integer.parseInt(categText), langue, Integer.parseInt(coteText));
                        listeFilms.add(film);
                        Toast.makeText(AjouterActivity.this, "Film ajoute", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putParcelableArrayListExtra("listeFilms", listeFilms);
                        setResult(1, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(AjouterActivity.this, "Le numero doit etre unique", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AjouterActivity.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setCategorySpinner() {
        ArrayList<Category> categories = db.getAllCategories();

        CategoryAdapterSpinner adapter = new CategoryAdapterSpinner(this, R.layout.simple_spinner_white, categories);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Category selectedCategory = (Category) parentView.getItemAtPosition(position);
                categ = String.valueOf(selectedCategory.getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(AjouterActivity.this, "Aucun code selectionne", Toast.LENGTH_SHORT).show();
            }
        });
    }

}