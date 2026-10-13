package videoteca.ui;

import videoteca.servicio.ServicioVideoteca;

import javax.swing.JFrame;

/**
 * La ventana. Solo conoce el contrato ServicioVideoteca. Ya está terminada:
 * no se toca. Todo lo que se ve vive en PanelVideoteca.
 */
@SuppressWarnings({"serial", "this-escape"})
public class VentanaPrincipal extends JFrame {

    private final ServicioVideoteca servicio;

    public VentanaPrincipal(ServicioVideoteca servicio) {
        super("Videoteca");
        this.servicio = servicio;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new PanelVideoteca(servicio));
        pack();
        setLocationRelativeTo(null);
    }

    public ServicioVideoteca getServicio() {
        return servicio;
    }
}
