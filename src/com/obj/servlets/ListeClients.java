package com.obj.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.obj.dao.ClientDao;
import com.obj.dao.DaoFactory;

public class ListeClients extends HttpServlet {

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHEMIN = "chemin";
	public static final String ATT_CLIENT = "client";
	public static final String ATT_FORM = "form";
	public static final String SESSION_CLIENTS = "clients";

	public static final String VUE_SUCCES = "/WEB-INF/listerClients.jsp";
	public static final String VUE_FORM = "/WEB-INF/creationClient.jsp";

	private ClientDao clientDao;

	public void init() throws ServletException {
		/* R�cup�ration d'une instance de notre DAO Utilisateur */
		this.clientDao = ((DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration dans le
		 * web.xml
		 */
		// String chemin = this.getServletConfig().getInitParameter(CHEMIN);

		/* Pr�paration de l'objet formulaire */
		// ListeClientsForm form = new ListeClientsForm(clientDao);

		/* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
		// List<Client> clients = form.listerClients();

		/* Ajout du bean et de l'objet m�tier � l'objet requ�te */
		// request.setAttribute(ATT_CLIENT, clients);
		// request.setAttribute(ATT_FORM, form);

		/* Si aucune erreur */
		// if (form.getErreurs().isEmpty()) {
		/* Alors r�cup�ration de la map des clients dans la session */
		// HttpSession session = request.getSession();
		/// session.setAttribute(SESSION_CLIENTS, clients);

		/* Affichage de la fiche r�capitulative */
		this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);

		// }
	}
}
