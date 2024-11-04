import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static reto.dao.PeliculaDAO.*;

/**
 * Esta clase representa la vista de detalles de una película específica.
 * Permite mostrar información detallada de la película seleccionada por un usuario.
 */
public class DetallePeli extends JFrame {
    private JPanel generalJPanel;
    private JPanel detallesJPanel;
    private JButton volverButton;
    private JScrollPane ListaJScroll;
    private JList<String> listaPelisDetalles; // Specify generic type for JList
    private String nombreUsuario; // Mantiene la sesión activa del usuario actual

    /**
     * Constructor que inicializa la vista de detalles de una película.
     *
     * @param nombrePeli el nombre de la película cuyos detalles se mostrarán
     * @param nombreUsuario el nombre del usuario activo en la sesión
     */
    public DetallePeli(String nombrePeli, String nombreUsuario) {
        this.setContentPane(generalJPanel);
        this.setTitle("Detalles de la película " + nombrePeli);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1600, 800);
        this.nombreUsuario = nombreUsuario;
        setLocationRelativeTo(null); // Center the window on the screen

        cargarDetallesPeliculas(nombrePeli);

        volverButton.addActionListener(e -> {
            ListaPelis listaPelis = new ListaPelis(nombreUsuario);
            listaPelis.setVisible(true);
            this.dispose(); // Dispose current frame to free up resources
        });
    }

    /**
     * Carga los detalles de la película desde la base de datos.
     * Utiliza la película y el usuario para filtrar los resultados y mostrar solo la versión correspondiente.
     *
     * @param nombrePeli el nombre de la película cuyos detalles se van a cargar
     */
    private void cargarDetallesPeliculas(String nombrePeli) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listaPelisDetalles.setModel(listModel);

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_DETALLES_FROM_PELIS_POR_USUARIO)) {

            ps.setString(1, nombrePeli);
            ps.setString(2, nombreUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String peliculaInfo =
                            "Título: " + rs.getString("titulo") + ", Género: " + rs.getString("genero") +
                                    ", Año: " + rs.getInt("año") + ", Descripción: " + rs.getString("descripcion") +
                                    ", Director: " + rs.getString("director") + ", Estado: " + rs.getString("estado") +
                                    ", Tipo: " + rs.getString("soporte");
                    listModel.addElement(peliculaInfo);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la película: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
