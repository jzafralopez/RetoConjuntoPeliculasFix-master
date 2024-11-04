package reto.dao;

import reto.models.Pelicula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static reto.dao.UsuarioDAO.SELECT_BY_ID;

/**
 * Esta clase gestiona el acceso a los datos de la entidad Pelicula desde la base de datos.
 * Implementa el patrón DAO (Data Access Object) para realizar operaciones CRUD sobre películas.
 */
public class PeliculaDAO implements DAO<Pelicula> {

    public static final String SELECT_FROM_PELIS = "SELECT Usuario.nombre_usuario, Pelicula.titulo, Copia.estado, Copia.soporte " + "FROM Pelicula " + "INNER JOIN Copia ON Pelicula.id = Copia.id_pelicula " + "INNER JOIN Usuario ON Usuario.id = Copia.id_usuario " + "WHERE Usuario.nombre_usuario = ?;";
    public static final String SELECT_DETALLES_FROM_PELIS_POR_USUARIO = "SELECT Pelicula.titulo, Pelicula.genero, Pelicula.año, Pelicula.descripcion, Pelicula.director, Copia.estado, Copia.soporte " + "FROM Pelicula " + "INNER JOIN Copia ON Pelicula.id = Copia.id_pelicula " + "INNER JOIN Usuario ON Usuario.id = Copia.id_usuario " + "WHERE Pelicula.titulo = ? AND Usuario.nombre_usuario = ?;";
    public static final String INSERT_PELICULA = "INSERT INTO pelicula (titulo, genero, Año, descripcion, director) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_PELICULA = "UPDATE pelicula SET titulo = ?, genero = ?, Año = ?, descripcion = ?, director = ? WHERE ID = ?";
    public static final String DELETE_PELICULA = "DELETE FROM pelicula WHERE ID = ?";

    Connection connection;

    public PeliculaDAO(Connection c) {
        connection = c;
    }

    @Override
    public List<Pelicula> findAll() {
        var resultado = new ArrayList<Pelicula>();
        try (var st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_FROM_PELIS)) {
            while (rs.next()) {
                Pelicula peli = new Pelicula();
                peli.setId(rs.getInt("ID"));
                peli.setTitulo(rs.getString("Título"));
                peli.setGenero(rs.getString("Género"));
                peli.setAnho(rs.getInt("Año"));
                peli.setDescripcion(rs.getString("descripcion"));
                peli.setDirector(rs.getString("director"));
                resultado.add(peli);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    @Override
    public Pelicula findById(Integer id) {
        Pelicula pelicula = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pelicula = new Pelicula();
                    pelicula.setId(rs.getInt("ID"));
                    pelicula.setTitulo(rs.getString("Título"));
                    pelicula.setGenero(rs.getString("Género"));
                    pelicula.setAnho(rs.getInt("Año"));
                    pelicula.setDescripcion(rs.getString("descripcion"));
                    pelicula.setDirector(rs.getString("director"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pelicula;
    }

    @Override
    public void save(Pelicula pelicula) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_PELICULA)) {
            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getGenero());
            ps.setInt(3, pelicula.getAnho());
            ps.setString(4, pelicula.getDescripcion());
            ps.setString(5, pelicula.getDirector());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Pelicula pelicula) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_PELICULA)) {
            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getGenero());
            ps.setInt(3, pelicula.getAnho());
            ps.setString(4, pelicula.getDescripcion());
            ps.setString(5, pelicula.getDirector());
            ps.setInt(6, pelicula.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Pelicula pelicula) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_PELICULA)) {
            ps.setInt(1, pelicula.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
