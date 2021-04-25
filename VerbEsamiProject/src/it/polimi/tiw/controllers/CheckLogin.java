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
		
		//insantiating a context to make thymeleaf work
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//obtain and escape params
		String usrn = null;
		String pwd = null;
		try {
			//getting the parameters from the request because we filled the form and
			//now we want to check if the parameters provided in the form are valid
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
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
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		//if in the db we didn't find the user --> the user is null so we show a message saying incorrect values in the form
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			
			//the template engine will process the provided string and context (with the errorMsg) and will dislplay the error of incorrect username
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			//if in the db we actually found some user with the right credentials --> we "put" in the session the user Bean from the db and the attribute we 
			//decided to use to get it in future is "user"
			request.getSession().setAttribute("user", user);
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
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}