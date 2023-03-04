package Main;

import Paneles_principales.noconexion;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.*;

public class Conexion {

    public String user = "sa";
    public String pass = "lauti";
    public java.sql.Statement s;
    public ResultSet resultado;
    public Connection conexion = null;

     public void Conectar(int ip) throws SQLException, ClassNotFoundException, UnknownHostException {

        try {
            if (conexion != null) {
                return;
            }

            String ip_local = Inet4Address.getLocalHost().getHostAddress().replaceAll("\\.\\d+$", "");
            DriverManager.setLoginTimeout(1);

            conexion = DriverManager.getConnection("jdbc:sqlserver://" + ip_local + "." + ip + ":1433;database=db_florida;encrypt=true;trustServerCertificate=true;", user, pass);

            if (conexion != null) {
                System.out.println("Conexion a base de datos:  ... Ok");
            }
        } catch (SQLException e) {
            ip++;
            if (ip < 130) {
                Conectar(ip);
                System.out.println("Error en conexion, volviendo a intentar.");
            } else {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new noconexion().setVisible(true);
                    }
                });
            }
        }
        this.s = conexion.createStatement();

    }

    public void Conectar2(int ip) throws SQLException, ClassNotFoundException, UnknownHostException {
        String ip_1 = Inet4Address.getLocalHost().getHostAddress();

        try {
            if (conexion != null) {
                return;
            }

            //String ip_local = Inet4Address.getLocalHost().getHostAddress().replaceAll("\\.\\d+$", "");
            DriverManager.setLoginTimeout(1);

            conexion = DriverManager.getConnection("jdbc:sqlserver://192.168.0.11:1433;database=db_florida;encrypt=true;trustServerCertificate=true;", user, pass);

            if (conexion != null) {
                System.out.println("Conexion a base de datos:  ... Ok");
                
            }
        } catch (SQLException e) {
            
            Conectar(0);
            
        }
        this.s = conexion.createStatement();
    }

   
   
    public void Desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            System.out.println("Problema para cerrar la Conexión a la base de datos ");
        }
    }

    public java.sql.ResultSet EjecutarConsultaSQL(String sql) {

        try {
            resultado = s.executeQuery(sql);

        } catch (SQLException ex) {
            return null;
        }
        return resultado;
    }

    public int EjecutarOperacion(String sql) {
        int respuesta = 0;
        try {
            respuesta = this.s.executeUpdate(sql);
            if (respuesta == 1) {
                System.out.println("Registro Guardado");
            } else {
                System.out.println("Ocurrio un problema al agregar el registro");

            }
        } catch (SQLException ex) {
            // Mostramos toda la informacion sobre el error disponible
            System.out.println("Error: SQLException");
            while (ex != null) {
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("Mensaje:  " + ex.getMessage());
                System.out.println("ErrorCode:   " + ex.getErrorCode());
                ex = ex.getNextException();
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error inesperado:    " + e.getMessage());
        }
        return respuesta;
    }

    /*
    public int InsertarIngreso(String documento, String nombre, String cod_pulsera, String categoria, String dia_ingreso, String importe){
        String sql="INSERT INTO ingreso (documento, nombre, cod_pulsera,categoria, dia_ingreso, importe) "
                + "VALUES('"+documento+"','"+nombre+"','"+cod_pulsera+"','"+categoria+"','"+dia_ingreso+"','"+importe+"')";
        return EjecutarOperacion(sql);
    }

    public int InsertarSalida(String documento, String cod_pulsera,String fecha_hora_salida, String tipo){
        String sql="INSERT INTO salida (doc, cod_pulsera, fecha_hora_salida, tipo) "
                + "VALUES('"+documento+"','"+cod_pulsera+"','"+fecha_hora_salida+"','"+tipo+"')";
        return EjecutarOperacion(sql);
    }

    public int InsertarRegreso(String cod_salida, String documento, String cod_pulsera, java.sql.Timestamp fecha_hora_salida, java.sql.Timestamp fecha_hora_regreso,String tipo){
        String sql="INSERT INTO regreso ( cod_salida, doc, cod_pulsera, fecha_hora_salida, fecha_hora_regreso, tipo) "
                + "VALUES('"+cod_salida+"','"+documento+"','"+cod_pulsera+"','"+fecha_hora_salida+"','"+fecha_hora_regreso+"','"+tipo+"')";
        return EjecutarOperacion(sql);
    }

    public int EliminarSalida(String cod_salida){
        String sql= "DELETE FROM salida WHERE id_salida='"+cod_salida+"'";
        return EjecutarOperacion(sql);
    }

    public  ResultSet BuscarEstudiante(String documento){
        String sql;
        sql="SELECT * FROM alumnos WHERE apellido = '"+documento+"'";
        return EjecutarConsultaSQL(sql);
    }

    public  ResultSet BuscarAportante(String documento){
        String sql;
        sql="SELECT * FROM aportantes WHERE doc = '"+documento+"'";
        return EjecutarConsultaSQL(sql);
    }
     */
}
