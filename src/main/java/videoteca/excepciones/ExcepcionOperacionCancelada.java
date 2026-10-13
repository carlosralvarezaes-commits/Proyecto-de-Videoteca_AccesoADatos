package videoteca.excepciones;

import java.nio.file.Path;

/**
 * Se lanza al EXPORTAR cuando el fichero ya existe y NO se ha marcado la
 * opcion de sobrescribir: la operacion se cancela sin tocar el fichero.
 */
public class ExcepcionOperacionCancelada extends Exception {

    private static final long serialVersionUID = 1L;

    public ExcepcionOperacionCancelada(Path fichero) {
        super("El fichero ya existe y no se ha marcado sobrescribir: " + fichero);
    }
}
