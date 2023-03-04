package Main;

import Paneles_principales.Login;
import static Paneles_rotativos.Ingre.calendario;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Modelo {

    public DateTimeFormatter f = DateTimeFormatter.ofPattern("HH':'mm");

    public ResultSet BuscarPrivilegio(String usuario) {

        String sql = "SELECT * FROM usuarios where usuario = '" + usuario + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public int InsertarIngreso(String documento, String nombre, String categoria, String dia_ingreso, String dia_egreso, String parsela, float importe, int acomp, String hora) {
        String sql = "INSERT INTO ingreso (documento, nombre, categoria ,fecha_ingreso ,fecha_egreso , parsela ,importe, acomp , hora )"
                + "VALUES('" + documento + "','" + nombre + "','" + categoria + "','" + dia_ingreso + "','" + dia_egreso + "','" + parsela + "','" + importe + "','" + acomp + "','" + hora + "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarIngresoDiario(String documento, String nombre, String hora_ingreso, String categoria, int acomp) {
        String sql = "INSERT INTO ingreso_diario (nombre, dni, hora_ingreso ,categoria,fecha_ingreso , acomp)"
                + "VALUES('" + nombre + "','" + documento + "','" + hora_ingreso + "','" + categoria + "','" + Main.DiaActual + "','" + acomp + "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarRegistro(String usuario, String comentario, String fecha, String hora, int codigo) {
        String sql = "INSERT INTO registros (usuario, comentario, fecha ,hora,codigo)"
                + "VALUES('" + usuario + "','" + comentario + "','" + fecha + "','" + hora + "','" + codigo +  "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarParsela(String documento, String parsela, int quincho, String fecha_egreso) {
        String sql = "INSERT INTO parselas (documento, parsela, quincho, fecha_egreso)"
                + "VALUES('" + documento + "','" + parsela + "','" + quincho + "','" + fecha_egreso + "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarSalida(String documento, String nombre, String fecha_hora_salida, String tipo) {
        String sql = "INSERT INTO salida (doc,nombre, Hora_salida, tipo) "
                + "VALUES('" + documento + "','" + nombre + "','" + fecha_hora_salida + "','" + tipo + "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarRegreso(String cod_salida, String documento, String cod_pulsera, java.sql.Timestamp fecha_hora_salida, java.sql.Timestamp fecha_hora_regreso, String tipo) {
        String sql = "INSERT INTO regreso ( cod_salida, doc, cod_pulsera, fecha_hora_salida, fecha_hora_regreso, tipo) "
                + "VALUES('" + cod_salida + "','" + documento + "','" + cod_pulsera + "','" + fecha_hora_salida + "','" + fecha_hora_regreso + "','" + tipo + "')";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int EliminarSalida(String documento) {
        String sql = "DELETE FROM salida WHERE doc='" + documento + "'";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int EliminarAcampante(String documento) throws SQLException {
        String sql = "SELECT * FROM ingreso where documento = '" + documento + "'";

        ResultSet res = Main.conexion.EjecutarConsultaSQL(sql);

        ArrayList<String> nombre = new ArrayList<>();
        ArrayList<String> dni = new ArrayList<>();
        ArrayList<String> categoria = new ArrayList<>();
        ArrayList<String> dia_ingreso = new ArrayList<>();
        ArrayList<String> dia_egreso = new ArrayList<>();
        ArrayList<Float> importe = new ArrayList<>();

        while (res.next()) {

            dni.add(res.getString("documento"));
            nombre.add(res.getString("nombre"));
            categoria.add(res.getString("categoria"));
            dia_ingreso.add(res.getString("fecha_ingreso"));
            dia_egreso.add(res.getString("fecha_egreso"));
            importe.add(res.getFloat("importe"));

        }

        for (int i = 0; i < dni.size(); i++) {

            String sql1 = "INSERT INTO egreso (documento, nombre, categoria ,fecha_ingreso ,fecha_egreso , importe )"
                    + "VALUES('" + dni.get(i) + "','" + nombre.get(i) + "','" + categoria.get(i) + "','" + dia_ingreso.get(i) + "','" + Main.DiaActual + "','" + importe.get(i) + "')";
            int c = Main.conexion.EjecutarOperacion(sql1);

        }

        //SELECT * FROM TABLENAME WHERE DateTime >= '2011-04-12T00:00:00.000'
        String sql2 = "DELETE FROM ingreso where documento = '" + documento + "'";

        int v = Main.conexion.EjecutarOperacion(sql2);
        return v;
    }

    public ResultSet BuscarEstudiante(String documento) {
        String sql;
        sql = "SELECT * FROM alumnos WHERE documento = '" + documento + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet BuscarAportante(String documento) {
        String sql;
        sql = "SELECT * FROM aportantes WHERE doc = '" + documento + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet mostraraportantes() {
        String sql;
        sql = "SELECT * FROM aportantes";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet mostraralumnos() {
        String sql;
        sql = "SELECT * FROM alumnos";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet BuscarDocumento(String documento) {
        String sql;
        sql = "SELECT * FROM ingreso WHERE documento = '" + documento + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet BuscarDocumentoDiario(String documento) {
        String sql;
        sql = "SELECT * FROM ingreso_diario WHERE dni = '" + documento + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet BuscarParsela(String parsela) {

        String sql;
        sql = "SELECT * FROM ingreso WHERE parsela = '" + parsela + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet EliminarParsela(String parsela) {

        String sql;
        sql = "DELETE FROM parselas WHERE parsela = '" + parsela + "'";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public void ConsultarSalida() throws SQLException {

        String sql;
        ResultSet res;

        sql = "SELECT * FROM ingreso where fecha_egreso <= '" + Main.DiaActual + "'";

        res = Main.conexion.EjecutarConsultaSQL(sql);

        ArrayList<String> nombre = new ArrayList<>();
        ArrayList<String> documento = new ArrayList<>();
        ArrayList<String> categoria = new ArrayList<>();
        ArrayList<String> dia_ingreso = new ArrayList<>();
        ArrayList<String> dia_egreso = new ArrayList<>();
        ArrayList<Float> importe = new ArrayList<>();

        while (res.next()) {

            documento.add(res.getString("documento"));
            nombre.add(res.getString("nombre"));
            categoria.add(res.getString("categoria"));
            dia_ingreso.add(res.getString("fecha_ingreso"));
            dia_egreso.add(res.getString("fecha_egreso"));
            importe.add(res.getFloat("importe"));

        }

        for (int i = 0; i < documento.size(); i++) {

            String sql1 = "INSERT INTO egreso (documento, nombre, categoria ,fecha_ingreso ,fecha_egreso , importe )"
                    + "VALUES('" + documento.get(i) + "','" + nombre.get(i) + "','" + categoria.get(i) + "','" + dia_ingreso.get(i) + "','" + dia_egreso.get(i) + "','" + importe.get(i) + "')";
            int c = Main.conexion.EjecutarOperacion(sql1);

        }

        //SELECT * FROM TABLENAME WHERE DateTime >= '2011-04-12T00:00:00.000'
        String sql2 = "DELETE FROM ingreso where fecha_egreso <= '" + Main.DiaActual + "'";

        int v = Main.conexion.EjecutarOperacion(sql2);

    }

    public void e_vehiculos() throws SQLException {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        String sql = "DELETE FROM vehiculos where fecha_egreso <= '" + Main.DiaActual + "' AND horas <= '"+Main.HoraActual+"';";
        ResultSet res;

        int v = Main.conexion.EjecutarOperacion(sql);
    }

    public void egreso_diario() throws SQLException {
        String sql;
        ResultSet res;

        sql = "SELECT * FROM ingreso_diario where fecha_ingreso < '" + Main.DiaActual + "'";

        res = Main.conexion.EjecutarConsultaSQL(sql);

        ArrayList<String> nombre = new ArrayList<>();
        ArrayList<String> documento = new ArrayList<>();
        ArrayList<String> categoria = new ArrayList<>();
        ArrayList<String> dia_ingreso = new ArrayList<>();
        ArrayList<String> hora_ingreso = new ArrayList<>();

        while (res.next()) {

            documento.add(res.getString("dni"));
            nombre.add(res.getString("nombre"));
            categoria.add(res.getString("categoria"));
            dia_ingreso.add(res.getString("fecha_ingreso"));
            hora_ingreso.add(res.getString("hora_ingreso"));

        }
        for (int i = 0; i < documento.size(); i++) {

            String sql1 = "INSERT INTO egreso_diario (documento, nombre, categoria ,fecha_ingreso ,hora_ingreso)"
                    + "VALUES('" + documento.get(i) + "','" + nombre.get(i) + "','" + categoria.get(i) + "','" + dia_ingreso.get(i) + "','" + hora_ingreso.get(i) + "')";
            int c = Main.conexion.EjecutarOperacion(sql1);

        }

        String sql2 = "DELETE FROM ingreso_diario where fecha_ingreso < '" + Main.DiaActual + "'";

        int v = Main.conexion.EjecutarOperacion(sql2);

    }

    public int NuevoUsuario(String nombre, String contrasenia, String privilegios) {

        String sql;
        sql = " INSERT INTO usuarios (usuario,password, privilegios)"
                + "VALUES ('" + nombre + "','" + contrasenia + "','" + privilegios + "')";
        return Main.conexion.EjecutarOperacion(sql);

    }

    public ResultSet ValidarUsuario(String usuario, String pass) {
        String sql = "SELECT * FROM usuarios WHERE usuario = '" + usuario + "' AND password = '" + pass + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public int EliminarUsuario(String usuario) {
        String sql = "DELETE FROM usuarios WHERE usuario = '" + usuario + "'";

        return Main.conexion.EjecutarOperacion(sql);

    }

    public int eliminarvehiculo(String patente) {
        String sql = "DELETE FROM vehiculos WHERE patente = '" + patente + "'";

        return Main.conexion.EjecutarOperacion(sql);
    }

    public ResultSet MostarTablaAlumnos() {
        String sql;

        sql = "SELECT * FROM alumnos";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet MostrarParselas() {
        String sql;
        sql = "SELECT * FROM parselas";
        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet MostarOcupacionActual() {

        String sql;
        sql = "SELECT * FROM ingreso";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet ConsultarSalidaTemporales() {

        String sql = "SELECT * FROM salida";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet ConsultarSalidaTemporal(String dni) {

        String sql = "SELECT * FROM salida where doc = '" + dni + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet ControlarAcampante(String dni) {

        String sql = "SELECT * FROM ingreso where documento = '" + dni + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet traerinfo(int parsela) {

        String sql = "SELECT * FROM ingreso WHERE parsela = '" + parsela + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    //float particular_d, float alumno_d, float aportante_d, float particular, float alumno,
    public int InsertarTarifaAportante(float aportante) {
        String sql = "UPDATE tarifas SET tarifa_aportante = ' " + aportante + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaParticular(float particular) {
        String sql = "UPDATE tarifas SET tarifa_particular = ' " + particular + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaInvitados_dia(float invitados_dia) {
        String sql = "UPDATE tarifas SET tarifa_invitados = ' " + invitados_dia + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaInvitados(float invitados) {
        String sql = "UPDATE tarifas SET invitados_acampar = ' " + invitados + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaCabañas(float invitados) {
        String sql = "UPDATE tarifas SET tarifa_cabañas = ' " + invitados + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaCabañasnoaportantes(float invitados) {
        String sql = "UPDATE tarifas SET cabañas_noaportantes = ' " + invitados + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaAlumno(float alumno) {
        String sql = "UPDATE tarifas SET tarifa_alumno = ' " + alumno + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaAlumno_dia(float alumno) {
        String sql = "UPDATE tarifas SET alumno_d = ' " + alumno + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaAportante_dia(float aportante) {
        String sql = "UPDATE tarifas SET aportante_d = ' " + aportante + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaParticular_dia(float particular) {
        String sql = "UPDATE tarifas SET particular_d = ' " + particular + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int InsertarTarifaVehiculo(float vehiculo) {
        String sql = "UPDATE tarifas SET tarifa_vehiculo = ' " + vehiculo + " ' WHERE id = 8";
        return Main.conexion.EjecutarOperacion(sql);
    }

    public int insertarvehiculo(String patente, String marca, String importe, String fecha_ingreso, String fecha_egreso, String horas) {
        String sql = "INSERT INTO vehiculos (marca,patente,importe,fecha_ingreso,fecha_egreso,horas)"
                + "VALUES ('" + marca + "','" + patente + "','" + importe + "','" + fecha_ingreso + "','" + fecha_egreso + "','" + horas + "')";

        return Main.conexion.EjecutarOperacion(sql);
    }

    public ResultSet tarifas() {
        String sql = "SELECT * FROM tarifas";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet MostrarVehiculos() {
        String sql = "SELECT * FROM vehiculos";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet MostarOcupacionActualDia() {

        String sql;
        sql = "SELECT * FROM ingreso_diario";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet MostrarUsuarios() {

        String sql;
        sql = "SELECT * FROM usuarios";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet BuscarDocumentoDia(String documento) {
        String sql;
        sql = "SELECT * FROM ingreso_diario WHERE dni = '" + documento + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet MostrarEgresoPorDocumento(String documento) {
        String sql;
        sql = "SELECT * FROM egreso WHERE fecha_egreso = '" + Main.DiaActual + "' AND documento = '" + documento + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public int ModificarParcela(int parcela, int parcela_actual) {
        String sql = "UPDATE ingreso SET parsela = '" + Integer.toString(parcela_actual) + "' WHERE parsela = " + Integer.toString(parcela);
        //     String sql = "UPDATE tarifas SET tarifa_particular = ' " + particular + " ' WHERE id = 8";

        System.out.println(parcela + "::::" + parcela_actual);

        return Main.conexion.EjecutarOperacion(sql);
    }

    public ResultSet MostrarEgreso() {
        String sql;
        sql = "SELECT * FROM egreso WHERE fecha_egreso = '" + Main.DiaActual + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet MostrarEgreso_1() {
        String sql;
        sql = "SELECT * FROM egreso";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public int AbrirCaja(String monto_inicial) {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        LocalTime HoraActual = LocalTime.now();
        String hora_actual = hora + ":" + minutos;

        String sql = "INSERT INTO caja_abierta (usuario,retiros,plata_en_caja,fecha_abertura)" + "VALUES('" + Login.usuario + "','" + "0" + "','" + monto_inicial + "','" + Main.DiaActual + " " + HoraActual.format(f) + "')";
        return Main.conexion.EjecutarOperacion(sql);

    }

    public ResultSet cajausuario(String usuario) {
        String sql;
        sql = "SELECT * FROM caja_abierta WHERE usuario = '" + usuario + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }
    public ResultSet todascajausuario() {
        String sql;
        sql = "SELECT * FROM caja_abierta";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet cajausuariocerrada(String usuario) {
        String sql;
        sql = "SELECT * FROM caja_cerradas WHERE usuario = '" + usuario + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }
    public ResultSet todaslascajausuariocerrada() {
        String sql;
        sql = "SELECT * FROM caja_cerradas";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public int CerrarCaja(String usuario) throws SQLException {
        String sql;
        sql = "SELECT * FROM caja_abierta WHERE usuario = '" + usuario + "'";
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        LocalTime HoraActual = LocalTime.now();
        String hora_actual = hora + ":" + minutos;
        ResultSet res = Main.conexion.EjecutarConsultaSQL(sql);

        if (res.next()) {
            String fecha_abertura = res.getString("fecha_abertura");
            float retiros = Float.valueOf(res.getString("retiros"));
            float plata_en_caja = Float.valueOf(res.getString("plata_en_caja"));

            float plata_alcierre = (plata_en_caja - retiros);

            String sql2 = "INSERT INTO caja_cerradas (usuario,retiros,total_recaudado,fecha_abertura,fecha_cierre,plata_en_caja_al_cierre)" + "VALUES('" + usuario
                    + "','" + retiros + "','" + String.valueOf(plata_en_caja) + "','" + fecha_abertura + "','" + Main.DiaActual + " " + HoraActual.format(f) + "','" + String.valueOf(plata_alcierre) + "')";
            int v = Main.conexion.EjecutarOperacion(sql2);

            if (v == 1) {

                String sql3 = "DELETE FROM caja_abierta WHERE usuario = '" + usuario + "'";

                int v1 = Main.conexion.EjecutarOperacion(sql3);
                if (v1 != 1) {
                    return 2;
                } else {
                    return 1;//todo ok
                }

            } else {
                return 2; //error no pudo cerra caja
            }

        } else {
            return 2;//error no encontro ninguna caja abierta a este usuario
        }

    }

    public ResultSet mostrarregistros() {

        String sql = "SELECT * FROM registros";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public ResultSet mostrarregistros1() {

        String sql = "SELECT * FROM registros ORDER BY id DESC";

        return Main.conexion.EjecutarConsultaSQL(sql);
    }

    public int generearretiro(String importe_retiro) throws SQLException {
        LocalTime HoraActual = LocalTime.now();
        String sql1;
        sql1 = "SELECT * FROM caja_abierta WHERE usuario = '" + Login.usuario + "'";

        ResultSet res = Main.conexion.EjecutarConsultaSQL(sql1);
        if (res.next()) {
            float retiro = Float.valueOf(res.getString("retiros"));

            float retiro_total = retiro + Float.valueOf(importe_retiro);

            String sql = "UPDATE caja_abierta SET retiros = ' " + String.valueOf(retiro_total) + " ' WHERE usuario = '" + Login.usuario + "'";

            int respuesta = Main.conexion.EjecutarOperacion(sql);
            if (respuesta == 1) {
                String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                String hora_actual = hora + ":" + minutos;

                String sql3 = "INSERT INTO retiros (usuario,fecha_hora,importe)" + "VALUES('" + Login.usuario + "','" + Main.DiaActual + "','" + importe_retiro + "')";
                Main.conexion.EjecutarOperacion(sql3);
                return 1;
            } else {
                return 0;
            }

        } else {
            return 0;
        }

    }

    public ResultSet mostrarretiros(String usuario) {

        String sql1;
        sql1 = "SELECT * FROM retiros WHERE usuario = '" + Login.usuario + "' and fecha_hora = '" + Main.DiaActual + "'";

        ResultSet res = Main.conexion.EjecutarConsultaSQL(sql1);
        return res;

    }

    public ResultSet mostrar_registros_fecha(String fecha, String usuario) {
        String sql;
        sql = "SELECT * FROM registros where usuario = '" + usuario + "' and fecha = '" + fecha + "'";

        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public ResultSet mostrar_registros_fecha_hora(String fecha, String usuario, String hora_apertura, String hora_cierrre) {
        String sql;
        sql = "SELECT * FROM registros where usuario = '" + usuario + "' and fecha = '" + fecha + "'and hora >= '" + hora_apertura + "'and hora<= '" + hora_cierrre + "'";
        System.out.println(sql);
        return Main.conexion.EjecutarConsultaSQL(sql);

    }

    public void borrarretiros() {
        String sql = "DELETE FROM retiros where usuario = '" + Login.usuario + "' and fecha_hora = '" + Main.DiaActual + "'";
        Main.conexion.EjecutarOperacion(sql);

    }

    public int insertardinerocaja(float importe) throws SQLException {
        String sql1;
        sql1 = "SELECT * FROM caja_abierta WHERE usuario = '" + Login.usuario + "'";

        ResultSet res = Main.conexion.EjecutarConsultaSQL(sql1);
        if (res.next()) {
            float actual = Float.valueOf(res.getString("plata_en_caja"));

            float ingreso_total = actual + importe;

            String sql = "UPDATE caja_abierta SET plata_en_caja = ' " + String.valueOf(ingreso_total) + " ' WHERE usuario = '" + Login.usuario + "'";
            int respuesta = Main.conexion.EjecutarOperacion(sql);
            if (respuesta == 1) {
                return respuesta;
            }
        } else {
            return 0;
        }
        return 0;
    }
}
