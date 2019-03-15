package com.obj.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.obj.beans.Films;
import com.obj.dao.DAOException;
import com.obj.dao.FilmDao;

public final class CreatForm {
	private static final String CHAMP_NUM_FILM = "numFilm";
	private static final String CHAMP_TITRE = "titre";
	private static final String CHAMP_DUREE = "duree";
	private static final String CHAMP_SORTIE = "sortie";
	private static final String CHAMP_ID_GENRE = "idGenre";

	// private static final String ALGO_CHIFFREMENT = "SHA-256";

	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private FilmDao filmDao;

	public CreatForm(FilmDao filmDao) {
		this.filmDao = filmDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}

	public Films creatFilm(HttpServletRequest request) {
		int numFilm = Integer.parseInt(getValeurChamp(request, CHAMP_NUM_FILM));
		String titre = getValeurChamp(request, CHAMP_TITRE);
		int duree = Integer.parseInt(getValeurChamp(request, CHAMP_DUREE));
		int sortie = Integer.parseInt(getValeurChamp(request, CHAMP_SORTIE));
		int idGenre = Integer.parseInt(getValeurChamp(request, CHAMP_ID_GENRE));

		Films film = new Films();
		try {
			traiterNumFilm(numFilm, film);
			traiterTitre(titre, film);

			film.setDuree(duree);
			film.setSortie(sortie);
			film.setIdGenre(idGenre);

			if (erreurs.isEmpty()) {
				filmDao.creer(film);
				resultat = "Succ�s de la cr�ation du film : ";
			} else {
				resultat = "Échec de l'inscription.";
			}
		} catch (DAOException e) {
			resultat = "Échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
			e.printStackTrace();
		}

		return film;
	}

	private void traiterNumFilm(int numFilm, Films film) {
		try {
			if (numFilm == 0) {
				throw new FormValidationException("Veuillez indiquer un numero diff�rent de 0.");
			}
		} catch (FormValidationException e) {
			setErreur(CHAMP_NUM_FILM, e.getMessage());
		}
		film.setNumFilm(numFilm);
	}

	/*
	 * Appel � la validation du nom reçu et initialisation de la propriété nom du
	 * bean
	 */
	private void traiterTitre(String titre, Films film) {
		try {
			if (titre == null) {
				throw new FormValidationException("Veuillez indiquer un titre pour le film.");
			}
		} catch (FormValidationException e) {
			setErreur(CHAMP_TITRE, e.getMessage());
		}
		film.setTitre(titre);
	}

	/*
	 * Appel � la validation de l'adresse email reçue et initialisation de la
	 * propriété email du bean
	 */
	/*
	 * private void traiterEmail(String email, Utilisateur utilisateur) { try {
	 * validationEmail(email); } catch (FormValidationException e) {
	 * setErreur(CHAMP_EMAIL, e.getMessage()); } utilisateur.setEmail(email); }
	 */

	/*
	 * Appel � la validation des mots de passe reçus, chiffrement du mot de passe
	 * et initialisation de la propriété motDePasse du bean
	 */
	/*
	 * private void traiterMotsDePasse( String motDePasse, String confirmation,
	 * Utilisateur utilisateur ) { try { validationMotsDePasse( motDePasse,
	 * confirmation ); } catch ( FormValidationException e ) { setErreur(
	 * CHAMP_PASS, e.getMessage() ); setErreur( CHAMP_CONF, null ); }
	 * 
	 * /* Utilisation de la bibliothèque Jasypt pour chiffrer le mot de passe
	 * efficacement.
	 * 
	 * L'algorithme SHA-256 est ici utilisé, avec par défaut un salage aléatoire
	 * et un grand nombre d'itérations de la fonction de hashage.
	 * 
	 * La String retournée est de longueur 56 et contient le hash en Base64.
	 */
	/*
	 * ConfigurablePasswordEncryptor passwordEncryptor = new
	 * ConfigurablePasswordEncryptor(); passwordEncryptor.setAlgorithm(
	 * ALGO_CHIFFREMENT ); passwordEncryptor.setPlainDigest( false ); String
	 * motDePasseChiffre = passwordEncryptor.encryptPassword( motDePasse );
	 * 
	 * utilisateur.setMotDePasse( motDePasseChiffre ); }
	 */

	/* Validation de l'adresse email */

	/*
	 * private void validationEmail(String email) throws FormValidationException {
	 * if (email != null) { if
	 * (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) { throw new
	 * FormValidationException("Merci de saisir une adresse mail valide."); } else
	 * if (utilisateurDao.trouver(email) != null) { throw new
	 * FormValidationException(
	 * "Cette adresse email est déj�  utilisée, merci d'en choisir une autre."); }
	 * } else { throw new
	 * FormValidationException("Merci de saisir une adresse mail."); } }
	 */

	/* Validation des mots de passe */
	/*
	 * private void validationMotsDePasse(String motDePasse, String confirmation)
	 * throws FormValidationException { if (motDePasse != null && confirmation !=
	 * null) { if (!motDePasse.equals(confirmation)) { throw new
	 * FormValidationException(
	 * "Les mots de passe entrés sont différents, merci de les saisir �  nouveau."
	 * ); } else if (motDePasse.length() < 3) { throw new
	 * FormValidationException("Les mots de passe doivent contenir au moins 3 caractères."
	 * ); } } else { throw new
	 * FormValidationException("Merci de saisir et confirmer votre mot de passe.");
	 * } }
	 */

	/*
	 * Ajoute un message correspondant au champ spécifié � la map des erreurs.
	 */
	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/*
	 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
	 * sinon.
	 */
	private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
		String valeur = request.getParameter(nomChamp);
		if (valeur == null || valeur.trim().length() == 0) {
			return null;
		} else {
			return valeur;
		}
	}
}