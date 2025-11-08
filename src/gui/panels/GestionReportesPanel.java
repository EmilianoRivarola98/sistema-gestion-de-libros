package gui.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;

import servicios.ServicioReportes;
import servicios.DatosReporte;

/**
 * Panel para mostrar reportes del sistema.
 * Recopila y muestra información consolidada sin persistencia en base de datos.
 */
public class GestionReportesPanel extends BasePanel {
    private ServicioReportes servicioReportes;
    private JLabel lblTotalUsuarios, lblTotalLibros, lblTotalAutores, lblTotalGeneros;
    private JLabel lblTotalSucursales, lblTotalVentas, lblVentasTotales, lblPromedioVenta;
    private JLabel lblDescuentosTotales, lblPromocionesActivas;
    private JTextArea areaReporteDetallado;

    public GestionReportesPanel() {
        super();
        this.servicioReportes = new ServicioReportes();
    }

    @Override
    public void inicializar() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        add(crearPanelSuperior(), BorderLayout.NORTH);

        // Panel central con estadísticas
        add(crearPanelEstadisticas(), BorderLayout.CENTER);

        // Panel inferior con reportes detallados
        add(crearPanelReportesDetallados(), BorderLayout.SOUTH);

        refrescar();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("REPORTES DEL SISTEMA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo, BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        panel.add(btnRefrescar, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("ESTADÍSTICAS GENERALES"));

        // Panel con grid de estadísticas
        JPanel gridEstadisticas = new JPanel(new GridLayout(3, 4, 10, 10));
        gridEstadisticas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Primera fila
        gridEstadisticas.add(crearPanelEstadistica("Total Usuarios", lblTotalUsuarios = new JLabel("0")));
        gridEstadisticas.add(crearPanelEstadistica("Total Libros", lblTotalLibros = new JLabel("0")));
        gridEstadisticas.add(crearPanelEstadistica("Total Autores", lblTotalAutores = new JLabel("0")));
        gridEstadisticas.add(crearPanelEstadistica("Total Géneros", lblTotalGeneros = new JLabel("0")));

        // Segunda fila
        gridEstadisticas.add(crearPanelEstadistica("Total Sucursales", lblTotalSucursales = new JLabel("0")));
        gridEstadisticas.add(crearPanelEstadistica("Total Ventas", lblTotalVentas = new JLabel("0")));
        gridEstadisticas.add(crearPanelEstadistica("Ventas Totales", lblVentasTotales = new JLabel("$0.00")));
        gridEstadisticas.add(crearPanelEstadistica("Promedio por Venta", lblPromedioVenta = new JLabel("$0.00")));

        // Tercera fila
        gridEstadisticas.add(crearPanelEstadistica("Descuentos Totales", lblDescuentosTotales = new JLabel("$0.00")));
        gridEstadisticas.add(crearPanelEstadistica("Promociones Activas", lblPromocionesActivas = new JLabel("0")));

        panel.add(gridEstadisticas, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstadistica(String titulo, JLabel valor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(new Color(240, 248, 255));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTitulo.setForeground(Color.DARK_GRAY);
        panel.add(lblTitulo, BorderLayout.NORTH);

        valor.setFont(new Font("Arial", Font.BOLD, 16));
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        valor.setForeground(new Color(0, 102, 204));
        panel.add(valor, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelReportesDetallados() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("REPORTES DETALLADOS"));
        panel.setPreferredSize(new Dimension(0, 150));

        // Área de texto para reportes
        areaReporteDetallado = new JTextArea();
        areaReporteDetallado.setEditable(false);
        areaReporteDetallado.setFont(new Font("Courier", Font.PLAIN, 11));
        areaReporteDetallado.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scroll = new JScrollPane(areaReporteDetallado);
        panel.add(scroll, BorderLayout.CENTER);

        // Panel de botones para reportes específicos
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        JButton btnReporteVentas = new JButton("Reporte Ventas");
        btnReporteVentas.addActionListener(e -> mostrarReporteVentas());
        panelBotones.add(btnReporteVentas);

        JButton btnReporteInventario = new JButton("Reporte Inventario");
        btnReporteInventario.addActionListener(e -> mostrarReporteInventario());
        panelBotones.add(btnReporteInventario);

        JButton btnReporteBiblioteca = new JButton("Reporte Biblioteca");
        btnReporteBiblioteca.addActionListener(e -> mostrarReporteBiblioteca());
        panelBotones.add(btnReporteBiblioteca);

        JButton btnReporteUsuarios = new JButton("Reporte Usuarios");
        btnReporteUsuarios.addActionListener(e -> mostrarReporteUsuarios());
        panelBotones.add(btnReporteUsuarios);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> areaReporteDetallado.setText(""));
        panelBotones.add(btnLimpiar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    public void refrescar() {
        try {
            DatosReporte reporte = servicioReportes.generarReporteGeneral();

            lblTotalUsuarios.setText(String.valueOf(reporte.getTotalUsuarios()));
            lblTotalLibros.setText(String.valueOf(reporte.getTotalLibros()));
            lblTotalAutores.setText(String.valueOf(reporte.getTotalAutores()));
            lblTotalGeneros.setText(String.valueOf(reporte.getTotalGeneros()));
            lblTotalSucursales.setText(String.valueOf(reporte.getTotalSucursales()));
            lblTotalVentas.setText(String.valueOf(reporte.getTotalVentas()));
            lblVentasTotales.setText("$" + String.format("%.2f", reporte.getVentasTotales()));
            lblPromedioVenta.setText("$" + String.format("%.2f", reporte.getPromedioVenta()));
            lblDescuentosTotales.setText("$" + String.format("%.2f", reporte.getDescuentosTotales()));
            lblPromocionesActivas.setText(String.valueOf(reporte.getPromocionesActivas()));

            areaReporteDetallado.setText("Seleccione un tipo de reporte para ver detalles.\n\n" +
                                        "Haga clic en los botones de arriba para generar reportes específicos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarReporteVentas() {
        String reporte = servicioReportes.generarReporteVentas();
        areaReporteDetallado.setText(reporte);
        areaReporteDetallado.setCaretPosition(0);
    }

    private void mostrarReporteInventario() {
        String reporte = servicioReportes.generarReporteInventario();
        areaReporteDetallado.setText(reporte);
        areaReporteDetallado.setCaretPosition(0);
    }

    private void mostrarReporteBiblioteca() {
        String reporte = servicioReportes.generarReporteBiblioteca();
        areaReporteDetallado.setText(reporte);
        areaReporteDetallado.setCaretPosition(0);
    }

    private void mostrarReporteUsuarios() {
        String reporte = servicioReportes.generarReporteUsuarios();
        areaReporteDetallado.setText(reporte);
        areaReporteDetallado.setCaretPosition(0);
    }

    @Override
    public void limpiar() {
        areaReporteDetallado.setText("");
    }
}
