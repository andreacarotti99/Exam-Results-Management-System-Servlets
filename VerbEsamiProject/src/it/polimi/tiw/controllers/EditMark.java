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

import it.polimi.tiw.beans.SavedOrder;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.EditMarkDAO;
import it.polimi.tiw.dao.GeneralChecksDAO;
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
		//the path for any type of error
		String loginpath = request.getServletContext().getContextPath() + "/Logout";
		
		HttpSession session = request.getSession();
		
		//these lines are here to keep the same order of the table before clicking the button
		SavedOrder savedOrder = (SavedOrder) session.getAttribute("savedOrder");
		int clickedColumn = savedOrder.getClickedColumn();
		savedOrder.checkLastClicked(clickedColumn);
		session.setAttribute("savedOrder", savedOrder);
		
		User user = (User) session.getAttribute("user");
		
		int newMark;
		int roundId;
		int studentId;
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			studentId = Integer.parseInt(request.getParameter("studentId"));
			newMark = Integer.parseInt(request.getParameter("newMark"));
			
		}catch(NumberFormatException | NullPointerException e) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		if (newMark >= 32 || newMark <= 0 || (newMark >= 4 && newMark <= 17)) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		//creating the dao to do the checks before actually doing the real operations
		GeneralChecksDAO generalChecksDAO = new GeneralChecksDAO(connection);
		boolean isRoundOfThisProfessor;
		boolean isStudentRegisteredToThisRound;
		boolean isMarkEditable;
		try {
			isRoundOfThisProfessor = generalChecksDAO.isRoundOfThisProfessor(user.getId(), roundId);
			isStudentRegisteredToThisRound = generalChecksDAO.isStudentRegisteredToThisRound(studentId, roundId);
			isMarkEditable = generalChecksDAO.isMarkEditable(roundId, studentId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		//checks for validity of the parameter passed in the URL
		if (!isRoundOfThisProfessor || !isStudentRegisteredToThisRound || !isMarkEditable) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
	
		
		//this is the DAO that actually changes the db
		EditMarkDAO editMarkDAO = new EditMarkDAO(connection);
		try {

			editMarkDAO.createNewMarkAndSetToInserted(newMark, studentId, roundId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
	
		
		String ctxpath = getServletContext().getContextPath();
		
		String path = ctxpath + "/GoToRegisteredToRoundPage?roundId=" + roundId + "&lastClicked=" + clickedColumn;
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
