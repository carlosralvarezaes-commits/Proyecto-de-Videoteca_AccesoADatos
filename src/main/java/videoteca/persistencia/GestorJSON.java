package videoteca.persistencia;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import videoteca.modelo.Categoria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * JSON con Gson o con Jackson.
 *
 * TAREA 2 (guardar) y TAREA 5 (obtener). Apuntes: apartado 12.
 * Elige UNA libreria para guardar y UNA para leer (pueden ser distintas).
 *
 * Los dos metodos "base" YA ESTAN HECHOS: miran la configuracion y llaman a la
 * variante de la libreria elegida. Tu solo rellenas la variante que elijas;
 * las demas las dejas con su UnsupportedOperationException.
 *
 * El JSON necesita UN objeto raiz: por eso existe la clase modelo.Videoteca (un
 * objeto con la lista "categorias")
 */
public final class GestorJSON {

    private GestorJSON() {
    }

    // ------------------------------------------------------------ metodos base (ya hechos)

    public static List<Categoria> obtenerCategoriasJSON(Path fichero, Configuracion configuracion) throws IOException {
        if (configuracion.getImportacionJson() == null) {
            throw new IllegalStateException("No hay tecnica configurada para importacion_json (gson o jackson)");
        }
        switch (configuracion.getImportacionJson()) {
            case GSON:
                return obtenerCategoriasJSONGson(fichero);
            case JACKSON:
                return obtenerCategoriasJSONJackson(fichero);
            default:
                throw new IllegalStateException("Tecnica JSON desconocida");
        }
    }

    public static void guardarCategoriasJSON(List<Categoria> categorias, Path fichero, Configuracion configuracion)
            throws IOException {
        if (configuracion.getExportacionJson() == null) {
            throw new IllegalStateException("No hay tecnica configurada para exportacion_json (gson o jackson)");
        }
        switch (configuracion.getExportacionJson()) {
            case GSON:
                guardarCategoriasJSONGson(categorias, fichero);
                break;
            case JACKSON:
                guardarCategoriasJSONJackson(categorias, fichero);
                break;
            default:
                throw new IllegalStateException("Tecnica JSON desconocida");
        }
    }

    // ------------------------------------------------------------ Gson

    /**
     * TAREA 2 (variante Gson).
     *
     * TODO: implementar (o dejar asi si eliges Jackson).
     */
    public static void guardarCategoriasJSONGson(List<Categoria> categorias, Path fichero) throws IOException {
        Gson gson= new Gson();
        Files.writeString(fichero,gson.toJson(categorias));
    }


    /**
     * TAREA 5 (variante Gson).
     *
     * TODO: implementar (o dejar asi si eliges Jackson).
     */
    public static List<Categoria> obtenerCategoriasJSONGson(Path fichero) throws IOException {
        throw new UnsupportedOperationException("Tecnica no implementada: Gson (obtener)");
    }

    // ------------------------------------------------------------ Jackson

    /**
     * TAREA 2 (variante Jackson).
     *
     * TODO: implementar (o dejar asi si eliges Gson).
     */
    public static void guardarCategoriasJSONJackson(List<Categoria> categorias, Path fichero) throws IOException {
        throw new UnsupportedOperationException("Tecnica no implementada: Jackson (guardar)");
    }

    /**
     * TAREA 5 (variante Jackson).
     *
     * TODO: implementar (o dejar asi si eliges Gson).
     */
    public static List<Categoria> obtenerCategoriasJSONJackson(Path fichero) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Categoria> c= mapper.readValue(fichero.toFile(),new TypeReference<List <Categoria>> (){});
        return c;
    }

    public void asd (){

    }

}


