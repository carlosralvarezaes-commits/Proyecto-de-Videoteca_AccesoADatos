package videoteca.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase «envoltorio»: el conjunto completo de categorías.
 *
 * <p>Hace falta porque tanto el XML (JAXB) como el JSON necesitan un único
 * elemento raíz. Esta clase ya está terminada.</p>
 */

@XmlRootElement(name="videoteca")
@XmlAccessorType(XmlAccessType.FIELD)
public class Videoteca {

    @XmlElement(name = "categoria")
    private List<Categoria> categorias = new ArrayList<>();

    //Constructor para JAXB
    public Videoteca(){ }

    public Videoteca(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
