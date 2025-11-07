package gui.panels;

import javax.swing.JPanel;
import java.awt.LayoutManager;

/**
 * Clase base abstracta para todos los paneles de la aplicación.
 */
public abstract class BasePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public BasePanel() {
        super();
    }

    public BasePanel(LayoutManager layout) {
        super(layout);
    }

    /**
     * Método que debe implementar cada panel para mostrar el contenido
     */
    public abstract void inicializar();

    /**
     * Método para refrescar el panel con datos actualizados
     */
    public abstract void refrescar();

    /**
     * Método para limpiar recursos cuando se sale del panel
     */
    public void limpiar() {}
}
