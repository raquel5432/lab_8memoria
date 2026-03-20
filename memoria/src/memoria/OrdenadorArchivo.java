/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoria;

import java.util.Comparator;

/**
 *
 * @author alira
 */
public class OrdenadorArchivo {
    
    public static void ordenarPorNombre(ListaEnlazadaArchivos lista){
        if(lista.estaVacia()||lista.getTamanio()==1){
            return;
        }
        Comparator<ElementoArchivo> porNombre=
                (primero,segundo)->
                        primero.getNombre().compareToIgnoreCase(segundo.getNombre());
        lista.setCabeza(mergeSort(lista.getCabeza(),porNombre));
    }
    
    public static void ordenarPorFecha(ListaEnlazadaArchivos lista){
        if(lista.estaVacia()||lista.getTamanio()==1){
            return;
        }
        Comparator<ElementoArchivo> porFecha=
                (primero,segundo)->
                        Long.compare(segundo.getFechaModificacion(), primero.getFechaModificacion());
        lista.setCabeza(mergeSort(lista.getCabeza(),porFecha));
    }
    
    public static void ordenarPorTipo(ListaEnlazadaArchivos lista){
        if(lista.estaVacia()||lista.getTamanio()==1){
            return;
        }
        Comparator<ElementoArchivo> porTipo=
                (primero,segundo)->
                        primero.getExtension().compareToIgnoreCase(segundo.getExtension());
        lista.setCabeza(mergeSort(lista.getCabeza(),porTipo));          
    }
    
    public static void ordenarPorTamanio(ListaEnlazadaArchivos lista){
        if(lista.estaVacia()||lista.getTamanio()==1){ return; }
        Comparator<ElementoArchivo> porTamanio=
            (primero,segundo)->
                Long.compare(primero.getTamanoEnBytes(),
                             segundo.getTamanoEnBytes());
        lista.setCabeza(mergeSort(lista.getCabeza(),porTamanio));
    }
    
    private static NodoArchivo mergeSort(NodoArchivo cabeza,
                                          Comparator<ElementoArchivo> comparador){
        if(cabeza==null||cabeza.siguiente==null){ return cabeza; }
        NodoArchivo mitad=encontrarMitad(cabeza);
        NodoArchivo segundaMitad=mitad.siguiente;
        mitad.siguiente=null;
        NodoArchivo izquierda=mergeSort(cabeza,comparador);
        NodoArchivo derecha=mergeSort(segundaMitad,comparador);
        return fusionar(izquierda,derecha,comparador);
    }
    
    private static NodoArchivo encontrarMitad(NodoArchivo cabeza){
        NodoArchivo punteroLento=cabeza;
        NodoArchivo punteroRapido=cabeza.siguiente;
        while(punteroRapido!=null&&punteroRapido.siguiente!=null){
            punteroLento=punteroLento.siguiente;
            punteroRapido=punteroRapido.siguiente.siguiente;
        }
        return punteroLento;
    }
    
    private static NodoArchivo fusionar(NodoArchivo izquierda,
                                         NodoArchivo derecha,
                                         Comparator<ElementoArchivo> comparador){
        NodoArchivo centinela=new NodoArchivo(null);
        NodoArchivo actual=centinela;
        while(izquierda!=null&&derecha!=null){
            if(comparador.compare(izquierda.elemento,derecha.elemento)<=0){
                actual.siguiente=izquierda;
                izquierda=izquierda.siguiente;
            }else{
                actual.siguiente=derecha;
                derecha=derecha.siguiente;
            }
            actual=actual.siguiente;
        }
        actual.siguiente=(izquierda!=null)?izquierda:derecha;
        return centinela.siguiente;
    }
}
