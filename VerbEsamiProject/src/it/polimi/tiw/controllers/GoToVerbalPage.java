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
import it.polimi.tiw.beans.Verbal;
import it.polimi.tiw.dao.RegisteredStudentsDAO;

import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.dao.VerbalizationDAO;

/**
 * Servlet implementation class GoToRegisteredToRoundPage
 */
@WebServlet("/GoToVerbalPage")
public class GoToVerbalPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToVerbalPage() {
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
		
		//we need the verbalizationDAO to set in the context the verbalid and to print it on the html VerbalOfRound
		RegisteredStudentsDAO registeredStudentsDAO = new RegisteredStudentsDAO(connection);	
	
		VerbalizationDAO verbalizationDAO = new VerbalizationDAO(connection);
		
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		int roundId;
		int newVerbalId;
		Verbal verbal = new Verbal();
		
		try {
			roundId = Integer.parseInt(request.getParameter("roundId"));
			

		} catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
 		
		try {
			//FARE TUTTO COME UNA UNICA TRANSAZIONE!!!!!!!!!!!!!!!!!
			
			//get the classid from the request and the userid from the session
			//isTaughtByProfessor = roundsDAO.isClassTaughtByProfessor(user.getId(), classid);
			//classExists = roundsDAO.doesClassExists(classid);

			//extracting the list of students registered to the given roundid (saved in the request)
			registeredStudents = registeredStudentsDAO.findVerbalizedStudentsToRound(roundId);
			
			//executing again the query to get the newVerbalIdFrom the db
			newVerbalId = verbalizationDAO.getTuplaGivenIdRoundAndTimestamp(roundId);
			
			verbal.setVerbalID(newVerbalId);
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover list of students registered");
			return;
		}
		
		
		String path = "/WEB-INF/prof/VerbalOfRound.html";
		
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("registeredStudents", registeredStudents);
		ctx.setVariable("verbal", verbal);

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

