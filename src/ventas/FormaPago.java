package ventas;

public class FormaPago {
    private int idFormaPago;
    private String descripcion;

    public FormaPago(int idFormaPago, String descripcion) {
        this.idFormaPago = idFormaPago;
        this.descripcion = descripcion;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
    	return descripcion;
    }
}