package memoria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BarraHerramientasArchivos extends JPanel {
    private final JTextField campoRutaActual;
    private final JButton botonCrearCarpeta;
    private final JButton botonOrganizar;
    private final JButton botonRenombrar;
    private final JButton botonCopiar;
    private final JButton botonPegar;
    private final JButton botonSubirNivel;
    private final JComboBox<String> comboOrdenar;
    
    private static final String[] CRITERIOS_ORDEN={"Ordenar por... ", "Nombre", "Fecha", "Tipo", "Tamano"};
    
    public BarraHerramientasArchivos(){
        setLayout(new BorderLayout(4,0));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY), BorderFactory.createEmptyBorder(4,6,4,6)));
        botonSubirNivel=crearBoton("↑ Subir", "Ir a la carpeta superior");
        botonCrearCarpeta=crearBoton("+ Carpeta", "Crear nueva carpeta");
        botonOrganizar=crearBoton("Organizar","Organizar archivos por tipo");
        botonRenombrar=crearBoton("Renombrar", "Renombrar el elemento seleccionado");
        botonCopiar=crearBoton("Copiar", "Copiar elementos seleccionados");
        botonPegar=crearBoton("Pegar", "Pegar elementos copiados");
        comboOrdenar=new JComboBox<>(CRITERIOS_ORDEN);
        comboOrdenar.setToolTipText("Ordenar contenido");
        comboOrdenar.setMaximumSize(new Dimension(130,26));
        comboOrdenar.setPreferredSize(new Dimension(130,26));
        JPanel panelBotones=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0));
        panelBotones.setOpaque(false);
        panelBotones.add(botonSubirNivel);
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        panelBotones.add(botonCrearCarpeta);
        panelBotones.add(botonOrganizar);
        panelBotones.add(botonRenombrar);
        panelBotones.add(botonCopiar);
        panelBotones.add(botonPegar);
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        panelBotones.add(new JLabel("  Ordenar:"));
        panelBotones.add(comboOrdenar);
        JPanel panelRuta=new JPanel(new BorderLayout(4,0));
        panelRuta.setOpaque(false);
        panelRuta.setBorder(BorderFactory.createEmptyBorder(2,0,2,0));
        JLabel etiquetaRuta=new JLabel("Ruta: ");
        etiquetaRuta.setFont(etiquetaRuta.getFont().deriveFont(Font.BOLD,12f));
        campoRutaActual=new JTextField();
        campoRutaActual.setEditable(false);
        campoRutaActual.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        campoRutaActual.setBackground(new Color(245, 245, 245));
        panelRuta.add(etiquetaRuta, BorderLayout.WEST);
        panelRuta.add(campoRutaActual, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.NORTH);
        add(panelRuta, BorderLayout.SOUTH);
    }
    
    private JButton crearBoton(String texto, String tooltip){
        JButton boton=new JButton(texto);
        boton.setToolTipText(tooltip);
        boton.setFocusPainted(false);
        boton.setFont(boton.getFont().deriveFont(12f));
        boton.setMargin(new Insets(2,8,2,8));
        return boton;
    }
    
    public void setRutaActual(String ruta){
        campoRutaActual.setText(ruta!=null?ruta:"");
    }
    
    public String getRutaActual(){
        return campoRutaActual.getText();
    }
    
    public void agregarListenerCrearCarpeta(ActionListener listener){
       botonCrearCarpeta.addActionListener(listener);
    }
    
    public void agregarListenerOrganizar(ActionListener listener){
        botonOrganizar.addActionListener(listener);
    }
    
    public void agregarListenerRenombrar(ActionListener listener){
        botonRenombrar.addActionListener(listener);
    }
    
    public void agregarListenerCopiar(ActionListener listener){
        botonCopiar.addActionListener(listener);
    }
    
    public void agregarListenerPegar(ActionListener listener){
        botonPegar.addActionListener(listener);
    }
    
    public void agregarListenerSubirNivel(ActionListener listener){
        botonSubirNivel.addActionListener(listener);
    }
    
    public void agregarListenerOrdenar(ActionListener listener){
        comboOrdenar.addActionListener(listener);
    }
    
    public String getCriterioOrdenSeleccionado(){
        String seleccion=(String)comboOrdenar.getSelectedItem();
        return (seleccion!=null&&!seleccion.equals("Ordenar por..."))?seleccion:"";
    }
    
    public void resetearComboOrden(){
        comboOrdenar.setSelectedIndex(0);
    }
    
    public void setBotonPegarHabilitado(boolean habilitado){
        botonPegar.setEnabled(habilitado);
    }
}
