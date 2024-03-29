package com.obj.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.obj.beans.Client;
import com.obj.beans.Commande;
import com.obj.dao.ClientDao;
import com.obj.dao.CommandeDao;
import com.obj.dao.DAOException;

public final class CreationCommandeForm {
	private static final String CHAMP_CHOIX_CLIENT = "choixNouveauClient";
	private static final String CHAMP_LISTE_CLIENTS = "listeClients";
	private static final String CHAMP_DATE = "dateCommande";
	private static final String CHAMP_MONTANT = "montantCommande";
	private static final String CHAMP_MODE_PAIEMENT = "modePaiementCommande";
	private static final String CHAMP_STATUT_PAIEMENT = "statutPaiementCommande";
	private static final String CHAMP_MODE_LIVRAISON = "modeLivraisonCommande";
	private static final String CHAMP_STATUT_LIVRAISON = "statutLivraisonCommande";

	private static final String ANCIEN_CLIENT = "ancienClient";
	private static final String SESSION_CLIENTS = "clients";
	private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";

	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private ClientDao clientDao;
	private CommandeDao commandeDao;

	public CreationCommandeForm(ClientDao clientDao, CommandeDao commandeDao) {
		this.clientDao = clientDao;
		this.commandeDao = commandeDao;
	}

	public Map<String, String> getErreurs() {
		return erreurs;
	}

	public String getResultat() {
		return resultat;
	}

	public Commande creerCommande(HttpServletRequest request, String chemin) {
		Client client;
		/*
		 * Si l'utilisateur choisit un client d�j� existant, pas de validation �
		 * effectuer
		 */
		String choixNouveauClient = getValeurChamp(request, CHAMP_CHOIX_CLIENT);
		if (ANCIEN_CLIENT.equals(choixNouveauClient)) {
			/* R�cup�ration de l'id du client choisi */
			String idAncienClient = getValeurChamp(request, CHAMP_LISTE_CLIENTS);
			Long id = null;
			try {
				id = Long.parseLong(idAncienClient);
			} catch (NumberFormatException e) {
				setErreur(CHAMP_CHOIX_CLIENT, "Client inconnu, merci d'utiliser le formulaire pr�vu � cet effet.");
				id = 0L;
			}
			/* R�cup�ration de l'objet client correspondant dans la session */
			HttpSession session = request.getSession();
			client = ((Map<Long, Client>) session.getAttribute(SESSION_CLIENTS)).get(id);
		} else {
			/*
			 * Sinon on garde l'ancien mode, pour la validation des champs.
			 * 
			 * L'objet m�tier pour valider la cr�ation d'un client existe d�j�, il est donc
			 * d�conseill� de dupliquer ici son contenu ! � la place, il suffit de passer la
			 * requ�te courante � l'objet m�tier existant et de r�cup�rer l'objet Client
			 * cr��.
			 */
			CreationClientForm clientForm = new CreationClientForm(clientDao);
			client = clientForm.creerClient(request, chemin);

			/*
			 * Et tr�s important, il ne faut pas oublier de r�cup�rer le contenu de la map
			 * d'erreur cr��e par l'objet m�tier CreationClientForm dans la map d'erreurs
			 * courante, actuellement vide.
			 */
			erreurs = clientForm.getErreurs();
		}

		/*
		 * Ensuite, il suffit de proc�der normalement avec le reste des champs
		 * sp�cifiques � une commande.
		 */

		/* R�cup�ration de la date dans un DateTime Joda. */
		DateTime dt = new DateTime();

		String montant = getValeurChamp(request, CHAMP_MONTANT);
		String modePaiement = getValeurChamp(request, CHAMP_MODE_PAIEMENT);
		String statutPaiement = getValeurChamp(request, CHAMP_STATUT_PAIEMENT);
		String modeLivraison = getValeurChamp(request, CHAMP_MODE_LIVRAISON);
		String statutLivraison = getValeurChamp(request, CHAMP_STATUT_LIVRAISON);

		Commande commande = new Commande();

		try {
			traiterClient(client, commande);

			commande.setDate(dt);

			traiterMontant(montant, commande);
			traiterModePaiement(modePaiement, commande);
			traiterStatutPaiement(statutPaiement, commande);
			traiterModeLivraison(modeLivraison, commande);
			traiterStatutLivraison(statutLivraison, commande);

			if (erreurs.isEmpty()) {
				commandeDao.creer(commande);
				resultat = "Succ�s de la cr�ation de la commande.";
			} else {
				resultat = "�chec de la cr�ation de la commande.";
			}
		} catch (DAOException e) {
			setErreur("impr�vu", "Erreur impr�vue lors de la cr�ation.");
			resultat = "�chec de la cr�ation de la commande : une erreur impr�vue est survenue, merci de r�essayer dans quelques instants.";
			e.printStackTrace();
		}

		return commande;
	}

