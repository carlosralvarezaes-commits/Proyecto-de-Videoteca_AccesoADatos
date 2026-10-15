/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videoteca.persistencia;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;

/**
 *
 * @author carlos.raul.alvareza
 */
public class VideotecaHandler extends DefaultHandler {
    private  ArrayList<Categoria> categorias;

    
   

    @Override
    public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {
        Categoria categoriaActual;

        switch (qName) {
            case "videoteca":
                    categorias = new ArrayList<>();

                break;
            case "categoria":
                categoriaActual=new Categoria();
                categoriaActual.setId(Integer.parseInt(att.getValue("id")));
                categoriaActual.setNombre(att.getValue("nombre"));
                categorias.add(categoriaActual);
                
                break;
            case "pelicula":
                Pelicula peli;
                peli=new Pelicula();
                peli.setId(Integer.parseInt(att.getValue("id")));
                peli.setTitulo(att.getValue("titulo"));
                peli.setDirector(att.getValue("director"));
                peli.setAnio(Integer.parseInt(att.getValue("anio")));
                        
                
                
                break;
        }
    }
    
    public ArrayList<Categoria>obtenerResultado(){
        return categorias;
    }





    
}
