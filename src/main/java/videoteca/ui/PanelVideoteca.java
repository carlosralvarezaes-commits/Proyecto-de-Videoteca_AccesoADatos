package videoteca.ui;

import videoteca.excepciones.ExcepcionDirectorioNoExiste;
import videoteca.excepciones.ExcepcionFicheroNoExiste;
import videoteca.excepciones.ExcepcionOperacionCancelada;
import videoteca.modelo.Categoria;
import videoteca.modelo.Pelicula;
import videoteca.servicio.Formato;
import videoteca.servicio.ServicioVideoteca;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Todo lo que se ve en la ventana. Ya está terminado: no se toca.
 *
 * <p>Solo habla con el contrato {@link ServicioVideoteca}. Si un método lanza
 * una excepción, enseña su MENSAJE; si todavía tiene el TODO
 * (UnsupportedOperationException), avisa de que está pendiente.</p>
 */
@SuppressWarnings({"serial", "this-escape"})
public class PanelVideoteca extends JPanel {

    private static final int ANCHO_IMAGEN = 150;
    private static final int ALTO_IMAGEN = 210;

    private final ServicioVideoteca servicio;

    private final DefaultListModel<Categoria> modeloCategorias = new DefaultListModel<>();
    private final DefaultListModel<Pelicula> modeloPeliculas = new DefaultListModel<>();
    private final JList<Categoria> listaCategorias = new JList<>(modeloCategorias);
    private final JList<Pelicula> listaPeliculas = new JList<>(modeloPeliculas);

    private final JLabel lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
    private final JTextField campoTitulo = campoSoloLectura();
    private final JTextField campoDirector = campoSoloLectura();
    private final JTextField campoAnio = campoSoloLectura();
    private final JTextField campoDuracion = campoSoloLectura();

    private final JTextField campoDirectorio = new JTextField(22);
    private final JTextField campoFichero = new JTextField(22);
    private final JComboBox<Formato> comboFormato = new JComboBox<>(Formato.values());

