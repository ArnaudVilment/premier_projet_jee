package com.obj.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.obj.dao.ClientDao;
import com.obj.dao.DaoFactory;

public class ListeCommandes extends HttpServlet {

	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String CHEMIN = "chemin";
	public static final String ATT_CLIENT = "client";
	public static final String ATT_FORM = "form";
	public static final String SESSION_CLIENTS = "clients";

	public static final String VUE_SUCCES = "/WEB-INF/listerCommandes.jsp";
	public static final String VUE_FORM = "/WEB-INF/creationClient.jsp";

	private ClientDao clientDao;

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.clientDao = ((DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getClientDao();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 
		 */
		this.getServletContext().getRequestDispatcher(VUE_SUCCES).forward(request, response);
	}
}
