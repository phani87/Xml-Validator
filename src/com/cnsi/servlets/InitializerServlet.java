package com.cnsi.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnsi.xml.util.XmlDao;
import com.cnsi.xml.util.XmlReader;

/**
 * Servlet implementation class InitializerServlet
 */
//@WebServlet("/InitializerServlet")
public class InitializerServlet extends HttpServlet {
	private final Logger log = LoggerFactory.getLogger(InitializerServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitializerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() 
    {
        log.info("Initializer");
        ServletContext sc = getServletContext();
		List<XmlDao> xmlList= new XmlReader().getAllXmls();
		sc.setAttribute("xmlList", xmlList);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Starting Initializer Servlet");
		//ServletContext sc = getServletContext();
		//List<XmlDao> xmlList= new XmlReader().getAllXmls();
		//sc.setAttribute("xmlList", xmlList);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