    private final JTextArea estado = new JTextArea(2, 70);

    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws Exception;
    }

    public PanelVideoteca(ServicioVideoteca servicio) {
        this.servicio = servicio;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(crearPanelListas(), BorderLayout.WEST);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        listaCategorias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refrescarPeliculas();
            }
        });
        listaPeliculas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });

        arrancar();
        refrescarCategorias();
    }

    // ------------------------------------------------------------------ construcción

    private static JTextField campoSoloLectura() {
        JTextField campo = new JTextField(20);
        campo.setEditable(false);
        return campo;
    }

    private JPanel crearPanelListas() {
        listaCategorias.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                setText(((Categoria) valor).getNombre());
                return this;
            }
        });
        listaPeliculas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                Pelicula pelicula = (Pelicula) valor;
                setText(pelicula.getTitulo() + " (" + pelicula.getAnio() + ")");
                return this;
            }
        });

        JButton btnNuevaCategoria = new JButton("Nueva categoría...");
        btnNuevaCategoria.addActionListener(e -> nuevaCategoria());
        JButton btnNuevaPelicula = new JButton("Nueva película...");
        btnNuevaPelicula.addActionListener(e -> nuevaPelicula());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(230, 400));
        panel.add(etiqueta("Categorías"));
        panel.add(new JScrollPane(listaCategorias));
        panel.add(Box.createVerticalStrut(4));
        panel.add(ancho(btnNuevaCategoria));
        panel.add(Box.createVerticalStrut(14));
        panel.add(etiqueta("Películas"));
        panel.add(new JScrollPane(listaPeliculas));
        panel.add(Box.createVerticalStrut(4));
        panel.add(ancho(btnNuevaPelicula));
        return panel;
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
        return l;
    }

    private static JButton ancho(JButton boton) {
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, boton.getPreferredSize().height));
        return boton;
    }

    private JPanel crearPanelCentral() {
        JPanel central = new JPanel(new BorderLayout(8, 8));
        central.add(crearPanelDetalle(), BorderLayout.CENTER);
        central.add(crearPanelImportarExportar(), BorderLayout.SOUTH);
        return central;
    }

    private JPanel crearPanelDetalle() {
        lblImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagen.setForeground(Color.GRAY);

        JPanel campos = new JPanel(new GridLayout(0, 1, 2, 2));
        campos.add(new JLabel("Título:"));
        campos.add(campoTitulo);
        campos.add(new JLabel("Director:"));
        campos.add(campoDirector);
        campos.add(new JLabel("Año:"));
        campos.add(campoAnio);
        campos.add(new JLabel("Duración (minutos):"));
        campos.add(campoDuracion);

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierda.add(lblImagen);

        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Detalle de la película"));
        panel.add(izquierda, BorderLayout.WEST);
        panel.add(campos, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelImportarExportar() {
        JButton btnImportar = new JButton("Importar");
        btnImportar.addActionListener(e -> importar());
        JButton btnExportar = new JButton("Exportar");
        btnExportar.addActionListener(e -> exportar());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Importar / Exportar"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 4, 3, 4);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; panel.add(new JLabel("Directorio:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; panel.add(campoDirectorio, g);
        g.gridx = 2; g.weightx = 0; g.fill = GridBagConstraints.NONE; panel.add(new JLabel("Formato:"), g);
        g.gridx = 3; panel.add(comboFormato, g);

        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Fichero:"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; panel.add(campoFichero, g);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        botones.add(btnImportar);
        botones.add(btnExportar);
        g.gridx = 2; g.gridwidth = 2; g.weightx = 0; g.fill = GridBagConstraints.NONE; panel.add(botones, g);
        return panel;
    }

    private JPanel crearBarraEstado() {
        estado.setEditable(false);
        estado.setLineWrap(true);
        estado.setWrapStyleWord(true);
        estado.setBackground(new Color(255, 243, 205));
        estado.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(183, 121, 31)));
        panel.add(estado, BorderLayout.CENTER);
        return panel;
    }

    // ------------------------------------------------------------------ arranque y refresco

    /** Llama a iniciar() una vez y enseña (y escribe en consola) el mensaje. */
    private void arrancar() {
        try {
            String mensaje = servicio.iniciar();
            System.out.println(mensaje);
            estado(mensaje);
        } catch (UnsupportedOperationException ex) {
            estado("iniciar() está pendiente de implementar (tarea 7): " + ex.getMessage());
        } catch (RuntimeException ex) {
            estado("Error al iniciar: " + ex);
        }
    }

    private void refrescarCategorias() {
        int idSeleccionada = listaCategorias.getSelectedValue() == null ? -1 : listaCategorias.getSelectedValue().getId();
        modeloCategorias.clear();
        try {
            List<Categoria> categorias = servicio.listarCategorias();
            for (Categoria categoria : categorias) {
                modeloCategorias.addElement(categoria);
            }
        } catch (RuntimeException ex) {
            estado("No se pueden listar las categorías: " + ex);
        }
        int aSeleccionar = 0;
        for (int i = 0; i < modeloCategorias.size(); i++) {
            if (modeloCategorias.get(i).getId() == idSeleccionada) {
                aSeleccionar = i;
            }
        }
        if (!modeloCategorias.isEmpty()) {
            listaCategorias.setSelectedIndex(aSeleccionar);
        }
        refrescarPeliculas();
    }

    private void refrescarPeliculas() {
        int idSeleccionada = listaPeliculas.getSelectedValue() == null ? -1 : listaPeliculas.getSelectedValue().getId();
        modeloPeliculas.clear();
        Categoria categoria = listaCategorias.getSelectedValue();
        if (categoria != null) {
            for (Pelicula pelicula : categoria.getPeliculas()) {
                modeloPeliculas.addElement(pelicula);
            }
        }
        for (int i = 0; i < modeloPeliculas.size(); i++) {
            if (modeloPeliculas.get(i).getId() == idSeleccionada) {
                listaPeliculas.setSelectedIndex(i);
            }
        }
        mostrarDetalle();
    }

    private void mostrarDetalle() {
        Pelicula pelicula = listaPeliculas.getSelectedValue();
        if (pelicula == null) {
            campoTitulo.setText("");
            campoDirector.setText("");
            campoAnio.setText("");
            campoDuracion.setText("");
            mostrarImagen(null);
            return;
        }
        campoTitulo.setText(pelicula.getTitulo());
        campoDirector.setText(pelicula.getDirector());
        campoAnio.setText(String.valueOf(pelicula.getAnio()));
        campoDuracion.setText(String.valueOf(pelicula.getDuracionMinutos()));

        byte[] datos = null;
        try {
            datos = servicio.obtenerImagen(pelicula.getId());
        } catch (UnsupportedOperationException ex) {
            estado("obtenerImagen() está pendiente de implementar (tarea 8): " + ex.getMessage());
        } catch (IOException | RuntimeException ex) {
            estado("No se puede leer la carátula: " + ex.getMessage());
        }
        mostrarImagen(datos);
    }

    private void mostrarImagen(byte[] datos) {
        if (datos == null || datos.length == 0) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
            return;
        }
        Image escalada = new ImageIcon(datos).getImage()
                .getScaledInstance(ANCHO_IMAGEN, ALTO_IMAGEN, Image.SCALE_SMOOTH);
        lblImagen.setText(null);
        lblImagen.setIcon(new ImageIcon(escalada));
    }

    // ------------------------------------------------------------------ acciones

    private void nuevaCategoria() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva categoría:", "Nueva categoría",
                JOptionPane.QUESTION_MESSAGE);
        if (nombre == null) {
            return;
        }
        ejecutar("Categoría creada: " + nombre.trim(), () -> {
            servicio.crearCategoria(nombre);
            refrescarCategorias();
        });
    }

    private void nuevaPelicula() {
        Categoria categoria = listaCategorias.getSelectedValue();
        if (categoria == null) {
            JOptionPane.showMessageDialog(this, "Primero selecciona una categoría.", "Nueva película",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        FormularioPelicula formulario = new FormularioPelicula();

        int respuesta = JOptionPane.showConfirmDialog(this, formulario,
                "Nueva película en «" + categoria.getNombre() + "»", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }
        ejecutar("Película creada: " + formulario.titulo.getText().trim(), () -> {
            servicio.crearPelicula(categoria.getId(), formulario.titulo.getText(), formulario.director.getText(),
                    (Integer) formulario.anio.getValue(), (Integer) formulario.duracion.getValue());
            refrescarCategorias();
        });
    }

    private void importar() {
        Path ruta = construirRuta();
        if (ruta == null) {
            return;
        }
        Formato formato = (Formato) comboFormato.getSelectedItem();
        ejecutar("Importado (" + formato + ") desde " + ruta, () -> {
            servicio.importar(formato, ruta);
            refrescarCategorias();
        });
    }

    private void exportar() {
        Path ruta = construirRuta();
        if (ruta == null) {
            return;
        }
        Formato formato = (Formato) comboFormato.getSelectedItem();
        ejecutar("Exportado (" + formato + ") a " + ruta, () -> servicio.exportar(formato, ruta));
    }

    private Path construirRuta() {
        String directorio = campoDirectorio.getText().trim();
        String fichero = campoFichero.getText().trim();
        if (directorio.isEmpty() || fichero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el directorio y el nombre del fichero.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Paths.get(directorio).resolve(fichero);
        } catch (InvalidPathException ex) {
            JOptionPane.showMessageDialog(this, "La ruta no es válida: " + ex.getMessage(), "Ruta no válida",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Ejecuta una acción del servicio y traduce cada excepción a un mensaje claro. */
    private void ejecutar(String mensajeOk, Accion accion) {
        try {
            accion.ejecutar();
            estado(mensajeOk);
        } catch (UnsupportedOperationException ex) {
            aviso("Pendiente de implementar", ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        } catch (ExcepcionOperacionCancelada ex) {
            aviso("Operación cancelada", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        } catch (ExcepcionDirectorioNoExiste | ExcepcionFicheroNoExiste ex) {
            aviso("Ruta incorrecta", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            aviso("Datos no válidos", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            aviso("Error de ficheros", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            aviso("Error inesperado", ex.toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aviso(String titulo, String mensaje, int tipo) {
        estado(titulo + ": " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    private void estado(String mensaje) {
        estado.setText(mensaje);
    }

    /** Los campos del cuadro «Nueva película». */
    @SuppressWarnings({"serial", "this-escape"})
    static final class FormularioPelicula extends JPanel {
        final JTextField titulo = new JTextField(20);
        final JTextField director = new JTextField(20);
        final JSpinner anio = new JSpinner(new SpinnerNumberModel(2000, 1888, 2100, 1));
        final JSpinner duracion = new JSpinner(new SpinnerNumberModel(100, 1, 600, 1));

        FormularioPelicula() {
            super(new GridLayout(0, 2, 6, 6));
            anio.setEditor(new JSpinner.NumberEditor(anio, "#")); // sin separador de miles: 2014, no 2.014
            duracion.setEditor(new JSpinner.NumberEditor(duracion, "#"));
            add(new JLabel("Título:"));
            add(titulo);
            add(new JLabel("Director:"));
            add(director);
            add(new JLabel("Año:"));
            add(anio);
            add(new JLabel("Duración (minutos):"));
            add(duracion);
        }
    }
}
