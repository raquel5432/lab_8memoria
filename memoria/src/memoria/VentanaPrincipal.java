package memoria;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class VentanaPrincipal extends JFrame {
    private PanelArbolArchivos panelArbol;
    private PanelContenidoArchivos panelContenido;
    private BarraHerramientasArchivos barraHerramientas;
    private BarraEstado barraEstado;

    public VentanaPrincipal(){
        super("Explorador de Archivos");
        inicializarComponentes();
        configurarLayout();
        configurarVentana();
        new ControladorEventos(this);
    }

    private void inicializarComponentes(){
        panelArbol=new PanelArbolArchivos();
        panelContenido=new PanelContenidoArchivos();
        barraHerramientas=new BarraHerramientasArchivos();
        barraEstado=new BarraEstado();
    }

    private void configurarLayout(){
        JSplitPane splitPane=new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelArbol,
            panelContenido
        );
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(4);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(barraHerramientas, BorderLayout.NORTH);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(barraEstado, BorderLayout.SOUTH);
    }

    private void configurarVentana(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950,620);
        setMinimumSize(new Dimension(700,450));
        setLocationRelativeTo(null);
        try{
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        }catch(Exception ignorado){}
        setVisible(true);
    }

    public PanelArbolArchivos getPanelArbol(){
        return panelArbol;
    }
    public PanelContenidoArchivos getPanelContenido(){
        return panelContenido;
    }
    public BarraHerramientasArchivos getBarraHerramientas(){
        return barraHerramientas;
    }
    public BarraEstado getBarraEstado(){
        return barraEstado;
    }
}
