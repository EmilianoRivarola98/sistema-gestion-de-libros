package ventas;

import java.math.BigDecimal;
import java.sql.Date;



public class Libro {
    private int idLibro;
    private String titulo;
    private int idAutor;  
    private int idGenero; 
    private String isbn;
    private BigDecimal precio;
    private Date fechaLanzamiento;  


    public Libro() {}

    public Libro(int idLibro, String titulo, int idAutor, int idGenero, String isbn, BigDecimal precio, Date fechaLanzamiento) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.idAutor = idAutor;
        this.idGenero = idGenero;
        this.isbn = isbn;
        this.precio = precio;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Libro(int idLibro, String titulo, int idAutor, int idGenero, String isbn, BigDecimal precio) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.idAutor = idAutor;
        this.idGenero = idGenero;
        this.isbn = isbn;
        this.precio = precio;
    }

    public Libro(String isbn, String titulo, int idAutor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.idAutor = idAutor;
    }

    // getters y setters


	public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getDetalleFormateado() {
        StringBuilder sb = new StringBuilder();
        sb.append("Libro ID: ").append(idLibro).append("\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Autor ID: ").append(idAutor).append("\n");
        sb.append("Género ID: ").append(idGenero).append("\n");
        sb.append("ISBN: ").append(isbn).append("\n");
        sb.append("Precio: $").append(precio).append("\n");
        sb.append("Lanzamiento: ").append(fechaLanzamiento).append("\n");
        sb.append("----------------------------------------\n");
        return sb.toString();
    }
    
}
