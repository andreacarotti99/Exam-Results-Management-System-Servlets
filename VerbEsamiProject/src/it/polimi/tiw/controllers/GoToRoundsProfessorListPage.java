package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.Round;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.ExtraInfoDAO;
import it.polimi.tiw.dao.GeneralChecksDAO;
import it.polimi.tiw.dao.RoundsDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/GoToRoundsProfessorListPage")
public class GoToRoundsProfessorListPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public GoToRoundsProfessorListPage() {
		super();
	}

	public void init() throws ServletException {
		//thymeleaf inizialitation...
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//the path for any type of error
		String loginpath = request.getServletContext().getContextPath() + "/HomePage";
		
		//this header is to prevent the browser caching the page during logout phase
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		
		HttpSession session = request.getSession();
		
		//removing a possible object from the session that is placed there by the GoToRegisteredToRoundsPage servlet
		session.removeAttribute("savedOrder");
		
		//no need to check the session variable 'user' because we are using filters
		User user = (User) session.getAttribute("user");
		
		// getting from the request the id of the clicked class from the list of classes
		// that we passed as a parameter to the servlet in the html page ... @{/GetMissionDetails(classid=${c.classID})}
		int classId;
		try {
			classId = Integer.parseInt(request.getParameter("classId"));
		} catch (NumberFormatException | NullPointerException e) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		//creating the dao to do the checks before actually doing the real operations
		GeneralChecksDAO generalChecksDAO = new GeneralChecksDAO(connection);
		boolean isTaughtByProfessor;
		try {
			isTaughtByProfessor = generalChecksDAO.isClassTaughtByProfessor(user.getId(), classId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		
		//checks for validity of the parameter passed in the URL
		if (!isTaughtByProfessor) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		
		//real DAO to do the actual intended operation
		RoundsDAO roundsDAO = new RoundsDAO(connection);
		List<Round> rounds = new ArrayList<Round>();
		ExtraInfoDAO extraInfoDAO = new ExtraInfoDAO(connection);
		String className;
		try {

			//extracting the list of rounds of the professor
			rounds = roundsDAO.findAllRoundsOfThisClass(classId);
			
			className = extraInfoDAO.getClassName(classId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		
		// Redirect to the Courses List page and add missions to the parameters
		String path = "/WEB-INF/prof/RoundsProfessorListPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("rounds", rounds);
		ctx.setVariable("className", className);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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