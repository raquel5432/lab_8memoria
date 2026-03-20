/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import memoria.ElementoArchivo;
import memoria.ListaEnlazadaArchivos;
/**
 *
 * @author andres
 */
public class GestorArchivos {
    private final PortapapelesArchivos portapapeles;
    public GestorArchivos(){
        portapapeles=PortapapelesArchivos.obtenerInstancia();
    }
    public ResultadoOperacion crearCarpeta(File carpetaActual, String nombreNuevaCarpeta){
        ResultadoOperacion validacion=validarNombre(carpetaActual,nombreNuevaCarpeta);
        if(!validacion.fueExitoso()){
            return validacion;
        }
        File nuevaCarpeta=new File(carpetaActual,nombreNuevaCarpeta);
        boolean creada=nuevaCarpeta.mkdir();
        if(creada){
            return ResultadoOperacion.exito(
                "Carpeta \""+nombreNuevaCarpeta+"\" creada correctamente.");
        }
        return ResultadoOperacion.error(
            "No se pudo crear la carpeta. Verifica los permisos.");
    }
    public ResultadoOperacion renombrarElemento(File elemento,String nuevoNombre){
        File carpetaPadre=elemento.getParentFile();
        ResultadoOperacion validacion=validarNombre(carpetaPadre,nuevoNombre);
        if(!validacion.fueExitoso()){
            return validacion;
        }
        File elementoRenombrado=new File(carpetaPadre,nuevoNombre);
        boolean renombrado=elemento.renameTo(elementoRenombrado);
        if(renombrado){
            return ResultadoOperacion.exito("\""+elemento.getName()+"\" renombrado a \""+nuevoNombre+"\".");
        }
        return ResultadoOperacion.error(
            "No se pudo renombrar. Verifica que no esté en uso.");
    }
    public ResultadoOperacion copiarElementos(
            List<File> elementosSeleccionados){
        if(elementosSeleccionados==null||elementosSeleccionados.isEmpty()){
            return ResultadoOperacion.error("No hay elementos seleccionados para copiar.");
        }
        portapapeles.copiar(elementosSeleccionados);
        int cantidad=elementosSeleccionados.size();
        String mensaje=cantidad==1 ? "1 elemento copiado al portapapeles.":cantidad+" elementos copiados al portapapeles.";
        return ResultadoOperacion.exito(mensaje);
    }
    public ResultadoOperacion pegarElementos(File destino){
        if(!portapapeles.tieneContenido()){
            return ResultadoOperacion.error("El portapapeles está vacío. Copia elementos primero.");
        }
        if(!destino.isDirectory()){
            return ResultadoOperacion.error("El destino no es una carpeta válida.");
        }
        int pegados=0;
        int fallidos=0;
        for(File elementoACopiar:portapapeles.obtenerElementos()){
            try{
                copiarEnDestino(elementoACopiar,destino);
                pegados++;
            }catch(IOException error){
                fallidos++;
            }
        }
        if(fallidos==0){
            String mensaje=pegados==1 ? "1 elemento pegado correctamente.":pegados+" elementos pegados correctamente.";
            return ResultadoOperacion.exito(mensaje);
        }
        return ResultadoOperacion.error(
            pegados+" pegados, "+fallidos+" fallaron al pegar.");
    }
    public ListaEnlazadaArchivos listarContenido(File carpeta){
        ListaEnlazadaArchivos lista=new ListaEnlazadaArchivos();
        if(carpeta==null || !carpeta.isDirectory()){ 
            return lista;
        }
        File[] contenido=carpeta.listFiles();
        if(contenido==null){ 
            return lista;
        }
        lista.cargarDesdeArregloDeFiles(contenido);
        return lista;
    }
    public ResultadoOperacion validarNombre(File carpetaPadre,String nombre){
        if(nombre==null||nombre.trim().isEmpty()){
            return ResultadoOperacion.error("El nombre no puede estar vacío.");
        }
        File posibleDuplicado=new File(carpetaPadre,nombre.trim());
        if(posibleDuplicado.exists()){
            return ResultadoOperacion.error("Ya existe un elemento con el nombre \""+nombre+"\" en esta carpeta.");
        }
        return ResultadoOperacion.exito("Nombre válido.");
    }
    private void copiarEnDestino(File origen,File carpetaDestino) throws IOException{
        String nombreFinal=resolverNombreSinConflicto(origen,carpetaDestino);
        Path rutaDestino=carpetaDestino.toPath().resolve(nombreFinal);
        if(origen.isDirectory()){
            copiarDirectorioRecursivo(origen.toPath(),rutaDestino);
        }
        else{
            Files.copy(origen.toPath(),rutaDestino,StandardCopyOption.COPY_ATTRIBUTES);
        }
    }
    private void copiarDirectorioRecursivo(Path origen,Path destino) throws IOException{
        Files.createDirectories(destino);
        try(var flujoArchivos=Files.walk(origen)){
            flujoArchivos.forEach(rutaOrigen->{
                Path rutaRelativa=origen.relativize(rutaOrigen);
                Path rutaFinal=destino.resolve(rutaRelativa);
                try{
                    if(Files.isDirectory(rutaOrigen)){
                        Files.createDirectories(rutaFinal);
                    }
                    else{
                        Files.copy(rutaOrigen,rutaFinal,StandardCopyOption.COPY_ATTRIBUTES);
                    }
                }
                catch(IOException errorCopia){
                    throw new RuntimeException("Error copiando: "+rutaOrigen,errorCopia);
                }
            });
        }
    }
    private String resolverNombreSinConflicto(File origen,File carpetaDestino){
        String nombreOriginal=origen.getName();
        File posibleConflicto=new File(carpetaDestino,nombreOriginal);
        if(!posibleConflicto.exists()){ 
            return nombreOriginal;
        }
        int posicionPunto=nombreOriginal.lastIndexOf('.');
        String nombreBase;
        String extensionConPunto;
        if(posicionPunto>0&&!origen.isDirectory()){
            nombreBase=nombreOriginal.substring(0,posicionPunto);
            extensionConPunto=nombreOriginal.substring(posicionPunto);
        }
        else{
            nombreBase=nombreOriginal;
            extensionConPunto="";
        }
        String nombreCandidato=nombreBase+"_copia"+extensionConPunto;
        int contador=2;
        while(new File(carpetaDestino,nombreCandidato).exists()){
            nombreCandidato=
                nombreBase+"_copia"+contador+extensionConPunto;
            contador++;
        }
        return nombreCandidato;
    }
}
