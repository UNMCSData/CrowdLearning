package com.crowdcs.utils;
import java.util.Properties;

import org.apache.log4j.Logger;



public class QueryDetails {
	private static final Logger logger = Logger.getLogger(QueryDetails.class);
	public String getQueryDetails(String requestCode) throws Exception{
		String query=null;
		try{
			Properties prop;
			logger.info("Entering getQueryDetails method");
			try {			
				prop = new Properties();
			    prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("Queries.properties"));
				query   = prop.getProperty(requestCode);
			} 
			catch (Exception e) {
				logger.fatal("Exception occurred in the getQueryDetails method".concat(String.valueOf(String.valueOf(e.toString()))));
				throw e;
			}
			logger.info("Exiting initailizeConnectionPool method");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return query;
	}
}
