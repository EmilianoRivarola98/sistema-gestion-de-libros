package usuarios;

public class Usuario {
    protected String nombre;
    protected String username;

    public Usuario(String nombre, String username) {
        this.nombre = nombre;
        this.username = username;
    }
    
    public String getNombre() { return nombre; }
    
    public String getUsername() { return username; }
}
