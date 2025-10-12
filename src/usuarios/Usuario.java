package usuarios;

import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Usuario {
	protected int id;
	private String nombre;
	private String email;
	private String password;
	private int idRol;
	private Integer idSucursal;

	// Constructor completo
	public Usuario(int id, String nombre, String email, String password, int idRol, Integer idSucursal) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.idRol = idRol;
		this.idSucursal = idSucursal;
	}

	// Constructor sin ID (para crear nuevos usuarios)
	public Usuario(String nombre, String email, String password, int idRol, Integer idSucursal) {
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.idRol = idRol;
		this.idSucursal = idSucursal;
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public int getIdRol() { return idRol; }
	public void setIdRol(int idRol) { this.idRol = idRol; }

	public Integer getIdSucursal() { return idSucursal; }
	public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

	// Validación de email
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches();
	}

	// Hash de password usando SHA-256
	public static String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error al hashear la contraseña", e);
		}
	}

	// Verificar password
	public boolean verifyPassword(String inputPassword) {
		String hashedInput = hashPassword(inputPassword);
		return this.password.equals(hashedInput);
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				", email='" + email + '\'' +
				", idRol=" + idRol +
				", idSucursal=" + idSucursal +
				'}';
	}
}
