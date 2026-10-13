package videoteca.servicio;

import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos de ejemplo: 3 categorías (la última, sin películas) y 4 películas.
 * La videoteca arranca con ellos en memoria. Ya está terminada.
 */
public final class DatosEjemplo {

    private DatosEjemplo() {
    }

    public static List<Categoria> crearVideoteca() {
        Categoria cienciaFiccion = new Categoria(1, "Ciencia ficción");
        cienciaFiccion.getPeliculas().add(new Pelicula(101, "Blade Runner", "Ridley Scott", 1982, 117));
        cienciaFiccion.getPeliculas().add(new Pelicula(102, "Interstellar", "Christopher Nolan", 2014, 169));

        Categoria comedia = new Categoria(2, "Comedia");
        comedia.getPeliculas().add(new Pelicula(201, "Jojo Rabbit", "Taika Waititi", 2019, 108));
        comedia.getPeliculas().add(new Pelicula(202, "Toy Story", "John Lasseter", 1995, 81));

        Categoria terror = new Categoria(3, "Terror"); // a propósito, sin películas

        List<Categoria> lista = new ArrayList<>();
        lista.add(cienciaFiccion);
        lista.add(comedia);
        lista.add(terror);
        return lista;
    }
}
