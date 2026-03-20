package memoria;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControladorEventos {

    private final VentanaPrincipal ventana;
    private final GestorArchivos gestorArchivos;
    private final OrganizadorArchivos organizadorArchivos;
    private final PortapapelesArchivos portapapeles;
    private File carpetaActual;

    public ControladorEventos(VentanaPrincipal ventana){
        this.ventana=ventana;
        gestorArchivos=new GestorArchivos();
        organizadorArchivos=new OrganizadorArchivos();
        portapapeles=PortapapelesArchivos.obtenerInstancia();
        conectarEventos();
    }

    private void conectarEventos(){
        conectarArbol();
        conectarBotones();
        conectarDobleClickTabla();
    }

    private void conectarArbol(){
        ventana.getPanelArbol().agregarListenerSelection(
            (TreeSelectionEvent evento)->{
                DefaultMutableTreeNode nodoSeleccionado=
                    ventana.getPanelArbol().getNodoSeleccionado();
                if(nodoSeleccionado==null){
                    return;
                }
                ventana.getPanelArbol().expandirNodo(nodoSeleccionado);
                File carpeta=ventana.getPanelArbol().getArchivoSeleccionado();
                if(carpeta!=null&&carpeta.isDirectory()){
                    navegarACarpeta(carpeta);
                }
            }
        );
        ventana.getPanelArbol().agregarListenerExpansion(
            new TreeExpansionListener(){
                @Override
                public void treeExpanded(TreeExpansionEvent evento){
                    DefaultMutableTreeNode nodo=(DefaultMutableTreeNode)evento.getPath().getLastPathComponent();
                    ventana.getPanelArbol().expandirNodo(nodo);
                }
                @Override
                public void treeCollapsed(TreeExpansionEvent evento){}
            }
        );
    }

    private void conectarBotones(){
        ventana.getBarraHerramientas().agregarListenerCrearCarpeta(
            evento->alCrearCarpeta());
        ventana.getBarraHerramientas().agregarListenerOrganizar(
            evento->alOrganizar());
        ventana.getBarraHerramientas().agregarListenerRenombrar(
            evento->alRenombrar());
        ventana.getBarraHerramientas().agregarListenerCopiar(
            evento->alCopiar());
        ventana.getBarraHerramientas().agregarListenerPegar(
            evento->alPegar());
        ventana.getBarraHerramientas().agregarListenerSubirNivel(
            evento->alSubirNivel());
        ventana.getBarraHerramientas().agregarListenerOrdenar(
            evento->alOrdenar());
    }

    private void conectarDobleClickTabla(){
        ventana.getPanelContenido().agregarListenerDobleClick(
            new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent evento){
                    if(evento.getClickCount()==2){
                        alAbrirElementoSeleccionado();
                    }
                }
            }
        );
    }

    private void navegarACarpeta(File carpeta){
        carpetaActual=carpeta;
        ListaEnlazadaArchivos contenido=gestorArchivos.listarContenido(carpeta);
        ElementoArchivo[] elementos=contenido.aArreglo();
        ventana.getPanelContenido().cargarElementos(elementos);
        ventana.getBarraHerramientas().setRutaActual(carpeta.getAbsolutePath());
        ventana.getBarraEstado().setMensaje("Carpeta: "+carpeta.getName());
        ventana.getBarraEstado().setConteo(elementos.length);
        ventana.getBarraHerramientas().setBotonPegarHabilitado(portapapeles.tieneContenido());
    }

    private void alAbrirElementoSeleccionado(){
        ElementoArchivo seleccionado=ventana.getPanelContenido().obtenerElementoSeleccionado();
        if(seleccionado==null){
            return;
        }
        if(seleccionado.esDirectorio()){
            File subcarpeta=new File(seleccionado.getRutaCompleta());
            navegarACarpeta(subcarpeta);
        }else{
            ventana.getBarraEstado().setMensaje("Archivo: "+seleccionado.getNombre()+" ("+seleccionado.getTamanoLegible()+")");
        }
    }

    private void alSubirNivel(){
        if(carpetaActual==null){
            return;
        }
        File carpetaPadre=carpetaActual.getParentFile();
        if(carpetaPadre!=null&&carpetaPadre.isDirectory()){
            navegarACarpeta(carpetaPadre);
        }else{
            ventana.getBarraEstado().setMensaje("Ya estás en la raíz del sistema.");
        }
    }

    private void alCrearCarpeta(){
        if(carpetaActual==null){
            ventana.getBarraEstado().setMensaje(
                "Selecciona primero una carpeta en el árbol.");
            return;
        }
        String nombreNuevo=JOptionPane.showInputDialog(ventana,"Nombre de la nueva carpeta:","Crear carpeta",JOptionPane.PLAIN_MESSAGE);
        if(nombreNuevo==null||nombreNuevo.trim().isEmpty()){
            return;
        }
        ResultadoOperacion resultado=gestorArchivos.crearCarpeta(carpetaActual,nombreNuevo.trim());
        ventana.getBarraEstado().setMensaje(resultado.getMensaje());
        if(resultado.fueExitoso()){
            ventana.getPanelArbol().refrescarNodoSeleccionado();
            navegarACarpeta(carpetaActual);
        }
    }

    private void alOrganizar(){
        if(carpetaActual==null){
            ventana.getBarraEstado().setMensaje(
                "Selecciona primero una carpeta para organizar.");
            return;
        }
        int confirmacion=JOptionPane.showConfirmDialog(ventana,"¿Organizar automáticamente los archivos de:\n"+carpetaActual.getAbsolutePath()+"?","Confirmar organización",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(confirmacion!=JOptionPane.YES_OPTION){
            return;
        }
        ResultadoOperacion resultado=organizadorArchivos.organizarCarpeta(carpetaActual);
        ventana.getBarraEstado().setMensaje(resultado.getMensaje());
        if(resultado.fueExitoso()){
            navegarACarpeta(carpetaActual);
        }
    }

    private void alRenombrar(){
        ElementoArchivo seleccionado=ventana.getPanelContenido().obtenerElementoSeleccionado();
        if(seleccionado==null){
            ventana.getBarraEstado().setMensaje("Selecciona un elemento para renombrar.");
            return;
        }
        String nuevoNombre=JOptionPane.showInputDialog(ventana,"Nuevo nombre para \""+seleccionado.getNombre()+"\":","Renombrar",JOptionPane.PLAIN_MESSAGE);
        if(nuevoNombre==null||nuevoNombre.trim().isEmpty()){
            return;
        }
        File elementoFile=new File(seleccionado.getRutaCompleta());
        ResultadoOperacion resultado=gestorArchivos.renombrarElemento(elementoFile,nuevoNombre.trim());
        ventana.getBarraEstado().setMensaje(resultado.getMensaje());
        if(resultado.fueExitoso()&&carpetaActual!=null){
            navegarACarpeta(carpetaActual);
        }
    }

    private void alCopiar(){
        List<ElementoArchivo> seleccionados=ventana.getPanelContenido().obtenerElementosSeleccionados();
        if(seleccionados.isEmpty()){
            ventana.getBarraEstado().setMensaje("Selecciona al menos un elemento para copiar.");
            return;
        }
        List<File> archivosParaCopiar=new ArrayList<>();
        for(ElementoArchivo elemento : seleccionados){
            archivosParaCopiar.add(new File(elemento.getRutaCompleta()));
        }
        ResultadoOperacion resultado=gestorArchivos.copiarElementos(archivosParaCopiar);
        ventana.getBarraEstado().setMensaje(resultado.getMensaje());
        ventana.getBarraHerramientas().setBotonPegarHabilitado(portapapeles.tieneContenido());
    }

    private void alPegar(){
        if(carpetaActual==null){
            ventana.getBarraEstado().setMensaje("Navega a una carpeta destino antes de pegar.");
            return;
        }
        ResultadoOperacion resultado=gestorArchivos.pegarElementos(carpetaActual);
        ventana.getBarraEstado().setMensaje(resultado.getMensaje());
        if(resultado.fueExitoso()){
            navegarACarpeta(carpetaActual);
        }
    }

    private void alOrdenar(){
        String criterio=ventana.getBarraHerramientas().getCriterioOrdenSeleccionado();
        if(criterio.isEmpty()||carpetaActual==null){
            return;
        }
        ListaEnlazadaArchivos listaActual=gestorArchivos.listarContenido(carpetaActual);
        switch(criterio){
            case "Nombre"  ->OrdenadorArchivo.ordenarPorNombre(listaActual);
            case "Fecha"   ->OrdenadorArchivo.ordenarPorFecha(listaActual);
            case "Tipo"    ->OrdenadorArchivo.ordenarPorTipo(listaActual);
            case "Tamaño"  ->OrdenadorArchivo.ordenarPorTamanio(listaActual);
            default        ->{}
        }
        ventana.getPanelContenido().cargarElementos(listaActual.aArreglo());
        ventana.getBarraEstado().setMensaje("Ordenado por: "+criterio);
        ventana.getBarraHerramientas().resetearComboOrden();
    }
}

