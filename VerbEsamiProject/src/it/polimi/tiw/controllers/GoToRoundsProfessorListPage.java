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
		
		//this header is to prevent the browser caching the page during logout phase
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		
		// If the user is not logged in (not present in session) redirect to the login
		//String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		
		if (session.getAttribute("savedOrder") != null) {
			session.removeAttribute("savedOrder");
		}
		//se arrivo alla pagina CoursesListPage in modi "diversi" e quindi la sessione è nuova oppure se nel db non si è trovato l'utente
		//rimanda alla pagina di login
		/*
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		commento queste righe perchè il controllo se la sessione è nuova o se lo user non è stato trovato lo effettuo
		nei filtri (ricordandoci che ogni servlet su cui eseguo il controllo deve essere mappata nel web.xml)
		*/
		//se invece non è una sessione nuova e ho lo user ottenuto correttamente
		User user = (User) session.getAttribute("user");
		RoundsDAO roundsDAO = new RoundsDAO(connection);
		List<Round> rounds = new ArrayList<Round>();
		boolean isTaughtByProfessor;
		boolean classExists;
		
		// getting from the request the id of the clicked class from the list of classes
		// that we passed as a parameter to the servlet in the html page ... @{/GetMissionDetails(classid=${c.classID})}
		Integer classid = null;
		try {
			classid = Integer.parseInt(request.getParameter("classid"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		

		try {
			//get the classid from the request and the userid from the session
			isTaughtByProfessor = roundsDAO.isClassTaughtByProfessor(user.getId(), classid);
			classExists = roundsDAO.doesClassExists(classid);

			//extracting the list of rounds of the professor
			rounds = roundsDAO.findRoundsByProfessorAndClass(user.getId(), classid);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover rounds");
			return;
		}
		
		
		//without this check we would have an empty list or a list of rounds but not of the right professor
		if (isTaughtByProfessor == false) {
			String path;
			if (classExists == false) {
				
				session.setAttribute("errorMessage", "stop hacking, the classid you insered doesn't exist");
				
				path = getServletContext().getContextPath() + "/HomePage";
				
				response.sendRedirect(path);
			}
			else {
				//this is the case where there is a class with the provided id (in the html or URL and the user doesn't attend this class)
				session.setAttribute("errorMessage", "stop hacking, you don't teach this class");
				
				path = getServletContext().getContextPath() + "/HomePage";
				response.sendRedirect(path);
			}
		}
		
		
		// Redirect to the Courses List page and add missions to the parameters
		String path = "/WEB-INF/prof/RoundsProfessorListPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("rounds", rounds);
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}