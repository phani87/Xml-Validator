package com.cnsi.xml.util;

import java.sql.Date;

public class XmlDao {

	private Integer xml_id;
	private String xml_file;
	private String xml_enabled;
	private Date xml_created_date;
	private Date xml_modified_date;
	
	
	
	public Integer getXml_id() {
		return xml_id;
	}
	public void setXml_id(Integer xml_id) {
		this.xml_id = xml_id;
	}
	public String getXml_file() {
		return xml_file;
	}
	public void setXml_file(String xml_file) {
		this.xml_file = xml_file;
	}
	public String getXml_enabled() {
		return xml_enabled;
	}
	public void setXml_enabled(String xml_enabled) {
		this.xml_enabled = xml_enabled;
	}
	public Date getXml_created_date() {
		return xml_created_date;
	}
	public void setXml_created_date(Date xml_created_date) {
		this.xml_created_date = xml_created_date;
	}
	public Date getXml_modified_date() {
		return xml_modified_date;
	}
	public void setXml_modified_date(Date xml_modified_date) {
		this.xml_modified_date = xml_modified_date;
	}
	
	
	
}
