import app.IniciarPrograma;
import conexion.Conexion;

public class 
Main {

    public static void main(String[] args) {
    	Conexion.getInstance();
        IniciarPrograma programa = new IniciarPrograma();
        programa.iniciar(); 
    }
}