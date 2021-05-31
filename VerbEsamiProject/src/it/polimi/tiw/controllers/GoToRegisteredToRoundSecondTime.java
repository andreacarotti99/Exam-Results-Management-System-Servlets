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
import it.polimi.tiw.beans.SavedOrder;
import it.polimi.tiw.dao.RegisteredStudentsDAO;
import it.polimi.tiw.dao.OrderDAO;


import it.polimi.tiw.utils.ConnectionHandler;

/**
 * Servlet implementation class GoToRegisteredToRoundPage
 */
@WebServlet("/GoToRegisteredToRoundSecondTime")
public class GoToRegisteredToRoundSecondTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToRegisteredToRoundSecondTime() {
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
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);	
		OrderDAO orderDAO = new OrderDAO(connection);
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		Integer roundid = null;
		SavedOrder savedOrder = new SavedOrder();
		int lastClicked = -1;
		
		/*
		 * - Getting the savedOrder object from the session
		 * - saving in the variable savedOrder
		 * - removing the object from the session
		 * - updating the new savedOrder object
		 * - setting the new savedOrder in the session
		 */
		
		
		
		
		try {
			
			lastClicked = Integer.parseInt(request.getParameter("lastClicked"));
			System.out.println("Last clicked column: "+ lastClicked);
			
			savedOrder = (SavedOrder) session.getAttribute("savedOrder");
			
			System.out.println("Saved clicked column: "+ savedOrder.getClickedColumn());
			
			
			savedOrder.checkLastClicked(lastClicked);
			
			/*
			if (session.getAttribute("savedOrder") != null) {
				session.removeAttribute("savedOrder");
			}
			
			session.setAttribute("savedOrder", savedOrder);
			
			 */
						
			roundid = (Integer) session.getAttribute("roundid");
			
			System.out.println("saved in the session the RoundID: " + roundid);
			

		} catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
 		
		try {
	
			switch (lastClicked) {
			case 1:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByStudNumbAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByStudNumbDesc(user.getId(), roundid);
				}
				break;
				
				//surname
			case 2:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderBySurnameAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderBySurnameDesc(user.getId(), roundid);
				}
				break;

				
				//name
			case 3:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByNameAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByNameDesc(user.getId(), roundid);
				}
				break;

				
				//email
			case 4:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByMailAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByMailDesc(user.getId(), roundid);
				}
				break;

				
				//degree course
			case 5:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByDegreeAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByDegreeDesc(user.getId(), roundid);
				}
				break;

				//mark
			case 6:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByMarkAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByMarkDesc(user.getId(), roundid);
				}
				break;

				
			case 7:
				if (savedOrder.getOrdineCrescente()) {
					registeredStudents = orderDAO.orderByStatusAsc(user.getId(), roundid);
				}
				else {
					registeredStudents = orderDAO.orderByStatusDesc(user.getId(), roundid);
				}
				break;

				
				
				
				
		}
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover list of students registered");
			return;
		}
		
		
		String path = "/WEB-INF/prof/RegisteredToRound.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
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
