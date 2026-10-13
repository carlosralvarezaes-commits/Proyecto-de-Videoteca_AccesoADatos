package videoteca.servicio;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;
import videoteca.excepciones.ExcepcionOperacionCancelada;
import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * EL CONTRATO: la frontera entre el Equipo A (la ventana) y el Equipo B
 * (negocio y persistencia). La ventana solo conoce esta interfaz.
 *
 * <p>Esta interfaz ya está terminada: no se toca.</p>
 *
 * <p>Cualquier método puede lanzar también: UnsupportedOperationException
 * (método todavía pendiente), IllegalStateException (algo sin configurar) o
 * IllegalArgumentException (datos no válidos).</p>
 */
public interface ServicioVideoteca {

    /**
     * Se llama UNA vez, al arrancar. Busca ./config/conf.properties, lo lee si
     * existe y devuelve un mensaje que dice dónde se ha buscado (ruta absoluta)
     * y si existe (EXISTE) o no (NO EXISTE). Nunca lanza excepciones: los
     * problemas van dentro del mensaje.
     */
    String iniciar();

    /** Categorías en memoria, con sus películas (lista vacía si no hay ninguna). */
    List<Categoria> listarCategorias();

    /**
     * Crea una categoría con un id nuevo y la guarda en memoria.
     *
     * @throws IllegalArgumentException si el nombre es null o está en blanco
     */
    Categoria crearCategoria(String nombre);

    /**
     * Crea una película con un id nuevo, dentro de la categoría indicada.
     *
     * @throws IllegalArgumentException si no existe la categoría o falta el título
     */
    Pelicula crearPelicula(int idCategoria, String titulo, String director, int anio, int duracionMinutos);

    /**
     * Lee el fichero y REEMPLAZA los datos en memoria, pero solo si la lectura
     * sale bien: si falla, la memoria no cambia.
     */
    void importar(Formato formato, Path fichero)
            throws IOException, ExcepcionDirectorioNoExiste, ExcepcionFicheroNoExiste;

    /**
     * Escribe los datos en memoria. Si el fichero ya existe, solo se
     * sobrescribe cuando así lo dice el conf.properties (por defecto, no).
     */
    void exportar(Formato formato, Path fichero)
            throws IOException, ExcepcionDirectorioNoExiste, ExcepcionOperacionCancelada;

    /** Carátula de una película (bytes de la imagen), o null si no tiene ninguna. */
    byte[] obtenerImagen(int idPelicula) throws IOException;
}
