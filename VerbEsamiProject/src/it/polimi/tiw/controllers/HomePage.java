package it.polimi.tiw.controllers;

import java.io.IOException;

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


@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
       
    
    public HomePage() {
        super();
    }
    
    public void init() throws ServletException {
    	//thymeleaf initialization
    	ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//this header is to prevent the browser caching the page during logout phase
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		
		HttpSession session = request.getSession();
		
		String path = "/WEB-INF/index.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		//printing custom message in the login/index page, usually used when error occurred
		if(session.getAttribute("errorMessage") != null) {
			ctx.setVariable("errorMsg", session.getAttribute("errorMessage"));
			session.removeAttribute("errorMessage");
		}
		
		//removing the two possible objects saved in the session
		session.invalidate();
		
		templateEngine.process(path,  ctx, response.getWriter());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
