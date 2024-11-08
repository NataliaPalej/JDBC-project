package makeup_store;

import java.sql.*;

public class DatabaseConnector {
	
	private static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/makeupdb";
	private static String USERNAME = "root";
	private static String PASSWORD = "";
	
	public static Connection getConnection() throws NataliaException {
		
		if (!DB_DRIVER.equalsIgnoreCase("com.mysql.cj.jdbc.Driver")) {
			throw new NataliaException("Invalid DB DRIVER.");
		}
		else if (!JDBC_URL.equalsIgnoreCase("jdbc:mysql://localhost:3306/makeupdb")) {
			throw new NataliaException("Invalid JDBC URL.");
		}
		else if (!USERNAME.equalsIgnoreCase("root")) {
			throw new NataliaException("Invalid database USERNAME.");
		}
		
		try {
			Class.forName(DB_DRIVER);
			return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't connect to database.\n " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't find the database driver.\n" + e.getMessage(), e);
		}
	}

	/**
	 * Getters and Setters
	 */
	public static String getDB_DRIVER() {
		return DB_DRIVER;
	}

	public static void setDB_DRIVER(String dB_DRIVER) {
		DB_DRIVER = dB_DRIVER;
	}

	public static String getJDBC_URL() {
		return JDBC_URL;
	}

	public static void setJDBC_URL(String jDBC_URL) {
		JDBC_URL = jDBC_URL;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

	public static void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
}