package com.labo2films;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Film implements Parcelable {
    private int num;
    private String titre;
    private int codeCateg;
    private String langue;
    private int cote;

    // Constructeur par défaut
    public Film() {
    }

    // Constructeur paramétré
    public Film(int num, String titre, int codeCateg, String langue, int cote) {
        this.num = num;
        this.titre = titre;
        this.codeCateg = codeCateg;
        this.langue = langue;
        this.cote = cote;
    }

    protected Film(Parcel in) {
        num = in.readInt();
        titre = in.readString();
        codeCateg = in.readInt();
        langue = in.readString();
        cote = in.readInt();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getCodeCateg() {
        return codeCateg;
    }

    public void setCodeCateg(int codeCateg) {
        this.codeCateg = codeCateg;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public int getCote() {
        return cote;
    }

    public void setCote(int cote) {
        this.cote = cote;
    }

    @NonNull
    @Override
    public String toString() {
        return "Film{" +
                "num=" + num +
                ", titre='" + titre + '\'' +
                ", codeCateg=" + codeCateg +
                ", langue='" + langue + '\'' +
                ", cote=" + cote +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(num);
        parcel.writeString(titre);
        parcel.writeInt(codeCateg);
        parcel.writeString(langue);
        parcel.writeInt(cote);
    }
}
