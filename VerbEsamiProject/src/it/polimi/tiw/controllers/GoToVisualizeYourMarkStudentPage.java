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

import it.polimi.tiw.beans.Classe;
import it.polimi.tiw.beans.RegisteredStudent;
import it.polimi.tiw.beans.Round;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.ClassesDAO;
import it.polimi.tiw.dao.RegisteredStudentsDAO;

import it.polimi.tiw.utils.ConnectionHandler;

/**
 * Servlet implementation class GoToRegisteredToRoundPage
 */
@WebServlet("/GoToVisualizeYourMarkStudentPage")
public class GoToVisualizeYourMarkStudentPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToVisualizeYourMarkStudentPage() {
        super();
        // TODO Auto-generated constructor stub
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		
		//int roundid = (int) session.getAttribute("roundid");
		int roundid = Integer.parseInt(request.getParameter("roundid"));
		
		
	
		List<RegisteredStudent> infoStudent = new ArrayList<RegisteredStudent>();
		Integer studentid = null;
		
		
		try {
			
			//getting studentid from the session
			studentid = user.getId();
			
			//System.out.println(studentid);
			//request.getSession().setAttribute("selectedstudent", studentid);
			
			
		} catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);
		
		try {
			//extracting info about the clicked student (attending that round) 
			
			
			System.out.println("roundid: " + roundid);
			System.out.println("studentid: " + studentid);

			infoStudent = registeredStudentsDAO.findInfoStudentByRoundIDAndStudentID(roundid, studentid);
			
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover student information");
			return;
		}
		
		
		String path = "/WEB-INF/stud/YourMark.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("infostudent", infoStudent);
		templateEngine.process(path, ctx, response.getWriter());
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);

	}
}
