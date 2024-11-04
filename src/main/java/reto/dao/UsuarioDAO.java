package reto.dao;

import reto.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase gestiona el acceso a los datos de la entidad Usuario desde la base de datos.
 * Implementa el patrón DAO (Data Access Object) para realizar operaciones CRUD sobre usuarios.
 */
public class UsuarioDAO implements DAO<Usuario> {

    public static final String SELECT_FROM_USUARIO = "SELECT * FROM usuario WHERE nombre_usuario = ? AND contraseña = ?";
    public static final String SELECT_ALL_USUARIOS = "SELECT * FROM usuario";
    public static final String SELECT_BY_ID = "SELECT * FROM usuario WHERE id = ?";
    public static final String INSERT_USUARIO = "INSERT INTO usuario (nombre_usuario, contraseña) VALUES (?, ?)";
    public static final String UPDATE_USUARIO = "UPDATE usuario SET nombre_usuario = ?, contraseña = ? WHERE id = ?";
    public static final String DELETE_USUARIO = "DELETE FROM usuario WHERE id = ?";

    Connection connection;

    public UsuarioDAO(Connection c) {
        connection = c;
    }

    @Override
    public List<Usuario> findAll() {
        var usuarios = new ArrayList<Usuario>();
        try (var st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_USUARIOS)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setContrasenha(rs.getString("contraseña"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuarios;
    }

    @Override
    public Usuario findById(Integer id) {
        Usuario usuario = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContrasenha(rs.getString("contraseña"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    @Override
    public void save(Usuario usuario) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_USUARIO)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasenha());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Usuario usuario) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USUARIO)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasenha());
            ps.setInt(3, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Usuario usuario) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_USUARIO)) {
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
