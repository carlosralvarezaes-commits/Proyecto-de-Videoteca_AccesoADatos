package videoteca.excepciones;

import java.nio.file.Path;

/** Se lanza cuando el DIRECTORIO donde debe estar (o ir) un fichero no existe. */
public class ExcepcionDirectorioNoExiste extends Exception {

    private static final long serialVersionUID = 1L;

    public ExcepcionDirectorioNoExiste(Path directorio) {
        super("El directorio no existe: " + directorio);
    }
}
