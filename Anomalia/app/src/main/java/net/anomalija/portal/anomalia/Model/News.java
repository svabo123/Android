package net.anomalija.portal.anomalia.Model;


import java.io.Serializable;

public class News implements Serializable {

    //    ova klasa sluzi za modelovanje podataka koji se korsite za dobijanja novosti
    private int id;
    private String naslov;
    private String sadrzaj;
    private String autor;
    private String datum_objave;
    private String slika;


    public News(int id, String naslov, String sadrzaj, String autor, String slika, String datum_objave) {
        this.id = id;
        this.autor = autor;
        this.datum_objave = datum_objave;
        this.naslov = naslov;
        this.slika = slika;
        this.sadrzaj = sadrzaj;

    }


    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDatum_objave() {
        return datum_objave;
    }

    public void setDatum_objave(String datum_objave) {
        this.datum_objave = datum_objave;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " Autor: " + this.getAutor() + " Sadrzaj: " + this.getSadrzaj() + " Naslov: " + this.naslov + " SlikaUrl: " + this.getSlika();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

