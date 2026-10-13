package videoteca.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

/**
 * Una película. Pertenece a una {@link Categoria} (relación 1 a N).
 *
 * <p>Esta clase ya está terminada. Lleva anotaciones de JAXB (las usa
 * GestorXML al escribir XML); GSON no necesita ninguna.</p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Pelicula {

    @XmlAttribute(name="id")
    private int id;

    @XmlAttribute(name="titulo")
    private String titulo;


    @XmlAttribute(name="director")
    private String director;


    @XmlAttribute(name="anio")
    private int anio;


    @XmlAttribute(name="duracionMinutos")
    private int duracionMinutos;

    public Pelicula(){ }

    public Pelicula(int id, String titulo, String director, int anio, int duracionMinutos) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.duracionMinutos = duracionMinutos;
    }




    public String toPlainTxt(){
        return "PELICULA;"+id+";"+titulo+";"+director+";"+anio+";"+duracionMinutos+"\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pelicula)) return false;
        Pelicula otra = (Pelicula) o;
        return id == otra.id
                && anio == otra.anio
                && duracionMinutos == otra.duracionMinutos
                && Objects.equals(titulo, otra.titulo)
                && Objects.equals(director, otra.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, director, anio, duracionMinutos);
    }

    @Override
    public String toString() {
        return id + " " + titulo + " (" + anio + ")";
    }
}
