package videoteca.persistencia;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

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

    /**
     * Libreria con la que se lee o se escribe un JSON.
     */
    public enum TecnicaJson {GSON, JACKSON}

    /**
     * Tecnica con la que se LEE un XML.
     */
    public enum TecnicaLecturaXml {SAX, JAXB, DOM}

    /**
     * Tecnica con la que se ESCRIBE un XML (SAX no puede escribir).
     */
    public enum TecnicaEscrituraXml {JAXB, DOM}

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

    /**
     * Valores por defecto: sobrescribir=false, gson, gson, dom, dom.
     */
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
     * <p>
     * PASOS:
     * 1. ComprobacionRutas.comprobarImportacion(fichero)  (asi tambien lanza
     * ExcepcionDirectorioNoExiste / ExcepcionFicheroNoExiste).
     * 2. new Properties() y load(reader), con un reader de Files.newBufferedReader(fichero).
     * 3. Para cada clave: getProperty(clave). Si es null -> su valor por defecto
     * (los puedes sacar de porDefecto()). Si esta, conviertela al valor del enumerado
     * (trim() y toUpperCase() ayudan; Enum.valueOf o un switch).
     * 4. return new Configuracion(...) con los 5 valores.
     * <p>
     * TODO: implementar.
     */
    public static Configuracion cargar(Path fichero) throws IOException, ExcepcionDirectorioNoExiste, ExcepcionFicheroNoExiste {

        ComprobacionRutas.comprobarImportacion(fichero);

        Properties p = new Properties();
        try(BufferedReader br = new BufferedReader(new FileReader(fichero.toFile()))){
            p.load(br);
        }

        Configuracion def = porDefecto();

        //Ingresa valor default
        boolean sobrescribir = def.isSobrescribir();

        //Ingresa el valor del fichero
        String  valorSobrescribir= p.getProperty("sobrescribir");

        //Comprueba si el valor del fichero no es nulo
        if(valorSobrescribir !=null){
            //pasamos el valor del fichero a un valor de tipo boolean
            sobrescribir=Boolean.parseBoolean(valorSobrescribir.trim());
        }

        //Ingresa el valor default
        TecnicaJson importacionJson = def.getImportacionJson();

        //ingresa el valor del fhicero
        String valorImpJson = p.getProperty("importacion_json");

        //Comprueba que el valor no sea nulo
        if(valorImpJson !=null){
            //ingresa su mismo valor, pero sin espacios y en MAYUSCULAS
            //osea si era "gson " ahora es "GSON" <-sin espacios y en mayus.
            valorImpJson= valorImpJson.trim().toUpperCase();

            //si el valor dentro de la variable "importada" es igual a gson <-(ignoreCase)
            if(valorImpJson.equalsIgnoreCase("gson")){
                //si es true , entonces es GSON
                importacionJson= TecnicaJson.GSON;
            }else if(valorImpJson.equalsIgnoreCase("jackson")){
                //es falso , pero su valor es jackson entonces es Jackson
                importacionJson=TecnicaJson.JACKSON;
            }
        }

        TecnicaJson exportacionJson = def.getExportacionJson();
        String valorExpJson = p.getProperty("exportacion_json");

        if(valorExpJson != null){

            valorExpJson=valorExpJson.toUpperCase();
            if(valorExpJson.equalsIgnoreCase("gson")){
                exportacionJson=TecnicaJson.GSON;
            } else if (valorExpJson.equalsIgnoreCase("jackson")) {
                exportacionJson=TecnicaJson.JACKSON;
            }

        }


        TecnicaLecturaXml importacionXML= def.getImportacionXml();
        String valorImpXML  = p.getProperty("importacion_xml");

        if(valorImpXML!=null){


            valorImpXML=valorImpXML.trim().toUpperCase();


            if(valorImpXML.equalsIgnoreCase("sax")){
                importacionXML=TecnicaLecturaXml.SAX;
            } else if (valorImpXML.equalsIgnoreCase("jaxb")) {
                importacionXML=TecnicaLecturaXml.JAXB;
            } else if (valorImpXML.equalsIgnoreCase("dom")) {
                importacionXML=TecnicaLecturaXml.DOM;
            }
        }


        TecnicaEscrituraXml exportacionXML = def.getExportacionXml();
        String valorExpXML = p.getProperty("exportacion_xml");

        if(valorExpXML !=null) {

            valorExpXML = valorExpXML.trim();

            if (valorExpXML.equalsIgnoreCase("dom")) {
                exportacionXML = TecnicaEscrituraXml.DOM;
            } else if (valorExpXML.equalsIgnoreCase("jaxb")) {
                exportacionXML = TecnicaEscrituraXml.JAXB;
            }
        }


        return new Configuracion(sobrescribir,importacionJson,exportacionJson,importacionXML,exportacionXML);
    }
}
