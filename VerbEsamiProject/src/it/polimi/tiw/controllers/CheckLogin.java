package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public CheckLogin() {
		super();
	}

	public void init() throws ServletException {
		//connecting to the db with ConnectionHandler (in the utils package) that allows me to 
		//make the connection and to close it 
		connection = ConnectionHandler.getConnection(getServletContext());
		
		//instantiating a context to make thymeleaf work
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//the path for any type of error
		String loginpath = request.getServletContext().getContextPath() + "/Logout";
		
		HttpSession session = request.getSession();
				
		//obtain and escape params
		String usrn = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		
		if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
			session.setAttribute("errorMessage", "Please input valid username and password");
			response.sendRedirect(loginpath);
			return;
		}

		
		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		User user = null;
		try {
			//we provide as parameters the credentials that we got from the form (more properly from the request)
			//and we check in the db if the user is either valid or null
			user = userDao.checkCredentials(usrn, pwd);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		//if in the db we didn't find the user --> the user is null so we show a message saying incorrect values in the form
		if (user == null) {
			
			session.setAttribute("errorMessage", "Username or password not valid");
			response.sendRedirect(loginpath);
			return;
			
		} else {
			//if in the db we actually found some user with the right credentials --> we "put" in the session the user Bean from the db and the attribute we 
			//decided to use to get it in future is "user"
			session.setAttribute("user", user);
			String target;
			
			//according to the isProfessor variable of the user that is trying to log into the application we will
			//launch the servlet of professor or of the user 
			if (user.getIsProfessor()) {
				target = "/GoToClassesProfessorListPage";
			}
			else {
				target = "/GoToClassesStudentListPage";
			}
			
			path = getServletContext().getContextPath() + target;
			response.sendRedirect(path);
			
		}

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