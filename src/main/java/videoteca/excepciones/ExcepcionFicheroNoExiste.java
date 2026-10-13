package videoteca.excepciones;

import java.nio.file.Path;

/** Se lanza al IMPORTAR cuando el fichero indicado no existe. */
public class ExcepcionFicheroNoExiste extends Exception {

    private static final long serialVersionUID = 1L;

    public ExcepcionFicheroNoExiste(Path fichero) {
        super("El fichero no existe: " + fichero);
    }
}
