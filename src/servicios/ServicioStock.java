package servicios;

import ventas.Stock;
import ventas.Sucursal;
import java.util.List;

public class ServicioStock {
	public static Stock consultarStock(String isbn, Sucursal sucursal, List<Stock> inventario){
		for(Stock item : inventario) {
			if(item.getLibro().getIsbn().equals(isbn) && item.getSucursal().equals(sucursal)) {
				return item;
			}
		}
		return null;
	}
}
