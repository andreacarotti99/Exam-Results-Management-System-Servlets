package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
import it.polimi.tiw.dao.EditMarkDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/EditMark")
public class EditMark extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public EditMark() {
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
		String loginpath = request.getServletContext().getContextPath() + "/HomePage";
		
		Integer newMark = null;
		
		HttpSession s = request.getSession();
		User u = (User) s.getAttribute("user");
		
		/*
		int roundid = (int) s.getAttribute("roundid");
		
		int selectedStudent = (int) s.getAttribute("selectedstudent");
		
		s.removeAttribute("selectedstudent");
		s.removeAttribute("roundid");
		
		System.out.println(roundid);
		System.out.println(selectedStudent);
			*/
		int roundId;
		int selectedStudent;
		
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			selectedStudent = Integer.parseInt(request.getParameter("studentId"));
			newMark = Integer.parseInt(request.getParameter("newMark"));
			
		}catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		
		if (newMark >= 32 || newMark <= 0 || (newMark >= 4 && newMark <= 17)) {
			s.setAttribute("errorMessage", "Don't try to input wrong marks");
			response.sendRedirect(loginpath);
			return;
		}
				
		int userid = u.getId();
		
		EditMarkDAO editMarkDAO = new EditMarkDAO(connection);

		try {
			
			System.out.println("Replacing Mark in the database and changing state...");

			editMarkDAO.createNewMark(newMark, selectedStudent, roundId);
			editMarkDAO.changeToInserted(selectedStudent, roundId);

			
		} catch (SQLException e) {
			// throw new ServletException(e);
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure of changing mark in database");
			return;
		}
	
		// return the user to the right view --> ERROR TO FIX HERE
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToRegisteredToRoundPage?roundId=" + roundId + "&lastClicked=1";
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
