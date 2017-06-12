package com.cnsi.xml.util;

import java.io.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnsi.utility.DBUtil;

import oracle.jdbc.OracleResultSet;
import oracle.sql.CLOB;

/**
 * This is a UTIL class that allows to make and retrieve CLOB file along with
 * fetching all the data in the table
 * 
 * @author turlapatip
 *
 */

public class ClobUtil {

	private final Logger log = LoggerFactory.getLogger(ClobUtil.class);
	DBUtil dbConnection;

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
		} finally {
			br.close();
		}

	}

	/**
	 * read the CLOB file in the DB and returns XML file as String to be
	 * displayed
	 * 
	 * @param id
	 * @throws IOException
	 * @throws SQLException
	 * @return String buffer.toString()
	 */
	public String readCLOBToFileGet(int id) throws IOException, SQLException {
		log.info("Entering readCLOBToFileGet()");

		FileOutputStream outputFileOutputStream = null;
		OutputStreamWriter outputOutputStreamWriter = null;
		BufferedWriter outputBufferedWriter = null;
		String sqlText = null;
		Statement stmt = null;
		ResultSet rset = null;
		CLOB xmlDocument = null;
		long clobLength;
		long position;
		int chunkSize;
		char[] textBuffer;
		int charsRead = 0;
		int charsWritten = 0;
		int totCharsRead = 0;
		int totCharsWritten = 0;
		Connection conn = null;
		StringBuffer buffer = null;
		DBUtil util = new DBUtil();
		conn = util.getConnection();
		try {

			stmt = conn.createStatement();

			sqlText = "SELECT xml_file " + "FROM   pipeline " + "WHERE  xml_id = " + id + " FOR UPDATE";
			rset = stmt.executeQuery(sqlText);
			rset.next();
			xmlDocument = ((OracleResultSet) rset).getCLOB("xml_file");

			clobLength = xmlDocument.length();
			chunkSize = xmlDocument.getChunkSize();
			textBuffer = new char[chunkSize];
			buffer = new StringBuffer();
			for (position = 1; position <= clobLength; position += chunkSize) {
				charsRead = xmlDocument.getChars(position, chunkSize, textBuffer);
				buffer.append(textBuffer);
				totCharsRead += charsRead;
				totCharsWritten += charsRead;
			}
			log.info("File Read - " + buffer.length());
		} catch (SQLException e) {
			log.error("Caught SQL Exception: (Write CLOB value to file - Get Method). - SQLException", e);
			throw e;
		} finally {
			rset.close();
			stmt.close();
			util.closeConnection(conn);
			log.info("Exiting readCLOBToFileGet()");
		}
		return buffer.toString();

	}

	/**
	 * Inserts the CLOB file into DB and updates the old entry as 'inactive'
	 * 
	 * @param xml
	 * @param id
	 * @throws IOException
	 * @throws SQLException
	 */

	public void insertXML(String xml, int id) throws IOException, SQLException {
		log.info("Entering insertXML()");
		FileInputStream inputFileInputStream = null;
		InputStreamReader inputInputStreamReader = null;
		BufferedReader inputBufferedReader = null;
		String sqlText = null;
		Statement stmt = null;
		ResultSet rset = null;
		CLOB xmlDocument = null;
		int chunkSize;
		char[] textBuffer;
		long position;
		int charsRead = 0;
		int charsWritten = 0;
		int totCharsRead = 0;
		int totCharsWritten = 0;
		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		try {
			updateActiveFlag();
			stmt = conn.createStatement();
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			inputBufferedReader = new BufferedReader(new InputStreamReader(is));
			sqlText = "INSERT INTO pipeline (xml_id, xml_file,xml_enabled,xml_created_date,xml_modified_date) "
					+ "   VALUES(" + id + ", EMPTY_CLOB(), 'A', SYSDATE, SYSDATE)";
			stmt.executeUpdate(sqlText);
			sqlText = "SELECT xml_file " + "FROM   pipeline " + "WHERE  xml_id = " + id + " FOR UPDATE";
			rset = stmt.executeQuery(sqlText);
			rset.next();
			xmlDocument = ((OracleResultSet) rset).getCLOB("xml_file");
			chunkSize = xmlDocument.getChunkSize();
			textBuffer = new char[chunkSize];
			position = 1;
			while ((charsRead = inputBufferedReader.read(textBuffer)) != -1) {
				charsWritten = xmlDocument.putChars(position, textBuffer, charsRead);
				position += charsRead;
				totCharsRead += charsRead;
				totCharsWritten += charsWritten;
			}
			inputBufferedReader.close();
		} catch (IOException e) {
			System.out.println("Caught I/O Exception: (Write CLOB value - Put Method).");
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			System.out.println("Caught SQL Exception: (Write CLOB value - Put Method).");
			System.out.println("SQL:\n" + sqlText);
			e.printStackTrace();
			throw e;
		} finally {
			conn.commit();
			rset.close();
			stmt.close();
			util.closeConnection(conn);
			log.info("Exiting insertXML()");
		}

	}

	/**
	 * This method displays all the XML data expect the CLOB data to show as
	 * Table on the display screen
	 * 
	 * @return List<XmlDao>
	 */
	public List<XmlDao> getAllXmls() {
		log.info("Entering getAllXmls()");
		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		ResultSet rs = null;
		List<XmlDao> lt = new ArrayList<XmlDao>();
		XmlDao xml = null;
		try {
			Statement statement = conn.createStatement();
			String sql = "select XML_ID, XML_ENABLED, XML_CREATED_DATE, XML_MODIFIED_DATE from PIPELINE";
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				xml = new XmlDao();
				xml.setXml_id(rs.getInt(1));
				xml.setXml_enabled(rs.getString(2));
				xml.setXml_created_date(rs.getDate(3));
				xml.setXml_modified_date(rs.getDate(4));
				lt.add(xml);
			}
		} catch (SQLException e) {
		} finally {
			util.closeConnection(conn);
			log.info("Exiting getAllXmls()");
		}
		return (ArrayList<XmlDao>) lt;
	}

	private void updateActiveFlag() {
		log.info("Updating Flag");
		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			String sql = "UPDATE pipeline " + "SET xml_enabled = 'I' " + "WHERE xml_enabled = 'A'";
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			util.closeConnection(conn);
		}
	}

	/**
	 * This method fetches the max ID to make a unique entry into DB
	 * 
	 * @return int
	 */

	public int getID() {
		DBUtil util = new DBUtil();
		Connection conn = util.getConnection();
		int id = 0;
		try {
			Statement statement = conn.createStatement();
			String sql = "select max(xml_id) from PIPELINE";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConnection(conn);
		}

		return (id);
	}

}
