package videoteca.persistencia;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Opciones del programa, leidas UNA vez de ./config/conf.properties.
 *
 * TAREA 7 (cargar). Apuntes: apartado 13 (ficheros .properties).
 *
 * <pre>
 * sobrescribir=false          (true | false)
 * importacion_json=gson       (gson | jackson)
 * exportacion_json=gson       (gson | jackson)
 * importacion_xml=dom         (sax | jaxb | dom)
 * exportacion_xml=dom         (jaxb | dom)
 * </pre>
 * Si falta una clave, vale su valor por defecto (el de la derecha).
 *
 * Los enumerados, el constructor, porDefecto() y los getters ya estan hechos.
 */
public final class Configuracion {

    /** Libreria con la que se lee o se escribe un JSON. */
    public enum TecnicaJson { GSON, JACKSON }

    /** Tecnica con la que se LEE un XML. */
    public enum TecnicaLecturaXml { SAX, JAXB, DOM }

    /** Tecnica con la que se ESCRIBE un XML (SAX no puede escribir). */
    public enum TecnicaEscrituraXml { JAXB, DOM }

    private final boolean sobrescribir;
    private final TecnicaJson importacionJson;
    private final TecnicaJson exportacionJson;
    private final TecnicaLecturaXml importacionXml;
    private final TecnicaEscrituraXml exportacionXml;

    public Configuracion(boolean sobrescribir, TecnicaJson importacionJson, TecnicaJson exportacionJson,
                         TecnicaLecturaXml importacionXml, TecnicaEscrituraXml exportacionXml) {
        this.sobrescribir = sobrescribir;
        this.importacionJson = importacionJson;
        this.exportacionJson = exportacionJson;
        this.importacionXml = importacionXml;
        this.exportacionXml = exportacionXml;
    }

    /** Valores por defecto: sobrescribir=false, gson, gson, dom, dom. */
    public static Configuracion porDefecto() {
        return new Configuracion(false, TecnicaJson.GSON, TecnicaJson.GSON,
                TecnicaLecturaXml.DOM, TecnicaEscrituraXml.DOM);
    }

    public boolean isSobrescribir() {
        return sobrescribir;
    }

    public TecnicaJson getImportacionJson() {
        return importacionJson;
    }

    public TecnicaJson getExportacionJson() {
        return exportacionJson;
    }

    public TecnicaLecturaXml getImportacionXml() {
        return importacionXml;
    }

    public TecnicaEscrituraXml getExportacionXml() {
        return exportacionXml;
    }

    /**
     * Lee el fichero .properties y devuelve la configuracion.
     *
     * PASOS:
     *   1. ComprobacionRutas.comprobarImportacion(fichero)  (asi tambien lanza
     *      ExcepcionDirectorioNoExiste / ExcepcionFicheroNoExiste).
     *   2. new Properties() y load(reader), con un reader de Files.newBufferedReader(fichero).
     *   3. Para cada clave: getProperty(clave). Si es null -> su valor por defecto
     *      (los puedes sacar de porDefecto()). Si esta, conviertela al valor del enumerado
     *      (trim() y toUpperCase() ayudan; Enum.valueOf o un switch).
     *   4. return new Configuracion(...) con los 5 valores.
     *
     * TODO: implementar.
     */
    public static Configuracion cargar(Path fichero)
            throws IOException, ExcepcionDirectorioNoExiste, ExcepcionFicheroNoExiste {
        throw new UnsupportedOperationException("TODO: Configuracion.cargar (tarea 7)");
    }
}
