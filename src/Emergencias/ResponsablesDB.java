package Emergencias;

import BaseDeDatos.ConexionSQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsablesDB {

    public static List<ResponsableItem> obtenerResponsables() {
        List<ResponsableItem> lista = new ArrayList<>();
        String sql = "SELECT ID, Nombre || ' ' || ApellidoPaterno FROM InformacionMedico ORDER BY Nombre";

        try (Connection conn = ConexionSQLite.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                lista.add(new ResponsableItem(id, nombre));
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar responsables: " + e.getMessage());
        }

        return lista;
    }
}
