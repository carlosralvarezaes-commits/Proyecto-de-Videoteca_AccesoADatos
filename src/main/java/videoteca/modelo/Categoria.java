package videoteca.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Una categoría de películas. Es el lado «1» de la relación 1 a N: contiene
 * su propia lista de {@link Pelicula}.
 *
 * <p>Esta clase ya está terminada.</p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Categoria {

    @XmlAttribute(name="id")
    private int id;

    @XmlAttribute(name="nombre")
    private String nombre;

    @XmlElement(name="pelicula")
    private List<Pelicula> peliculas = new ArrayList<>();

    //Constructor para JAXB
    public Categoria(){ }


    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String toPrintTxt(){
        String retornar="CATEGORIA;"+id+";"+nombre+"\n";


        for(Pelicula elemento: peliculas){
            retornar+= elemento.toPlainTxt();
        }

        return retornar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria otra = (Categoria) o;
        return id == otra.id
                && Objects.equals(nombre, otra.nombre)
                && Objects.equals(peliculas, otra.peliculas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, peliculas);
    }

    @Override
    public String toString() {
        return id + " " + nombre + " (" + peliculas.size() + " películas)";
    }
}
