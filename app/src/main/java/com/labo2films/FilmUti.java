package com.labo2films;

import java.util.ArrayList;

public class FilmUti {
    public static boolean numeroExisteDansListe(int numero, ArrayList<Film> listeFilms) {
        for (Film film : listeFilms) {
            if (film.getNum() == numero) {
                return true;
            }
        }
        return false;
    }
}
