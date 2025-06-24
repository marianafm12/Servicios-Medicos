# ServiciosMedicos-POO
  Proyecto final de la materia Programación Orientada a Objetos en el cual se creó un sistema desde cero enfocado para optimizar servicios médicos UDLAP.

# Intrucciones de uso del programa y sobre el repositorio
  ### El proyecto inicialmente se tiene que ejecutar desde **\src\Inicio\InterfazLogin**, en donde saltará la interfaz gráfica para iniciar sesión y de ahí se sigue automáticamente el flujo del programa

  * La branch de dev es en la que se hacían todos los merge y cuando ya se revisaba el correcto funcionamiento, se hacía el merge con el main.
  * Se automatizó la actualización de las branches para que cuando los integrantes modificaran sus branches e hicieran los pull request, push y merge a dev las demás branches se actualizaran de igual forma haciendo que sea más llevadero el proceso para los integrantes optimizando el tiempo al reducir la cantidad de pulls que tuvieran que hacer.
  * Los cambios del main están bloqueados para así evitar que se hiciera un cambio que no funcionara y reducir de manera significativa el riesgo general del mal funcionamiento del programa.
  * **Inicio de sesión**
    * Para iniciar sesiónd desde el punto de vista del médico se puede ingresar un ID entre 5000 y 5030, y la contraseña es passID, por ejemplo; ID: 5009 contraseña: pass5009
    * Para iniciar sesión desde el punto de vista del paciente se puede ingresar un IS entre 180000 y 180030, y la contraseña funciona de forma similar a la de médico, se ingresa passID; por ejemplo: ID: 180000 contraseña: pas18000

# Estructura del Proyecto
  Se dividó el proyecto en diferentes áreas, la cual se le asignó a cada uno de los integrantes del equipo.
  
| Package               | Descripción                                                                                                                                                                                               | Integrante |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------- |
| BaseDeDatos           | Se incluyen los archivos de la base de datos, como la conexión con SQLite y la clase que, al ingresar el ID de un alumno, automáticamente busca sus otros datos.                                       | Mariana    |
| Consultas             | Códigos del área de consultas destinados a cuando el médico está en consulta y se registran los síntomas, el diagnóstico y la información relevante de la consulta.                                      | Mariana, Robbie, Serenity(inicialmente)           |
| Emergencias           | Área destinada a emergencias: botón de pánico, registro de llamada de emergencia y llenado del reporte de un accidente.                                                                                   | Mariana    |
| GestiónCitas          | Perspectiva del paciente para agendar citas de Consulta, Examen médico y Enfermería; permite modificar, cancelar y, en caso de fecha ocupada, agregarse a lista de espera con notificación al liberarse. | Arlette    |
| GestionEnfermedades   | Permite al médico acceder al historial médico de un paciente específico y al paciente visualizar su propio historial, con información de registros y consultas realizadas.                               | Robbie     |
| Inicio                | Interfaces iniciales (Login y base) con paneles fijos (título, bienvenida y cerrar sesión), menú dinámico según rol (médico o paciente) y área central que muestra el `JFrame` correspondiente.           | Mariana    |
| Justificantes         | Gestión de solicitudes de justificantes médicos (externos e internos); el médico puede aprobar, rechazar o generar justificantes desde consultas internas.                                             | Eduardo    |
| Modelo                | Código para gestionar la lista de espera de citas.                                                                                                                                                         | Arlette    |
| Registro              | Código para el registro de nuevos pacientes, creación de expediente médico y altas de pacientes.                                                                                                           | Sebastián  |
| Utilidades            | Clases base (colores de interfaces, barra de controles, etc.) y componentes reutilizables empleados en toda la aplicación.                                                                               | Mariana    |



## Mejoras Visuales (GUI)
  * Incialmente se implementaron las interfaces gráficas para la interacción del usuario con el programa, sin embargo, para la entrega final, además del uso de los JFrames, se implementaron JPanels. Estos cambios hacen nuestra interfaz visualmente más atractiva y funcional, ya que sólo se cambian los paneles y no toda la interfaz, haciendo el proyecto mucho más fluido. Además este cambio nos ayudó a cambiar la paleta de colores por colores UDLAP.
  * Encargadas de las mejoras visuales: **Arlette** y **Mariana**.
