package Main;

import Paneles_principales.Login;
import static Paneles_rotativos.Ingre.calendario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import java.util.Scanner;
//ig:lauti_reynosoo

public class Controlador {

    Modelo modelo = new Modelo();
    Scanner tc = new Scanner(System.in);

    /**
     * *****************************************************
     */
    /*                         INGRESO                      */
    /**
     * *****************************************************
     */
    /**
     * *************************************************************************
     * LOGEARSE Y COMPROBAR * *
     * *************************************************************************
     *
     */
    public Boolean login(String usuario, String contraseña) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.ValidarUsuario(usuario, contraseña);

            if (res.next() == true) {
                return true;
            } else {
                return false;
            }

        }

        return false;
    }


    /*---------------------- Buscar Alumno ------------------------------- */
    public ResultSet BuscarAlumno(String dni) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;
            //le paso la conexion de la base de datos

            res = modelo.BuscarEstudiante(dni);
            return res;

        }
        return null;
    }

    public ResultSet BuscarAportante(String dni) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res = modelo.BuscarAportante(dni);

            return res;
        }

        return null;
    }

    public ResultSet BuscarParsela(String parsela) throws SQLException {

        if (Main.conexion != null) {

            //le paso la conexion de la base de datos
            //      String dni = tc.nextLine();
            ResultSet res = modelo.BuscarParsela(parsela);

            return res;
        }

        return null;
    }

    public int IngresoParticular(String documento, String nombre, String categoria, String dia_ingreso, String dia_egreso, String parsela, float importe, int acomp, String hora) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarIngreso(documento, nombre, categoria, dia_ingreso, dia_egreso, parsela, importe, acomp, hora);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int ControlarCajaAbierta() throws SQLException {

        ResultSet res = modelo.cajausuario(Login.usuario);

        if (res.next()) {
            return 1; //existe una caja abierta
        } else {
            return 0;
        }

    }

    public int IngresoDiario(String documento, String nombre, String hora_ingreso, String categoria, int acomp) {
        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarIngresoDiario(documento, nombre, hora_ingreso, categoria, acomp);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoRegistro(String usuario, String comentario, String fecha, String hora, int codigo) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarRegistro(usuario, comentario, fecha, hora, codigo);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoParsela(String documento, String parsela, int quincho, String fecha_egreso) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarParsela(documento, parsela, quincho, fecha_egreso);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    /**
     * *************************************************************************
     * INSERTAR UNA SALIDA TEMPORAL DE UN ACAMPANTE * *
     * *************************************************************************
     */
    public int Salidas_Temporales(String nombre, String categoria, String documento) {

        if (Main.conexion != null) {
            String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
            String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
            String segundos = String.valueOf(calendario.get(Calendar.SECOND));

            String hora_actual = hora + ":" + minutos;
            int c = modelo.InsertarSalida(documento, nombre, hora_actual, categoria);

            if (c == 1) {
                return 1;
            } else {
                return 0;
            }
        } else {
            System.out.println("error, no esta conectado a la base de datos");
        }
        return 0;

    }

    /**
     * *************************************************************************
     * Controlar y eliminar salida temporal de acampantes * *
     * *************************************************************************
     */
    public ResultSet MostrarSalida(String dni) throws SQLException {

        if (Main.conexion != null) {
            ResultSet res;

            res = modelo.ConsultarSalidaTemporal(dni);

            return res;

        }

        return null;
    }

    public int Verificar_salida(String dni) {

        if (Main.conexion != null) {

            int c = modelo.EliminarSalida(dni);

            return c;

        }
        return 0;
    }

    /**
     * *************************************************************************
     * CREAR NUEVO USUARIO * *
     * *************************************************************************
     */
    public void NuevoUsuario(String usuario, String contrasenia, String privilegio) {

        int c = modelo.NuevoUsuario(usuario, contrasenia, privilegio);

        if (c == 1) {
            System.out.println("Nuevos usuarios agregado");

        } else {
            System.out.println("Ocurrio un problema a la hora de cargar un nuevo usuario");
        }

    }

    /**
     * *************************************************************************
     * ELIMINAR NUEVO USUARIO * *
     * *************************************************************************
     */
    public void EliminarUsuario(String usuario) {

        if (Main.conexion != null) {

            int c = modelo.EliminarUsuario(usuario);

            if (c != 1) {
                System.out.println("Este usuario no existe");
            }

        }

    }

    /**
     * *************************************************************************
     * MOSTRAR PADRON ALUMNOS * *
     * *************************************************************************
     */
    public ResultSet MostrarPadronAlumnos() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostarTablaAlumnos();
            return res;

        }

        return null;

    }

    /**
     * *************************************************************************
     * MOSTRAR OCUPACION ACTUAL DEL CAMPING * *
     * *************************************************************************
     */
    public ResultSet MostraParselas() throws SQLException {

        if (Main.conexion != null) {
            ResultSet res;

            res = modelo.MostrarParselas();

            return res;

        }

        return null;
    }

    public ResultSet MostarOcupacionActual() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostarOcupacionActual();
            return res;

        }
        return null;
    }

    public ResultSet ControlarAcampante(String dni) {

        if (Main.conexion != null) {
            ResultSet res;

            res = modelo.ControlarAcampante(dni);

            return res;

        }

        return null;
    }

    /**
     * *************************************************************************
     * BUSCAR DOCUMENTO MATEIN * *
     * *************************************************************************
     */
    public ResultSet MostarDocumento(String documento) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.BuscarDocumento(documento);
            return res;

        }
        return null;
    }

    /**
     * *************************************************************************
     * MOSTRAR PRIVILEGIO* *
     * *************************************************************************
     */
    public void SetearPrivilegio(String usuario) throws SQLException {

        int privilegio;

        ResultSet res;

        if (Main.conexion != null) {

            res = modelo.BuscarPrivilegio(usuario);
            if (res.next() == true) {
                privilegio = res.getInt("privilegios");

                if (privilegio == 1) {
                    Main.privilegio = "Administrador";
                }
                if (privilegio == 2) {
                    Main.privilegio = "Cajero";
                }
                if (privilegio == 3) {
                    Main.privilegio = "Personal";
                }

            }

        }

    }

    /**
     * *************************************************************************
     * CALCULAR IMPORTE* *
     * *************************************************************************
     * @param CantDias
     * @param categoria
     * @return
     */
    public float CalcularImporte(int CantDias, int categoria) {

        float importe = 0;

        if (Main.conexion != null) {

            if (categoria == 1) {

                importe = (CantDias * (Main.tarfia_acampar_alumnos));
                return importe;

            }
            if (categoria == 2) {

                importe = (CantDias * (Main.tarfia_acampar_aportantes));
                return importe;

            }
            if (categoria == 3) {

                importe = (CantDias * (Main.tarfia_acampar_particular));
                return importe;

            }
            if (categoria == 4) {

                importe = (CantDias * (Main.tarifa_acampar_invitados));
                return importe;

            }
            if (categoria == 5) {//es un aportante ingresando a una cabaña
                return importe = (CantDias * (Main.tarifa_cabaña_aportante));
            }
            if (categoria == 6) {//es un NO aportante ingresando a una cabaña
                return importe = (CantDias * (Main.tarifa_cabaña_noaportante));
            }

        }

        return 0;
    }

        public String calcularimportevehiculo() throws SQLException {
        ResultSet res;
        res = modelo.tarifas();
        String importe;
        if (res.next()) {
            importe = res.getString("tarifa_vehiculo");
            return importe;
        } else {
            return null;
        }

    }

    public void setear_tarifas() throws SQLException {
        ResultSet res;
        res = modelo.tarifas();
        while (res.next()) {
            Main.tarfia_acampar_alumnos = Float.parseFloat(res.getString("tarifa_alumno"));
            Main.tarfia_acampar_aportantes = Float.parseFloat(res.getString("tarifa_aportante"));
            Main.tarfia_acampar_particular = Float.parseFloat(res.getString("tarifa_particular"));
            Main.tarifa_dia_alumnos = Float.parseFloat(res.getString("alumno_d"));
            Main.tarifa_dia_aportantes = Float.parseFloat(res.getString("aportante_d"));
            Main.tarifa_dia_particular = Float.parseFloat(res.getString("particular_d"));

            Main.tarifa_dia_invitados = Float.parseFloat(res.getString("tarifa_invitados"));
            Main.tarifa_acampar_invitados = Float.parseFloat(res.getString("invitados_acampar"));
            Main.tarifa_cabaña_aportante = Float.parseFloat(res.getString("tarifa_cabañas"));
            Main.tarifa_cabaña_noaportante = Float.parseFloat(res.getString("cabañas_noaportantes"));
            Main.tarifa_casillas = Float.parseFloat(res.getString("tarifa_vehiculo"));

        }
    }

    public int Controldnirepetidoingreso(String dni) throws SQLException {

        ResultSet res;

        res = modelo.BuscarDocumento(dni);

        if (res.next() == true) {
            return 1;
        } else {
            return 0;
        }

    }

    public int Controldnirepetidoingresodiario(String dni) throws SQLException {

        ResultSet res;

        res = modelo.BuscarDocumentoDiario(dni);

        if (res.next() == true) {
            return 1;
        } else {
            return 0;
        }

    }

    public int IngresoTarifaParticular(float particular) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaParticular(particular);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoTarifaAlumno(float alumno) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaAlumno(alumno);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoTarifaAportante(float aportante) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaAportante(aportante);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoTarifaAportante_dia(float aportante) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaAportante_dia(aportante);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoTarifaAlumno_dia(float alumno) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaAlumno_dia(alumno);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

    public int IngresoTarifaParticular_dia(float particular) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaParticular_dia(particular);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }
    
       public int IngresoTarifaVehiculo(float vehiculo) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.InsertarTarifaVehiculo(vehiculo);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }


    public ResultSet MostarOcupacionActualDia() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostarOcupacionActualDia();
            return res;

        }
        return null;
    }

    public ResultSet MostarDocumentoDia(String documento) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.BuscarDocumentoDia(documento);
            return res;

        }
        return null;
    }

    public ResultSet MostrarEgresoPorDocumento(String documento) throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostrarEgresoPorDocumento(documento);
            return res;

        }
        return null;
    }

    public ResultSet MostrarUsuarios() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostrarUsuarios();
            return res;

        }
        return null;
    }

    public ResultSet MostarVehiculos() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostrarVehiculos();
            return res;

        }
        return null;
    }

    public ResultSet MostarEgreso() throws SQLException {

        if (Main.conexion != null) {

            ResultSet res;

            res = modelo.MostrarEgreso();
            return res;

        }
        return null;
    }

    public int ModificarParcela(int parcela, int parcela_actual) {

        if (Main.conexion != null) {

            //devuelve 1 si se completo el registro
            int c = modelo.ModificarParcela(parcela, parcela_actual);

            if (c == 1) {
                return c;
            } else {
                return c;
            }
        }
        return 0;
    }

}
