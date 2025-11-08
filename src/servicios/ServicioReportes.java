package servicios;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para generar reportes del sistema.
 * Recopila información de todos los servicios y la presenta en formato resumido.
 */
public class ServicioReportes {
    private ServicioUsuario servicioUsuario;
    private ServicioLibro servicioLibro;
    private ServicioAutor servicioAutor;
    private ServicioGenero servicioGenero;
    private ServicioSucursal servicioSucursal;
    private ServicioVenta servicioVenta;
    private ServicioStock servicioStock;
    private ServicioPromocion servicioPromocion;

    public ServicioReportes() {
        this.servicioUsuario = new ServicioUsuario();
        this.servicioLibro = new ServicioLibro();
        this.servicioAutor = new ServicioAutor();
        this.servicioGenero = new ServicioGenero();
        this.servicioSucursal = new ServicioSucursal();
        this.servicioVenta = new ServicioVenta();
        this.servicioStock = new ServicioStock();
        this.servicioPromocion = new ServicioPromocion();
    }

    /**
     * Genera un reporte general del sistema
     */
    public DatosReporte generarReporteGeneral() {
        DatosReporte reporte = new DatosReporte();

        try {
            // Contar registros
            int totalUsuarios = servicioUsuario.obtenerTodosLosUsuarios().size();
            int totalLibros = servicioLibro.obtenerTodos().size();
            int totalAutores = servicioAutor.obtenerTodos().size();
            int totalGeneros = servicioGenero.obtenerTodos().size();
            int totalSucursales = servicioSucursal.obtenerTodasLasSucursales().size();

            reporte.setTotalUsuarios(totalUsuarios);
            reporte.setTotalLibros(totalLibros);
            reporte.setTotalAutores(totalAutores);
            reporte.setTotalGeneros(totalGeneros);
            reporte.setTotalSucursales(totalSucursales);

            // Datos de ventas
            List<ventas.Venta> ventas = servicioVenta.obtenerTodasLasVentas();
            reporte.setTotalVentas(ventas.size());

            BigDecimal totalVentas = BigDecimal.ZERO;
            BigDecimal totalDescuentos = BigDecimal.ZERO;

            for (ventas.Venta venta : ventas) {
                totalVentas = totalVentas.add(venta.getTotal());
                totalDescuentos = totalDescuentos.add(venta.getDescuento());
            }

            reporte.setVentasTotales(totalVentas);
            reporte.setDescuentosTotales(totalDescuentos);

            // Promedio de ventas
            if (ventas.size() > 0) {
                BigDecimal promedio = totalVentas.divide(BigDecimal.valueOf(ventas.size()), 2, BigDecimal.ROUND_HALF_UP);
                reporte.setPromedioVenta(promedio);
            } else {
                reporte.setPromedioVenta(BigDecimal.ZERO);
            }

            // Promociones
            int totalPromociones = servicioPromocion.obtenerTodas().size();
            reporte.setPromocionesActivas(totalPromociones);

        } catch (Exception ex) {
            System.err.println("Error al generar reporte: " + ex.getMessage());
        }

        return reporte;
    }

    /**
     * Genera reporte de ventas por período
     */
    public String generarReporteVentas() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("========== REPORTE DE VENTAS ==========\n\n");

        try {
            List<ventas.Venta> ventas = servicioVenta.obtenerTodasLasVentas();
            
            if (ventas.isEmpty()) {
                reporte.append("No hay ventas registradas.\n");
            } else {
                BigDecimal totalVentas = BigDecimal.ZERO;
                int totalItems = 0;

                reporte.append("Total de Ventas: ").append(ventas.size()).append("\n");
                reporte.append("----------------------------------------\n");

                for (ventas.Venta venta : ventas) {
                    reporte.append("Venta ID: ").append(venta.getIdVenta()).append("\n");
                    reporte.append("  Fecha: ").append(venta.getFecha()).append("\n");
                    reporte.append("  Total: $").append(String.format("%.2f", venta.getTotal())).append("\n");
                    reporte.append("  Descuento: $").append(String.format("%.2f", venta.getDescuento())).append("\n");
                    reporte.append("----------------------------------------\n");

                    totalVentas = totalVentas.add(venta.getTotal());
                    if (venta.getItems() != null) {
                        totalItems += venta.getItems().size();
                    }
                }

                reporte.append("\nRESUMEN:\n");
                reporte.append("Total Ventas: $").append(String.format("%.2f", totalVentas)).append("\n");
                reporte.append("Total Ítems Vendidos: ").append(totalItems).append("\n");
            }
        } catch (Exception ex) {
            reporte.append("Error al generar reporte: ").append(ex.getMessage()).append("\n");
        }

