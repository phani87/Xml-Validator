package com.cnsi.utility;

public class DbUtilDAO {

	
	private String userName;
	private String password;
	private String serviceName;
	private String hostName;
	private String port;
	private String classZ;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getClassZ() {
		return classZ;
	}
	public void setClassZ(String classZ) {
		this.classZ = classZ;
	}
	
}
