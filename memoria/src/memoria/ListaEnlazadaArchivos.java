/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoria;

import java.io.File;

/**
 *
 * @author alira
 */
public class ListaEnlazadaArchivos {

    private NodoArchivo cabeza;
    private int tamanio;

    public ListaEnlazadaArchivos(){
        cabeza=null;
        tamanio=0;
    }

    public void agregar(ElementoArchivo elemento){
        NodoArchivo nuevoNodo=new NodoArchivo(elemento);
        if(cabeza==null){
            cabeza=nuevoNodo;
        }else{
            NodoArchivo actual=cabeza;
            while(actual.siguiente!=null){
                actual=actual.siguiente;
            }
            actual.siguiente=nuevoNodo;
        }
        tamanio++;
    }

    public void agregarAlInicio(ElementoArchivo elemento){
        NodoArchivo nuevoNodo=new NodoArchivo(elemento);
        nuevoNodo.siguiente=cabeza;
        cabeza=nuevoNodo;
        tamanio++;
    }

    public ElementoArchivo obtener(int indice){
        if(indice<0||indice>=tamanio){
            throw new IndexOutOfBoundsException(
                "Índice "+indice+" fuera de rango. Tamaño: "+tamanio);
        }
        NodoArchivo actual=cabeza;
        for(int posicion=0;posicion<indice;posicion++){
            actual=actual.siguiente;
        }
        return actual.elemento;
    }

    public void eliminar(int indice){
        if(indice<0||indice>=tamanio){
            throw new IndexOutOfBoundsException(
                "Índice "+indice+" fuera de rango. Tamaño: "+tamanio);
        }
        if(indice==0){
            cabeza=cabeza.siguiente;
        }else{
            NodoArchivo anterior=cabeza;
            for(int posicion=0;posicion<indice-1;posicion++){
                anterior=anterior.siguiente;
            }
            anterior.siguiente=anterior.siguiente.siguiente;
        }
        tamanio--;
    }

    public boolean estaVacia(){ return tamanio==0; }
    public int getTamanio(){ return tamanio; }

    public void limpiar(){
        cabeza=null;
        tamanio=0;
    }

    NodoArchivo getCabeza(){ return cabeza; }
    void setCabeza(NodoArchivo nuevaCabeza){ cabeza=nuevaCabeza; }

    public void cargarDesdeArregloDeFiles(File[] archivos){
        limpiar();
        if(archivos==null){
            return;
        }
        for(File archivo:archivos){
            agregar(ElementoArchivo.desdeFile(archivo));
        }
    }

    public ElementoArchivo[] aArreglo(){
        ElementoArchivo[] arreglo=new ElementoArchivo[tamanio];
        NodoArchivo actual=cabeza;
        for(int posicion=0;posicion<tamanio;posicion++){
            arreglo[posicion]=actual.elemento;
            actual=actual.siguiente;
        }
        return arreglo;
    }

    @Override
    public String toString(){
        StringBuilder texto=new StringBuilder("ListaEnlazadaArchivos[\n");
        NodoArchivo actual=cabeza;
        int posicion=0;
        while(actual!=null){
            texto.append("  ").append(posicion).append(": ")
                 .append(actual.elemento).append("\n");
            actual=actual.siguiente;
            posicion++;
        }
        texto.append("]");
        return texto.toString();
    }
}