        return reporte.toString();
    }

    /**
     * Genera reporte de inventario (Stock)
     */
    public String generarReporteInventario() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("========== REPORTE DE INVENTARIO ==========\n\n");

        try {
            List<ventas.Sucursal> sucursales = servicioSucursal.obtenerTodasLasSucursales();

            if (sucursales.isEmpty()) {
                reporte.append("No hay sucursales registradas.\n");
            } else {
                for (ventas.Sucursal sucursal : sucursales) {
                    reporte.append("SUCURSAL: ").append(sucursal.getNombre()).append("\n");
                    reporte.append("  Dirección: ").append(sucursal.getDireccion()).append("\n");
                    reporte.append("----------------------------------------\n");

                    List<String> stock = servicioStock.obtenerStockPorSucursal(sucursal.getId());
                    if (stock.isEmpty()) {
                        reporte.append("  Sin stock\n");
                    } else {
                        for (String item : stock) {
                            reporte.append("  ").append(item).append("\n");
                        }
                    }
                    reporte.append("\n");
                }
            }
        } catch (Exception ex) {
            reporte.append("Error al generar reporte: ").append(ex.getMessage()).append("\n");
        }

        return reporte.toString();
    }

    /**
     * Genera reporte de libros y autores
     */
    public String generarReporteBiblioteca() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("========== REPORTE DE BIBLIOTECA ==========\n\n");

        try {
            List<ventas.Libro> libros = servicioLibro.obtenerTodos();
            List<ventas.Autor> autores = servicioAutor.obtenerTodos();
            List<ventas.Genero> generos = servicioGenero.obtenerTodos();

            reporte.append("ESTADÍSTICAS:\n");
            reporte.append("Total de Libros: ").append(libros.size()).append("\n");
            reporte.append("Total de Autores: ").append(autores.size()).append("\n");
            reporte.append("Total de Géneros: ").append(generos.size()).append("\n");
            reporte.append("\n----------------------------------------\n\n");

            reporte.append("LIBROS REGISTRADOS:\n");
            if (libros.isEmpty()) {
                reporte.append("No hay libros registrados.\n");
            } else {
                for (ventas.Libro libro : libros) {
                    reporte.append("ID: ").append(libro.getIdLibro()).append("\n");
                    reporte.append("  Título: ").append(libro.getTitulo()).append("\n");
                    reporte.append("  Autor: ").append(libro.getNombreAutor() != null ? libro.getNombreAutor() : "N/A").append("\n");
                    reporte.append("  Género: ").append(libro.getNombreGenero() != null ? libro.getNombreGenero() : "N/A").append("\n");
                    reporte.append("  ISBN: ").append(libro.getIsbn()).append("\n");
                    reporte.append("  Precio: $").append(String.format("%.2f", libro.getPrecio())).append("\n");
                    reporte.append("----------------------------------------\n");
                }
            }

            reporte.append("\nAUTORES:\n");
            if (autores.isEmpty()) {
                reporte.append("No hay autores registrados.\n");
            } else {
                for (ventas.Autor autor : autores) {
                    reporte.append("- ").append(autor.getNombre()).append("\n");
                }
            }

        } catch (Exception ex) {
            reporte.append("Error al generar reporte: ").append(ex.getMessage()).append("\n");
        }

        return reporte.toString();
    }

    /**
     * Genera reporte de usuarios
     */
    public String generarReporteUsuarios() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("========== REPORTE DE USUARIOS ==========\n\n");

        try {
            List<usuarios.Usuario> usuarios = servicioUsuario.obtenerTodosLosUsuarios();

            if (usuarios.isEmpty()) {
                reporte.append("No hay usuarios registrados.\n");
            } else {
                reporte.append("Total de Usuarios: ").append(usuarios.size()).append("\n");
                reporte.append("----------------------------------------\n\n");

                for (usuarios.Usuario usuario : usuarios) {
                    reporte.append("ID: ").append(usuario.getId()).append("\n");
                    reporte.append("  Nombre: ").append(usuario.getNombre()).append("\n");
                    reporte.append("  Email: ").append(usuario.getEmail()).append("\n");
                    reporte.append("  Rol ID: ").append(usuario.getIdRol()).append("\n");
                    reporte.append("  Sucursal ID: ").append(usuario.getIdSucursal()).append("\n");
                    reporte.append("----------------------------------------\n");
                }
            }
        } catch (Exception ex) {
            reporte.append("Error al generar reporte: ").append(ex.getMessage()).append("\n");
        }

        return reporte.toString();
    }
}
