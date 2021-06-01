package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.VerbalizationDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/VerbalizeMark")
public class VerbalizeMark extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public VerbalizeMark() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		connection = ConnectionHandler.getConnection(getServletContext());
	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
				
		HttpSession s = request.getSession();
		
		if (s.getAttribute("savedOrder") != null) {
			s.removeAttribute("savedOrder");
		}
		
		
		User u = (User) s.getAttribute("user");
		
		/*
		int roundid = (int) s.getAttribute("roundid");
				*/
		
		int roundId;
		
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
		}catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		int userid = u.getId();
		
		VerbalizationDAO verbalizationDAO = new VerbalizationDAO(connection);

		
		
		try {
			//FARE TUTTO COME UNA UNICA TRANSAZIONE!!!!!!!!!!!!!!!!!
			
			Timestamp date = verbalizationDAO.createVerbalFromStatePubblicatoOrRifiutato(roundId);
			Integer newVerbalId = verbalizationDAO.getTuplaGivenIdRoundAndTimestamp(roundId);
			verbalizationDAO.updateNewVerbalIdRegistered(roundId, newVerbalId);
			verbalizationDAO.changeStatusToVerbalizzato(roundId);

			
		} catch (SQLException e) {
			// throw new ServletException(e);
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure of editing date for verbalization in database");
			return;
		}
			
			
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToVerbalPage?roundId=" + roundId;
		response.sendRedirect(path);
		
		System.out.println("Redirect was correct");

	}

	public void destroy() {
		try {
			if (connection != null) {
				
				connection.close();
				
			}
		} catch (SQLException sqle) {
		
		}
	}
}