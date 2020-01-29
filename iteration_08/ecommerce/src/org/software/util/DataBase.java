package org.software.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DataBase {
	public Connection getConnection(String profile) {
		Connection connection = null;
		
		/*
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql://localhost:5432/ecommerce";
		String user = "";
		String password = "";
		*/
		
		String JndiDataSourceName = "";
		
		if (profile.equals("admin")) {
			JndiDataSourceName = "eCommerceAdminDS";
			//user = "ecommerce_admin";
			//password = "234567";
		}
		if (profile.equals("client")) {
			JndiDataSourceName = "eCommerceClientDS";
			//user = "ecommerce_client";
			//password = "345678";
		}
		if (profile.equals("guest")) {
			JndiDataSourceName = "eCommerceGuestDS";
			//user = "ecommerce_guest";
			//password = "456789";
		}
		
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(JndiDataSourceName);
			connection = ds.getConnection();
			
			//Class.forName(driver);
			//connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}	
		
		return connection;
	}

	public void closeObject(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeObject(PreparedStatement preparedStatement) {
		try {
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeObject(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeObject(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
