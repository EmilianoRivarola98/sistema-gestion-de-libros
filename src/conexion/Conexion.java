package conexion;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class Conexion {
	private static String URL ="jdbc:mysql://localhost:3306/libreria_gestion";//cambiar "base-de-datos" por el nombre correspondiente
	private static String USER = "root";
	private static String PASSWORD ="";

	private static Connection conect;
	private static Conexion instance;

	private Conexion() {
		try {
			conect =  (Connection) DriverManager.getConnection(URL,USER,PASSWORD);
			System.out.println("Se conectó");
		} catch (SQLException e) {
			System.out.println("No se conectó " + e.getMessage());
		}
	}
	public static Conexion getInstance() {
		if(instance ==null) {
			instance = new Conexion();
		}
		return instance;	
	}
	public Connection getConnection() {
		return conect;
	}
}