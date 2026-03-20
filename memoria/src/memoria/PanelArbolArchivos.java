package memoria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class PanelArbolArchivos extends JPanel {
    
    private final JTree arbolCarpetas;
    private final DefaultTreeModel modeloArbol;
    
    public PanelArbolArchivos(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());
        setPreferredSize(new Dimension(220,0));
        DefaultMutableTreeNode nodoRaiz=construirArbolDesdeRaices();
        modeloArbol=new DefaultTreeModel(nodoRaiz);
        arbolCarpetas=new JTree(modeloArbol);
        arbolCarpetas.setRootVisible(true);
        arbolCarpetas.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbolCarpetas.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        arbolCarpetas.setRowHeight(20);
        JScrollPane scrollArbol=new JScrollPane(arbolCarpetas);
        scrollArbol.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        scrollArbol.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JLabel etiquetaTitulo=new JLabel("  Carpetas");
        etiquetaTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
        etiquetaTitulo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        etiquetaTitulo.setBackground(new Color(236,236,236));
        etiquetaTitulo.setOpaque(true);
        add(etiquetaTitulo, BorderLayout.NORTH);
        add(scrollArbol, BorderLayout.CENTER);
    }
    
    private DefaultMutableTreeNode construirArbolDesdeRaices(){
        DefaultMutableTreeNode nodoRaiz=new DefaultMutableTreeNode("Este equipo");
        File[] unidadesRaiz=File.listRoots();
        if(unidadesRaiz!=null){
            for(File unidad: unidadesRaiz){
                DefaultMutableTreeNode nodoUnidad=new DefaultMutableTreeNode(new NodoArchivoArbol(unidad));
                nodoUnidad.add(new DefaultMutableTreeNode("Cargando..."));
                nodoRaiz.add(nodoUnidad);
            }
        }
        return nodoRaiz;
    }
    
    public void expandirNodo(DefaultMutableTreeNode nodo){
        if(nodo==null){
            return;
        }
        Object userObject=nodo.getUserObject();
        if(!(userObject instanceof NodoArchivoArbol nodoArbol)){
            return;
        }
        File carpeta=nodoArbol.getArchivo();
        if(!carpeta.isDirectory()){
            return;
        }
        nodo.removeAllChildren();
        File[] subcarpetas=carpeta.listFiles(File::isDirectory);
        if(subcarpetas!=null){
            for(File subcarpeta : subcarpetas){
                DefaultMutableTreeNode nodoHijo=
                    new DefaultMutableTreeNode(
                        new NodoArchivoArbol(subcarpeta));
                nodoHijo.add(new DefaultMutableTreeNode("Cargando..."));
                nodo.add(nodoHijo);
            }
        }
        modeloArbol.reload(nodo);
    }
    
    public void refrescarNodoSeleccionado(){
        DefaultMutableTreeNode nodoSeleccionado=getNodoSeleccionado();
        if(nodoSeleccionado!=null){
            expandirNodo(nodoSeleccionado);
        }
    }
    
    public DefaultMutableTreeNode getNodoSeleccionado(){
        return (DefaultMutableTreeNode) arbolCarpetas.getLastSelectedPathComponent();
    }
    
    public File getArchivoSeleccionado(){
        DefaultMutableTreeNode nodo=getNodoSeleccionado();
        if(nodo==null){
            return null;
        }
        Object userObject=nodo.getUserObject();
        if(userObject instanceof NodoArchivoArbol nodoArbol){
            return nodoArbol.getArchivo();
        }
        return null;
    }
        
    public void agregarListenerSelection(TreeSelectionListener listener){
        arbolCarpetas.addTreeSelectionListener(listener);
    }
    
    public void agregarListenerExpansion(javax.swing.event.TreeExpansionListener listener){
        arbolCarpetas.addTreeExpansionListener(listener);
    }
    
    public static class NodoArchivoArbol{
        private final File archivo;
        public NodoArchivoArbol(File archivo){
            this.archivo=archivo;
        }
        public File getArchivo(){
            return archivo;
        }
        @Override
        public String toString(){
            String nombre=archivo.getName();
            return (nombre==null||nombre.isEmpty())?archivo.getAbsolutePath():nombre;
        }
    }
}
