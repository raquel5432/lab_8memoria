/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoria;

import java.text.SimpleDateFormat;

/**
 *
 * @author alira
 */
public class ElementoArchivo {
 
    private String nombre;
    private String rutaCompleta;
    private long tamanoEnBytes;
    private String extension;
    private long fechaModificada;
    private boolean esDirectortio;
    
    private static final SimpleDateFormat FORMATO_FECHA= 
            new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public ElementoArchivo(String nombre,String rutaCOmpletada, long tamanoEnBytes, String extension, long fechaModificada, boolean esDirectortio){
     
        this.nombre = nombre;
        this.rutaCompleta = rutaCompleta;
        this.tamanoEnBytes = tamanoEnBytes;
        this.extension = extension;
        this.esDirectortio = esDirectortio;
    }
}
