/************************************************************************ 
* File Name: EstablishConnection                                                                                                                        
* File Type:Common    
* Description: Establish Connection class contains the methods for establishing connection to the Database 
* and relasing connection.                                                                                                                     *
* Methods defined: 
* 				 1.getConnection
* 				 2.releaseConnection
* 				 3.getFilePath                                                                                            *
* Calling method(s):   
*                  1.All DAO's - All methods                                                                                                      
* Change History:                                                                                                                 
* Change Date	       Changed By              Change                  Change Description                	                                                                                          Identification Tag                                                       *
* 23/12/2009			AjithaSamuel		  All Methods                  Applied coding 
*                                             								standards 	                                                                                       *   *                                                                                                                                           * 
*************************************************************************************************/
package com.crowdcs.utils;

import java.sql.*;
import java.util.*;  

import org.apache.log4j.Logger;

public class EstablishConnection
{
	private static Hashtable connections;
	private static String DATABASE_DRIVER;
	private static String DB_JDBC_URL;
	private static String DB_USER_NAME;	
	private static String DB_USER_PASSWORD;
	private static final Logger logger = Logger.getLogger(EstablishConnection.class);
	public EstablishConnection()  throws Exception{
		logger.info("Entering EstablishConnection Constructor");
		try {
			if(connections==null){
				initailizeConnectionPool();
		    }			
		} 
		catch (Exception e) {
			connections=null;
			logger.fatal("Exception occurred in the EstablishConnection constructor".concat(String.valueOf(String.valueOf(e.toString()))));
			throw e;
		}
		logger.info("Exiting EstablishConnection Constructor");		
	}
	@SuppressWarnings("unchecked")
	public Connection getConnection() throws Exception{
		logger.info("Entering getConnection method");	
		int increment;
		Connection connection = null;
		Enumeration availableConnections;
	    try{
	    	increment=5;
	    	availableConnections = connections.keys();
	    	synchronized (connections){
	    	    while(availableConnections.hasMoreElements()) {
		    	    connection = (Connection)availableConnections.nextElement();
	    	        Boolean b = (Boolean)connections.get(connection);
	    	        if (!b && !connection.isClosed()){
		    	        try {
		    	        	connection.setAutoCommit(true);
		    	        }
		    	        catch(SQLException e) {
		    	        	 connection = DriverManager.getConnection(DB_JDBC_URL, DB_USER_NAME, DB_USER_PASSWORD);
		    	        }
		    	        connections.put(connection, Boolean.TRUE);
		    	        return connection;
	    	        }
	    	    }
	    	}
	        for(int i = 0; i < increment; i++) {
	          connections.put(DriverManager.getConnection(DB_JDBC_URL, DB_USER_NAME, DB_USER_PASSWORD),Boolean.FALSE); 
	        }	
	       
		}
        catch(Exception exp){
        	logger.fatal("Exception occurred in the getConnection method".concat(String.valueOf(String.valueOf(exp.toString()))));
        	throw exp;
        }
	    logger.info("Exiting getConnection method");
	    return getConnection();
	 }		
	
	public void initailizeConnectionPool() throws Exception {
		Properties prop;
		int initialConnections;
		logger.info("Entering initailizeConnectionPool method");
		try {			
			prop = new Properties();
		    initialConnections=5;
		    prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("DBProperties.properties"));
			DATABASE_DRIVER   = prop.getProperty("DATABASE_DRIVER");
			DB_JDBC_URL   = prop.getProperty("DB_JDBC_URL");
			DB_USER_NAME  = prop.getProperty("DB_USER_NAME");	
			DB_USER_PASSWORD   = prop.getProperty("DB_USER_PASSWORD");	
			Class.forName(DATABASE_DRIVER);
			connections = new Hashtable();
			for(int i = 0; i < initialConnections; i++) {
				connections.put(DriverManager.getConnection(DB_JDBC_URL, DB_USER_NAME, DB_USER_PASSWORD),Boolean.FALSE);
			}
		} 
		catch (Exception e) {
			logger.fatal("Exception occurred in the initailizeConnectionPool method".concat(String.valueOf(String.valueOf(e.toString()))));
			throw e;
		}
		logger.info("Exiting initailizeConnectionPool method");
	}
	public void releaseConnection(Connection returned) throws Exception {
	    boolean conValue;
	    logger.info("Entering releaseConnection method");
	    if(connections!=null && returned!=null){
	    	conValue=(Boolean) connections.get(returned);
		    if(conValue){
		    	
		    	try {
					if(!returned.getAutoCommit())
						returned.commit();
					connections.remove(returned);
			    	connections.put(returned, Boolean.FALSE);
				} 
		    	catch (SQLException e) {
		    		logger.fatal("Exception occurred in the releaseConnection method".concat(String.valueOf(String.valueOf(e.toString()))));
					throw e;
				}
		   
		    }
	    }
	    logger.info("Entering releaseConnection method");
    }

	/*public String getFilePath(String propName) throws Exception{
		 logger.info("Entering getFilePath method");
		 String FILEPATH = "";
		 try {
			 ClassLoader clsobj=ClassLoader.getSystemClassLoader();
			 Properties prop = new Properties();
			 prop.load(clsobj.getResourceAsStream("DBProperties.Properties"));
			 FILEPATH  = prop.getProperty(propName).trim();
			    
		} 
		catch (Exception e) {
			logger.fatal("Exception occurred in the releaseConnection method".concat(String.valueOf(String.valueOf(e.toString()))));
			throw e;
		}
		logger.info("Exiting getFilePath method"); 
		return FILEPATH;
	 }*/
}