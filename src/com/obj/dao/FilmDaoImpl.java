package com.obj.dao;

import static com.obj.dao.DaoUtilitaire.fermeturesSilencieuses;
import static com.obj.dao.DaoUtilitaire.initialisationRequetePreparee;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.obj.beans.Films;

public class FilmDaoImpl implements FilmDao {

	private DaoFactory daoFactory;
	private static final String SQL_SELECT_PAR_TITRE = "SELECT * FROM Films WHERE titre = ?";
	private static final String SQL_INSERT = "INSERT INTO Films (num_film, titre, duree, sortie, id_genre) VALUES (?, ?, ?, ?, ?)";

	FilmDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/*
	 * Simple méthode utilitaire permettant de faire la correspondance (le mapping)
	 * entre une ligne issue de la table des utilisateurs (un ResultSet) et un bean
	 * Utilisateur.
	 */
	private static Films map(ResultSet resultSet) throws SQLException {
		Films films = new Films();
		films.setIdFilm(resultSet.getLong("id_film"));
		films.setNumFilm(resultSet.getInt("num_film"));
		films.setTitre(resultSet.getString("titre"));
		films.setDuree(resultSet.getInt("duree"));
		films.setSortie(resultSet.getInt("sortie"));
		films.setIdGenre(resultSet.getInt("id_genre"));
		return films;
	}

	/*
	 * Implémentation de la méthode trouver() définie dans l'interface
	 * UtilisateurDao
	 */
	@Override
	public Films trouver(String titre) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Films films = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = (Connection) daoFactory.getConnection();
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(connexion, SQL_SELECT_PAR_TITRE,
					false, titre);
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if (resultSet.next()) {
				films = map(resultSet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(resultSet, preparedStatement, connexion);
		}

		return films;
	}

	/*
	 * Implémentation de la méthode creer() définie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Films film) throws IllegalArgumentException, DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = (Connection) daoFactory.getConnection();
			preparedStatement = (PreparedStatement) initialisationRequetePreparee(connexion, SQL_INSERT, true,
					film.getNumFilm(), film.getTitre(), film.getDuree(), film.getSortie(), film.getIdGenre());
			int statut = preparedStatement.executeUpdate();
			/* Analyse du statut retourné par la requête d'insertion */
			if (statut == 0) {
				throw new DAOException("Échec de la création du film, aucune ligne ajoutée dans la table.");
			}
			/* Récupération de l'id auto-généré par la requête d'insertion */
			valeursAutoGenerees = preparedStatement.getGeneratedKeys();
			if (valeursAutoGenerees.next()) {
				/* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
				film.setIdFilm(valeursAutoGenerees.getLong(1));
			} else {
				throw new DAOException("Échec de la création du film en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
		}
	}
}