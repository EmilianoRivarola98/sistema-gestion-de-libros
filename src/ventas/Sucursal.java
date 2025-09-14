package ventas;

public class Sucursal {
	private int id;
    private String direccion;
    
    public Sucursal(int id, String direccion) {
    	this.id = id;
    	this.direccion = direccion;
    }
    
    public int getId() { return id; }
    public String getDireccion() { return direccion; }
}