	private void traiterClient(Client client, Commande commande) {
		if (client == null) {
			setErreur(CHAMP_CHOIX_CLIENT, "Client inconnu, merci d'utiliser le formulaire pr�vu � cet effet.");
		}
		commande.setClient(client);
	}

	private void traiterMontant(String montant, Commande commande) {
		double valeurMontant = -1;
		try {
			valeurMontant = validationMontant(montant);
		} catch (FormValidationException e) {
			setErreur(CHAMP_MONTANT, e.getMessage());
		}
		commande.setMontant(valeurMontant);
	}

	private void traiterModePaiement(String modePaiement, Commande commande) {
		try {
			validationModePaiement(modePaiement);
		} catch (FormValidationException e) {
			setErreur(CHAMP_MODE_PAIEMENT, e.getMessage());
		}
		commande.setModePaiement(modePaiement);
	}

	private void traiterStatutPaiement(String statutPaiement, Commande commande) {
		try {
			validationStatutPaiement(statutPaiement);
		} catch (FormValidationException e) {
			setErreur(CHAMP_STATUT_PAIEMENT, e.getMessage());
		}
		commande.setStatutPaiement(statutPaiement);
	}

	private void traiterModeLivraison(String modeLivraison, Commande commande) {
		try {
			validationModeLivraison(modeLivraison);
		} catch (FormValidationException e) {
			setErreur(CHAMP_MODE_LIVRAISON, e.getMessage());
		}
		commande.setModeLivraison(modeLivraison);
	}

	private void traiterStatutLivraison(String statutLivraison, Commande commande) {
		try {
			validationStatutLivraison(statutLivraison);
		} catch (FormValidationException e) {
			setErreur(CHAMP_STATUT_LIVRAISON, e.getMessage());
		}
		commande.setStatutLivraison(statutLivraison);
	}

	private double validationMontant(String montant) throws FormValidationException {
		double temp;
		if (montant != null) {
			try {
				temp = Double.parseDouble(montant);
				if (temp < 0) {
					throw new FormValidationException("Le montant doit �tre un nombre positif.");
				}
			} catch (NumberFormatException e) {
				temp = -1;
				throw new FormValidationException("Le montant doit �tre un nombre.");
			}
		} else {
			temp = -1;
			throw new FormValidationException("Merci d'entrer un montant.");
		}
		return temp;
	}

	private void validationModePaiement(String modePaiement) throws FormValidationException {
		if (modePaiement != null) {
			if (modePaiement.length() < 2) {
				throw new FormValidationException("Le mode de paiement doit contenir au moins 2 caract�res.");
			}
		} else {
			throw new FormValidationException("Merci d'entrer un mode de paiement.");
		}
	}

	private void validationStatutPaiement(String statutPaiement) throws FormValidationException {
		if (statutPaiement != null && statutPaiement.length() < 2) {
			throw new FormValidationException("Le statut de paiement doit contenir au moins 2 caract�res.");
		}
	}

	private void validationModeLivraison(String modeLivraison) throws FormValidationException {
		if (modeLivraison != null) {
			if (modeLivraison.length() < 2) {
				throw new FormValidationException("Le mode de livraison doit contenir au moins 2 caract�res.");
			}
		} else {
			throw new FormValidationException("Merci d'entrer un mode de livraison.");
		}
	}

	private void validationStatutLivraison(String statutLivraison) throws FormValidationException {
		if (statutLivraison != null && statutLivraison.length() < 2) {
			throw new FormValidationException("Le statut de livraison doit contenir au moins 2 caract�res.");
		}
	}

	/*
	 * Ajoute un message correspondant au champ sp�cifi� � la map des erreurs.
	 */
	private void setErreur(String champ, String message) {
		erreurs.put(champ, message);
	}

	/*
	 * M�thode utilitaire qui retourne null si un champ est vide, et son contenu
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