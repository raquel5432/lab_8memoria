/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author andres
 */
public class OrganizadorArchivos {
    private static final Map<String,String> MAPA_EXTENSIONES=construirMapaExtensiones();
    private static Map<String,String> construirMapaExtensiones(){
        Map<String,String> mapa=new HashMap<>();
        mapa.put("jpg","Imagenes");
        mapa.put("jpeg","Imagenes");
        mapa.put("png","Imagenes");
        mapa.put("gif","Imagenes");
        mapa.put("bmp","Imagenes");
        mapa.put("webp","Imagenes");
        mapa.put("pdf","Documentos");
        mapa.put("docx","Documentos");
        mapa.put("doc","Documentos");
        mapa.put("txt","Documentos");
        mapa.put("xlsx","Documentos");
        mapa.put("pptx","Documentos");
        mapa.put("mp3","Musica");
        mapa.put("wav","Musica");
        mapa.put("ogg","Musica");
        mapa.put("flac","Musica");
        mapa.put("aac","Musica");
        return mapa;
    }
    public ResultadoOperacion organizarCarpeta(File carpetaSeleccionada){
        if(carpetaSeleccionada==null || !carpetaSeleccionada.isDirectory()){
            return ResultadoOperacion.error("La ruta seleccionada no es una carpeta válida.");
        }
        File[] contenido=carpetaSeleccionada.listFiles();
        if(contenido==null||contenido.length==0){
            return ResultadoOperacion.error("La carpeta está vacía, no hay nada que organizar.");
        }
        int archivosMovidos=0;
        int archivosOmitidos=0;
        Map<String,Integer> contadorPorCategoria=new HashMap<>();
        for(File archivo:contenido){
            if(archivo.isDirectory()){ 
                continue;
            }
            String extension=extraerExtension(archivo.getName());
            String nombreCarpetaDestino=MAPA_EXTENSIONES.get(extension);
            if(nombreCarpetaDestino==null){
                archivosOmitidos++;
                continue;
            }
            ResultadoOperacion resultadoMover=moverArchivo(archivo,carpetaSeleccionada,nombreCarpetaDestino);
            if(resultadoMover.fueExitoso()){
                archivosMovidos++;
                contadorPorCategoria.merge(nombreCarpetaDestino,1,Integer::sum);
            }
            else{
                archivosOmitidos++;
            }
        }
        return construirResumen(archivosMovidos,archivosOmitidos,contadorPorCategoria);
    }
    public String obtenerCarpetaDestino(String extension){
        if(extension==null){
            return null;
        }
        return MAPA_EXTENSIONES.get(extension.toLowerCase().trim());
    }
    private ResultadoOperacion moverArchivo(File archivo, File carpetaRaiz, String nombreSubcarpeta){
       File subcarpetaDestino=new File(carpetaRaiz,nombreSubcarpeta);
        if(!subcarpetaDestino.exists()){
            boolean creada=subcarpetaDestino.mkdir();
            if(!creada){
                return ResultadoOperacion.error("No se pudo crear la carpeta "+nombreSubcarpeta+".");
            }
        } 
        String nombreFinal=resolverNombreSinConflicto(archivo, subcarpetaDestino);
        File destino=new File(subcarpetaDestino, nombreFinal);
        try{
            Files.move(archivo.toPath(),destino.toPath(),StandardCopyOption.REPLACE_EXISTING);
            return ResultadoOperacion.exito("Movido: "+archivo.getName());
        }
        catch(IOException error){
            return ResultadoOperacion.error("No se pudo mover: "+archivo.getName());
        }
    }
    private String extraerExtension(String nombreArchivo){
        int posicionPunto=nombreArchivo.lastIndexOf('.');
        if(posicionPunto>0 && posicionPunto<nombreArchivo.length()-1){
            return nombreArchivo.substring(posicionPunto+1).toLowerCase();
        }
        return "";
    }
    private String resolverNombreSinConflicto(File archivo, File carpetaDestino){
        String nombreOriginal=archivo.getName();
        if(!new File(carpetaDestino,nombreOriginal).exists()){
            return nombreOriginal;
        }
        int posicionPunto=nombreOriginal.lastIndexOf('.');
        String nombreBase;
        String extensionConPunto;
        if(posicionPunto>0){
            nombreBase=nombreOriginal.substring(0,posicionPunto);
            extensionConPunto=nombreOriginal.substring(posicionPunto);
        }
        else{
            nombreBase=nombreOriginal;
            extensionConPunto="";
        }
        String nombreCandidato=nombreBase+"_org"+extensionConPunto;
        int contador=2;
        while(new File(carpetaDestino,nombreCandidato).exists()){
            nombreCandidato=nombreBase+"_org"+contador+extensionConPunto;
            contador++;
        }
        return nombreCandidato;
    }
    private ResultadoOperacion construirResumen(int movidos,int omitidos,Map<String,Integer> contadorPorCategoria){
        if(movidos==0){
            return ResultadoOperacion.exito(
                "No se encontraron archivos organizables.");
        }
        StringBuilder resumen=new StringBuilder();
        resumen.append(movidos).append(" archivo(s) organizado(s): ");
        contadorPorCategoria.forEach((categoria,cantidad)->
            resumen.append(cantidad).append(" → ").append(categoria).append("  "));
        if(omitidos>0){
            resumen.append("(").append(omitidos).append(" omitido(s) sin categoría)");
        }
        return ResultadoOperacion.exito(resumen.toString().trim());
    }
}
