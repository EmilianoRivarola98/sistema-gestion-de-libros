package servicios;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase para almacenar datos de reportes.
 * No requiere persistencia en BD, solo agrega datos calculados.
 */
public class DatosReporte {
    private int totalUsuarios;
    private int totalLibros;
    private int totalAutores;
    private int totalGeneros;
    private int totalSucursales;
    private int totalVentas;
    private BigDecimal ventasTotales;
    private BigDecimal descuentosTotales;
    private int stockTotal;
    private BigDecimal promedioVenta;
    private int promocionesActivas;

    // Getters y Setters
    public int getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public int getTotalLibros() { return totalLibros; }
    public void setTotalLibros(int totalLibros) { this.totalLibros = totalLibros; }

    public int getTotalAutores() { return totalAutores; }
    public void setTotalAutores(int totalAutores) { this.totalAutores = totalAutores; }

    public int getTotalGeneros() { return totalGeneros; }
    public void setTotalGeneros(int totalGeneros) { this.totalGeneros = totalGeneros; }

    public int getTotalSucursales() { return totalSucursales; }
    public void setTotalSucursales(int totalSucursales) { this.totalSucursales = totalSucursales; }

    public int getTotalVentas() { return totalVentas; }
    public void setTotalVentas(int totalVentas) { this.totalVentas = totalVentas; }

    public BigDecimal getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }

    public BigDecimal getDescuentosTotales() { return descuentosTotales; }
    public void setDescuentosTotales(BigDecimal descuentosTotales) { this.descuentosTotales = descuentosTotales; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }

    public BigDecimal getPromedioVenta() { return promedioVenta; }
    public void setPromedioVenta(BigDecimal promedioVenta) { this.promedioVenta = promedioVenta; }

    public int getPromocionesActivas() { return promocionesActivas; }
    public void setPromocionesActivas(int promocionesActivas) { this.promocionesActivas = promocionesActivas; }
}
