package Emergencias;

import BaseDeDatos.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Accidente.
 * Gestiona la inserción y búsqueda completa de un accidente
 * con todos los campos definidos en la tabla Accidentes,
 * y almacena/recupera también las fotos en la tabla AccidenteFotos.
 */
public class AccidenteDB {

    /**
     * Inserta un nuevo registro en la tabla Accidentes y sus fotos.
     * @param a Objeto Accidente conteniendo toda la información y la lista de fotos.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public static boolean guardarAccidenteCompleto(Accidente a) {
        String sqlAccidente = """
            INSERT INTO Accidentes (
              IDEmergencia, Matricula, NombreEstudiante, ApellidoPaterno, ApellidoMaterno,
              Edad, Sexo, Escuela, ProgramaAcademico, Semestre, CorreoUDLAP, TelefonoEstudiante, Direccion,
              FechaAccidente, DiaSemana, LugarOcurrencia, UbicacionExacta, EnHorarioClase,
              TipoTrayecto, CentroAtencionInicial,
              LesionPrincipal, LesionSecundaria, ParteCuerpo, GravedadTriage, NivelConsciencia,
              SignosVitales, DescripcionDetallada, PrimerosAuxilios, Medicamentos, DiagnosticoCIE10,
              LesionesAtribuibles, RiesgoMuerte, IncapacidadDias, RequiereHospitalizacion,
              TratamientoRecomendado, MedicoTratante, CedulaProfesional, FechaInformeMedico,
              HospitalDestino, ResponsableTraslado, MedioTransporte, FechaHoraIngreso,
              NombreContacto, RelacionContacto, TelefonoContacto, CorreoContacto, DomicilioContacto,
              Testigo1Nombre, Testigo1Telefono, NarrativaDetallada, FechaElaboracion
            ) VALUES (
              ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
            )
            """;

        try (
            Connection conn = ConexionSQLite.conectar();
            PreparedStatement ps = conn.prepareStatement(sqlAccidente, Statement.RETURN_GENERATED_KEYS)
        ) {
            int i = 1;
            // I. Datos del Estudiante
            ps.setInt   (i++, a.getIdEmergencia());
            ps.setInt   (i++, a.getMatricula());
            ps.setString(i++, a.getNombreEstudiante());
            ps.setString(i++, a.getApellidoPaterno());
            ps.setString(i++, a.getApellidoMaterno());
            ps.setInt   (i++, a.getEdad());
            ps.setString(i++, a.getSexo());
            ps.setString(i++, a.getEscuela());
            ps.setString(i++, a.getProgramaAcademico());
            ps.setInt   (i++, a.getSemestre());
            ps.setString(i++, a.getCorreoUDLAP());
            ps.setString(i++, a.getTelefonoEstudiante());
            ps.setString(i++, a.getDireccion());

            // II. Información del Accidente
            ps.setString(i++, a.getFechaAccidente());
            ps.setString(i++, a.getDiaSemana());
            ps.setString(i++, a.getLugarOcurrencia());
            ps.setString(i++, a.getUbicacionExacta());
            ps.setString(i++, a.getEnHorarioClase());
            ps.setString(i++, a.getTipoTrayecto());
            ps.setString(i++, a.getCentroAtencionInicial());

            // III. Lesiones y Daños
            ps.setString(i++, a.getLesionPrincipal());
            ps.setString(i++, a.getLesionSecundaria());
            ps.setString(i++, a.getParteCuerpo());
            ps.setString(i++, a.getGravedadTriage());
            ps.setString(i++, a.getNivelConsciencia());
            ps.setString(i++, a.getSignosVitales());
            ps.setString(i++, a.getDescripcionDetallada());
            ps.setString(i++, a.getPrimerosAuxilios());
            ps.setString(i++, a.getMedicamentos());
            ps.setString(i++, a.getDiagnosticoCIE10());

            // IV. Evaluación Médica
            ps.setString(i++, a.getLesionesAtribuibles());
            ps.setString(i++, a.getRiesgoMuerte());
            ps.setInt   (i++, a.getIncapacidadDias());
            ps.setString(i++, a.getRequiereHospitalizacion());
            ps.setString(i++, a.getTratamientoRecomendado());
            ps.setString(i++, a.getMedicoTratante());
            ps.setString(i++, a.getCedulaProfesional());
            ps.setString(i++, a.getFechaInformeMedico());

            // V. Traslado y Seguimiento
            ps.setString(i++, a.getHospitalDestino());
            ps.setString(i++, a.getResponsableTraslado());
            ps.setString(i++, a.getMedioTransporte());
            ps.setString(i++, a.getFechaHoraIngreso());

            // VI. Contacto de Emergencia
            ps.setString(i++, a.getNombreContacto());
            ps.setString(i++, a.getRelacionContacto());
            ps.setString(i++, a.getTelefonoContacto());
            ps.setString(i++, a.getCorreoContacto());
            ps.setString(i++, a.getDomicilioContacto());

            // VII. Testigos
            ps.setString(i++, a.getTestigo1Nombre());
            ps.setString(i++, a.getTestigo1Telefono());

            // VIII. Declaraciones y Firmas
            ps.setString(i++, a.getNarrativaDetallada());
            ps.setString(i++, a.getFechaElaboracion());

            // Ejecutar inserción de accidente
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return false;
            }

            // Recuperar la clave generada (IDAccidente) y guardar las fotos
            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    int idAccidente = rsKeys.getInt(1);

                    String sqlFoto = "INSERT INTO AccidenteFotos (IDAccidente, Foto) VALUES (?,?)";
                    try (PreparedStatement psF = conn.prepareStatement(sqlFoto)) {
                        for (byte[] foto : a.getFotos()) {
                            psF.setInt   (1, idAccidente);
                            psF.setBytes (2, foto);
                            psF.executeUpdate();
                        }
                    }
                }
            }

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera de BD un accidente por su IDEmergencia,
     * mapeándolo completamente a un objeto Accidente (incluyendo sus fotos).
     */
    public static Accidente buscarAccidente(int idEmergencia) {
        String sql = "SELECT * FROM Accidentes WHERE IDEmergencia = ?";
        try (
            Connection conn = ConexionSQLite.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idEmergencia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1) Cargar las fotos asociadas
                    List<byte[]> fotos = new ArrayList<>();
                    String sqlFotos = "SELECT Foto FROM AccidenteFotos WHERE IDAccidente = ?";
                    int idAcc = rs.getInt("IDAccidente");
                    try (PreparedStatement psF = conn.prepareStatement(sqlFotos)) {
                        psF.setInt(1, idAcc);
                        try (ResultSet rsF = psF.executeQuery()) {
                            while (rsF.next()) {
                                fotos.add(rsF.getBytes("Foto"));
                            }
                        }
                    }

                    // 2) Construir y devolver el objeto Accidente (con fotos al final)
                    return new Accidente(
                        rs.getInt("IDEmergencia"),
                        rs.getInt("Matricula"),
                        rs.getString("NombreEstudiante"),
                        rs.getString("ApellidoPaterno"),
                        rs.getString("ApellidoMaterno"),
                        rs.getInt("Edad"),
                        rs.getString("Sexo"),
                        rs.getString("Escuela"),
                        rs.getString("ProgramaAcademico"),
                        rs.getInt("Semestre"),
                        rs.getString("CorreoUDLAP"),
                        rs.getString("TelefonoEstudiante"),
                        rs.getString("Direccion"),
                        rs.getString("FechaAccidente"),
                        rs.getString("DiaSemana"),
                        rs.getString("LugarOcurrencia"),
                        rs.getString("UbicacionExacta"),
                        rs.getString("EnHorarioClase"),
                        rs.getString("TipoTrayecto"),
                        rs.getString("CentroAtencionInicial"),
                        rs.getString("LesionPrincipal"),
                        rs.getString("LesionSecundaria"),
                        rs.getString("ParteCuerpo"),
                        rs.getString("GravedadTriage"),
                        rs.getString("NivelConsciencia"),
                        rs.getString("SignosVitales"),
                        rs.getString("DescripcionDetallada"),
                        rs.getString("PrimerosAuxilios"),
                        rs.getString("Medicamentos"),
                        rs.getString("DiagnosticoCIE10"),
                        rs.getString("LesionesAtribuibles"),
                        rs.getString("RiesgoMuerte"),
                        rs.getInt("IncapacidadDias"),
                        rs.getString("RequiereHospitalizacion"),
                        rs.getString("TratamientoRecomendado"),
                        rs.getString("MedicoTratante"),
                        rs.getString("CedulaProfesional"),
                        rs.getString("FechaInformeMedico"),
                        rs.getString("HospitalDestino"),
                        rs.getString("ResponsableTraslado"),
                        rs.getString("MedioTransporte"),
                        rs.getString("FechaHoraIngreso"),
                        rs.getString("NombreContacto"),
                        rs.getString("RelacionContacto"),
                        rs.getString("TelefonoContacto"),
                        rs.getString("CorreoContacto"),
                        rs.getString("DomicilioContacto"),
                        rs.getString("Testigo1Nombre"),
                        rs.getString("Testigo1Telefono"),
                        rs.getString("NarrativaDetallada"),
                        rs.getString("FechaElaboracion"),
                        fotos
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
