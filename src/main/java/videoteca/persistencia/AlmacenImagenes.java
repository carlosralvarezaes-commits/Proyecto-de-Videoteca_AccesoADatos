package videoteca.persistencia;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Map;

/**
 * Busca caratulas en un fichero de acceso directo. SOLO LECTURA.
 *
 * TAREA 8. Apuntes: apartado 6 (RandomAccessFile), apartado 7 (serializacion), UD1 EP 13.
 *
 * Los ficheros imagenes.dat e indice.dat están en la carpeta config.
 * Deberán buscarse esos ficheros tanto en el propio directorio como en cualquier subdirectorio 
 * dentro de él de forma recursiva ( directorioConfig/.../imagenes.dat, directorioConfig/.../.../imagenes.dat
 * , etc).
 * 
 * Al leer: se lee el registro ENTERO y se quitan los ceros del final ("trim" para bytes).
 * Es correcto porque las caratulas son PNG, y un PNG termina siempre en 0x82 (nunca en 0).
 */
public class AlmacenImagenes implements Closeable {

    public static final int TAMANO_REGISTRO = 100 * 1024;

    private final Path ficheroImagenes;
    private final Path ficheroIndice;
    private RandomAccessFile almacen;
    private Map<Integer, Integer> indice; // idPelicula -> numero de registro

    public AlmacenImagenes(Path ficheroImagenes, Path ficheroIndice) {
        this.ficheroImagenes = ficheroImagenes;
        this.ficheroIndice = ficheroIndice;
    }

    /**
     * Abre el fichero de imagenes y carga el indice.
     *
     * PASOS:
     *   1. RandomAccessFile en modo "r" (solo lectura) sobre ficheroImagenes
     *      (pide un File: usa ficheroImagenes.toFile()).
     *   2. indice = cargarIndice();
     *
     * TODO: implementar.
     */
    public void abrir() throws IOException {
        throw new UnsupportedOperationException("TODO: AlmacenImagenes.abrir (tarea 8)");
    }

    /**
     * Lee el indice de ficheroIndice DE OBJETO casteando a Map<Integer,Integer>).
     *
     * TODO: implementar.
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> cargarIndice() throws IOException {
        throw new UnsupportedOperationException("TODO: AlmacenImagenes.cargarIndice (tarea 8)");
    }

    /**
     * Devuelve los bytes de la imagen de esa pelicula, o null si no tiene ninguna.
     *
     * PASOS:
     *   1. Busca el registro en el indice (si no esta -> return null).
     *   2. seek(numRegistro * TAMANO_REGISTRO)  -> cuidado: usa (long) para que no desborde.
     *   3. Lee el registro ENTERO con readFully (un byte[TAMANO_REGISTRO]).
     *   4. Recorre el array desde el final mientras el byte sea 0.
     *   5. Devuelve solo la parte util (Arrays.copyOf).
     *
     * TODO: implementar.
     */
    public byte[] obtener(int idPelicula) throws IOException {
        throw new UnsupportedOperationException("TODO: AlmacenImagenes.obtener (tarea 8)");
    }

    @Override
    public void close() throws IOException {
        if (almacen != null) {
            almacen.close();
        }
    }
}
