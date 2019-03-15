package com.obj.dao;

import com.obj.beans.Films;

public interface FilmDao {

	void creer(Films film) throws DAOException;

	Films trouver(String titre) throws DAOException;

}