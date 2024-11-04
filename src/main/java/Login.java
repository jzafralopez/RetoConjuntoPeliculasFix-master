import reto.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase representa la ventana de inicio de sesión de la aplicación.
 * Permite a los usuarios introducir sus credenciales y autentificarse para acceder a la lista de películas.
 */
public class Login extends JFrame {
    private JTextField emailTextField;
    private JLabel emailJLabel;
    private JLabel inicioSesionJLabel;
    private JTextField contrasenhaTextField;
    private JPanel generalJPanel;
    private JLabel contrasenhaJLabel;
    private JPanel contentJPanel;
    private JPanel botonesJPanel;
    private JButton iniciarSesionBoton;
    private JButton salirButton;

    /**
     * Constructor que inicializa la interfaz gráfica para el inicio de sesión.
     * Configura los componentes y establece los eventos necesarios para manejar la interacción con el usuario.
     */
    public Login() {
        this.setContentPane(generalJPanel);
        this.setTitle("Inicio de sesión");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);

        salirButton.addActionListener(e -> System.exit(0));

        iniciarSesionBoton.addActionListener(e -> {
            String email = emailTextField.getText();
            String contrasenha = contrasenhaTextField.getText();

            if (email.isEmpty() || contrasenha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos para iniciar sesión...", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Connection connection = JdbcUtil.getConnection();
                try {
                    PreparedStatement ps = connection.prepareStatement(UsuarioDAO.SELECT_FROM_USUARIO);
                    ps.setString(1, email);
                    ps.setString(2, contrasenha);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String nombreUsuario = rs.getString("nombre_usuario");
                        JOptionPane.showMessageDialog(this, "Usuario " + email + " ha iniciado sesión correctamente.", "Ok", JOptionPane.INFORMATION_MESSAGE);
                        limpiar();
                        ListaPelis listaPeliculas = new ListaPelis(nombreUsuario);
                        listaPeliculas.setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos...", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadido que he metido: habilitar la tecla Enter para iniciar sesión y no tener necesariamente que hacer click en el botón
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesionBoton.doClick();
                }
            }
        };

        emailTextField.addKeyListener(enterKeyListener);
        contrasenhaTextField.addKeyListener(enterKeyListener);

        setLocationRelativeTo(null); // centrar el formulario en la pantalla al iniciar la app
    }

    /**
     * Método que limpia los campos de texto del formulario.
     */
    private void limpiar() {
        emailTextField.setText("");
        contrasenhaTextField.setText("");
    }
}
