package com.obj.mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ConMysql {

	static List<String> messages = new ArrayList<String>();

	public List<String> executerTests(HttpServletRequest request) {
		/* Chargement du driver JDBC pour MySQL */
		try {
			messages.add("Chargement du driver...");
			Class.forName("com.mysql.jdbc.Driver");
			messages.add("Driver charg� !");
		} catch (ClassNotFoundException e) {
			messages.add("Erreur lors du chargement : le driver n'a pas �t� trouv� dans le classpath ! <br/>"
					+ e.getMessage());
		}

		/* Connexion � la base de donn�es */
		String url = "jdbc:mysql://localhost:3306/cinema";
		String utilisateur = "root";
		String motDePasse = "root";
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultat = null;
		try {
			messages.add("Connexion � la base de donn�es...");
			connexion = (Connection) DriverManager.getConnection(url, utilisateur, motDePasse);
			messages.add("Connexion r�ussie !");

			/* Cr�ation de l'objet g�rant les requ�tes */
			statement = (Statement) connexion.createStatement();
			messages.add("Objet requ�te cr�� !");

			/* R�cup�ration des param�tres d'URL saisis par l'utilisateur */
			String numFilm = request.getParameter("numFilm");
			String titre = request.getParameter("titre");
			String duree = request.getParameter("duree");
			String sortie = request.getParameter("sortie");
			String id_genre = request.getParameter("id_genre");

			/* Ex�cution d'une requ�te d'�criture */
			if (numFilm != null && titre != null && duree != null && sortie != null && id_genre != null) {
				int statut = statement.executeUpdate(
						"INSERT INTO films (num_Film, titre, duree, sortie, id_Genre) VALUES ('" + numFilm + "', '"
								+ titre + "', '" + duree + "', '" + sortie + "', '" + id_genre + "');",
						Statement.RETURN_GENERATED_KEYS);

				/* Formatage pour affichage dans la JSP finale. */
				messages.add("R�sultat de la requ�te d'insertion : " + statut + ".");

				/* R�cup�ration de l'id auto-g�n�r� par la requ�te d'insertion. */
				resultat = statement.getGeneratedKeys();
				/*
				 * Parcours du ResultSet et formatage pour affichage de la valeur qu'il contient
				 * dans la JSP finale.
				 */
				while (resultat.next()) {
					messages.add("ID retourn� lors de la requ�te d'insertion :" + resultat.getInt(1));
				}
			}

			/* Ex�cution d'une requ�te de lecture */
			resultat = statement.executeQuery("select * from films;");
			messages.add("Requ�te \"select * from films;\" effectu�e !");

			/* R�cup�ration des donn�es du r�sultat de la requ�te de lecture */
			while (resultat.next()) {
				int idFilm = resultat.getInt("id_film");
				String titreFilm = resultat.getString("titre");

				/* Formatage des donn�es pour affichage dans la JSP finale. */
				messages.add("Donn�es retourn�es par la requ�te : id = " + idFilm + ", titre = " + titreFilm);
			}
		} catch (SQLException e) {
			messages.add("Erreur lors de la connexion : <br/>" + e.getMessage());
		} finally {
			messages.add("Fermeture de l'objet ResultSet.");
			if (resultat != null) {
				try {
					resultat.close();
				} catch (SQLException ignore) {
				}
			}
			messages.add("Fermeture de l'objet Statement.");
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
			}
			messages.add("Fermeture de l'objet Connection.");
			if (connexion != null) {
				try {
					connexion.close();
				} catch (SQLException ignore) {
				}
			}
		}
		return messages;
	}

}