/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;

/**
 *
 * @author andres
 */
public class ResultadoOperacion {
    private final boolean exitoso;
    private final String mensaje;
    private ResultadoOperacion(boolean exitoso,String mensaje){
        this.exitoso=exitoso;
        this.mensaje=mensaje;
    }
    public static ResultadoOperacion exito(String mensaje){
        return new ResultadoOperacion(true,mensaje);
    }
    public static ResultadoOperacion error(String mensaje){
        return new ResultadoOperacion(false,mensaje);
    }
    public boolean fueExitoso(){
        return exitoso;
    }
    public String getMensaje(){
        return mensaje; 
    }
    public String toString(){
        return (exitoso ? "[OK] ":"[ERROR] ")+mensaje;
    }
}
