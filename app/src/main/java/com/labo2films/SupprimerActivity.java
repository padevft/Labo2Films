package com.labo2films;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class SupprimerActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>();
    EditText numEdiText;
    Button delete;

    FilmDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        db = new FilmDbHelper(this);

        Intent intent = getIntent();
        listeFilms = intent.getParcelableArrayListExtra("listeFilms");


        numEdiText = findViewById(R.id.editTextNum);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclikDelete();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onclikDelete() {
        String numText = numEdiText.getText().toString();
        if (!numText.equals("")) {
            int num = Integer.parseInt(numText);
            if (FilmUti.numeroExisteDansListe(num, listeFilms)) {
                for (Film film : listeFilms) {
                    if (film.getNum() == num) {
                        db.deleteFilm(num);
                        listeFilms.remove(film);
                        break;
                    }
                }
                Toast.makeText(SupprimerActivity.this, "Film supprime", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra("listeFilms", listeFilms);
                setResult(1, resultIntent);
                finish();
            } else {
                Toast.makeText(SupprimerActivity.this, "Ce numero n'existe pas", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SupprimerActivity.this, "Entrez un numero", Toast.LENGTH_SHORT).show();

        }
    }
}