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
    private Categoria categoriaActual;

    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {
        switch (qName) {
            case "videoteca":
                    categorias = new ArrayList<>();

                break;
            case "categoria":
                categoriaActual=new Categoria();
                categoriaActual.setId(Integer.parseInt(att.getValue("id")));
                categoriaActual.setNombre(att.getValue("nombre"));
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
    
    





    
}
