package memoria;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BarraEstado extends JPanel {
    private final JLabel etiquetaMensaje;
    private final JLabel etiquetaConteo;
    
    public BarraEstado(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, java.awt.Color.GRAY),BorderFactory.createEmptyBorder(3,8,3,8)));
        setPreferredSize(new Dimension(0,24));
        etiquetaMensaje=new JLabel("Listo");
        etiquetaConteo=new JLabel("");
        etiquetaMensaje.setFont(etiquetaMensaje.getFont().deriveFont(11.5f));
        etiquetaConteo.setFont(etiquetaConteo.getFont().deriveFont(11.5f));
        add(etiquetaMensaje, BorderLayout.WEST);
        add(etiquetaConteo, BorderLayout.EAST);
    }
    
    public void setMensaje(String mensaje){
        etiquetaMensaje.setText(mensaje!=null?mensaje:"");
    }
    
    public void setConteo(int cantidad){
        etiquetaConteo.setText(cantidad>0?cantidad+" elemento(s)":"");
    }
    
    public void limpiar(){
        etiquetaMensaje.setText("Listo");
        etiquetaConteo.setText("");
    }
}
