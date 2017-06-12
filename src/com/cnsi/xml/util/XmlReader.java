package com.cnsi.xml.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This is the Business class that makes sure the the XML is valid against XSD  
 * @author turlapatip
 *
 */


public class XmlReader {

	private final Logger log = LoggerFactory.getLogger(XmlReader.class);
	public String path = "";
	public ClobUtil clobUtil;
	
	public static XmlDao xmlDao;
	
	public static void  initialize() {
		xmlDao = new XmlDao();
		xmlDao.setXml_id(new ClobUtil().getID());
		
	}
	
	public XmlReader() {
		initialize();
	}
	
	
	/**
	 * Use this main method to make an initial DB entry
	 * This can be run as standalone app
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 */
	
	public static void main(String[] args) throws IOException, SQLException {
		BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Clin2\\pipeline.xml")));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	       new XmlReader().xmlUpdate(sb.toString());
	    } finally {
	        br.close();
	    }

	}
	
	/**
	 * This method hits the Clob Util class that would fetch the CLOB file as String to be displayed
	 * @return String
	 */
	public String getXMLFile(){
		String xml = null;
		try {
			log.info("Getting XML file");
			xml = new ClobUtil().readCLOBToFileGet(2);
		} catch (SQLException e) {
			log.info("Unable to get the File - Check DB " + e);
		}catch (IOException i){
			log.info("Unable to get the File - Check DB " + i);
		}
		return xml;
		
	}
	
	/**
	 * This method hits the Clob Util class that would fetch the CLOB file as String to be displayed 
	 * @param id
	 * @return String
	 */
	public String getXMLFile(int id){
		String xml = null;
		try {
			log.info("Getting XML file");
			xml = new ClobUtil().readCLOBToFileGet(xmlDao.getXml_id());
		}catch (SQLException e) {
			log.info("Unable to get the File - Check DB " + e);
		}catch (IOException i){
			log.info("Unable to get the File - Check DB " + i);
		}
		return xml;
		
	}
	
	/**
	 * This method updates the inserts the XML file 
	 * @param String (XML file as String)
	 */
	
	public void xmlUpdate(String s){
		try {
			log.info("creating new XML entry");
			new XmlReader().initialize();
			new ClobUtil().insertXML(s, (xmlDao.getXml_id()+1));
		} catch (SQLException e) {
			log.info("Unable to get the File - Check DB " + e);
		}catch (IOException i){
			log.info("Unable to get the File - Check DB " + i);
		}
	}
	
	
	/**
	 * This is a method that fetches all the XML entries in reverse sorted Order with a comparator implementation
	 * @return
	 */
	public ArrayList<XmlDao> getAllXmls(){
		List<XmlDao> allXml = new ClobUtil().getAllXmls();
		Collections.sort(allXml , new Comparator<XmlDao>() {
			@Override
			public int compare(XmlDao o1, XmlDao o2) {

				if(o1.getXml_id() != null && o2.getXml_id() != null && o1.getXml_id().compareTo(o2.getXml_id()) != 0) {
                    return o1.getXml_id() < o2.getXml_id() ? 1 :(o1.getXml_id() > o2.getXml_id() ? -1 : 0);
                } else {
                  return o1.getXml_enabled().compareTo(o2.getXml_enabled());
               }
			
			}
		});
		List<XmlDao> truncList = new ArrayList<XmlDao>(allXml.subList(0, 10));
		return (ArrayList<XmlDao>) truncList;
		
	}
	
	
	public StringBuilder xmlReader(String filepath) throws IOException{
		File file = new File(filepath);
		String line = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine())!=null){
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			log.error("File Not found " +e);
		} catch (IOException e) {
			log.error("IOException " +e);
		}finally {
			br.close();
		}
		return sb;
	}
	
	public void xmlReader(){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Pipeline pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File("C:\\Clin2\\pipeline.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		
	}
	
	/**
	 * Validates the XML file against the Schema file placed a location
	 * @param xsdPath
	 * @param xml
	 * @return
	 */
	
	 public boolean validateXMLSchema(String xsdPath, String xml){
		 log.info("Validating XML using XSD" );
	        try {
	        	StringReader xmlReader = new StringReader(xml);
	            SchemaFactory factory = 
	                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = factory.newSchema(new File(xsdPath));
	            Validator validator = schema.newValidator();
	            validator.validate(new StreamSource(xmlReader));
	            return true;
	        }catch (IOException i){
				log.info("Unable to get the File - Check DB " + i);
				return false;
			} catch (SAXException e) {
				log.error("SAXException " +e);
				return false;
			}
	        
	    }
	
	
}
