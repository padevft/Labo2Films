package com.labo2films;

import android.provider.BaseColumns;

public final class FilmContract {
    private FilmContract() {}

    public static class FilmEntry implements BaseColumns {
        public static final String TABLE_NAME = "films";
        public static final String COLUMN_NAME_NUM = "num";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CATEGORY_CODE = "category_code";
        public static final String COLUMN_NAME_LANGUAGE = "language";
        public static final String COLUMN_NAME_COTE = "cote";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NUM + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_TITLE + " TEXT," +
                        COLUMN_NAME_CATEGORY_CODE + " INTEGER," +
                        COLUMN_NAME_LANGUAGE + " TEXT," +
                        COLUMN_NAME_COTE + " INTEGER," +
                        "FOREIGN KEY (" + COLUMN_NAME_CATEGORY_CODE + ") REFERENCES " +
                        CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_NAME_CODE + "))";
       public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FilmContract.FilmEntry.TABLE_NAME;

    }

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME_NAME + " TEXT)";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

