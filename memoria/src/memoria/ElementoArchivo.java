/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoria;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author alira
 */
public class ElementoArchivo {
 
    private String nombre;
    private String rutaCompleta;
    private long tamanoEnBytes;
    private String extension;
    private long fechaModificacion;
    private boolean esDirectorio;
    
    private static final SimpleDateFormat FORMATO_FECHA= 
            new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public ElementoArchivo(String nombre,String rutaCOmpletada, long tamanoEnBytes, String extension, long fechaModificada, boolean esDirectortio){
     
        this.nombre = nombre;
        this.rutaCompleta = rutaCompleta;
        this.tamanoEnBytes = tamanoEnBytes;
        this.extension = extension;
        this.esDirectorio = esDirectorio;
        
    }
    
    public static ElementoArchivo desdeFile(File archivo){
        String nombreArchivo=archivo.getName();
        String extensionArchivo=extraerExtension(nombreArchivo,
                                                  archivo.isDirectory());
        return new ElementoArchivo(
            nombreArchivo,
            archivo.getAbsolutePath(),
            archivo.length(),
            extensionArchivo,
            archivo.lastModified(),
            archivo.isDirectory()
        );
    }
    
    private static String extraerExtension(String nombreArchivo,
                                            boolean esDirectorio){
        if(esDirectorio){
            return "Carpeta";
        }
        int posicionPunto=nombreArchivo.lastIndexOf('.');
        if(posicionPunto>0&&posicionPunto<nombreArchivo.length()-1){
            return nombreArchivo.substring(posicionPunto+1).toLowerCase();
        }
        return "Archivo";
    }
    
    public String getTamanoLegible(){
        if(esDirectorio){
            return "-";
        }
        if(tamanoEnBytes<1024){
            return tamanoEnBytes+" B";
        }
        else if(tamanoEnBytes<1024*1024){
            return String.format("%.1f KB",tamanoEnBytes/1024.0);
        }
        else if(tamanoEnBytes<1024L*1024*1024){
            return String.format("%.1f MB",tamanoEnBytes/(1024.0*1024));
        }
        else{
            return String.format("%.1f GB",tamanoEnBytes/(1024.0*1024*1024));
        }
    }
    
    public String getFechaFormateada(){
        return FORMATO_FECHA.format(new Date(fechaModificacion));
    }
    
    public String getNombre(){ return nombre; }
    public String getRutaCompleta(){ return rutaCompleta; }
    public long getTamanoEnBytes(){ return tamanoEnBytes; }
    public String getExtension(){ return extension; }
    public long getFechaModificacion(){ return fechaModificacion; }
    public boolean esDirectorio(){ return esDirectorio; }
    public void setNombre(String nombre){ this.nombre=nombre; }
    public void setRutaCompleta(String rutaCompleta){
        this.rutaCompleta=rutaCompleta;
    }
    
    @Override
    public String toString(){
        return String.format("[%s] %s (%s) — %s",
            esDirectorio?"DIR":"ARC",
            nombre,
            getTamanoLegible(),
            getFechaFormateada()
        );
    }
}
