package com.labo2films;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class AjouterCategorieActivity extends AppCompatActivity {
    EditText nomEdiText;
    Button add;

    FilmDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        db = new FilmDbHelper(this);

        nomEdiText = findViewById(R.id.editTextNom);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
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

    public void addCategory() {
        String nom = nomEdiText.getText().toString();
        if (!nom.equals("")) {
            Category category = new Category(0, nom);
            db.addCategory(category);
            Toast.makeText(AjouterCategorieActivity.this, "Categorie ajoute", Toast.LENGTH_SHORT).show();
            nomEdiText.setText("");
        } else {
            Toast.makeText(AjouterCategorieActivity.this, "Entrez un numero", Toast.LENGTH_SHORT).show();

        }
    }
}