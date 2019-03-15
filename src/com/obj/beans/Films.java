package com.obj.beans;

public class Films {

	private long idFilm;
	private int numFilm;
	private String titre;
	private int duree;
	private int sortie;
	private int idGenre;
	private int test;

	public long getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(long id) {
		this.idFilm = id;
	}

	public int getNumFilm() {
		return numFilm;
	}

	public void setNumFilm(int numF) {
		this.numFilm = numF;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String t) {
		this.titre = t;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int d) {
		this.duree = d;
	}

	public int getSortie() {
		return sortie;
	}

	public void setSortie(int s) {
		this.sortie = s;
	}

	public int getIdGenre() {
		return idGenre;
	}

	public void setIdGenre(int idG) {
		this.idGenre = idG;
	}
}