package Justificantes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Image;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class GeneradorPDFJustificante {

    public static File generar(Justificante j) {
        try {
            // Carpeta de salida
            String carpetaBase = System.getProperty("user.home") + File.separator + "JustificantesGenerados";
            File carpeta = new File(carpetaBase);
            if (!carpeta.exists())
                carpeta.mkdirs();

            // Nombre del archivo
            String nombreArchivo = String.format("Justificante_Folio%d_%s.pdf",
                    j.getFolio(), j.getNombrePaciente().replaceAll("\\s+", "_"));
            File file = new File(carpeta, nombreArchivo);

            // Crear documento PDF
            Document doc = new Document(PageSize.A4, 50, 50, 70, 50);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            // Fuentes y estilos
            Font titulo = new Font(Font.HELVETICA, 26, Font.BOLD, new Color(242, 140, 40)); // Naranja UDLAP
            Font texto = new Font(Font.HELVETICA, 12);
            Font seccion = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font firma = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Formato de fechas
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // LOGOTIPO
            try {
                String pathLogo = "src/icons/udlap_logo.png";
                Image logo = Image.getInstance(pathLogo);
                logo.scaleToFit(150, 60);
                logo.setAlignment(Image.ALIGN_CENTER);
                doc.add(logo);
            } catch (Exception ex) {
                System.out.println("No se pudo cargar el logotipo: " + ex.getMessage());
            }

            // Título
            doc.add(new Paragraph("Justificante", titulo));
            doc.add(new Paragraph(" "));

            // Cuerpo del texto
            StringBuilder cuerpo = new StringBuilder();
            cuerpo.append("Sistemas Médicos UDLAP emite el siguiente justificante médico al alumno/a ");
            cuerpo.append(j.getNombrePaciente()).append(" con ID ").append(j.getIdPaciente()).append(", ");
            cuerpo.append("por el motivo de ").append(j.getMotivo()).append(".\n\n");

            cuerpo.append("Se solicita reposo obligatorio desde el ")
                  .append(j.getFechaInicio().format(fmt)).append(" hasta el ")
                  .append(j.getFechaFin().format(fmt)).append(", ")
                  .append("con aprobación del médico ").append(j.getResueltoPor()).append(".\n\n");

            cuerpo.append("El diagnóstico emitido es: ").append(j.getDiagnostico()).append("\n\n");

            doc.add(new Paragraph(cuerpo.toString(), texto));
            doc.add(new Paragraph(" "));

            // Información institucional
            Paragraph branding = new Paragraph("UDLAP - SISTEMAS MÉDICOS", seccion);
            branding.setAlignment(Element.ALIGN_CENTER);
            doc.add(branding);
            doc.add(new Paragraph(" "));

            // Información de control
            doc.add(new Paragraph("Folio: " + j.getFolio(), texto));
            doc.add(new Paragraph("Estado del justificante: " + j.getEstado(), texto));
            doc.add(new Paragraph("Resuelto por: " + j.getResueltoPor(), texto));
            doc.add(new Paragraph("Fecha de resolución: " + j.getFechaResolucion().format(fmt), texto));

            // Firma
            doc.add(new Paragraph("\n\n________________________", texto));
            doc.add(new Paragraph(j.getResueltoPor(), firma));

            doc.close();
            return file;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
