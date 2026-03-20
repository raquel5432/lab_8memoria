/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author andres
 */
public class PortapapelesArchivos {
    private static PortapapelesArchivos instancia;
    private List<File> elementosCopiados;
    private PortapapelesArchivos(){
        elementosCopiados=new ArrayList<>();
    }
    public static PortapapelesArchivos obtenerInstancia(){
        if(instancia==null){
            instancia=new PortapapelesArchivos();
        }
        return instancia;
    }
    public void copiar(List<File> elementosSeleccionados){
        elementosCopiados.clear();
        if(elementosSeleccionados!=null){
            elementosCopiados.addAll(elementosSeleccionados);
        }
    }
    public List<File> obtenerElementos(){
        return Collections.unmodifiableList(elementosCopiados);
    }
    public boolean tieneContenido(){
        return !elementosCopiados.isEmpty();
    }
    public void limpiar(){
        elementosCopiados.clear();
    }
    public int getCantidad(){
        return elementosCopiados.size();
    }
}
