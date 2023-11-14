package com.labo2films;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FilmDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bdfilms.db";


    public FilmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FilmContract.CategoryEntry.SQL_CREATE_ENTRIES);
        db.execSQL(FilmContract.FilmEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FilmContract.CategoryEntry.SQL_DELETE_ENTRIES);
        db.execSQL(FilmContract.FilmEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilmContract.FilmEntry.COLUMN_NAME_TITLE, film.getTitre());
        values.put(FilmContract.FilmEntry.COLUMN_NAME_CATEGORY_CODE, film.getCodeCateg());
        values.put(FilmContract.FilmEntry.COLUMN_NAME_LANGUAGE, film.getLangue());
        values.put(FilmContract.FilmEntry.COLUMN_NAME_COTE, film.getCote());
        long newRowId = db.insert(FilmContract.FilmEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFilm(long filmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FilmContract.FilmEntry.TABLE_NAME,
                FilmContract.FilmEntry.COLUMN_NAME_NUM + " = ?",
                new String[]{String.valueOf(filmId)});
        db.close();
    }


    @SuppressLint("Range")
    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> filmList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + FilmContract.FilmEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film();
                film.setNum(cursor.getInt(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_NAME_NUM)));
                film.setTitre(cursor.getString(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_NAME_TITLE)));
                film.setCodeCateg(cursor.getInt(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_NAME_CATEGORY_CODE)));
                film.setLangue(cursor.getString(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_NAME_LANGUAGE)));
                film.setCote(cursor.getInt(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_NAME_COTE)));
                filmList.add(film);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return filmList;
    }

    private boolean filmExist(int numFilm) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * " +
                " FROM " + FilmContract.FilmEntry.TABLE_NAME +
                " WHERE " + FilmContract.FilmEntry.COLUMN_NAME_NUM + " = ?";

        String[] selectionArgs = {String.valueOf(numFilm)};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }


    @SuppressLint("Range")
    public Category getCategory(int code) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * " +
                " FROM " + FilmContract.CategoryEntry.TABLE_NAME +
                " WHERE " + FilmContract.CategoryEntry.COLUMN_NAME_CODE + " = ?";

        String[] selectionArgs = {String.valueOf(code)};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        Category category = new Category();
        if (cursor != null && cursor.moveToFirst()) {
            category.setCode(cursor.getInt(cursor.getColumnIndex(FilmContract.CategoryEntry.COLUMN_NAME_CODE)));
            category.setName(cursor.getString(cursor.getColumnIndex(FilmContract.CategoryEntry.COLUMN_NAME_NAME)));
            cursor.close();
        }
        return category;
    }

    public void addFilmList(ArrayList<Film> films) {
        for (Film film : films) {
            if (!filmExist(film.getNum())) {
                addFilm(film);
            }
        }
    }


    @SuppressLint("Range")
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categoryList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + FilmContract.CategoryEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCode(cursor.getInt(cursor.getColumnIndex(FilmContract.CategoryEntry.COLUMN_NAME_CODE)));
                category.setName(cursor.getString(cursor.getColumnIndex(FilmContract.CategoryEntry.COLUMN_NAME_NAME)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public long addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(FilmContract.CategoryEntry.COLUMN_NAME_CODE, category.getCode());
        values.put(FilmContract.CategoryEntry.COLUMN_NAME_NAME, category.getName());
        long newRowId = db.insert(FilmContract.CategoryEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }


}
