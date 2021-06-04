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
import it.polimi.tiw.beans.Round;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.ExtraInfoDAO;
import it.polimi.tiw.dao.GeneralChecksDAO;
import it.polimi.tiw.dao.RegisteredStudentsDAO;
import it.polimi.tiw.beans.SavedOrder;


import it.polimi.tiw.utils.ConnectionHandler;


@WebServlet("/GoToRegisteredToRoundPage")
public class GoToRegisteredToRoundPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    
    public GoToRegisteredToRoundPage() {
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
		String loginpath = request.getServletContext().getContextPath() + "/Logout";
		
		//removing a possible object from the session that is placed there by the GoToRegisteredToRoundsPage servlet
		HttpSession session = request.getSession();

		//no need to check the session variable 'user' because we are using filters
		User user = (User) session.getAttribute("user");	
	
		int roundId;
		int lastClicked;
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			lastClicked = Integer.parseInt(request.getParameter("lastClicked"));
		
		} catch (NumberFormatException | NullPointerException e) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		//checks for validity of the parameter passed in the URL
		if (lastClicked <= 0 || lastClicked >= 8) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		//creating the dao to do the checks before actually doing the real operations
		GeneralChecksDAO generalChecksDAO = new GeneralChecksDAO(connection);
		boolean isRoundOfThisProfessor;
		try {
			isRoundOfThisProfessor = generalChecksDAO.isRoundOfThisProfessor(user.getId(), roundId);
			
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
		
		
		//this object is used to order the table
		SavedOrder savedOrder;
		
		if (session.getAttribute("savedOrder") != null) { //second or more time getting to this page
			
			savedOrder = (SavedOrder) session.getAttribute("savedOrder");
			savedOrder.checkLastClicked(lastClicked);  //this method does all the logic of choosing the correct order for the list
			session.setAttribute("savedOrder", savedOrder);  //updating the object in session
		}
		else { //first time getting to this page
			
			savedOrder = new SavedOrder();
			session.setAttribute("savedOrder", savedOrder);
		}
		
		
		//actual DAO to get the informations from the db
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		ExtraInfoDAO extraInfoDAO = new ExtraInfoDAO(connection);
		Round roundInfo;
		try {
			
			roundInfo = extraInfoDAO.getRoundInfo(roundId);
			
			switch (savedOrder.getClickedColumn()) {
			//studentNumber
			case 1:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.studentnumber", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.studentnumber", "desc");
				}
				break;
				
				//surname
			case 2:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.surname", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.surname", "desc");
				}
				break;

				
				//name
			case 3:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.name", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.name", "desc");
				}
				break;

				
				//email
			case 4:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.email", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "u.email", "desc");
				}
				break;

				
				//degree course
			case 5:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.degreecourse", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "s.degreecourse", "desc");
				}
				break;

				//mark
			case 6:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.mark", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.mark", "desc");
				}
				break;

				
			case 7:
				if (savedOrder.isAscendantOrder()) {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.state", "asc");
				}
				else {
					registeredStudents = registeredStudentsDAO.getRegisteredStudentsOrdered(roundId, "r.state", "desc");
				}
				break;

			default:
				//due to the checks we did before this line should never be reached
				System.out.println("Error");
				break;
		}
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
 		
		
		String path = "/WEB-INF/prof/RegisteredToRound.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("registeredStudents", registeredStudents);
		ctx.setVariable("roundInfo", roundInfo);
		
		//custom string message if user tried to verbalize with no marks available for the verbal
		if (session.getAttribute("errorMessage") != null) {
			ctx.setVariable("errorVerbalizing", session.getAttribute("errorMessage"));
			session.removeAttribute("errorMessage");
		}

		templateEngine.process(path, ctx, response.getWriter());
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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




