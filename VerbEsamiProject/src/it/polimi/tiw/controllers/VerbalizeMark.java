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
import it.polimi.tiw.dao.GeneralChecksDAO;
import it.polimi.tiw.dao.VerbalizationDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/VerbalizeMark")
public class VerbalizeMark extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public VerbalizeMark() {
		super();
		
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
		//the path for any type of error
		String loginpath = request.getServletContext().getContextPath() + "/Logout";
				
		HttpSession session = request.getSession();
		
		//removing a possible object from the session that is placed there by the GoToRegisteredToRoundsPage servlet
		session.removeAttribute("savedOrder");
		
		//no need to check the session variable 'user' because we are using filters
		User user = (User) session.getAttribute("user");
		
		int roundId;
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
		}catch(NumberFormatException | NullPointerException e) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		//creating the dao to do the checks before actually doing the real operations
		GeneralChecksDAO generalChecksDAO = new GeneralChecksDAO(connection);
		boolean isRoundOfThisProfessor;
		boolean isRoundVerbalizable;
		try {
			connection.setAutoCommit(true);
			isRoundOfThisProfessor = generalChecksDAO.isRoundOfThisProfessor(user.getId(), roundId);
			isRoundVerbalizable = generalChecksDAO.isRoundVerbalizable(roundId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		//checks for validity of the parameter passed in the URL
		if (!isRoundOfThisProfessor) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		if (!isRoundVerbalizable) {
			//if round is not verbalizable then redirect to GoToRegisteredToRoundPage with custom error string
			session.setAttribute("errorMessage", "There are no votes to verbalize");
			response.sendRedirect(request.getServletContext().getContextPath() + "/GoToRegisteredToRoundPage?roundId=" + roundId + "&lastClicked=1");
			return;
		}
		
		
		//This is the dao that creates the verbal
		VerbalizationDAO verbalizationDAO = new VerbalizationDAO(connection);
		try {
			//we need to create a transaction because the db gets modified with multiple queries
			connection.setAutoCommit(false);
			
			verbalizationDAO.createNewVerbal(roundId);
			
			int newVerbalId = verbalizationDAO.getVerbalIdOfTheNewVerbal(roundId);
			
			verbalizationDAO.setRejectedMarksToFailedMark(roundId);
			
			verbalizationDAO.updateNewVerbalIdRegisteredAndSetVerbalized(roundId, newVerbalId);
			
			connection.commit();
			
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("Fatal error during rollback");
				session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
				response.sendRedirect(loginpath);
				return;
			}
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
			
			
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToVerbalPage?roundId=" + roundId;
		response.sendRedirect(path);

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