package com.revature.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtils {
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		String url = "";
		String username = "";
		String password = "";

		try {
			FileInputStream fis = new FileInputStream("../MerinoJDBCBank/src/main/resources/DB_properties.properties");
			Properties prop = new Properties();
			prop.load(fis);
			url = prop.getProperty("URL");
			username = prop.getProperty("DB_Username");
			password = prop.getProperty("DB_Password");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (IOException e2) {
			e2.printStackTrace();
			// TODO: handle exception
		} catch (Exception e3) {
			// TODO: handle exception
			e3.printStackTrace();
		}

		return DriverManager.getConnection(url, username, password);

	}

//	public static void main(String[] args) {
//		try (Connection conn = ConnectionUtils.getConnection()) {
//			System.out.println("Connection was Successful!");
//
//		} catch (SQLException e) {
//			// TODO: handle exception
//		}
//	}
}