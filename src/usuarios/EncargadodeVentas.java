package usuarios;
import interfaces.Menu;
import javax.swing.JOptionPane;

public class EncargadodeVentas extends Usuario implements Menu{
    public EncargadodeVentas(int id, String nombre, String email, String password, int idRol, int idSucursal) {
		super(id, nombre, email, password, idRol, idSucursal);
	
	}

    @Override
    public void MostrarMenu() {
        String[] opciones = { 
            "Buscar libro", 
            "Consultar stock", 
            "Ingresar venta", 
            "Consultar ventas", 
            "Salir" 
        };

        int seleccion = 0;

        while (seleccion != 4) { 
            seleccion = JOptionPane.showOptionDialog(null, 
                    "Menú Encargado de Ventas", 
                    "Seleccione una opción",
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    opciones, 
                    opciones[0]);

            switch (seleccion) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Buscar libro");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Consultar stock");
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Ingresar venta");
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Consultar ventas");
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Salir del menú de ventas");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida");
            }
        }
        }

        public void buscarLibro() {
            JOptionPane.showMessageDialog(null, "Función Buscar libro");

        }

        public void consultarStock() {
            JOptionPane.showMessageDialog(null, "Función Consultar stock");

        }

        public void ingresarVenta() {
            JOptionPane.showMessageDialog(null, "Función Ingresar venta");

        }

        public void consutarVentas() {
            JOptionPane.showMessageDialog(null, "Función Consultar ventas");

        }

    }
    
