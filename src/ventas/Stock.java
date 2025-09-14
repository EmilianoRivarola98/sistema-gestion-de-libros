package ventas;

public class Stock {
    private Libro libro;
    private Sucursal sucursal;
    private int cantidad;

    public Stock(Libro libro, Sucursal sucursal, int cantidad) {
        this.libro = libro;
        this.sucursal = sucursal;
        this.cantidad = cantidad;
    }
    
    public Libro getLibro() { return libro; }
    
    public Sucursal getSucursal() { return sucursal; }
    
    public int getCantidad() { return cantidad; }
}
