package videoteca.persistencia;

import videoteca.modelo.Categoria;
import videoteca.modelo.Videoteca;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.nio.file.Path;
import java.util.List;

/**
 * XML con SAX, JAXB o DOM.
 *
 * TAREA 3 (guardar) y TAREA 6 (obtener). Apuntes: apartados 8 (DOM), 9 (SAX) y 10 (JAXB).
 * Elige UNA tecnica para guardar y UNA para leer (pueden ser distintas).
 *
 *
 * FORMATO del XML (todo son atributos):
 *     <videoteca>
 *         <categoria id="1" nombre="Ciencia ficcion">
 *             <pelicula id="101" titulo="Blade Runner" director="Ridley Scott"
 *                       anio="1982" duracionMinutos="117"/>
 *         </categoria>
 *         <categoria id="3" nombre="Terror"/>
 *     </videoteca>
 */
public final class GestorXML {

    private GestorXML() {





    }

    // ------------------------------------------------------------ metodos base (ya hechos)

    public static List<Categoria> obtenerCategoriasXML(Path fichero, Configuracion configuracion) throws Exception {
        if (configuracion.getImportacionXml() == null) {
            throw new IllegalStateException("No hay tecnica configurada para importacion_xml (sax, jaxb o dom)");
        }
        switch (configuracion.getImportacionXml()) {
            case SAX:
                return obtenerCategoriasXMLSAX(fichero);
            case JAXB:
                return obtenerCategoriasXMLJAXB(fichero);
            case DOM:
                return obtenerCategoriasXMLDOM(fichero);
            default:
                throw new IllegalStateException("Tecnica XML desconocida");
        }
    }

    public static void guardarCategoriasXML(List<Categoria> categorias, Path fichero, Configuracion configuracion)
            throws Exception {
        if (configuracion.getExportacionXml() == null) {
            throw new IllegalStateException("No hay tecnica configurada para exportacion_xml (jaxb o dom)");
        }
        switch (configuracion.getExportacionXml()) {
            case JAXB:
                guardarCategoriasXMLJAXB(categorias, fichero);
                break;
            case DOM:
                guardarCategoriasXMLDOM(categorias, fichero);
                break;
            default:
                throw new IllegalStateException("Tecnica XML desconocida");
        }
    }

    // ------------------------------------------------------------ SAX (solo lee)

    /**
     * TAREA 6 (variante SAX).
     *
     * PASOS:
     *   1. CREA TU PROPIA CLASE VideotecaHandler (en este paquete) que extienda DefaultHandler.
     *
     * TODO: implementar (y crear VideotecaHandler) o dejar asi si eliges otra tecnica.
     */
    public static List<Categoria> obtenerCategoriasXMLSAX(Path fichero) throws Exception {
        
        
        
        return null;
    }

    // ------------------------------------------------------------ JAXB

    /**
     * TAREA 6 (variante JAXB).
     *
     * TODO: implementar o dejar asi si eliges otra tecnica.
     */
    public static List<Categoria> obtenerCategoriasXMLJAXB(Path fichero) throws Exception {
        throw new UnsupportedOperationException("Tecnica no implementada: JAXB (obtener)");
    }

    /**
     * TAREA 3 (variante JAXB). El modelo (Categoria, Pelicula, Videoteca) YA lleva las
     * anotaciones necesarias.

     * TODO: implementar o dejar asi si eliges DOM.
     */
    public static void guardarCategoriasXMLJAXB(List<Categoria> categorias, Path fichero) throws Exception {
        Videoteca videoteca= new Videoteca(categorias);
        JAXBContext context= JAXBContext.newInstance(Videoteca.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(videoteca,fichero.toFile());

    }

    // ------------------------------------------------------------ DOM

    /**
     * TAREA 6 (variante DOM).
     *
     * TODO: implementar o dejar asi si eliges otra tecnica.
     */
    public static List<Categoria> obtenerCategoriasXMLDOM(Path fichero) throws Exception {
        throw new UnsupportedOperationException("Tecnica no implementada: DOM (obtener)");
    }

    /**
     * TAREA 3 (variante DOM).
     *
     * TODO: implementar o dejar asi si eliges JAXB.
     */
    public static void guardarCategoriasXMLDOM(List<Categoria> categorias, Path fichero) throws Exception {
        throw new UnsupportedOperationException("Tecnica no implementada: DOM (guardar)");
    }
}
