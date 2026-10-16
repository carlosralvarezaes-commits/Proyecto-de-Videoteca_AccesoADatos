package videoteca.persistencia;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;
import videoteca.excepciones.ExcepcionOperacionCancelada;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Comprobaciones previas a importar y a exportar.
 *
 * TAREA 1 (comprobarExportacion) y TAREA 4 (comprobarImportacion).
 * Apuntes: apartado 2 (NIO.2) y apartado 14 (excepciones).
 *
 * Las 3 excepciones ya estan creadas (paquete videoteca.excepciones): solo hay
 * que lanzarlas con "throw new ...".
 */
public final class ComprobacionRutas {

    private ComprobacionRutas() {
    }

    /**
     * TAREA 4. Antes de IMPORTAR: el directorio Y el fichero tienen que existir.
     *
     * PASOS:
     *   1. Saca el directorio: fichero.toAbsolutePath().getParent()
     *      (con toAbsolutePath() getParent() nunca es null, aunque el Path sea solo "datos.txt").
     *   2. Si NO es un directorio (Files.isDirectory) -> throw new ExcepcionDirectorioNoExiste(directorio).
     *   3. Si NO es un fichero (Files.isRegularFile) -> throw new ExcepcionFicheroNoExiste(fichero).
     *
     * TODO: implementar.
     */
    public static void comprobarImportacion(Path fichero)throws ExcepcionDirectorioNoExiste, ExcepcionFicheroNoExiste {
        Path directorio=fichero.toAbsolutePath().getParent();

        if(directorio==null && !Files.isDirectory(directorio) ){
            throw new ExcepcionDirectorioNoExiste(directorio);
        }

        if(!Files.isRegularFile(directorio)) {
            throw new ExcepcionFicheroNoExiste(directorio);
        }





    }

    /**
     * TAREA 1. Antes de EXPORTAR.
     *
     * PASOS:
     *   1. El directorio tiene que existir -> si no, ExcepcionDirectorioNoExiste.
     *   2. Si el fichero NO existe: no pasa nada (se creara al escribir).
     *   3. Si el fichero YA existe y sobrescribir es false -> ExcepcionOperacionCancelada.
     *   4. Si el fichero YA existe y sobrescribir es true -> no pasa nada (se sobrescribira).
     *
     * PISTAS: Files.exists, Files.isDirectory.
     *
     * TODO: implementar.
     */
    public static void comprobarExportacion(Path fichero, boolean sobrescribir)throws ExcepcionDirectorioNoExiste, ExcepcionOperacionCancelada {

        Path dir= fichero.toAbsolutePath().getParent();

        if(!Files.isDirectory(dir)){
            throw new ExcepcionDirectorioNoExiste(fichero);
        }else {
            if(Files.exists(fichero) && sobrescribir==false){
                  throw new ExcepcionOperacionCancelada(fichero);
            }
        }




    }
}
