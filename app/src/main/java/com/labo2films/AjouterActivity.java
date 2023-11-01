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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AjouterActivity extends AppCompatActivity {
    ArrayList<Film> listeFilms = new ArrayList<>();
    EditText num, title, categ, cote;
    RadioGroup lang;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Ajouter un film");

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
        categ = findViewById(R.id.editTextCateg);
        cote = findViewById(R.id.editTextCote);
        lang = findViewById(R.id.radioGroup);
        save = findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numText = num.getText().toString();
                String titleText = title.getText().toString();
                String categText = categ.getText().toString();
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

}