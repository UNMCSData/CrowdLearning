package com.crowdcs.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import com.crowdcs.bo.csBo;
import com.crowdcs.utils.EstablishConnection;
import com.crowdcs.utils.QueryDetails;
public class csDao {

	private EstablishConnection establishConnection;
	private Connection connection;
	private ResultSet resultSet;
	private java.sql.PreparedStatement preparedStatement;
	QueryDetails queryDetails;
	
	public csBo getLogin(csBo lb) throws Exception {
		
		csBo lb3=null;
		String username=lb.getUsername();
		String password=lb.getPassword();
		// TODO Auto-generated method stub
		if(establishConnection==null){
			establishConnection = new EstablishConnection();
		}
		if(establishConnection!=null){
			connection = establishConnection.getConnection();
		}
		if(connection!=null){
			queryDetails=new QueryDetails();
			String query=queryDetails.getQueryDetails("Login");				
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2,password);
			resultSet=preparedStatement.executeQuery();
			if(resultSet!=null){
				lb3= new csBo();
				lb3.setUsername(resultSet.getString("email"));
				lb3.setPassword(resultSet.getString("password"));
				lb3.setRollid(resultSet.getInt("role_id"));
			}
		
	}
		return lb3;
}
}
