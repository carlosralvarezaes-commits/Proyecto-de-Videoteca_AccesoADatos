package videoteca.servicio;

import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;

import java.util.List;

/**
 * Calcula los ids nuevos: el mayor que existe + 1.
 *
 * <p>Esta clase ya está terminada. El id de película es único en TODA la
 * videoteca (se mira en las películas de todas las categorías).</p>
 */
public final class GeneradorIds {

    private GeneradorIds() {
    }

    /** Mayor id de categoría + 1 (1 si no hay ninguna). */
    public static int siguienteIdCategoria(List<Categoria> categorias) {
        return categorias.stream().mapToInt(Categoria::getId).max().orElse(0) + 1;
    }

    /** Mayor id de película de TODAS las categorías + 1 (1 si no hay ninguna). */
    public static int siguienteIdPelicula(List<Categoria> categorias) {
        return categorias.stream()
                .flatMap(categoria -> categoria.getPeliculas().stream())
                .mapToInt(Pelicula::getId)
                .max().orElse(0) + 1;
    }
}
