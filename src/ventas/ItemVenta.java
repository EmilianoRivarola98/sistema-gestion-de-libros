package ventas;

import java.math.BigDecimal;

public class ItemVenta {
	private Libro libro;
	private int cantidad;
	private BigDecimal precioUnitario;

	public ItemVenta(Libro libro, int cantidad, BigDecimal precioUnitario) {
		this.libro = libro;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal calcularSubtotal() {
		return precioUnitario.multiply(new BigDecimal(cantidad));
	}

	public Libro getLibro() { return libro; }

	public int getCantidad() { return cantidad; }

	public BigDecimal getPrecioUnitario() { return precioUnitario; }
}
