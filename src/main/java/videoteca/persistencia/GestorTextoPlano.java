package videoteca.persistencia;

import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Guardar y leer la videoteca en texto plano.
 *
 * TAREA 1 (guardarCategoriasTexto) y TAREA 4 (obtenerCategoriasTexto).
 * Apuntes: apartado 4 (BufferedReader / BufferedWriter).
 *
 * FORMATO (una linea por registro):
 *     CATEGORIA;id;nombre
 *     PELICULA;id;titulo;director;anio;duracionMinutos
 *
 * Cada linea PELICULA pertenece a la ULTIMA CATEGORIA leida antes.
 *
 * Ejemplo:
 *     CATEGORIA;1;Ciencia ficcion
 *     PELICULA;101;Blade Runner;Ridley Scott;1982;117
 *     CATEGORIA;3;Terror
 */
public final class  GestorTextoPlano {

    private GestorTextoPlano() {
    }

    /**
     * TAREA 1. Escribe las categorias (y sus peliculas) en el fichero.
     *
     * TODO: implementar.
     */
    public static void guardarCategoriasTexto(List<Categoria> categorias, Path fichero) throws IOException{
        try(BufferedWriter bw= new BufferedWriter(new FileWriter(fichero.toFile()))){
            for (Categoria categoria: categorias) {
                bw.write(categoria.toPrintTxt());                
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * TAREA 4. Lee el fichero y devuelve la lista de categorias, cada una con sus peliculas.
     *
     * PISTAS: Integer.parseInt lanza NumberFormatException si no es un numero.
     *
     * TODO: implementar.
     */
    public static List<Categoria> obtenerCategoriasTexto(Path fichero) throws IOException {
        List<Categoria> listCategorias= new ArrayList<>();
        Categoria categoriaActual = null;
        Pelicula peliculaActual=null;
        int numeroLinea=0;

        try(BufferedReader br = new BufferedReader(new FileReader(fichero.toFile()))){
            String linea;
            while((linea=br.readLine()) !=null){
                //Aumenta el numero de la linea (empieza en 1 )
                numeroLinea++;
                //elimina espacios en blanco de esa linea
                linea= linea.trim();
                //Salta linea en blanco
                if(linea.isEmpty()){
                    continue;
                }
                //agarra esa parte cada vez que se encuentra con ";"
                String[] partes= linea.split(";",-1);
                String tipo= partes[0];
                try {
                    switch (tipo) {
                        case ("CATEGORIA") -> {
                            if(partes.length !=3){
                                throw new IOException("Error en la linea "+numeroLinea+" : Categoria necesita 2 campos");
                            }

                            int id = Integer.parseInt(partes[1]);
                            categoriaActual = new Categoria(id, partes[2]);
                            listCategorias.add(categoriaActual);
                        }
                        case ("PELICULA") -> {
                            if(partes.length != 6){
                                throw new IOException("Error en la linea "+numeroLinea+" : Pelicula necesita 6 campos");
                            }
                            if (categoriaActual == null) {
                                continue;
                            }
                            peliculaActual = new Pelicula(Integer.parseInt(partes[1]), partes[2], partes[3], Integer.parseInt(partes[4]), Integer.parseInt(partes[5]));
                            categoriaActual.getPeliculas().add(peliculaActual);
                        }
                        default -> throw new IOException("Error en la linea "+ numeroLinea+" : tipo desconocido ('"+tipo+"')");
                    }
                }catch(NumberFormatException e){
                    throw new IOException("Error en la Linea "+numeroLinea+":"+ e.getMessage());
                }



        }
        return listCategorias;
        }
    }
}
