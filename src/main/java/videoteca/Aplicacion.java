package videoteca;

import videoteca.servicio.ServicioVideotecaImpl;
import videoteca.ui.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Programa que abre la ventana. Ya está terminado: no se toca.
 * Arranca aunque no hayas programado nada (lo pendiente se avisa en la ventana).
 */
public class Aplicacion {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(new ServicioVideotecaImpl());
            ventana.setVisible(true);
        });
    }
}
