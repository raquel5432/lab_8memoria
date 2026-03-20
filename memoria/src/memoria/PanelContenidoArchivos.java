package memoria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

public class PanelContenidoArchivos extends JPanel{
    
    private final JTable tablaContenido;
    private final ModeloTablaArchivos modeloTabla;

    public PanelContenidoArchivos(){
        setLayout(new BorderLayout());
        modeloTabla=new ModeloTablaArchivos();
        tablaContenido=new JTable(modeloTabla);
        configurarTabla();
        JScrollPane scrollTabla=new JScrollPane(tablaContenido);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        JLabel etiquetaTitulo=new JLabel("  Contenido");
        etiquetaTitulo.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
        etiquetaTitulo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),BorderFactory.createEmptyBorder(4,6,4,6)));
        etiquetaTitulo.setBackground(new Color(236,236,236));
        etiquetaTitulo.setOpaque(true);
        add(etiquetaTitulo, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void configurarTabla(){
        tablaContenido.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaContenido.setRowHeight(20);
        tablaContenido.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
        tablaContenido.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
        tablaContenido.getTableHeader().setBackground(new Color(236,236,236));
        tablaContenido.setShowHorizontalLines(true);
        tablaContenido.setGridColor(new Color(220,220,220));
        tablaContenido.setIntercellSpacing(new Dimension(8,1));
        tablaContenido.setFillsViewportHeight(true);
        tablaContenido.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        TableColumnModel columnas=tablaContenido.getColumnModel();
        columnas.getColumn(0).setPreferredWidth(240);
        columnas.getColumn(1).setPreferredWidth(150);
        columnas.getColumn(2).setPreferredWidth(80);
        columnas.getColumn(3).setPreferredWidth(90);
        tablaContenido.setAutoCreateRowSorter(false);
    }

    public void cargarElementos(ElementoArchivo[] elementos){
        modeloTabla.cargarElementos(elementos);
    }

    public ElementoArchivo obtenerElementoSeleccionado(){
        int filaSeleccionada=tablaContenido.getSelectedRow();
        if(filaSeleccionada<0){
            return null;
        }
        return modeloTabla.obtenerElemento(filaSeleccionada);
    }

    public List<ElementoArchivo> obtenerElementosSeleccionados(){
        int[] filasSeleccionadas=tablaContenido.getSelectedRows();
        List<ElementoArchivo> seleccionados=new ArrayList<>();
        for(int fila : filasSeleccionadas){
            ElementoArchivo elemento=modeloTabla.obtenerElemento(fila);
            if(elemento!=null){
                seleccionados.add(elemento);
            }
        }
        return seleccionados;
    }

    public void limpiarTabla(){
        modeloTabla.cargarElementos(null);
    }


    public void agregarListenerDobleClick(MouseListener listener){
        tablaContenido.addMouseListener(listener);
    }

    public ModeloTablaArchivos getModeloTabla(){
        return modeloTabla;
    }
}

