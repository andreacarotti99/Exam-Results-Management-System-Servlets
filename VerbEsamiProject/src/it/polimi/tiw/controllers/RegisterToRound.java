package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.ExtraInfoDAO;
import it.polimi.tiw.dao.GeneralChecksDAO;
import it.polimi.tiw.dao.RegisteredStudentsDAO;
import it.polimi.tiw.utils.ConnectionHandler;


@WebServlet("/RegisterToRound")
public class RegisterToRound extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	
	
    public RegisterToRound() {
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
		boolean isStudentAlreadyRegisteredToThisRound;
		boolean isStudentAttendingThisRoundsClass;
		try {
			isStudentAlreadyRegisteredToThisRound = generalChecksDAO.isStudentRegisteredToThisRound(user.getId(), roundId);
			isStudentAttendingThisRoundsClass = generalChecksDAO.isStudentAttendingThisRoundsClass(roundId, user.getId());
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		//checks for validity of the parameter passed in the URL (if he is already registered or if he isnt attending the round's class)
		if (isStudentAlreadyRegisteredToThisRound || !isStudentAttendingThisRoundsClass) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		
		//this is the actual dao that changes the db
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);
		ExtraInfoDAO extraInfoDAO = new ExtraInfoDAO(connection);
		int classId;
		try {
			registeredStudentsDAO.registerStudentToRound(roundId, user.getId());
			classId = extraInfoDAO.getClassIdOfRound(roundId);
			
		}catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToRoundsStudentListPage?classid=" + classId;
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
