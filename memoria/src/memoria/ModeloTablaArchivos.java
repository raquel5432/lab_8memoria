package memoria;

import javax.swing.table.AbstractTableModel;

public class ModeloTablaArchivos extends AbstractTableModel {
    
    private static final String[] NOMBRES_COLUMNAS={"Nombre", "Fecha de modificacion", "Tipo", "Tamano"};
    private ElementoArchivo[] elementosMostrados;
    public ModeloTablaArchivos(){
        elementosMostrados=new ElementoArchivo[0];
    }
    
    public void cargarElementos(ElementoArchivo[] elementos){
        elementosMostrados=(elementos!=null)?elementos:new ElementoArchivo[0];
        fireTableDataChanged();
    }
    
    public ElementoArchivo obtenerElemento(int fila){
        if(fila<0 || fila>=elementosMostrados.length){
            return null;
        }
        return elementosMostrados[fila];
    }
    
    public ElementoArchivo[] obtenerTodosLosElementos(){
        return elementosMostrados;
    }
    
    @Override
    public int getRowCount(){
        return elementosMostrados.length;
    }
    
    @Override
    public int getColumnCount(){
        return NOMBRES_COLUMNAS.length;
    }
    
    @Override
    public String getColumnName(int columna){
        return NOMBRES_COLUMNAS[columna];
    }
    
    @Override
    public Object getValueAt(int fila, int columna){
        ElementoArchivo elemento=elementosMostrados[fila];
        return switch(columna){
            case 0 -> elemento.getNombre();
            case 1 -> elemento.getFechaFormateada();
            case 2 -> elemento.getExtension();
            case 3 -> elemento.getTamanoLegible();
            default -> "";
        };
    }
    
    @Override
    public boolean isCellEditable(int fila, int columna){
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int Columna){
        return String.class;
    }
}