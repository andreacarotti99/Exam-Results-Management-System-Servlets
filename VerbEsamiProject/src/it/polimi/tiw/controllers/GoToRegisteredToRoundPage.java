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

import it.polimi.tiw.beans.RegisteredStudent;
import it.polimi.tiw.beans.User;

import it.polimi.tiw.dao.RegisteredStudentsDAO;
import it.polimi.tiw.beans.SavedOrder;


import it.polimi.tiw.utils.ConnectionHandler;

/**
 * Servlet implementation class GoToRegisteredToRoundPage
 */
@WebServlet("/GoToRegisteredToRoundPage")
public class GoToRegisteredToRoundPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToRegisteredToRoundPage() {
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
	
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		int roundId;
		int lastClicked;
		
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			lastClicked = Integer.parseInt(request.getParameter("lastClicked"));
		
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		//this is for the correct order of the list
		SavedOrder savedOrder;
		
		if (session.getAttribute("savedOrder") != null) { //second or more time getting to this page
			
			savedOrder = (SavedOrder) session.getAttribute("savedOrder");
			savedOrder.checkLastClicked(lastClicked);
			session.setAttribute("savedOrder", savedOrder);  //updating the object in session
		}
		else { //first time getting to this page
			
			savedOrder = new SavedOrder();
			session.setAttribute("savedOrder", savedOrder);
		}
		
		
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);
		
		
		try {
			
			switch (savedOrder.getClickedColumn()) {
			//studentNumber
			case 1:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.studentnumber", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.studentnumber", "desc");
				}
				break;
				
				//surname
			case 2:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.surname", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.surname", "desc");
				}
				break;

				
				//name
			case 3:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.name", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.name", "desc");
				}
				break;

				
				//email
			case 4:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.email", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.email", "desc");
				}
				break;

				
				//degree course
			case 5:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.degreecourse", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.degreecourse", "desc");
				}
				break;

				//mark
			case 6:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.mark", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.mark", "desc");
				}
				break;

				
			case 7:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.state", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.state", "desc");
				}
				break;

			default:
				System.out.println("Error");
				break;
		}
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover list of students registered");
			return;
		}
 		

		
		String path = "/WEB-INF/prof/RegisteredToRound.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("roundId", roundId);
		ctx.setVariable("registeredStudents", registeredStudents);

		
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




