package com.cnsi.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {
	
	private final Logger log = LoggerFactory.getLogger(DBUtil.class);

	DbUtilDAO utilDAO = null;
	
	
	/**
	 *Overloaded Constructor 
	 * @param fileName
	 */
	public DBUtil(String fileName) {
		utilDAO = new DbUtilDAO();
		Properties props = new Properties();
		try  {
			InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
			props.load(input);
			utilDAO.setHostName(props.getProperty("hostname"));
			utilDAO.setPort(props.getProperty("port"));
			utilDAO.setServiceName(props.getProperty("servicename"));
			utilDAO.setUserName(props.getProperty("username"));
			utilDAO.setPassword(props.getProperty("password"));
			utilDAO.setClassZ(props.getProperty("class"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 *Constructor 
	 * @param fileName
	 */
	public DBUtil() {
		String fileName = "dbconnection.properties";
		utilDAO = new DbUtilDAO();
		Properties props = new Properties();
		try  {
			InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
			props.load(input);
			utilDAO.setHostName(props.getProperty("hostname"));
			utilDAO.setPort(props.getProperty("port"));
			utilDAO.setServiceName(props.getProperty("servicename"));
			utilDAO.setUserName(props.getProperty("username"));
			utilDAO.setPassword(props.getProperty("password"));
			utilDAO.setClassZ(props.getProperty("class"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Class to create DB connection
	 * @return
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			
			Class.forName(utilDAO.getClassZ());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + utilDAO.getHostName() + ":"
					+ utilDAO.getPort() + ":" + utilDAO.getServiceName(), utilDAO.getUserName(), utilDAO.getPassword());
			log.info("Connection Created");
		} catch (ClassNotFoundException e) {
			log.error("Create Connection failed - ClassNotFoundException" , e);
		} catch (SQLException e) {
			log.error("Create Connection failed - SQLException" , e);
		}

		return conn;

	}
	
	/**
	 * Class to close DB connection
	 * @param conn
	 */
	public void closeConnection(Connection conn){
		try {
			conn.close();
			log.info("Closing connection");
		} catch (SQLException e) {
			log.error("Close Connection failed - SQLException" , e);
		}
	}

	public static void main(String[] args) {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		dbUtil.closeConnection(conn);
	}

}
