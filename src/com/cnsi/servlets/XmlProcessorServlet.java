package com.cnsi.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnsi.xml.util.XmlDao;
import com.cnsi.xml.util.XmlReader;

/**
 * Servlet implementation class XmlProcessor
 */
//@WebServlet("/XmlProcessor")
public class XmlProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(XmlProcessorServlet.class);
	public XmlReader reader;
	private final static String FILENAME = "pipeline.xsd";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XmlProcessorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("get_xml").equalsIgnoreCase("xml")) {
			showXML(request, response);
		} else if (request.getParameter("get_xml").equalsIgnoreCase("save")) {
			if (isValidXml(request, response)) {
				createNewXMLEntry(request, response);
			} else {
				errorPage(request, response);
			}
		} else if (request.getParameter("get_xml").equalsIgnoreCase("allxml")) {
			getAllXMLDetails(request, response);
		}
	}
	
	/**
	 * This method Fetches all the XML details and displays it to the XML jsp page
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getAllXMLDetails(HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException{
		ServletContext context = (ServletContext) getServletContext();
		List<XmlDao> allXml = (List<XmlDao>) context.getAttribute("xmlList");
		HttpSession session = request.getSession(false);
		session.setAttribute("allXML", allXml);
		session.setAttribute("error", "noError");
		response.sendRedirect(request.getContextPath() + "/jsp/XmlTable.jsp");
	}
	/**
	 * This method is invoked when the XSD fails the validation and displays the error on the page
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void errorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ServletContext context = (ServletContext) getServletContext();
		String xml = request.getParameter("xml_content");
		HttpSession session = request.getSession(false);
		session.setAttribute("error", "error");
		session.setAttribute("xml", xml);
		response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");
	}
	
	/**
	 * The method is invoked when we create an XML entry into the DB as CLOB file
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void createNewXMLEntry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Creating new XML entry");
		String xml_content = request.getParameter("xml_content");
		new XmlReader().xmlUpdate(request.getParameter("xml_content"));
		HttpSession session = request.getSession(false);
		List<XmlDao> allXml = new XmlReader().getAllXmls();
		log.info("Resetting Servlet Context");
		ServletContext sc = getServletContext();
		sc.setAttribute("xmlList", allXml);
		session.setAttribute("allXML", allXml);
		response.sendRedirect(request.getContextPath() + "/jsp/XmlTable.jsp");
		log.info("Exiting showXML");
	}
	
	/**
	 * Checks if the XMl is valid and returns a boolean parameter
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean isValidXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Entering validate XML against XSD");
		String xmlContent = request.getParameter("xml_content");
		return new XmlReader().validateXMLSchema(FILENAME, xmlContent);
	}
	
	/**
	 * This methods show the selected XML from the screen
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	
	private void showXML(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Entering showXML");
		String xml_id = (String) request.getParameter("xml_id");
		String active_flag = (String) request.getParameter("active_flag");
		reader = new XmlReader();
		String xml = reader.getXMLFile(Integer.parseInt(xml_id.substring(xml_id.length() - 5, xml_id.length() - 4)));
		request.setAttribute("xml", xml);
		HttpSession session = request.getSession(false);
		session.setAttribute("xml", xml);
		session.setAttribute("active_flag", active_flag);
		response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");
		log.info("Exiting showXML");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
