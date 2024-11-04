import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static reto.dao.PeliculaDAO.SELECT_FROM_PELIS;

/**
 * Esta clase representa la interfaz gráfica de una lista de películas asociadas a un usuario.
 * Permite ver el listado de películas, regresar al login o cerrar la aplicación.
 */
public class ListaPelis extends JFrame {
    private JPanel generalJPanel;
    private JScrollPane tablaJScrollPane;
    private JButton botonVolver;
    private JButton botonCerrar;
    private JTable tablaPeliculas;
    private JPanel botonesJPanel;

    /**
     * Constructor de la clase que inicializa la vista con la lista de películas del usuario.
     *
     * @param nombreUsuario el nombre del usuario del que se mostrarán las películas
     */
    public ListaPelis(String nombreUsuario) {
        this.setContentPane(generalJPanel);
        this.setTitle("Lista de películas de " + nombreUsuario);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 300);

        botonVolver.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            this.dispose();
        });

        botonCerrar.addActionListener(e -> System.exit(0));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Título");
        model.addColumn("Estado");
        model.addColumn("Tipo");

        Connection connection = JdbcUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(SELECT_FROM_PELIS);
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("titulo"),
                        rs.getString("estado"),
                        rs.getString("soporte")
                };
                model.addRow(row);
            }
            tablaPeliculas.setModel(model);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setLocationRelativeTo(null); // centrar la tabla en la pantalla

        tablaPeliculas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaPeliculas.getSelectedRow(); // coger la fila seleccionada
                if (fila != -1) {
                    String tituloPelicula = (String) tablaPeliculas.getValueAt(fila, 0);

                    DetallePeli detallePeli = new DetallePeli(tituloPelicula, nombreUsuario);
                    detallePeli.setVisible(true);
                    dispose();
                }
            }
        });
    }

}
