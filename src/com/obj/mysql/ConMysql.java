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
			messages.add("Driver chargé !");
		} catch (ClassNotFoundException e) {
			messages.add("Erreur lors du chargement : le driver n'a pas été trouvé dans le classpath ! <br/>"
					+ e.getMessage());
		}

		/* Connexion à la base de données */
		String url = "jdbc:mysql://localhost:3306/cinema";
		String utilisateur = "root";
		String motDePasse = "root";
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultat = null;
		try {
			messages.add("Connexion à la base de données...");
			connexion = (Connection) DriverManager.getConnection(url, utilisateur, motDePasse);
			messages.add("Connexion réussie !");

			/* Création de l'objet gérant les requêtes */
			statement = (Statement) connexion.createStatement();
			messages.add("Objet requête créé !");

			/* Récupération des paramètres d'URL saisis par l'utilisateur */
			String numFilm = request.getParameter("numFilm");
			String titre = request.getParameter("titre");
			String duree = request.getParameter("duree");
			String sortie = request.getParameter("sortie");
			String id_genre = request.getParameter("id_genre");

			/* Exécution d'une requête d'écriture */
			if (numFilm != null && titre != null && duree != null && sortie != null && id_genre != null) {
				int statut = statement.executeUpdate(
						"INSERT INTO films (num_Film, titre, duree, sortie, id_Genre) VALUES ('" + numFilm + "', '"
								+ titre + "', '" + duree + "', '" + sortie + "', '" + id_genre + "');",
						Statement.RETURN_GENERATED_KEYS);

				/* Formatage pour affichage dans la JSP finale. */
				messages.add("Résultat de la requête d'insertion : " + statut + ".");

				/* Récupération de l'id auto-généré par la requête d'insertion. */
				resultat = statement.getGeneratedKeys();
				/*
				 * Parcours du ResultSet et formatage pour affichage de la valeur qu'il contient
				 * dans la JSP finale.
				 */
				while (resultat.next()) {
					messages.add("ID retourné lors de la requête d'insertion :" + resultat.getInt(1));
				}
			}

			/* Exécution d'une requête de lecture */
			resultat = statement.executeQuery("select * from films;");
			messages.add("Requête \"select * from films;\" effectuée !");

			/* Récupération des données du résultat de la requête de lecture */
			while (resultat.next()) {
				int idFilm = resultat.getInt("id_film");
				String titreFilm = resultat.getString("titre");

				/* Formatage des données pour affichage dans la JSP finale. */
				messages.add("Données retournées par la requête : id = " + idFilm + ", titre = " + titreFilm);
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