package videoteca.servicio;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;
import videoteca.excepciones.ExcepcionOperacionCancelada;
import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;
import videoteca.persistencia.ComprobacionRutas;
import videoteca.persistencia.Configuracion;
import videoteca.persistencia.GestorJSON;
import videoteca.persistencia.GestorTextoPlano;
import videoteca.persistencia.GestorXML;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * La implementacion del contrato: la capa de NEGOCIO.
 *
 * Ya hechos: los constructores, listarCategorias(), exportar() e importar()
 * (que solo llaman a tus clases de persistencia). Los programas tu:
 *   - iniciar()                          (TAREA 7)
 *   - obtenerImagen()                    (TAREA 8)
 *   - crearCategoria() y crearPelicula() (TAREA 9)
 */
public class ServicioVideotecaImpl implements ServicioVideoteca {

    /** Los datos arrancan con el ejemplo, para poder exportar desde el primer minuto. */
    private List<Categoria> categorias = DatosEjemplo.crearVideoteca();
    private Configuracion configuracion;
    private final Path directorioConfig;

    /** Usa la carpeta ./config (fija e inmutable). */
    public ServicioVideotecaImpl() {
        this(Paths.get("./config"));
    }

    /** Usa otra carpeta (la usa el Comprobador para probar sin tocar tu config). */
    public ServicioVideotecaImpl(Path directorioConfig) {
        this(directorioConfig, Configuracion.porDefecto());
    }

    /** Igual, pero con una configuración ya hecha (la usa el Comprobador). */
    public ServicioVideotecaImpl(Path directorioConfig, Configuracion configuracion) {
        this.directorioConfig = directorioConfig;
        this.configuracion = configuracion;
    }

    // ------------------------------------------------------------------ ya hechos

    @Override
    public List<Categoria> listarCategorias() {
        return categorias;
    }

    @Override
    public void exportar(Formato formato, Path fichero)
            throws IOException, ExcepcionDirectorioNoExiste, ExcepcionOperacionCancelada {
        ComprobacionRutas.comprobarExportacion(fichero, configuracion.isSobrescribir());
        try {
            switch (formato) {
                case TEXTO:
                    GestorTextoPlano.guardarCategoriasTexto(categorias, fichero);
                    break;
                case JSON:
                    GestorJSON.guardarCategoriasJSON(categorias, fichero, configuracion);
                    break;
                case XML:
                    GestorXML.guardarCategoriasXML(categorias, fichero, configuracion);
                    break;
            }
        } catch (IOException | RuntimeException ex) {
            throw ex;
        } catch (Exception ex) { // SAX, JAXB y DOM lanzan excepciones distintas: las unificamos
            throw new IOException("No se ha podido exportar a " + formato + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public void importar(Formato formato, Path fichero)
            throws IOException, ExcepcionDirectorioNoExiste, ExcepcionFicheroNoExiste {
        ComprobacionRutas.comprobarImportacion(fichero);
        List<Categoria> leidas;
        try {
            switch (formato) {
                case TEXTO:
                    leidas = GestorTextoPlano.obtenerCategoriasTexto(fichero);
                    break;
                case JSON:
                    leidas = GestorJSON.obtenerCategoriasJSON(fichero, configuracion);
                    break;
                case XML:
                    leidas = GestorXML.obtenerCategoriasXML(fichero, configuracion);
                    break;
                default:
                    throw new IllegalArgumentException("Formato desconocido: " + formato);
            }
        } catch (IOException | RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException("No se ha podido leer el " + formato + ": " + ex.getMessage(), ex);
        }
        this.categorias = new ArrayList<>(leidas); // solo si la lectura ha salido bien
    }

    // ------------------------------------------------------------------ los programas tu

    /**
     * TAREA 7. Se llama UNA vez, al arrancar.
     *
     * PASOS:
     *   1. Ruta del fichero: directorioConfig.resolve("conf.properties").
     *      Construye el mensaje con la RUTA ABSOLUTA (toAbsolutePath().normalize()).
     *   2. Si EXISTE -> Configuracion.cargar(...), guardala en el atributo "configuracion"
     *      y el mensaje contiene la palabra EXISTE.
     *   3. Si NO existe -> Configuracion.porDefecto() y el mensaje contiene NO EXISTE.
     *   4. Si existe pero falla al leerla -> valores por defecto; el mensaje contiene EXISTE
     *      y el motivo del error.
     *   5. iniciar() NUNCA lanza excepciones: todo problema va DENTRO del mensaje.
     *
     * Ejemplo de mensaje:
     *   Fichero de configuracion: C:\proy\config\conf.properties -> EXISTE. Opciones leidas.
     *
     * TODO: implementar.
     */
    @Override
    public String iniciar() {
        throw new UnsupportedOperationException("TODO: iniciar (tarea 7)");
    }

    /**
     * TAREA 8. Devuelve la caratula de una pelicula, o null si no tiene.
     *
     * PASOS:
     *   1. Las rutas son directorioConfig.resolve("imagenes.dat") y ("indice.dat").
     *   2. Si NO existe alguno de los dos ficheros -> return null (no es un error).
     *   3. Si existen: AlmacenImagenes en un try-with-resources, abrir() y obtener(idPelicula).
     *      Abre y cierra el almacen en cada operacion.
     *
     * TODO: implementar.
     */
    @Override
    public byte[] obtenerImagen(int idPelicula) throws IOException {
        throw new UnsupportedOperationException("TODO: obtenerImagen (tarea 8)");
    }

    /**
     * TAREA 9. Crea una categoria con un id nuevo y la guarda en memoria.
     *
     * PASOS:
     *   1. Si el nombre es null o esta en blanco -> IllegalArgumentException con mensaje claro.
     *   2. id = GeneradorIds.siguienteIdCategoria(categorias)   (ya esta hecho).
     *   3. new Categoria(id, nombre), anadela a "categorias" y devuelvela.
     *
     * TODO: implementar.
     */
    @Override
    public Categoria crearCategoria(String nombre) {
        throw new UnsupportedOperationException("TODO: crearCategoria (tarea 9)");
    }

    /**
     * TAREA 9. Crea una pelicula con un id nuevo, dentro de la categoria indicada.
     *
     * PASOS:
     *   1. Si falta el titulo -> IllegalArgumentException.
     *   2. Busca la categoria por su id. Si NO existe -> IllegalArgumentException.
     *   3. id = GeneradorIds.siguienteIdPelicula(categorias)   (ya esta hecho).
     *   4. Crea la Pelicula, anadela A ESA CATEGORIA (getPeliculas().add(...)) y devuelvela.
     *
     * TODO: implementar.
     */
    @Override
    public Pelicula crearPelicula(int idCategoria, String titulo, String director, int anio, int duracionMinutos) {
        throw new UnsupportedOperationException("TODO: crearPelicula (tarea 9)");
    }
}
