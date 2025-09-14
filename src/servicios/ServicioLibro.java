package servicios;

import java.util.List;

import ventas.Libro;

public class ServicioLibro {
	public static Libro buscarPorIsbn(String isbn, List<Libro> libros) {
        for (Libro libro : libros) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }
}
