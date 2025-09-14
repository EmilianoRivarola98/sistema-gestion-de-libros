package ventas;

import usuarios.EncargadoVentas;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Venta {
    private int id;
    private Date fecha;
    private List<ItemVenta> itemsVenta = new ArrayList<>(); 
    private EncargadoVentas encargado;
    private Sucursal sucursal;

    public Venta(int id, Date fecha, EncargadoVentas encargado, Sucursal sucursal) {
        this.id = id;
        this.fecha = fecha;
        this.encargado = encargado;
        this.sucursal = sucursal;
    }
    
    public void agregarItem(ItemVenta item) {
        this.itemsVenta.add(item);
    }
    
    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        
        for(ItemVenta item : itemsVenta) {
        	total = total.add(item.calcularSubtotal());
        }
        
        return total;
    }

    public int getId() { return id; }
    
    public Date getFecha() { return fecha; }
    
    public List<ItemVenta> getItemsVenta() { 
        return itemsVenta; 
    }
}
