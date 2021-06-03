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
import it.polimi.tiw.beans.SavedOrder;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.ClassesDAO;
import it.polimi.tiw.dao.ExtraInfoDAO;
import it.polimi.tiw.dao.GeneralChecksDAO;
import it.polimi.tiw.dao.RegisteredStudentsDAO;

import it.polimi.tiw.utils.ConnectionHandler;


@WebServlet("/GoToEditMarkPage")
public class GoToEditMarkPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    
    public GoToEditMarkPage() {
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
		
		//do not change nor delete the "savedOrder" object in the session here
		
		User user = (User) session.getAttribute("user");
		
		int roundId;
		int studentId;
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			studentId = Integer.parseInt(request.getParameter("studentId"));
			
		}catch(NumberFormatException | NullPointerException e) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		//creating the dao to do the checks before actually doing the real operations
		GeneralChecksDAO generalChecksDAO = new GeneralChecksDAO(connection);
		boolean isRoundOfThisProfessor;
		boolean isStudentRegisteredToThisRound;
		try {
			isRoundOfThisProfessor = generalChecksDAO.isRoundOfThisProfessor(user.getId(), roundId);
			isStudentRegisteredToThisRound = generalChecksDAO.isStudentRegisteredToThisRound(studentId, roundId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		//checks for validity of the parameter passed in the URL
		if (!isRoundOfThisProfessor || !isStudentRegisteredToThisRound) {
			session.setAttribute("errorMessage", "Stop hacking, don't try to change parameters");
			response.sendRedirect(loginpath);
			return;
		}
		
		
		
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);
		RegisteredStudent infoStudent;
		ExtraInfoDAO extraInfoDAO = new ExtraInfoDAO(connection);
		Round roundInfo;
		try {
			//extracting info about the clicked student (attending that round) 
			
			infoStudent = registeredStudentsDAO.findInfoStudentByRoundIDAndStudentID(roundId, studentId);
			
			roundInfo = extraInfoDAO.getRoundInfo(roundId);
			
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Failure in database retrieving information, please try again later");
			response.sendRedirect(loginpath);
			return;
		}
		
		
		String path = "/WEB-INF/prof/EditMark.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.setVariable("infoStudent", infoStudent);
		ctx.setVariable("roundInfo", roundInfo);
		
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

