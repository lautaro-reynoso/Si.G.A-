/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles_rotativos;

import Main.Controlador;
import Main.Main;
import static Main.Main.controlador;
import Main.Modelo;
import Paneles_principales.Login;
import Paneles_principales.Principal;

import com.formdev.flatlaf.json.ParseException;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import Clases_tiketera.ConectorPluginV3;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author ig:lauti_reynosoo
 */
public class Ingre extends javax.swing.JPanel {

    public DateTimeFormatter f = DateTimeFormatter.ofPattern("HH':'mm");

    int filas = 0;
    Modelo modelo = new Modelo();
    public static Calendar calendario = Calendar.getInstance();

    public Ingre() {
        initComponents();
        tarifa1.setEnabled(false);
        fecha_ingreso_p.setEnabled(true);
        fecha_egreso_p.setEnabled(true);
        fecha_ingreso_p1.setEnabled(true);
        fecha_egreso_p1.setEnabled(true);
        fecha_ingreso.setEnabled(true);
        fecha_egreso.setEnabled(true);

    }

    Controlador controlador = new Controlador();

    public void setearnullparticular() {
        documento_p.setText("");
        nombre_p.setText("");

        Parsela_p.setText("Parcela");
        acomp_particular.setText("0");

    }

    public void setearnullinvitado() {
        documento_p1.setText("");
        nombre_p1.setText("");

        Parsela_p1.setText("Parcela");

        documento_p1.setEnabled(false);
        nombre_p1.setEnabled(false);

        documento_a1.setText("");
        Documento_a2.setText("");
        nombre_a1.setText("");

        cod_aportante1.setText("");
        apellido_a1.setText("");
        acomp_invitados.setText("0");

    }

    public void stearnullotros() {
        nombre_otros.setText("");
        documento_otros.setText("");
        descripcion_otros.setText("");
        tarifa_otros.setText("0.0");
        acomp_otros.setText("0");
        Parcela_otros.setText("");

    }

    public void setearnullaportante() {
        documento_a.setText("");
        nombre_a.setText("");

        Parsela_a.setText("Parcela");
        cod_aportante.setText("");
        apellido_a.setText("");
        familiares.setText("0");

    }

    public void setearnullalumno() {
        documento_e.setText("");
        nombre_e.setText("");

        Parcela.setText("Parcela");
        apellido_e.setText("");
        carrera_e.setText("");
        facultad_e.setText("");

    }

    public void BuscarEstudiante() throws SQLException {
        ResultSet res;

        int c = controlador.Controldnirepetidoingreso(Documento.getText());
        if (c == 0) {
            res = controlador.BuscarAlumno(Documento.getText());
            if (res.next() == true) {
                nombre_e.setText(res.getString("nombre"));
                apellido_e.setText(res.getString("apellido"));
                documento_e.setText(res.getString("documento"));
                carrera_e.setText(res.getString("carrera"));
                facultad_e.setText(res.getString("facultad"));
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se encontro el documento solicitado.\n Intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void RegistrarIngresoParticular() throws java.text.ParseException, java.lang.NullPointerException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        try {

            float importe = 0;
            int c;
            if (Integer.parseInt(Parsela_p.getText()) >= 1 && Integer.parseInt(Parsela_p.getText()) <= 4) {
                importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 6);
                tarifa1.setText(String.valueOf(importe));
            } else {
                if (Integer.parseInt(acomp_particular.getText()) == 0) {
                    importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 3);
                    tarifa1.setText(String.format("%.2f", importe));
                } else {
                    importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 3) * (Integer.parseInt(acomp_particular.getText()) + 1);
                }
            }

            if (fecha_egreso_p.getDate() != null && fecha_ingreso_p.getDate() != null) {
                c = controlador.IngresoParticular(documento_p.getText(), nombre_p.getText(), "Particular", calc_fecha(fecha_ingreso_p), calc_fecha(fecha_egreso_p), Parsela_p.getText(), importe, Integer.parseInt(acomp_particular.getText()),HoraActual.format(f));
                System.out.println(HoraActual.format(f));
                if (c != 1) {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                    String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                    String hora_actual = hora + ":" + minutos;
                    if (Integer.parseInt(acomp_particular.getText()) == 0) {
                        modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo particular acampante", Main.DiaActual, HoraActual.format(f), 0);
                    } else {
                        modelo.InsertarRegistro(Login.usuario, "ha ingresado " + String.format("%02d", (Integer.parseInt(acomp_particular.getText()) + 1)) + " nuevos particulares acampantes", Main.DiaActual, HoraActual.format(f), 1);
                    }

                    if (Integer.parseInt(acomp_particular.getText()) == 0) {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja: " + importe);
                    } else {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja con acampañantes: " + importe);
                    }

                    Component jFrame = null;
                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {

                        if (!pasar_dia2.isSelected()) {

                            imprimirtiketacampante(calc_fecha(fecha_ingreso_p), calc_fecha(fecha_egreso_p), "Particular", importe, nombre_p.getText(), documento_p.getText(), Integer.parseInt(acomp_particular.getText()), Main.tarfia_acampar_particular);
                        } else {

                            importe = Main.tarifa_dia_particular;
                            imprimirtiketdia("Particular", importe, nombre_p.getText(), documento_p.getText(), Integer.parseInt(acomp_particular.getText()), importe);
                        }
                    }
                    setearnullparticular();

                }

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe de ingresar una fecha de ingreso y egreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.lang.NullPointerException e) {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe completar todos los campos de ingreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public void RegistrarIngresoOtros() throws java.text.ParseException, java.lang.NullPointerException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        try {

            float importe = 0;
            int c;

            if (Integer.parseInt(Parcela_otros.getText()) >= 1 && Integer.parseInt(Parcela_otros.getText()) <= 4) {

                importe = (cant_dias(fecha_ingreso_otros, fecha_egreso_otros) * Float.parseFloat(tarifa_otros.getText()));
                tarifa4.setText(String.valueOf(importe));
            } else {
                if (Integer.parseInt(acomp_otros.getText()) == 0) {
                    importe = (cant_dias(fecha_ingreso_otros, fecha_egreso_otros) * Float.parseFloat(tarifa_otros.getText()));
                    tarifa4.setText(String.format("%.2f", importe));
                } else {

                    importe = cant_dias(fecha_ingreso_otros, fecha_egreso_otros) * (Integer.parseInt(acomp_otros.getText()) + 1) * Float.parseFloat(tarifa_otros.getText());
                }
            }

            if (fecha_egreso_otros.getDate() != null && fecha_ingreso_otros.getDate() != null) {
                c = controlador.IngresoParticular(documento_otros.getText(), nombre_otros.getText(), "Otros " + "(" + descripcion_otros.getText() + ")(" + tarifa_otros.getText() + ")", calc_fecha(fecha_ingreso_otros), calc_fecha(fecha_egreso_otros), Parcela_otros.getText(), importe, Integer.parseInt(acomp_otros.getText()),HoraActual.format(f));

                if (c != 1) {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                    String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                    String hora_actual = hora + ":" + minutos;

                    modelo.InsertarRegistro(Login.usuario, "registro " + String.format("%02d", (Integer.parseInt(acomp_otros.getText()) + 1)) + " nuevo/s ingresante/s(otros) acampante/s", Main.DiaActual, HoraActual.format(f), 2);

                    if (Integer.parseInt(acomp_otros.getText()) == 0) {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja otros: " + importe);
                    } else {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja con acampañantes otros: " + importe);
                    }

                    Component jFrame = null;
                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {

                        if (!pasar_dia4.isSelected()) {

                            imprimirtiketacampanteotros(calc_fecha(fecha_ingreso_otros), calc_fecha(fecha_egreso_otros), descripcion_otros.getText(), importe, nombre_otros.getText(), documento_otros.getText(), Integer.parseInt(acomp_otros.getText()), Float.parseFloat(tarifa_otros.getText()));
                        } else {

                            importe = Float.parseFloat(tarifa_otros.getText());
                            imprimirtiketdiaotros(descripcion_otros.getText(), importe, nombre_otros.getText(), documento_otros.getText(), Integer.parseInt(acomp_otros.getText()), Float.parseFloat(tarifa_otros.getText()));
                        }
                    }
                    stearnullotros();

                }

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe de ingresar una fecha de ingreso y egreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.lang.NullPointerException e) {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe completar todos los campos de ingreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public void RegistrarIngresoAportante() throws java.text.ParseException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        try {
            float importe = 0;

            if (Integer.parseInt(Parsela_a.getText()) >= 1 && Integer.parseInt(Parsela_a.getText()) <= 4) {
                importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p1, fecha_egreso_p1), 5);
                tarifa2.setText(String.valueOf(importe));
            } else {
                importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p1, fecha_egreso_p1), 2);
                tarifa2.setText(String.valueOf(importe));
            }
            int c;
            if (fecha_egreso_p1.getDate() != null && fecha_ingreso_p1.getDate() != null) {

                c = controlador.IngresoParticular(documento_a.getText(), nombre_a.getText(), "Aportante", calc_fecha(fecha_ingreso_p1), calc_fecha(fecha_egreso_p1), Parsela_a.getText(), importe, Integer.parseInt(acomp_particular.getText()),HoraActual.format(f));

                if (c != 1) {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                    String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                    String hora_actual = hora + ":" + minutos;
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo aportante acampante", Main.DiaActual, HoraActual.format(f), 0);
                    if (!familiares.getText().equals("0")) {
                        modelo.InsertarRegistro(Login.usuario, "ha ingresado " + familiares.getText() + " familiares de aportantes acamparse", Main.DiaActual, HoraActual.format(f), 4);
                    }
                    modelo.insertardinerocaja(importe);
                    Component jFrame = null;
                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {

                        if (!pasar_dia1.isSelected()) {

                            imprimirtiketacampanteaportante(calc_fecha(fecha_ingreso_p1), calc_fecha(fecha_egreso_p1), nombre_a.getText(), documento_a.getText(), Integer.parseInt(familiares.getText()));
                        } else {

                            importe = Main.tarifa_dia_aportantes;
                            imprimirtiketdia("Aportante", importe, nombre_a.getText(), documento_a.getText(), 0, 0);
                        }
                    }
                    familiares.setText("0");
                }

            } else {

                setearnullaportante();
                javax.swing.JOptionPane.showMessageDialog(this, "Debe de ingresar una fecha de ingreso y egreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.lang.NullPointerException e) {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe completar todos los campos de ingreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }
    }

    public void BuscarAportante() throws SQLException {
        LocalTime HoraActual = LocalTime.now();
        ResultSet res;
        res = controlador.BuscarAportante(Documento_a.getText());
        if (res.next() == true) {
            nombre_a.setText(res.getString("nombre"));
            documento_a.setText(res.getString("doc"));
            cod_aportante.setText(res.getString("cod_aportante"));
            apellido_a.setText(res.getString("apellido"));

        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontro el documento solicitado.\n Intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }
    }

    public void TablaParselas() throws SQLException {
        LocalTime HoraActual = LocalTime.now();
        int f = 0;
        ResultSet res;
        res = controlador.MostraParselas();
        //   System.out.println(res.getString("documento"));
        while (res.next()) {
            f++;
            String documento = res.getString("documento");
            String parsela = res.getString("parsela");
            String quincho = String.valueOf(res.getInt("quincho"));
            String fecha_egreso = res.getString("fecha_egreso");
            String tab[] = {documento, parsela, quincho, fecha_egreso};

        }

    }

    public void RegistrarIngreso() throws java.text.ParseException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        float importe = 0;
        int c;
        if (Integer.parseInt(Parcela.getText()) >= 1 && Integer.parseInt(Parcela.getText()) <= 4) {
            importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso, fecha_egreso), 6);
            tarifa.setText(String.valueOf(importe));
        } else {
            importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso, fecha_egreso), 1);
            tarifa.setText(String.valueOf(importe));
        }

        if (fecha_egreso.getDate() != null && fecha_ingreso.getDate() != null) {
            System.out.println("importe::" + importe);
            c = controlador.IngresoParticular(documento_e.getText(), nombre_e.getText(), "Alumno", calc_fecha(fecha_ingreso), calc_fecha(fecha_egreso), Parcela.getText(), importe, Integer.parseInt(acomp_particular.getText()),HoraActual.format(f));

            if (c != 1) {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                String hora_actual = hora + ":" + minutos;
                modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo alumno acampante", Main.DiaActual, HoraActual.format(f), 0);
                modelo.insertardinerocaja(importe);

                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia.isSelected()) {

                        imprimirtiketacampante(calc_fecha(fecha_ingreso), calc_fecha(fecha_egreso), "Alumno", importe, nombre_e.getText(), documento_e.getText(), 0, Main.tarfia_acampar_alumnos);
                    } else {

                        importe = Main.tarifa_dia_alumnos;
                        imprimirtiketdia("Alumno", importe, nombre_e.getText(), documento_e.getText(), 0, importe);

                    }
                }
                setearnullalumno();
            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        menu_ingreso_egreso = new javax.swing.JTabbedPane();
        aportantes = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        Documento_a = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Buscar_a = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        documento_a = new javax.swing.JTextField();
        cod_aportante = new javax.swing.JTextField();
        apellido_a = new javax.swing.JTextField();
        nombre_a = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        fecha_ingreso_p1 = new com.toedter.calendar.JDateChooser();
        fecha_egreso_p1 = new com.toedter.calendar.JDateChooser();
        Obtener2 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        tarifa2 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        Boton_ingreso_a = new javax.swing.JLabel();
        Parsela_a = new javax.swing.JTextField();
        pasar_dia1 = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        familiares = new javax.swing.JTextField();
        alumnoss = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fecha_ingreso = new com.toedter.calendar.JDateChooser();
        fecha_egreso = new com.toedter.calendar.JDateChooser();
        Obtener = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        tarifa = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        Boton_ingreso = new javax.swing.JLabel();
        Parcela = new javax.swing.JTextField();
        pasar_dia = new javax.swing.JCheckBox();
        jLabel36 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        documento_e = new javax.swing.JTextField();
        apellido_e = new javax.swing.JTextField();
        nombre_e = new javax.swing.JTextField();
        carrera_e = new javax.swing.JTextField();
        facultad_e = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Documento = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        Buscar = new javax.swing.JLabel();
        particular = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        documento_p = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        nombre_p = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        fecha_ingreso_p = new com.toedter.calendar.JDateChooser();
        fecha_egreso_p = new com.toedter.calendar.JDateChooser();
        Obtener1 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        tarifa1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        Boton_ingreso_p = new javax.swing.JLabel();
        Parsela_p = new javax.swing.JTextField();
        pasar_dia2 = new javax.swing.JCheckBox();
        jLabel37 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        acomp_particular = new javax.swing.JTextField();
        invitados = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        fecha_ingreso_p2 = new com.toedter.calendar.JDateChooser();
        fecha_egreso_p2 = new com.toedter.calendar.JDateChooser();
        Obtener3 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        tarifa3 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        Boton_ingreso_p1 = new javax.swing.JLabel();
        Parsela_p1 = new javax.swing.JTextField();
        pasar_dia3 = new javax.swing.JCheckBox();
        jLabel44 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        Documento_a2 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        Buscar_a2 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        documento_a1 = new javax.swing.JTextField();
        cod_aportante1 = new javax.swing.JTextField();
        apellido_a1 = new javax.swing.JTextField();
        nombre_a1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        nombre_p1 = new javax.swing.JTextField();
        documento_p1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        acomp_invitados = new javax.swing.JTextField();
        egresos = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        dni_buscado = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_egreso = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        vahiculos = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_vehiculo = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        fecha_ingreso_p3 = new com.toedter.calendar.JDateChooser();
        fecha_egreso_p3 = new com.toedter.calendar.JDateChooser();
        jPanel30 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        tarifa5 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        Obtener5 = new javax.swing.JButton();
        pasar_dia5 = new javax.swing.JCheckBox();
        jLabel68 = new javax.swing.JLabel();
        tarifa_casilla = new javax.swing.JLabel();
        Boton_ingreso_p3 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        patente_c = new javax.swing.JTextField();
        otros = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        documento_otros = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        nombre_otros = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        fecha_ingreso_otros = new com.toedter.calendar.JDateChooser();
        fecha_egreso_otros = new com.toedter.calendar.JDateChooser();
        Obtener4 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        tarifa4 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        Boton_ingreso_p2 = new javax.swing.JLabel();
        Parcela_otros = new javax.swing.JTextField();
        pasar_dia4 = new javax.swing.JCheckBox();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        descripcion_otros = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        acomp_otros = new javax.swing.JTextField();
        tarifa_otros = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        j10 = new javax.swing.JLabel();
        j12 = new javax.swing.JLabel();
        j5 = new javax.swing.JLabel();
        j27 = new javax.swing.JLabel();
        j7 = new javax.swing.JLabel();
        j29 = new javax.swing.JLabel();
        j20 = new javax.swing.JLabel();
        j3 = new javax.swing.JLabel();
        j31 = new javax.swing.JLabel();
        j40 = new javax.swing.JLabel();
        j49 = new javax.swing.JLabel();
        j45 = new javax.swing.JLabel();
        j50 = new javax.swing.JLabel();
        j6 = new javax.swing.JLabel();
        j15 = new javax.swing.JLabel();
        j2 = new javax.swing.JLabel();
        j25 = new javax.swing.JLabel();
        j33 = new javax.swing.JLabel();
        j37 = new javax.swing.JLabel();
        j36 = new javax.swing.JLabel();
        j48 = new javax.swing.JLabel();
        j39 = new javax.swing.JLabel();
        j38 = new javax.swing.JLabel();
        j34 = new javax.swing.JLabel();
        j43 = new javax.swing.JLabel();
        j30 = new javax.swing.JLabel();
        j42 = new javax.swing.JLabel();
        j24 = new javax.swing.JLabel();
        j23 = new javax.swing.JLabel();
        j21 = new javax.swing.JLabel();
        j19 = new javax.swing.JLabel();
        j18 = new javax.swing.JLabel();
        j4 = new javax.swing.JLabel();
        j16 = new javax.swing.JLabel();
        j17 = new javax.swing.JLabel();
        j14 = new javax.swing.JLabel();
        j13 = new javax.swing.JLabel();
        j8 = new javax.swing.JLabel();
        j9 = new javax.swing.JLabel();
        j11 = new javax.swing.JLabel();
        j46 = new javax.swing.JLabel();
        j35 = new javax.swing.JLabel();
        j41 = new javax.swing.JLabel();
        j32 = new javax.swing.JLabel();
        j28 = new javax.swing.JLabel();
        j26 = new javax.swing.JLabel();
        j1 = new javax.swing.JLabel();
        j22 = new javax.swing.JLabel();
        j44 = new javax.swing.JLabel();
        j47 = new javax.swing.JLabel();
        j51 = new javax.swing.JLabel();
        j52 = new javax.swing.JLabel();
        j53 = new javax.swing.JLabel();
        j54 = new javax.swing.JLabel();
        j55 = new javax.swing.JLabel();
        j56 = new javax.swing.JLabel();
        j57 = new javax.swing.JLabel();
        j58 = new javax.swing.JLabel();
        j59 = new javax.swing.JLabel();
        j60 = new javax.swing.JLabel();
        j61 = new javax.swing.JLabel();
        j62 = new javax.swing.JLabel();
        j63 = new javax.swing.JLabel();
        j64 = new javax.swing.JLabel();
        j65 = new javax.swing.JLabel();
        j66 = new javax.swing.JLabel();
        j67 = new javax.swing.JLabel();
        j68 = new javax.swing.JLabel();
        j69 = new javax.swing.JLabel();
        j70 = new javax.swing.JLabel();
        j71 = new javax.swing.JLabel();
        j72 = new javax.swing.JLabel();
        j73 = new javax.swing.JLabel();
        j74 = new javax.swing.JLabel();
        j75 = new javax.swing.JLabel();
        j76 = new javax.swing.JLabel();
        j77 = new javax.swing.JLabel();
        j78 = new javax.swing.JLabel();
        j79 = new javax.swing.JLabel();
        j80 = new javax.swing.JLabel();
        j81 = new javax.swing.JLabel();
        j82 = new javax.swing.JLabel();
        j83 = new javax.swing.JLabel();
        j84 = new javax.swing.JLabel();
        j85 = new javax.swing.JLabel();
        j86 = new javax.swing.JLabel();
        j87 = new javax.swing.JLabel();
        j88 = new javax.swing.JLabel();
        j89 = new javax.swing.JLabel();
        j90 = new javax.swing.JLabel();
        j91 = new javax.swing.JLabel();
        j92 = new javax.swing.JLabel();
        j93 = new javax.swing.JLabel();
        j94 = new javax.swing.JLabel();
        j95 = new javax.swing.JLabel();
        j96 = new javax.swing.JLabel();
        j97 = new javax.swing.JLabel();
        j98 = new javax.swing.JLabel();
        j99 = new javax.swing.JLabel();
        j100 = new javax.swing.JLabel();
        j101 = new javax.swing.JLabel();
        j102 = new javax.swing.JLabel();
        j103 = new javax.swing.JLabel();
        j104 = new javax.swing.JLabel();
        j105 = new javax.swing.JLabel();
        j106 = new javax.swing.JLabel();
        j107 = new javax.swing.JLabel();
        j108 = new javax.swing.JLabel();
        j109 = new javax.swing.JLabel();
        j110 = new javax.swing.JLabel();
        j111 = new javax.swing.JLabel();
        j112 = new javax.swing.JLabel();
        j113 = new javax.swing.JLabel();
        j114 = new javax.swing.JLabel();
        j115 = new javax.swing.JLabel();
        j116 = new javax.swing.JLabel();
        j117 = new javax.swing.JLabel();
        j118 = new javax.swing.JLabel();
        j119 = new javax.swing.JLabel();
        j120 = new javax.swing.JLabel();
        j121 = new javax.swing.JLabel();
        j122 = new javax.swing.JLabel();
        j123 = new javax.swing.JLabel();
        j124 = new javax.swing.JLabel();
        j125 = new javax.swing.JLabel();
        j126 = new javax.swing.JLabel();
        j127 = new javax.swing.JLabel();
        j128 = new javax.swing.JLabel();
        boton_v = new javax.swing.JLabel();
        boton_r = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        j129 = new javax.swing.JLabel();
        j130 = new javax.swing.JLabel();
        j131 = new javax.swing.JLabel();
        j132 = new javax.swing.JLabel();
        j133 = new javax.swing.JLabel();
        j134 = new javax.swing.JLabel();
        j135 = new javax.swing.JLabel();
        j136 = new javax.swing.JLabel();
        j137 = new javax.swing.JLabel();
        j138 = new javax.swing.JLabel();
        j139 = new javax.swing.JLabel();
        j140 = new javax.swing.JLabel();
        j141 = new javax.swing.JLabel();
        j142 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        j143 = new javax.swing.JLabel();
        j144 = new javax.swing.JLabel();
        j145 = new javax.swing.JLabel();
        j146 = new javax.swing.JLabel();
        j147 = new javax.swing.JLabel();
        j148 = new javax.swing.JLabel();
        j149 = new javax.swing.JLabel();
        j150 = new javax.swing.JLabel();
        j151 = new javax.swing.JLabel();
        j152 = new javax.swing.JLabel();
        j153 = new javax.swing.JLabel();
        j154 = new javax.swing.JLabel();
        j155 = new javax.swing.JLabel();
        j156 = new javax.swing.JLabel();
        j157 = new javax.swing.JLabel();
        j158 = new javax.swing.JLabel();
        j159 = new javax.swing.JLabel();
        j160 = new javax.swing.JLabel();
        j161 = new javax.swing.JLabel();
        j162 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        j163 = new javax.swing.JLabel();
        j164 = new javax.swing.JLabel();
        j165 = new javax.swing.JLabel();
        j166 = new javax.swing.JLabel();
        j167 = new javax.swing.JLabel();
        j168 = new javax.swing.JLabel();
        j169 = new javax.swing.JLabel();
        j170 = new javax.swing.JLabel();
        j171 = new javax.swing.JLabel();
        j172 = new javax.swing.JLabel();
        j173 = new javax.swing.JLabel();
        j174 = new javax.swing.JLabel();
        j175 = new javax.swing.JLabel();
        j176 = new javax.swing.JLabel();
        j177 = new javax.swing.JLabel();
        j178 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        j179 = new javax.swing.JLabel();
        j180 = new javax.swing.JLabel();
        j181 = new javax.swing.JLabel();
        j182 = new javax.swing.JLabel();
        j183 = new javax.swing.JLabel();
        j184 = new javax.swing.JLabel();
        j185 = new javax.swing.JLabel();
        j186 = new javax.swing.JLabel();
        j187 = new javax.swing.JLabel();
        j188 = new javax.swing.JLabel();
        j189 = new javax.swing.JLabel();
        j190 = new javax.swing.JLabel();
        j191 = new javax.swing.JLabel();
        j192 = new javax.swing.JLabel();
        j193 = new javax.swing.JLabel();
        j194 = new javax.swing.JLabel();
        j195 = new javax.swing.JLabel();
        j196 = new javax.swing.JLabel();
        j197 = new javax.swing.JLabel();
        j198 = new javax.swing.JLabel();
        j199 = new javax.swing.JLabel();
        j200 = new javax.swing.JLabel();
        j201 = new javax.swing.JLabel();
        j202 = new javax.swing.JLabel();
        j203 = new javax.swing.JLabel();
        j204 = new javax.swing.JLabel();
        j205 = new javax.swing.JLabel();
        j206 = new javax.swing.JLabel();
        j207 = new javax.swing.JLabel();
        j208 = new javax.swing.JLabel();
        j209 = new javax.swing.JLabel();
        j210 = new javax.swing.JLabel();
        j211 = new javax.swing.JLabel();
        j212 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        menu_ingreso_egreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu_ingreso_egresoMousePressed(evt);
            }
        });
        menu_ingreso_egreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                menu_ingreso_egresoKeyPressed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso Aportante"));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Busqueda de Aportante"));

        Documento_a.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Documento_a.setText(null);
        Documento_a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Documento_aActionPerformed(evt);
            }
        });
        Documento_a.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Documento_aKeyPressed(evt);
            }
        });

        jLabel3.setText("DOCUMENTO DE APORTANTE");

        Buscar_a.setBackground(new java.awt.Color(255, 255, 255));
        Buscar_a.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_search_black_24dp.png"))); // NOI18N
        Buscar_a.setText("BUSCAR");
        Buscar_a.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Buscar_a.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Buscar_a.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Buscar_aMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Buscar_a, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Documento_a, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Documento_a, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Buscar_a)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion del Aportante"));

        jLabel4.setText("Nombre:");

        jLabel15.setText("Apellido:");

        jLabel16.setText("Cod. aportante:");

        jLabel20.setText("Documento:");

        documento_a.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_a.setText(null);
        documento_a.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                documento_aKeyPressed(evt);
            }
        });

        cod_aportante.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cod_aportante.setText(null);
        cod_aportante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cod_aportanteKeyPressed(evt);
            }
        });

        apellido_a.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        apellido_a.setText(null);

        nombre_a.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_a.setText(null);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel20))
                .addGap(90, 90, 90)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(documento_a)
                    .addComponent(cod_aportante)
                    .addComponent(apellido_a)
                    .addComponent(nombre_a, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(nombre_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellido_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cod_aportante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documento_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso"));

        jLabel27.setText("FECHA DE EGRESO");

        jLabel28.setText("FECHA DE INGRESO");

        fecha_ingreso_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingreso_p1KeyPressed(evt);
            }
        });

        fecha_egreso_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egreso_p1KeyPressed(evt);
            }
        });

        Obtener2.setText("Obtener Total");
        Obtener2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Obtener2MousePressed(evt);
            }
        });

        jPanel14.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel14ComponentHidden(evt);
            }
        });

        jLabel29.setText("Total");

        tarifa2.setEditable(false);
        tarifa2.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifa2ActionPerformed(evt);
            }
        });

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Boton_ingreso_a.setText(" REGISTRAR INGRESO ");
        Boton_ingreso_a.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso_a.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso_a.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingreso_aMousePressed(evt);
            }
        });

        Parsela_a.setText("Parcela");
        Parsela_a.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Parsela_aMousePressed(evt);
            }
        });
        Parsela_a.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Parsela_aActionPerformed(evt);
            }
        });
        Parsela_a.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Parsela_aKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Parsela_a)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso_a))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(tarifa2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Parsela_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addGap(2, 2, 2)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(tarifa2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Boton_ingreso_a, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fecha_ingreso_p1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                        .addComponent(fecha_egreso_p1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Obtener2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso_p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_egreso_p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Obtener2)))
                .addContainerGap())
        );

        pasar_dia1.setText("Pasar el dia");
        pasar_dia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_dia1ActionPerformed(evt);
            }
        });
        pasar_dia1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_dia1KeyPressed(evt);
            }
        });

        jLabel17.setText("Selecione si solo pasa el dia:");

        jLabel9.setText("Especifique los familiares:");

        familiares.setText("0");
        familiares.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                familiaresMousePressed(evt);
            }
        });
        familiares.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                familiaresKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)))
                .addGap(12, 12, 12))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pasar_dia1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(familiares)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(pasar_dia1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(familiares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout aportantesLayout = new javax.swing.GroupLayout(aportantes);
        aportantes.setLayout(aportantesLayout);
        aportantesLayout.setHorizontalGroup(
            aportantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aportantesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        aportantesLayout.setVerticalGroup(
            aportantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aportantesLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 104, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Aportantes", aportantes);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso Alumno"));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso"));

        jLabel1.setText("FECHA DE EGRESO");

        jLabel2.setText("FECHA DE INGRESO");

        fecha_ingreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingresoKeyPressed(evt);
            }
        });

        fecha_egreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egresoKeyPressed(evt);
            }
        });

        Obtener.setText("Obtener Total");
        Obtener.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ObtenerMousePressed(evt);
            }
        });

        jPanel8.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel8ComponentHidden(evt);
            }
        });

        jLabel11.setText("Total");

        tarifa.setEditable(false);
        tarifa.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifaActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Boton_ingreso.setText(" REGISTRAR INGRESO ");
        Boton_ingreso.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingresoMousePressed(evt);
            }
        });

        Parcela.setText("Parcela");
        Parcela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ParcelaMousePressed(evt);
            }
        });
        Parcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ParcelaActionPerformed(evt);
            }
        });
        Parcela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ParcelaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Parcela)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(tarifa, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Parcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(tarifa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Boton_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fecha_ingreso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                        .addComponent(fecha_egreso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Obtener, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_egreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Obtener)
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );

        pasar_dia.setText("Pasar el dia");
        pasar_dia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_diaActionPerformed(evt);
            }
        });
        pasar_dia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_diaKeyPressed(evt);
            }
        });

        jLabel36.setText("Selecione si solo pasa el dia:");

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion de Alumno"));

        jLabel7.setText("DOCUMENTO:");

        documento_e.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_e.setText(null);
        documento_e.setEnabled(false);
        documento_e.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_eActionPerformed(evt);
            }
        });

        apellido_e.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        apellido_e.setText(null);
        apellido_e.setEnabled(false);

        nombre_e.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_e.setText(null);
        nombre_e.setEnabled(false);

        carrera_e.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        carrera_e.setText(null);
        carrera_e.setEnabled(false);
        carrera_e.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carrera_eActionPerformed(evt);
            }
        });

        facultad_e.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        facultad_e.setText(null);
        facultad_e.setEnabled(false);
        facultad_e.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facultad_eActionPerformed(evt);
            }
        });

        jLabel13.setText("FACULTAD:");

        jLabel12.setText("CARRERA:");

        jLabel6.setText("NOMBRE:");

        jLabel8.setText("APELLIDO:");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(facultad_e, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(carrera_e, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre_e, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(apellido_e, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(documento_e, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documento_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellido_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carrera_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(facultad_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Busqueda por Documento"));

        Documento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Documento.setText(null);
        Documento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DocumentoActionPerformed(evt);
            }
        });
        Documento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DocumentoKeyPressed(evt);
            }
        });

        jLabel10.setText("Ingrese un Documento");

        Buscar.setBackground(new java.awt.Color(255, 255, 255));
        Buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_search_black_24dp.png"))); // NOI18N
        Buscar.setText("BUSCAR");
        Buscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Buscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BuscarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Documento, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Documento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(pasar_dia, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(48, 48, 48))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(pasar_dia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout alumnossLayout = new javax.swing.GroupLayout(alumnoss);
        alumnoss.setLayout(alumnossLayout);
        alumnossLayout.setHorizontalGroup(
            alumnossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnossLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        alumnossLayout.setVerticalGroup(
            alumnossLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnossLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Alumnos", alumnoss);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar un Particular"));

        documento_p.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_p.setText(null);
        documento_e.setEnabled(false);
        documento_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_pActionPerformed(evt);
            }
        });
        documento_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                documento_pKeyPressed(evt);
            }
        });

        jLabel18.setText("NOMBRE");

        nombre_p.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_p.setText(null);
        nombre_e.setEnabled(false);
        nombre_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombre_pKeyPressed(evt);
            }
        });

        jLabel19.setText("DOCUMENTO");

        jLabel23.setText("FECHA DE EGRESO");

        jLabel24.setText("FECHA DE INGRESO");

        fecha_ingreso_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingreso_pKeyPressed(evt);
            }
        });

        fecha_egreso_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egreso_pKeyPressed(evt);
            }
        });

        Obtener1.setText("Obtener Total");
        Obtener1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Obtener1MousePressed(evt);
            }
        });

        jPanel12.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel12ComponentHidden(evt);
            }
        });

        jLabel25.setText("Total");

        tarifa1.setEditable(false);
        tarifa1.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifa1ActionPerformed(evt);
            }
        });

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Boton_ingreso_p.setText(" REGISTRAR INGRESO ");
        Boton_ingreso_p.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso_p.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso_p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingreso_pMousePressed(evt);
            }
        });
        Boton_ingreso_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Boton_ingreso_pKeyPressed(evt);
            }
        });

        Parsela_p.setText("Parcela");
        Parsela_p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Parsela_pMousePressed(evt);
            }
        });
        Parsela_p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Parsela_pActionPerformed(evt);
            }
        });
        Parsela_p.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Parsela_pKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Parsela_p)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso_p))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(tarifa1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Parsela_p, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addGap(2, 2, 2)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(tarifa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Boton_ingreso_p, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Obtener1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(fecha_egreso_p, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fecha_ingreso_p, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24))
                        .addGap(75, 75, 75))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso_p, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fecha_egreso_p, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Obtener1))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pasar_dia2.setText("Pasar el dia");
        pasar_dia2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pasar_dia2MousePressed(evt);
            }
        });
        pasar_dia2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_dia2ActionPerformed(evt);
            }
        });
        pasar_dia2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_dia2KeyPressed(evt);
            }
        });

        jLabel37.setText("Seleccione si solo pasa el dia:");

        jLabel46.setText("Acompañantes (MAX 98):");

        acomp_particular.setText("0");
        acomp_particular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                acomp_particularMousePressed(evt);
            }
        });
        acomp_particular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                acomp_particularKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(367, 367, 367))
                            .addComponent(nombre_p)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel46)
                                .addGap(52, 52, 52)
                                .addComponent(acomp_particular, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pasar_dia2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(65, 65, 65))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(documento_p)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documento_p, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombre_p, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acomp_particular, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pasar_dia2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addGap(43, 43, 43)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout particularLayout = new javax.swing.GroupLayout(particular);
        particular.setLayout(particularLayout);
        particularLayout.setHorizontalGroup(
            particularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, particularLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        particularLayout.setVerticalGroup(
            particularLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(particularLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Particular", particular);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresar un Invitado"));

        jLabel40.setText("FECHA DE EGRESO");

        jLabel41.setText("FECHA DE INGRESO");

        fecha_ingreso_p2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingreso_p2KeyPressed(evt);
            }
        });

        fecha_egreso_p2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egreso_p2KeyPressed(evt);
            }
        });

        Obtener3.setText("Obtener Total");
        Obtener3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Obtener3MousePressed(evt);
            }
        });

        jPanel23.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel23ComponentHidden(evt);
            }
        });

        jLabel42.setText("Total");

        tarifa3.setEditable(false);
        tarifa3.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifa3ActionPerformed(evt);
            }
        });

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Boton_ingreso_p1.setText(" REGISTRAR INGRESO ");
        Boton_ingreso_p1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso_p1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso_p1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingreso_p1MousePressed(evt);
            }
        });
        Boton_ingreso_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Boton_ingreso_p1KeyPressed(evt);
            }
        });

        Parsela_p1.setText("Parcela");
        Parsela_p1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Parsela_p1MousePressed(evt);
            }
        });
        Parsela_p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Parsela_p1ActionPerformed(evt);
            }
        });
        Parsela_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Parsela_p1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Parsela_p1)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso_p1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(tarifa3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Parsela_p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42)
                .addGap(2, 2, 2)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(tarifa3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Boton_ingreso_p1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pasar_dia3.setText("Pasar el dia");
        pasar_dia3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pasar_dia3MousePressed(evt);
            }
        });
        pasar_dia3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_dia3ActionPerformed(evt);
            }
        });
        pasar_dia3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_dia3KeyPressed(evt);
            }
        });

        jLabel44.setText("Seleccione si solo pasa el dia:");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel40)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fecha_egreso_p2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fecha_ingreso_p2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(Obtener3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)))
                .addGap(35, 35, 35))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pasar_dia3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pasar_dia3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso_p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(fecha_egreso_p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Obtener3)
                        .addContainerGap())
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Busqueda de Aportante"));

        Documento_a2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Documento_a2.setText(null);
        Documento_a2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Documento_a2ActionPerformed(evt);
            }
        });
        Documento_a2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Documento_a2KeyPressed(evt);
            }
        });

        jLabel58.setText("INGRESE EL DNI DEL APORTANTE ");

        Buscar_a2.setBackground(new java.awt.Color(255, 255, 255));
        Buscar_a2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_search_black_24dp.png"))); // NOI18N
        Buscar_a2.setText("BUSCAR");
        Buscar_a2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Buscar_a2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Buscar_a2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Buscar_a2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel58)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Buscar_a2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Documento_a2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Documento_a2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(Buscar_a2))
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion del Aportante"));

        jLabel59.setText("Nombre:");

        jLabel60.setText("Apellido:");

        jLabel61.setText("Cod. aportante:");

        jLabel62.setText("Documento:");

        documento_a1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_a1.setText(null);
        documento_a1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                documento_a1KeyPressed(evt);
            }
        });

        cod_aportante1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cod_aportante1.setText(null);
        cod_aportante1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cod_aportante1KeyPressed(evt);
            }
        });

        apellido_a1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        apellido_a1.setText(null);

        nombre_a1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_a1.setText(null);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60)
                    .addComponent(jLabel61)
                    .addComponent(jLabel62))
                .addGap(90, 90, 90)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(documento_a1)
                    .addComponent(cod_aportante1)
                    .addComponent(apellido_a1)
                    .addComponent(nombre_a1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(nombre_a1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apellido_a1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cod_aportante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documento_a1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del Invitado"));

        jLabel38.setText("NOMBRE");

        nombre_p1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_p1.setText(null);
        nombre_e.setEnabled(false);
        nombre_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombre_p1KeyPressed(evt);
            }
        });

        documento_p1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_p1.setText(null);
        documento_e.setEnabled(false);
        documento_p1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_p1ActionPerformed(evt);
            }
        });
        documento_p1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                documento_p1KeyPressed(evt);
            }
        });

        jLabel39.setText("DOCUMENTO");

        jLabel57.setText("Acompañantes MAX(4)");

        acomp_invitados.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        acomp_invitados.setText("0");
        documento_e.setEnabled(false);
        acomp_invitados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                acomp_invitadosMousePressed(evt);
            }
        });
        acomp_invitados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acomp_invitadosActionPerformed(evt);
            }
        });
        acomp_invitados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                acomp_invitadosKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(25, 25, 25)
                        .addComponent(acomp_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(documento_p1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre_p1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre_p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(documento_p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(acomp_invitados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout invitadosLayout = new javax.swing.GroupLayout(invitados);
        invitados.setLayout(invitadosLayout);
        invitadosLayout.setHorizontalGroup(
            invitadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invitadosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        invitadosLayout.setVerticalGroup(
            invitadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invitadosLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Invitados", invitados);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Acampante"));

        jLabel31.setText("Ingrese el documento");

        dni_buscado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dni_buscadoActionPerformed(evt);
            }
        });
        dni_buscado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dni_buscadoKeyPressed(evt);
            }
        });

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_search_black_24dp.png"))); // NOI18N
        jLabel32.setText("BUSCAR");
        jLabel32.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel32MousePressed(evt);
            }
        });
        jLabel32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel32KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dni_buscado, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dni_buscado, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Registrar un Egreso Anticipado de Acampante"));

        tabla_egreso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documento", "Nombre", "Categoria", "Importe", "Fecha de egreso", "Fecha de ingreso", "Parcela"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_egreso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_egresoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_egreso);

        jLabel34.setText("ELIMINAR");
        jLabel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel34.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel34MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel34MouseReleased(evt);
            }
        });

        jLabel33.setText("ACTUALIZAR ");
        jLabel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel33.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel33MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel33))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 69, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout egresosLayout = new javax.swing.GroupLayout(egresos);
        egresos.setLayout(egresosLayout);
        egresosLayout.setHorizontalGroup(
            egresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, egresosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(589, 589, 589))
            .addGroup(egresosLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        egresosLayout.setVerticalGroup(
            egresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(egresosLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Egresos", egresos);

        tabla_vehiculo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Patente", "Fecha de ingreso", "Fecha de egreso"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_vehiculoKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tabla_vehiculo);

        jLabel35.setText("Vehiculos registrados en el camping");

        jLabel5.setText("Eliminar");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso de Casillas/Motorhome"));

        jLabel64.setText("FECHA DE EGRESO");

        jLabel65.setText("FECHA DE INGRESO");

        fecha_ingreso_p3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingreso_p3KeyPressed(evt);
            }
        });

        fecha_egreso_p3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egreso_p3KeyPressed(evt);
            }
        });

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder("Tarifa total"));
        jPanel30.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel30ComponentHidden(evt);
            }
        });

        jLabel66.setText("Total");

        tarifa5.setEditable(false);
        tarifa5.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifa5ActionPerformed(evt);
            }
        });

        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Obtener5.setText("Obtener Total");
        Obtener5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Obtener5MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Obtener5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tarifa5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel66)
                .addGap(42, 42, 42))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel67)
                    .addComponent(tarifa5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Obtener5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pasar_dia5.setText("Pasar el dia");
        pasar_dia5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pasar_dia5MousePressed(evt);
            }
        });
        pasar_dia5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_dia5ActionPerformed(evt);
            }
        });
        pasar_dia5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_dia5KeyPressed(evt);
            }
        });

        jLabel68.setText("Seleccione si solo pasa el dia:");

        tarifa_casilla.setText("Tarifa por dia:");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pasar_dia5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addComponent(jLabel64)
                                        .addGap(82, 82, 82))
                                    .addGroup(jPanel27Layout.createSequentialGroup()
                                        .addComponent(fecha_egreso_p3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(53, 53, 53))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel65)
                                    .addComponent(tarifa_casilla)
                                    .addComponent(fecha_ingreso_p3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(53, 53, 53))))))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pasar_dia5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(tarifa_casilla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso_p3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_egreso_p3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Boton_ingreso_p3.setText(" REGISTRAR INGRESO ");
        Boton_ingreso_p3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso_p3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso_p3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingreso_p3MousePressed(evt);
            }
        });
        Boton_ingreso_p3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Boton_ingreso_p3KeyPressed(evt);
            }
        });

        jLabel69.setText("PATENTE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(patente_c, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel69))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso_p3)
                        .addGap(47, 47, 47)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(patente_c, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(Boton_ingreso_p3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout vahiculosLayout = new javax.swing.GroupLayout(vahiculos);
        vahiculos.setLayout(vahiculosLayout);
        vahiculosLayout.setHorizontalGroup(
            vahiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vahiculosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(vahiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(vahiculosLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(345, 345, 345))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(vahiculosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        vahiculosLayout.setVerticalGroup(
            vahiculosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vahiculosLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu_ingreso_egreso.addTab("Vehiculos", vahiculos);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso de caracter caso especial"));

        documento_otros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documento_otros.setText(null);
        documento_e.setEnabled(false);
        documento_otros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_otrosActionPerformed(evt);
            }
        });
        documento_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                documento_otrosKeyPressed(evt);
            }
        });

        jLabel47.setText("NOMBRE");

        nombre_otros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre_otros.setText(null);
        nombre_e.setEnabled(false);
        nombre_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombre_otrosKeyPressed(evt);
            }
        });

        jLabel48.setText("DOCUMENTO");

        jLabel49.setText("FECHA DE EGRESO");

        jLabel50.setText("FECHA DE INGRESO");

        fecha_ingreso_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_ingreso_otrosKeyPressed(evt);
            }
        });

        fecha_egreso_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fecha_egreso_otrosKeyPressed(evt);
            }
        });

        Obtener4.setText("Obtener Total");
        Obtener4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Obtener4MousePressed(evt);
            }
        });

        jPanel26.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel26ComponentHidden(evt);
            }
        });

        jLabel51.setText("Total");

        tarifa4.setEditable(false);
        tarifa4.setText("Tarifa");
        tarifa.setEnabled(false);
        tarifa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tarifa4ActionPerformed(evt);
            }
        });

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/baseline_attach_money_black_24dp.png"))); // NOI18N

        Boton_ingreso_p2.setText(" REGISTRAR INGRESO ");
        Boton_ingreso_p2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Boton_ingreso_p2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_ingreso_p2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Boton_ingreso_p2MousePressed(evt);
            }
        });
        Boton_ingreso_p2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Boton_ingreso_p2KeyPressed(evt);
            }
        });

        Parcela_otros.setText("Parcela");
        Parcela_otros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Parcela_otrosMousePressed(evt);
            }
        });
        Parcela_otros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Parcela_otrosActionPerformed(evt);
            }
        });
        Parcela_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Parcela_otrosKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Parcela_otros)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_ingreso_p2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(tarifa4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Parcela_otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51)
                .addGap(2, 2, 2)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(tarifa4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Boton_ingreso_p2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fecha_egreso_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fecha_ingreso_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel50)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(Obtener4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_ingreso_otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_egreso_otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Obtener4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pasar_dia4.setText("Pasar el dia");
        pasar_dia4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pasar_dia4MousePressed(evt);
            }
        });
        pasar_dia4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasar_dia4ActionPerformed(evt);
            }
        });
        pasar_dia4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pasar_dia4KeyPressed(evt);
            }
        });

        jLabel53.setText("Seleccione si solo pasa el dia:");

        jLabel54.setText("DESCRIPCIÓN");

        descripcion_otros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descripcion_otrosActionPerformed(evt);
            }
        });

        jLabel55.setText("Acompañantes (MAX 98):");

        acomp_otros.setText("0");
        acomp_otros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                acomp_otrosMousePressed(evt);
            }
        });
        acomp_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                acomp_otrosKeyPressed(evt);
            }
        });

        tarifa_otros.setText("0.0");
        tarifa_otros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tarifa_otrosMousePressed(evt);
            }
        });
        tarifa_otros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tarifa_otrosKeyPressed(evt);
            }
        });

        jLabel56.setText("Tarifa individual por persona:");

        jLabel63.setText("Parcela:");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel24Layout.createSequentialGroup()
                                        .addComponent(jLabel55)
                                        .addGap(52, 52, 52))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                                        .addComponent(jLabel56)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tarifa_otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(acomp_otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel47))
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel54))
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel63)
                                    .addGroup(jPanel24Layout.createSequentialGroup()
                                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76)
                                        .addComponent(pasar_dia4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombre_otros, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descripcion_otros, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(documento_otros, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombre_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documento_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcion_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tarifa_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addGap(28, 28, 28)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acomp_otros, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pasar_dia4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout otrosLayout = new javax.swing.GroupLayout(otros);
        otros.setLayout(otrosLayout);
        otrosLayout.setHorizontalGroup(
            otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otrosLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        otrosLayout.setVerticalGroup(
            otrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otrosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        jPanel24.getAccessibleContext().setAccessibleName("");

        menu_ingreso_egreso.addTab("Otros", otros);

        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        j10.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j10.setForeground(new java.awt.Color(255, 255, 255));
        j10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j10.setText("10");
        j10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j10MousePressed(evt);
            }
        });
        jPanel3.add(j10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, -1, -1));

        j12.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j12.setForeground(new java.awt.Color(255, 255, 255));
        j12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j12.setText("12");
        j12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j12MousePressed(evt);
            }
        });
        jPanel3.add(j12, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 320, -1, -1));

        j5.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j5.setForeground(new java.awt.Color(255, 255, 255));
        j5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j5.setText("5");
        j5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j5MousePressed(evt);
            }
        });
        jPanel3.add(j5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, -1, -1));

        j27.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j27.setForeground(new java.awt.Color(255, 255, 255));
        j27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j27.setText("27");
        j27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j27MousePressed(evt);
            }
        });
        jPanel3.add(j27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 30, -1));

        j7.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j7.setForeground(new java.awt.Color(255, 255, 255));
        j7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j7.setText("7");
        j7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j7MousePressed(evt);
            }
        });
        jPanel3.add(j7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 30, -1));

        j29.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j29.setForeground(new java.awt.Color(255, 255, 255));
        j29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j29.setText("29");
        j29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j29MousePressed(evt);
            }
        });
        jPanel3.add(j29, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 30, -1));

        j20.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j20.setForeground(new java.awt.Color(255, 255, 255));
        j20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j20.setText("20");
        j20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j20MousePressed(evt);
            }
        });
        jPanel3.add(j20, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, -1, -1));

        j3.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j3.setForeground(new java.awt.Color(255, 255, 255));
        j3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j3.setText("3");
        j3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j3MousePressed(evt);
            }
        });
        jPanel3.add(j3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, 30, -1));

        j31.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j31.setForeground(new java.awt.Color(255, 255, 255));
        j31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j31.setText("31");
        j31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j31MousePressed(evt);
            }
        });
        jPanel3.add(j31, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        j40.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j40.setForeground(new java.awt.Color(255, 255, 255));
        j40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j40.setText("40");
        j40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j40MousePressed(evt);
            }
        });
        jPanel3.add(j40, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));

        j49.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j49.setForeground(new java.awt.Color(255, 255, 255));
        j49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j49.setText("49");
        j49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j49MousePressed(evt);
            }
        });
        jPanel3.add(j49, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, -1, -1));

        j45.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j45.setForeground(new java.awt.Color(255, 255, 255));
        j45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j45.setText("45");
        j45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j45MousePressed(evt);
            }
        });
        jPanel3.add(j45, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, -1, -1));

        j50.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j50.setForeground(new java.awt.Color(255, 255, 255));
        j50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j50.setText("50");
        j50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j50MousePressed(evt);
            }
        });
        jPanel3.add(j50, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, -1, -1));

        j6.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j6.setForeground(new java.awt.Color(255, 255, 255));
        j6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j6.setText("6");
        j6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j6MousePressed(evt);
            }
        });
        jPanel3.add(j6, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, -1, -1));

        j15.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j15.setForeground(new java.awt.Color(255, 255, 255));
        j15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j15.setText("15");
        j15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j15MousePressed(evt);
            }
        });
        jPanel3.add(j15, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, -1, -1));

        j2.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j2.setForeground(new java.awt.Color(255, 255, 255));
        j2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j2.setText("2");
        j2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        j2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j2MousePressed(evt);
            }
        });
        jPanel3.add(j2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 30, 10));

        j25.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j25.setForeground(new java.awt.Color(255, 255, 255));
        j25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j25.setText("25");
        j25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j25MousePressed(evt);
            }
        });
        jPanel3.add(j25, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, -1, -1));

        j33.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j33.setForeground(new java.awt.Color(255, 255, 255));
        j33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j33.setText("33");
        j33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j33MousePressed(evt);
            }
        });
        jPanel3.add(j33, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));

        j37.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j37.setForeground(new java.awt.Color(255, 255, 255));
        j37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j37.setText("37");
        j37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j37MousePressed(evt);
            }
        });
        jPanel3.add(j37, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, -1, -1));

        j36.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j36.setForeground(new java.awt.Color(255, 255, 255));
        j36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j36.setText("36");
        j36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j36MousePressed(evt);
            }
        });
        jPanel3.add(j36, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, -1, -1));

        j48.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j48.setForeground(new java.awt.Color(255, 255, 255));
        j48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j48.setText("48");
        j48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j48MousePressed(evt);
            }
        });
        jPanel3.add(j48, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));

        j39.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j39.setForeground(new java.awt.Color(255, 255, 255));
        j39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j39.setText("39");
        j39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j39MousePressed(evt);
            }
        });
        jPanel3.add(j39, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, -1, -1));

        j38.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j38.setForeground(new java.awt.Color(255, 255, 255));
        j38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j38.setText("38");
        j38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j38MousePressed(evt);
            }
        });
        jPanel3.add(j38, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, -1, -1));

        j34.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j34.setForeground(new java.awt.Color(255, 255, 255));
        j34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j34.setText("34");
        j34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j34MousePressed(evt);
            }
        });
        jPanel3.add(j34, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, -1, -1));

        j43.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j43.setForeground(new java.awt.Color(255, 255, 255));
        j43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j43.setText("43");
        j43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j43MousePressed(evt);
            }
        });
        jPanel3.add(j43, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        j30.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j30.setForeground(new java.awt.Color(255, 255, 255));
        j30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j30.setText("30");
        j30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j30MousePressed(evt);
            }
        });
        jPanel3.add(j30, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, -1));

        j42.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j42.setForeground(new java.awt.Color(255, 255, 255));
        j42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j42.setText("42");
        j42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j42MousePressed(evt);
            }
        });
        jPanel3.add(j42, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, -1, -1));

        j24.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j24.setForeground(new java.awt.Color(255, 255, 255));
        j24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j24.setText("24");
        j24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j24MousePressed(evt);
            }
        });
        jPanel3.add(j24, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, -1, -1));

        j23.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j23.setForeground(new java.awt.Color(255, 255, 255));
        j23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j23.setText("23");
        j23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j23MousePressed(evt);
            }
        });
        jPanel3.add(j23, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, -1, -1));

        j21.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j21.setForeground(new java.awt.Color(255, 255, 255));
        j21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j21.setText("21");
        j21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j21MousePressed(evt);
            }
        });
        jPanel3.add(j21, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, -1, -1));

        j19.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j19.setForeground(new java.awt.Color(255, 255, 255));
        j19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j19.setText("19");
        j19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j19MousePressed(evt);
            }
        });
        jPanel3.add(j19, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, -1));

        j18.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j18.setForeground(new java.awt.Color(255, 255, 255));
        j18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j18.setText("18");
        j18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j18MousePressed(evt);
            }
        });
        jPanel3.add(j18, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, -1, -1));

        j4.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j4.setForeground(new java.awt.Color(255, 255, 255));
        j4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j4.setText("4");
        j4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j4MousePressed(evt);
            }
        });
        jPanel3.add(j4, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 90, 30, -1));

        j16.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j16.setForeground(new java.awt.Color(255, 255, 255));
        j16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j16.setText("16");
        j16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j16MousePressed(evt);
            }
        });
        jPanel3.add(j16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, -1, -1));

        j17.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j17.setForeground(new java.awt.Color(255, 255, 255));
        j17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j17.setText("17");
        j17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j17MousePressed(evt);
            }
        });
        jPanel3.add(j17, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, -1, -1));

        j14.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j14.setForeground(new java.awt.Color(255, 255, 255));
        j14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j14.setText("14");
        j14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j14MousePressed(evt);
            }
        });
        jPanel3.add(j14, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, -1, -1));

        j13.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j13.setForeground(new java.awt.Color(255, 255, 255));
        j13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j13.setText("13");
        j13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j13MousePressed(evt);
            }
        });
        jPanel3.add(j13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 320, -1, -1));

        j8.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j8.setForeground(new java.awt.Color(255, 255, 255));
        j8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j8.setText("8");
        j8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j8MousePressed(evt);
            }
        });
        jPanel3.add(j8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, -1, -1));

        j9.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j9.setForeground(new java.awt.Color(255, 255, 255));
        j9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j9.setText("9");
        j9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j9MousePressed(evt);
            }
        });
        jPanel3.add(j9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, -1, -1));

        j11.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j11.setForeground(new java.awt.Color(255, 255, 255));
        j11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j11.setText("11");
        j11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j11MousePressed(evt);
            }
        });
        jPanel3.add(j11, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, -1, -1));

        j46.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j46.setForeground(new java.awt.Color(255, 255, 255));
        j46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j46.setText("46");
        j46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j46MousePressed(evt);
            }
        });
        jPanel3.add(j46, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, -1, -1));

        j35.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j35.setForeground(new java.awt.Color(255, 255, 255));
        j35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j35.setText("35");
        j35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j35MousePressed(evt);
            }
        });
        jPanel3.add(j35, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, -1, -1));

        j41.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j41.setForeground(new java.awt.Color(255, 255, 255));
        j41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j41.setText("41");
        j41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j41MousePressed(evt);
            }
        });
        jPanel3.add(j41, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, -1, -1));

        j32.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j32.setForeground(new java.awt.Color(255, 255, 255));
        j32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j32.setText("32");
        j32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j32MousePressed(evt);
            }
        });
        jPanel3.add(j32, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, -1, -1));

        j28.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j28.setForeground(new java.awt.Color(255, 255, 255));
        j28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j28.setText("28");
        j28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j28MousePressed(evt);
            }
        });
        jPanel3.add(j28, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, -1, -1));

        j26.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j26.setForeground(new java.awt.Color(255, 255, 255));
        j26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j26.setText("26");
        j26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j26MousePressed(evt);
            }
        });
        jPanel3.add(j26, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 300, -1, -1));

        j1.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j1.setForeground(new java.awt.Color(255, 255, 255));
        j1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j1.setText("1");
        j1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j1MousePressed(evt);
            }
        });
        jPanel3.add(j1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 150, 30, -1));

        j22.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j22.setForeground(new java.awt.Color(255, 255, 255));
        j22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j22.setText("22");
        j22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j22MousePressed(evt);
            }
        });
        jPanel3.add(j22, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 300, -1, -1));

        j44.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j44.setForeground(new java.awt.Color(255, 255, 255));
        j44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j44.setText("44");
        j44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j44MousePressed(evt);
            }
        });
        jPanel3.add(j44, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 160, -1, -1));

        j47.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j47.setForeground(new java.awt.Color(255, 255, 255));
        j47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j47.setText("47");
        j47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j47MousePressed(evt);
            }
        });
        jPanel3.add(j47, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, -1, -1));

        j51.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j51.setForeground(new java.awt.Color(255, 255, 255));
        j51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j51.setText("51");
        j51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j51MousePressed(evt);
            }
        });
        jPanel3.add(j51, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, -1, -1));

        j52.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j52.setForeground(new java.awt.Color(255, 255, 255));
        j52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j52.setText("52");
        j52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j52MousePressed(evt);
            }
        });
        jPanel3.add(j52, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, -1, -1));

        j53.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j53.setForeground(new java.awt.Color(255, 255, 255));
        j53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j53.setText("53");
        j53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j53MousePressed(evt);
            }
        });
        jPanel3.add(j53, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, -1, -1));

        j54.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j54.setForeground(new java.awt.Color(255, 255, 255));
        j54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j54.setText("54");
        j54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j54MousePressed(evt);
            }
        });
        jPanel3.add(j54, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, -1, -1));

        j55.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j55.setForeground(new java.awt.Color(255, 255, 255));
        j55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j55.setText("55");
        j55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j55MousePressed(evt);
            }
        });
        jPanel3.add(j55, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, -1, -1));

        j56.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j56.setForeground(new java.awt.Color(255, 255, 255));
        j56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j56.setText("56");
        j56.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j56MousePressed(evt);
            }
        });
        jPanel3.add(j56, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, -1, -1));

        j57.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j57.setForeground(new java.awt.Color(255, 255, 255));
        j57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j57.setText("57");
        j57.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j57MousePressed(evt);
            }
        });
        jPanel3.add(j57, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        j58.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j58.setForeground(new java.awt.Color(255, 255, 255));
        j58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j58.setText("58");
        j58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j58MousePressed(evt);
            }
        });
        jPanel3.add(j58, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, -1, -1));

        j59.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j59.setForeground(new java.awt.Color(255, 255, 255));
        j59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j59.setText("59");
        j59.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j59MousePressed(evt);
            }
        });
        jPanel3.add(j59, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, -1, -1));

        j60.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j60.setForeground(new java.awt.Color(255, 255, 255));
        j60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j60.setText("60");
        j60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j60MousePressed(evt);
            }
        });
        jPanel3.add(j60, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, -1, -1));

        j61.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j61.setForeground(new java.awt.Color(255, 255, 255));
        j61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j61.setText("61");
        j61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j61MousePressed(evt);
            }
        });
        jPanel3.add(j61, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, -1, -1));

        j62.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j62.setForeground(new java.awt.Color(255, 255, 255));
        j62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j62.setText("62");
        j62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j62MousePressed(evt);
            }
        });
        jPanel3.add(j62, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, -1, -1));

        j63.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j63.setForeground(new java.awt.Color(255, 255, 255));
        j63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j63.setText("63");
        j63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j63MousePressed(evt);
            }
        });
        jPanel3.add(j63, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, -1, -1));

        j64.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j64.setForeground(new java.awt.Color(255, 255, 255));
        j64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j64.setText("64");
        j64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j64MousePressed(evt);
            }
        });
        jPanel3.add(j64, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, -1, -1));

        j65.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j65.setForeground(new java.awt.Color(255, 255, 255));
        j65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j65.setText("65");
        j65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j65MousePressed(evt);
            }
        });
        jPanel3.add(j65, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, -1, -1));

        j66.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j66.setForeground(new java.awt.Color(255, 255, 255));
        j66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j66.setText("66");
        j66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j66MousePressed(evt);
            }
        });
        jPanel3.add(j66, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, -1, -1));

        j67.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j67.setForeground(new java.awt.Color(255, 255, 255));
        j67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j67.setText("67");
        j67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j67MousePressed(evt);
            }
        });
        jPanel3.add(j67, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));

        j68.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j68.setForeground(new java.awt.Color(255, 255, 255));
        j68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j68.setText("68");
        j68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j68MousePressed(evt);
            }
        });
        jPanel3.add(j68, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, -1, -1));

        j69.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j69.setForeground(new java.awt.Color(255, 255, 255));
        j69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j69.setText("69");
        j69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j69MousePressed(evt);
            }
        });
        jPanel3.add(j69, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        j70.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j70.setForeground(new java.awt.Color(255, 255, 255));
        j70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j70.setText("70");
        j70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j70MousePressed(evt);
            }
        });
        jPanel3.add(j70, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, -1, -1));

        j71.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j71.setForeground(new java.awt.Color(255, 255, 255));
        j71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j71.setText("71");
        j71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j71MousePressed(evt);
            }
        });
        jPanel3.add(j71, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, -1, -1));

        j72.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j72.setForeground(new java.awt.Color(255, 255, 255));
        j72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j72.setText("72");
        j72.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j72MousePressed(evt);
            }
        });
        jPanel3.add(j72, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, -1, -1));

        j73.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j73.setForeground(new java.awt.Color(255, 255, 255));
        j73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j73.setText("73");
        j73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j73MousePressed(evt);
            }
        });
        jPanel3.add(j73, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, -1));

        j74.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j74.setForeground(new java.awt.Color(255, 255, 255));
        j74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j74.setText("74");
        j74.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j74MousePressed(evt);
            }
        });
        jPanel3.add(j74, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, -1));

        j75.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j75.setForeground(new java.awt.Color(255, 255, 255));
        j75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j75.setText("75");
        j75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j75MousePressed(evt);
            }
        });
        jPanel3.add(j75, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, -1, -1));

        j76.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j76.setForeground(new java.awt.Color(255, 255, 255));
        j76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j76.setText("76");
        j76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j76MousePressed(evt);
            }
        });
        jPanel3.add(j76, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        j77.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j77.setForeground(new java.awt.Color(255, 255, 255));
        j77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j77.setText("77");
        j77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j77MousePressed(evt);
            }
        });
        jPanel3.add(j77, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        j78.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j78.setForeground(new java.awt.Color(255, 255, 255));
        j78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j78.setText("78");
        j78.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j78MousePressed(evt);
            }
        });
        jPanel3.add(j78, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        j79.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j79.setForeground(new java.awt.Color(255, 255, 255));
        j79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j79.setText("79");
        j79.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j79MousePressed(evt);
            }
        });
        jPanel3.add(j79, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        j80.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j80.setForeground(new java.awt.Color(255, 255, 255));
        j80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j80.setText("80");
        j80.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j80MousePressed(evt);
            }
        });
        jPanel3.add(j80, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, -1, -1));

        j81.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j81.setForeground(new java.awt.Color(255, 255, 255));
        j81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j81.setText("81");
        j81.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j81MousePressed(evt);
            }
        });
        jPanel3.add(j81, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        j82.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j82.setForeground(new java.awt.Color(255, 255, 255));
        j82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j82.setText("82");
        j82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j82MousePressed(evt);
            }
        });
        jPanel3.add(j82, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        j83.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j83.setForeground(new java.awt.Color(255, 255, 255));
        j83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j83.setText("83");
        j83.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j83MousePressed(evt);
            }
        });
        jPanel3.add(j83, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        j84.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j84.setForeground(new java.awt.Color(255, 255, 255));
        j84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j84.setText("84");
        j84.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j84MousePressed(evt);
            }
        });
        jPanel3.add(j84, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        j85.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j85.setForeground(new java.awt.Color(255, 255, 255));
        j85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j85.setText("85");
        j85.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j85MousePressed(evt);
            }
        });
        jPanel3.add(j85, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        j86.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j86.setForeground(new java.awt.Color(255, 255, 255));
        j86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j86.setText("86");
        j86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j86MousePressed(evt);
            }
        });
        jPanel3.add(j86, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, -1));

        j87.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j87.setForeground(new java.awt.Color(255, 255, 255));
        j87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j87.setText("87");
        j87.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j87MousePressed(evt);
            }
        });
        jPanel3.add(j87, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        j88.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j88.setForeground(new java.awt.Color(255, 255, 255));
        j88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j88.setText("88");
        j88.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j88MousePressed(evt);
            }
        });
        jPanel3.add(j88, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        j89.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j89.setForeground(new java.awt.Color(255, 255, 255));
        j89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j89.setText("89");
        j89.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j89MousePressed(evt);
            }
        });
        jPanel3.add(j89, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 350, -1, -1));

        j90.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j90.setForeground(new java.awt.Color(255, 255, 255));
        j90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j90.setText("90");
        j90.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j90MousePressed(evt);
            }
        });
        jPanel3.add(j90, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 350, -1, -1));

        j91.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j91.setForeground(new java.awt.Color(255, 255, 255));
        j91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j91.setText("91");
        j91.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j91MousePressed(evt);
            }
        });
        jPanel3.add(j91, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 350, -1, -1));

        j92.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j92.setForeground(new java.awt.Color(255, 255, 255));
        j92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j92.setText("92");
        j92.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j92MousePressed(evt);
            }
        });
        jPanel3.add(j92, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, -1, -1));

        j93.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j93.setForeground(new java.awt.Color(255, 255, 255));
        j93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j93.setText("93");
        j93.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j93MousePressed(evt);
            }
        });
        jPanel3.add(j93, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 350, -1, -1));

        j94.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j94.setForeground(new java.awt.Color(255, 255, 255));
        j94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j94.setText("94");
        j94.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j94MousePressed(evt);
            }
        });
        jPanel3.add(j94, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, -1, -1));

        j95.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j95.setForeground(new java.awt.Color(255, 255, 255));
        j95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j95.setText("95");
        j95.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j95MousePressed(evt);
            }
        });
        jPanel3.add(j95, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 370, -1, -1));

        j96.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j96.setForeground(new java.awt.Color(255, 255, 255));
        j96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j96.setText("96");
        j96.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j96MousePressed(evt);
            }
        });
        jPanel3.add(j96, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 370, -1, -1));

        j97.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j97.setForeground(new java.awt.Color(255, 255, 255));
        j97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j97.setText("97");
        j97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j97MousePressed(evt);
            }
        });
        jPanel3.add(j97, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, -1, -1));

        j98.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j98.setForeground(new java.awt.Color(255, 255, 255));
        j98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j98.setText("98");
        j98.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j98MousePressed(evt);
            }
        });
        jPanel3.add(j98, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, -1, -1));

        j99.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j99.setForeground(new java.awt.Color(255, 255, 255));
        j99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j99.setText("99");
        j99.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j99MousePressed(evt);
            }
        });
        jPanel3.add(j99, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, -1, -1));

        j100.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j100.setForeground(new java.awt.Color(255, 255, 255));
        j100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j100.setText("100");
        j100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j100MousePressed(evt);
            }
        });
        jPanel3.add(j100, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, -1, -1));

        j101.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j101.setForeground(new java.awt.Color(255, 255, 255));
        j101.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j101.setText("101");
        j101.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j101MousePressed(evt);
            }
        });
        jPanel3.add(j101, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, -1, -1));

        j102.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j102.setForeground(new java.awt.Color(255, 255, 255));
        j102.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j102.setText("102");
        j102.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j102MousePressed(evt);
            }
        });
        jPanel3.add(j102, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 370, -1, -1));

        j103.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j103.setForeground(new java.awt.Color(255, 255, 255));
        j103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j103.setText("103");
        j103.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j103MousePressed(evt);
            }
        });
        jPanel3.add(j103, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 370, -1, -1));

        j104.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j104.setForeground(new java.awt.Color(255, 255, 255));
        j104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j104.setText("104");
        j104.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j104MousePressed(evt);
            }
        });
        jPanel3.add(j104, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 370, -1, -1));

        j105.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j105.setForeground(new java.awt.Color(255, 255, 255));
        j105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j105.setText("105");
        j105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j105MousePressed(evt);
            }
        });
        jPanel3.add(j105, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, -1, -1));

        j106.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j106.setForeground(new java.awt.Color(255, 255, 255));
        j106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j106.setText("106");
        j106.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j106MousePressed(evt);
            }
        });
        jPanel3.add(j106, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, -1, -1));

        j107.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j107.setForeground(new java.awt.Color(255, 255, 255));
        j107.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j107.setText("107");
        j107.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j107MousePressed(evt);
            }
        });
        jPanel3.add(j107, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, -1, -1));

        j108.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j108.setForeground(new java.awt.Color(255, 255, 255));
        j108.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j108.setText("108");
        j108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j108MousePressed(evt);
            }
        });
        jPanel3.add(j108, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 400, -1, -1));

        j109.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j109.setForeground(new java.awt.Color(255, 255, 255));
        j109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j109.setText("109");
        j109.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j109MousePressed(evt);
            }
        });
        jPanel3.add(j109, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 400, -1, -1));

        j110.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j110.setForeground(new java.awt.Color(255, 255, 255));
        j110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j110.setText("110");
        j110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j110MousePressed(evt);
            }
        });
        jPanel3.add(j110, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 400, -1, -1));

        j111.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j111.setForeground(new java.awt.Color(255, 255, 255));
        j111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j111.setText("111");
        j111.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j111MousePressed(evt);
            }
        });
        jPanel3.add(j111, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, -1, -1));

        j112.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j112.setForeground(new java.awt.Color(255, 255, 255));
        j112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j112.setText("112");
        j112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j112MousePressed(evt);
            }
        });
        jPanel3.add(j112, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 400, 30, -1));

        j113.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j113.setForeground(new java.awt.Color(255, 255, 255));
        j113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j113.setText("113");
        j113.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j113MousePressed(evt);
            }
        });
        jPanel3.add(j113, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, -1, -1));

        j114.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j114.setForeground(new java.awt.Color(255, 255, 255));
        j114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j114.setText("114");
        j114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j114MousePressed(evt);
            }
        });
        jPanel3.add(j114, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 400, -1, -1));

        j115.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j115.setForeground(new java.awt.Color(255, 255, 255));
        j115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j115.setText("115");
        j115.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j115MousePressed(evt);
            }
        });
        jPanel3.add(j115, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 400, -1, -1));

        j116.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j116.setForeground(new java.awt.Color(255, 255, 255));
        j116.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j116.setText("116");
        j116.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j116MousePressed(evt);
            }
        });
        jPanel3.add(j116, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, -1, -1));

        j117.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j117.setForeground(new java.awt.Color(255, 255, 255));
        j117.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j117.setText("117");
        j117.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j117MousePressed(evt);
            }
        });
        jPanel3.add(j117, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, -1, -1));

        j118.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j118.setForeground(new java.awt.Color(255, 255, 255));
        j118.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j118.setText("118");
        j118.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j118MousePressed(evt);
            }
        });
        jPanel3.add(j118, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, -1, -1));

        j119.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j119.setForeground(new java.awt.Color(255, 255, 255));
        j119.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j119.setText("119");
        j119.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j119MousePressed(evt);
            }
        });
        jPanel3.add(j119, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 220, -1, -1));

        j120.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j120.setForeground(new java.awt.Color(255, 255, 255));
        j120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j120.setText("120");
        j120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j120MousePressed(evt);
            }
        });
        jPanel3.add(j120, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, -1, -1));

        j121.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j121.setForeground(new java.awt.Color(255, 255, 255));
        j121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j121.setText("121");
        j121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j121MousePressed(evt);
            }
        });
        jPanel3.add(j121, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, -1, -1));

        j122.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j122.setForeground(new java.awt.Color(255, 255, 255));
        j122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j122.setText("122");
        j122.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j122MousePressed(evt);
            }
        });
        jPanel3.add(j122, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, -1, -1));

        j123.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j123.setForeground(new java.awt.Color(255, 255, 255));
        j123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j123.setText("123");
        j123.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j123MousePressed(evt);
            }
        });
        jPanel3.add(j123, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, -1, -1));

        j124.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j124.setForeground(new java.awt.Color(255, 255, 255));
        j124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j124.setText("124");
        j124.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j124MousePressed(evt);
            }
        });
        jPanel3.add(j124, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 240, -1, -1));

        j125.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j125.setForeground(new java.awt.Color(255, 255, 255));
        j125.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j125.setText("125");
        j125.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j125MousePressed(evt);
            }
        });
        jPanel3.add(j125, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, -1, -1));

        j126.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j126.setForeground(new java.awt.Color(255, 255, 255));
        j126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j126.setText("126");
        j126.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j126MousePressed(evt);
            }
        });
        jPanel3.add(j126, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, -1, -1));

        j127.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j127.setForeground(new java.awt.Color(255, 255, 255));
        j127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j127.setText("127");
        j127.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j127MousePressed(evt);
            }
        });
        jPanel3.add(j127, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, -1, -1));

        j128.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j128.setForeground(new java.awt.Color(255, 255, 255));
        j128.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j128.setText("128");
        j128.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j128MousePressed(evt);
            }
        });
        jPanel3.add(j128, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, -1, -1));

        boton_v.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        boton_v.setForeground(new java.awt.Color(255, 255, 255));
        boton_v.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        boton_v.setText("Libre");
        boton_v.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton_vMousePressed(evt);
            }
        });
        jPanel3.add(boton_v, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 520, -1, -1));

        boton_r.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        boton_r.setForeground(new java.awt.Color(255, 255, 255));
        boton_r.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/false1x.png"))); // NOI18N
        boton_r.setText("Ocupado");
        boton_r.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton_rMousePressed(evt);
            }
        });
        jPanel3.add(boton_r, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 540, -1, -1));
        jPanel3.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Calle Interna");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, -1, 20));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Calle Interna");
        jPanel3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 330, -1, 20));

        jLabel71.setBackground(new java.awt.Color(255, 255, 255));
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Calle lateral");
        jPanel3.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 280, 80, -1));

        jLabel72.setText("Calle bahía");
        jPanel3.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(-60, 120, -1, -1));

        jLabel73.setBackground(new java.awt.Color(255, 255, 255));
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Calle Los Tilos");
        jPanel3.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jLabel74.setBackground(new java.awt.Color(255, 255, 255));
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Calle Los Sauces");
        jPanel3.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, -1, -1));

        jLabel75.setBackground(new java.awt.Color(255, 255, 255));
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Calle Los Álamos");
        jPanel3.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        j129.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j129.setForeground(new java.awt.Color(255, 255, 255));
        j129.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j129.setText("129");
        j129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j129MousePressed(evt);
            }
        });
        jPanel3.add(j129, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, -1, -1));

        j130.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j130.setForeground(new java.awt.Color(255, 255, 255));
        j130.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j130.setText("130");
        j130.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j130MousePressed(evt);
            }
        });
        jPanel3.add(j130, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, -1, -1));

        j131.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j131.setForeground(new java.awt.Color(255, 255, 255));
        j131.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j131.setText("131");
        j131.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j131MousePressed(evt);
            }
        });
        jPanel3.add(j131, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, -1, -1));

        j132.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j132.setForeground(new java.awt.Color(255, 255, 255));
        j132.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j132.setText("132");
        j132.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j132MousePressed(evt);
            }
        });
        jPanel3.add(j132, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

        j133.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j133.setForeground(new java.awt.Color(255, 255, 255));
        j133.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j133.setText("133");
        j133.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j133MousePressed(evt);
            }
        });
        jPanel3.add(j133, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 260, -1, -1));

        j134.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j134.setForeground(new java.awt.Color(255, 255, 255));
        j134.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j134.setText("134");
        j134.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j134MousePressed(evt);
            }
        });
        jPanel3.add(j134, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, -1, -1));

        j135.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j135.setForeground(new java.awt.Color(255, 255, 255));
        j135.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j135.setText("135");
        j135.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j135MousePressed(evt);
            }
        });
        jPanel3.add(j135, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, -1, -1));

        j136.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j136.setForeground(new java.awt.Color(255, 255, 255));
        j136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j136.setText("136");
        j136.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j136MousePressed(evt);
            }
        });
        jPanel3.add(j136, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 200, -1, -1));

        j137.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j137.setForeground(new java.awt.Color(255, 255, 255));
        j137.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j137.setText("137");
        j137.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j137MousePressed(evt);
            }
        });
        jPanel3.add(j137, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, -1, -1));

        j138.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j138.setForeground(new java.awt.Color(255, 255, 255));
        j138.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j138.setText("138");
        j138.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j138MousePressed(evt);
            }
        });
        jPanel3.add(j138, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 160, -1, -1));

        j139.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j139.setForeground(new java.awt.Color(255, 255, 255));
        j139.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j139.setText("139");
        j139.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j139MousePressed(evt);
            }
        });
        jPanel3.add(j139, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, -1, -1));

        j140.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j140.setForeground(new java.awt.Color(255, 255, 255));
        j140.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j140.setText("140");
        j140.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j140MousePressed(evt);
            }
        });
        jPanel3.add(j140, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, -1, -1));

        j141.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j141.setForeground(new java.awt.Color(255, 255, 255));
        j141.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j141.setText("141");
        j141.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j141MousePressed(evt);
            }
        });
        jPanel3.add(j141, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, -1, -1));

        j142.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j142.setForeground(new java.awt.Color(255, 255, 255));
        j142.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j142.setText("142");
        j142.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j142MousePressed(evt);
            }
        });
        jPanel3.add(j142, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, -1, -1));

        jLabel76.setBackground(new java.awt.Color(255, 255, 255));
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Calle Los Paraísos");
        jPanel3.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        j143.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j143.setForeground(new java.awt.Color(255, 255, 255));
        j143.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j143.setText("143");
        j143.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j143MousePressed(evt);
            }
        });
        jPanel3.add(j143, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 260, -1, -1));

        j144.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j144.setForeground(new java.awt.Color(255, 255, 255));
        j144.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j144.setText("144");
        j144.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j144MousePressed(evt);
            }
        });
        jPanel3.add(j144, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 260, -1, -1));

        j145.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j145.setForeground(new java.awt.Color(255, 255, 255));
        j145.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j145.setText("145");
        j145.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j145MousePressed(evt);
            }
        });
        jPanel3.add(j145, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 240, -1, -1));

        j146.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j146.setForeground(new java.awt.Color(255, 255, 255));
        j146.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j146.setText("146");
        j146.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j146MousePressed(evt);
            }
        });
        jPanel3.add(j146, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 240, -1, -1));

        j147.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j147.setForeground(new java.awt.Color(255, 255, 255));
        j147.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j147.setText("147");
        j147.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j147MousePressed(evt);
            }
        });
        jPanel3.add(j147, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, -1, -1));

        j148.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j148.setForeground(new java.awt.Color(255, 255, 255));
        j148.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j148.setText("148");
        j148.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j148MousePressed(evt);
            }
        });
        jPanel3.add(j148, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 220, -1, -1));

        j149.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j149.setForeground(new java.awt.Color(255, 255, 255));
        j149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j149.setText("149");
        j149.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j149MousePressed(evt);
            }
        });
        jPanel3.add(j149, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 200, -1, -1));

        j150.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j150.setForeground(new java.awt.Color(255, 255, 255));
        j150.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j150.setText("150");
        j150.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j150MousePressed(evt);
            }
        });
        jPanel3.add(j150, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, -1, -1));

        j151.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j151.setForeground(new java.awt.Color(255, 255, 255));
        j151.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j151.setText("151");
        j151.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j151MousePressed(evt);
            }
        });
        jPanel3.add(j151, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, -1, -1));

        j152.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j152.setForeground(new java.awt.Color(255, 255, 255));
        j152.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j152.setText("152");
        j152.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j152MousePressed(evt);
            }
        });
        jPanel3.add(j152, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, -1, -1));

        j153.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j153.setForeground(new java.awt.Color(255, 255, 255));
        j153.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j153.setText("153");
        j153.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j153MousePressed(evt);
            }
        });
        jPanel3.add(j153, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, -1, -1));

        j154.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j154.setForeground(new java.awt.Color(255, 255, 255));
        j154.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j154.setText("154");
        j154.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j154MousePressed(evt);
            }
        });
        jPanel3.add(j154, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, -1, -1));

        j155.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j155.setForeground(new java.awt.Color(255, 255, 255));
        j155.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j155.setText("155");
        j155.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j155MousePressed(evt);
            }
        });
        jPanel3.add(j155, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, -1, -1));

        j156.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j156.setForeground(new java.awt.Color(255, 255, 255));
        j156.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j156.setText("156");
        j156.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j156MousePressed(evt);
            }
        });
        jPanel3.add(j156, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, -1, -1));

        j157.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j157.setForeground(new java.awt.Color(255, 255, 255));
        j157.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j157.setText("157");
        j157.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j157MousePressed(evt);
            }
        });
        jPanel3.add(j157, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, -1, -1));

        j158.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j158.setForeground(new java.awt.Color(255, 255, 255));
        j158.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j158.setText("158");
        j158.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j158MousePressed(evt);
            }
        });
        jPanel3.add(j158, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, -1, -1));

        j159.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j159.setForeground(new java.awt.Color(255, 255, 255));
        j159.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j159.setText("159");
        j159.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j159MousePressed(evt);
            }
        });
        jPanel3.add(j159, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, -1, -1));

        j160.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j160.setForeground(new java.awt.Color(255, 255, 255));
        j160.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j160.setText("160");
        j160.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j160MousePressed(evt);
            }
        });
        jPanel3.add(j160, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, -1, -1));

        j161.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j161.setForeground(new java.awt.Color(255, 255, 255));
        j161.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j161.setText("161");
        j161.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j161MousePressed(evt);
            }
        });
        jPanel3.add(j161, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));

        j162.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j162.setForeground(new java.awt.Color(255, 255, 255));
        j162.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j162.setText("162");
        j162.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j162MousePressed(evt);
            }
        });
        jPanel3.add(j162, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, -1, -1));

        jLabel77.setBackground(new java.awt.Color(255, 255, 255));
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Calle Los Fresnos");
        jPanel3.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, -1, -1));

        j163.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j163.setForeground(new java.awt.Color(255, 255, 255));
        j163.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j163.setText("163");
        j163.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j163MousePressed(evt);
            }
        });
        jPanel3.add(j163, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 220, -1, -1));

        j164.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j164.setForeground(new java.awt.Color(255, 255, 255));
        j164.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j164.setText("164");
        j164.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j164MousePressed(evt);
            }
        });
        jPanel3.add(j164, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 200, -1, -1));

        j165.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j165.setForeground(new java.awt.Color(255, 255, 255));
        j165.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j165.setText("165");
        j165.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j165MousePressed(evt);
            }
        });
        jPanel3.add(j165, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, -1, -1));

        j166.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j166.setForeground(new java.awt.Color(255, 255, 255));
        j166.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j166.setText("166");
        j166.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j166MousePressed(evt);
            }
        });
        jPanel3.add(j166, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, -1, -1));

        j167.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j167.setForeground(new java.awt.Color(255, 255, 255));
        j167.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j167.setText("167");
        j167.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j167MousePressed(evt);
            }
        });
        jPanel3.add(j167, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, -1, -1));

        j168.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j168.setForeground(new java.awt.Color(255, 255, 255));
        j168.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j168.setText("168");
        j168.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j168MousePressed(evt);
            }
        });
        jPanel3.add(j168, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, -1, -1));

        j169.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j169.setForeground(new java.awt.Color(255, 255, 255));
        j169.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j169.setText("169");
        j169.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j169MousePressed(evt);
            }
        });
        jPanel3.add(j169, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 40, -1));

        j170.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j170.setForeground(new java.awt.Color(255, 255, 255));
        j170.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j170.setText("170");
        j170.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j170MousePressed(evt);
            }
        });
        jPanel3.add(j170, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 40, -1));

        j171.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j171.setForeground(new java.awt.Color(255, 255, 255));
        j171.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j171.setText("171");
        j171.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j171MousePressed(evt);
            }
        });
        jPanel3.add(j171, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 220, -1, -1));

        j172.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j172.setForeground(new java.awt.Color(255, 255, 255));
        j172.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j172.setText("172");
        j172.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j172MousePressed(evt);
            }
        });
        jPanel3.add(j172, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 200, -1, -1));

        j173.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j173.setForeground(new java.awt.Color(255, 255, 255));
        j173.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j173.setText("173");
        j173.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j173MousePressed(evt);
            }
        });
        jPanel3.add(j173, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 180, -1, -1));

        j174.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j174.setForeground(new java.awt.Color(255, 255, 255));
        j174.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j174.setText("174");
        j174.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j174MousePressed(evt);
            }
        });
        jPanel3.add(j174, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, -1, -1));

        j175.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j175.setForeground(new java.awt.Color(255, 255, 255));
        j175.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j175.setText("175");
        j175.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j175MousePressed(evt);
            }
        });
        jPanel3.add(j175, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, -1, -1));

        j176.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j176.setForeground(new java.awt.Color(255, 255, 255));
        j176.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j176.setText("176");
        j176.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j176MousePressed(evt);
            }
        });
        jPanel3.add(j176, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 120, -1, -1));

        j177.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j177.setForeground(new java.awt.Color(255, 255, 255));
        j177.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j177.setText("177");
        j177.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j177MousePressed(evt);
            }
        });
        jPanel3.add(j177, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, -1, -1));

        j178.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j178.setForeground(new java.awt.Color(255, 255, 255));
        j178.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j178.setText("178");
        j178.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j178MousePressed(evt);
            }
        });
        jPanel3.add(j178, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, -1, -1));

        jLabel78.setBackground(new java.awt.Color(255, 255, 255));
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Calle Los Pinos");
        jPanel3.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, -1, 20));

        jLabel79.setBackground(new java.awt.Color(255, 255, 255));
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Desembarcadero");
        jPanel3.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 290, -1, -1));

        j179.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j179.setForeground(new java.awt.Color(255, 255, 255));
        j179.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j179.setText("179");
        j179.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j179MousePressed(evt);
            }
        });
        jPanel3.add(j179, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 310, -1, -1));

        j180.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j180.setForeground(new java.awt.Color(255, 255, 255));
        j180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j180.setText("180");
        j180.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j180MousePressed(evt);
            }
        });
        jPanel3.add(j180, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 330, -1, -1));

        j181.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j181.setForeground(new java.awt.Color(255, 255, 255));
        j181.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j181.setText("181");
        j181.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j181MousePressed(evt);
            }
        });
        jPanel3.add(j181, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 330, -1, -1));

        j182.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j182.setForeground(new java.awt.Color(255, 255, 255));
        j182.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j182.setText("182");
        j182.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j182MousePressed(evt);
            }
        });
        jPanel3.add(j182, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, -1, -1));

        j183.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j183.setForeground(new java.awt.Color(255, 255, 255));
        j183.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j183.setText("183");
        j183.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j183MousePressed(evt);
            }
        });
        jPanel3.add(j183, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 330, -1, -1));

        j184.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j184.setForeground(new java.awt.Color(255, 255, 255));
        j184.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j184.setText("184");
        j184.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j184MousePressed(evt);
            }
        });
        jPanel3.add(j184, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, -1, -1));

        j185.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j185.setForeground(new java.awt.Color(255, 255, 255));
        j185.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j185.setText("185");
        j185.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j185MousePressed(evt);
            }
        });
        jPanel3.add(j185, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 330, -1, -1));

        j186.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j186.setForeground(new java.awt.Color(255, 255, 255));
        j186.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j186.setText("186");
        j186.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j186MousePressed(evt);
            }
        });
        jPanel3.add(j186, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 310, -1, -1));

        j187.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j187.setForeground(new java.awt.Color(255, 255, 255));
        j187.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j187.setText("187");
        j187.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j187MousePressed(evt);
            }
        });
        jPanel3.add(j187, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 310, -1, -1));

        j188.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j188.setForeground(new java.awt.Color(255, 255, 255));
        j188.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j188.setText("188");
        j188.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j188MousePressed(evt);
            }
        });
        jPanel3.add(j188, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 310, -1, -1));

        j189.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j189.setForeground(new java.awt.Color(255, 255, 255));
        j189.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j189.setText("189");
        j189.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j189MousePressed(evt);
            }
        });
        jPanel3.add(j189, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 310, -1, -1));

        j190.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j190.setForeground(new java.awt.Color(255, 255, 255));
        j190.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j190.setText("190");
        j190.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j190MousePressed(evt);
            }
        });
        jPanel3.add(j190, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 310, -1, -1));

        j191.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j191.setForeground(new java.awt.Color(255, 255, 255));
        j191.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j191.setText("191");
        j191.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j191MousePressed(evt);
            }
        });
        jPanel3.add(j191, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 370, -1, -1));

        j192.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j192.setForeground(new java.awt.Color(255, 255, 255));
        j192.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j192.setText("192");
        j192.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j192MousePressed(evt);
            }
        });
        jPanel3.add(j192, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 370, -1, -1));

        j193.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j193.setForeground(new java.awt.Color(255, 255, 255));
        j193.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j193.setText("193");
        j193.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j193MousePressed(evt);
            }
        });
        jPanel3.add(j193, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 370, -1, -1));

        j194.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j194.setForeground(new java.awt.Color(255, 255, 255));
        j194.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j194.setText("194");
        j194.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j194MousePressed(evt);
            }
        });
        jPanel3.add(j194, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 370, -1, -1));

        j195.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j195.setForeground(new java.awt.Color(255, 255, 255));
        j195.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j195.setText("195");
        j195.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j195MousePressed(evt);
            }
        });
        jPanel3.add(j195, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, -1, -1));

        j196.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j196.setForeground(new java.awt.Color(255, 255, 255));
        j196.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j196.setText("196");
        j196.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j196MousePressed(evt);
            }
        });
        jPanel3.add(j196, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 370, -1, -1));

        j197.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j197.setForeground(new java.awt.Color(255, 255, 255));
        j197.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j197.setText("197");
        j197.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j197MousePressed(evt);
            }
        });
        jPanel3.add(j197, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 390, -1, -1));

        j198.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j198.setForeground(new java.awt.Color(255, 255, 255));
        j198.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j198.setText("198");
        j198.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j198MousePressed(evt);
            }
        });
        jPanel3.add(j198, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 390, -1, -1));

        j199.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j199.setForeground(new java.awt.Color(255, 255, 255));
        j199.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j199.setText("199");
        j199.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j199MousePressed(evt);
            }
        });
        jPanel3.add(j199, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 390, -1, -1));

        j200.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j200.setForeground(new java.awt.Color(255, 255, 255));
        j200.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j200.setText("200");
        j200.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j200MousePressed(evt);
            }
        });
        jPanel3.add(j200, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 390, -1, -1));

        j201.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j201.setForeground(new java.awt.Color(255, 255, 255));
        j201.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j201.setText("201");
        j201.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j201MousePressed(evt);
            }
        });
        jPanel3.add(j201, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 390, -1, -1));

        j202.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j202.setForeground(new java.awt.Color(255, 255, 255));
        j202.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j202.setText("202");
        j202.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j202MousePressed(evt);
            }
        });
        jPanel3.add(j202, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 390, -1, -1));

        j203.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j203.setForeground(new java.awt.Color(255, 255, 255));
        j203.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j203.setText("203");
        j203.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j203MousePressed(evt);
            }
        });
        jPanel3.add(j203, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 430, -1, -1));

        j204.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j204.setForeground(new java.awt.Color(255, 255, 255));
        j204.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j204.setText("204");
        j204.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j204MousePressed(evt);
            }
        });
        jPanel3.add(j204, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, -1, -1));

        j205.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j205.setForeground(new java.awt.Color(255, 255, 255));
        j205.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j205.setText("205");
        j205.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j205MousePressed(evt);
            }
        });
        jPanel3.add(j205, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 430, -1, -1));

        j206.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j206.setForeground(new java.awt.Color(255, 255, 255));
        j206.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j206.setText("206");
        j206.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j206MousePressed(evt);
            }
        });
        jPanel3.add(j206, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 430, -1, -1));

        j207.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j207.setForeground(new java.awt.Color(255, 255, 255));
        j207.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j207.setText("207");
        j207.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j207MousePressed(evt);
            }
        });
        jPanel3.add(j207, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 430, -1, -1));

        j208.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j208.setForeground(new java.awt.Color(255, 255, 255));
        j208.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j208.setText("208");
        j208.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j208MousePressed(evt);
            }
        });
        jPanel3.add(j208, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 450, -1, -1));

        j209.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j209.setForeground(new java.awt.Color(255, 255, 255));
        j209.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j209.setText("292");
        j209.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j209MousePressed(evt);
            }
        });
        jPanel3.add(j209, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 450, -1, -1));

        j210.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j210.setForeground(new java.awt.Color(255, 255, 255));
        j210.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j210.setText("210");
        j210.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j210MousePressed(evt);
            }
        });
        jPanel3.add(j210, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 450, -1, -1));

        j211.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j211.setForeground(new java.awt.Color(255, 255, 255));
        j211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j211.setText("211");
        j211.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j211MousePressed(evt);
            }
        });
        jPanel3.add(j211, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 450, -1, -1));

        j212.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        j212.setForeground(new java.awt.Color(255, 255, 255));
        j212.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
        j212.setText("212");
        j212.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j212MousePressed(evt);
            }
        });
        jPanel3.add(j212, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 450, -1, -1));

        jLabel80.setBackground(new java.awt.Color(255, 255, 255));
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Calle los Acacios");
        jPanel3.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 410, -1, -1));

        jLabel81.setBackground(new java.awt.Color(255, 255, 255));
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Calle los Ceibos");
        jPanel3.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 350, -1, -1));

        jLabel85.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(204, 204, 0));
        jLabel85.setText("INFORMES");
        jPanel3.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 80, -1));

        jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/123123.png"))); // NOI18N
        jPanel3.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 750));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menu_ingreso_egreso, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(menu_ingreso_egreso))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Documento_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Documento_aActionPerformed

    }//GEN-LAST:event_Documento_aActionPerformed

    private void Buscar_aMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Buscar_aMousePressed
        try {
            BuscarAportante();
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Buscar_aMousePressed

    private void documento_eActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_eActionPerformed

    }//GEN-LAST:event_documento_eActionPerformed

    private void carrera_eActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carrera_eActionPerformed

    }//GEN-LAST:event_carrera_eActionPerformed

    private void facultad_eActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facultad_eActionPerformed

    }//GEN-LAST:event_facultad_eActionPerformed

    private void BuscarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarMousePressed
        try {
            BuscarEstudiante();
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BuscarMousePressed

    private void DocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DocumentoActionPerformed

    }//GEN-LAST:event_DocumentoActionPerformed
    public void ingre_p() throws NullPointerException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        if (!documento_p.getText().isEmpty() || !nombre_p.getText().isEmpty()) {

            int parsela = 0;
            try {

                //controlar que en parsela no haya un string
                try {
                    parsela = Integer.parseInt(Parsela_p.getText());
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("es un string y no se puede pasar");
                }

                //control que parsela este entre 128 y 1
                if (parsela <= 212 && parsela >= 1) {

                    //  Ocupacion ocupacion = new Ocupacion();
                    // RegistrarParsela();
                    //ocupacion.BuscarParsela(Parsela.getText());
                    RegistrarIngresoParticular();

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar, Ingreso una parsela que no se encuentra en la base de datos.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (java.text.ParseException ex) {
                Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (java.lang.NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ingreso un texto donde se esperaba un numero.", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "ERROR, complete todos los campos antes de registrar un ingreso. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void ingre_otros() throws NullPointerException, SQLException {

        if (!documento_otros.getText().isEmpty() || !nombre_otros.getText().isEmpty()) {

            int parsela = 0;
            try {

                //controlar que en parsela no haya un string
                try {
                    parsela = Integer.parseInt(Parcela_otros.getText());
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("es un string y no se puede pasar");
                }

                //control que parsela este entre 128 y 1
                if (parsela <= 212 && parsela >= 1) {

                    //  Ocupacion ocupacion = new Ocupacion();
                    // RegistrarParsela();
                    //ocupacion.BuscarParsela(Parsela.getText());
                    RegistrarIngresoOtros();

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar, Ingreso una parcela que no se encuentra en la base de datos.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (java.text.ParseException ex) {
                Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            } catch (java.lang.NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ingreso un texto donde se esperaba un numero.", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "ERROR, complete todos los campos antes de registrar un ingreso. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void ingre() throws SQLException {
        int parcela = 0;
        try {

            //controlar que en parsela no haya un string
            try {
                parcela = Integer.parseInt(Parcela.getText());
            } catch (java.lang.NumberFormatException e) {
                System.out.println("es un string y no se puede pasar");
            }

            //control que parsela este entre 128 y 1
            if (parcela <= 212 && parcela >= 1) {

                RegistrarIngreso();

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar, Ingreso una parsela que no se encuentra en la base de datos.", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ingre_a() throws SQLException {
        int parsela = 0;
        try {

            //controlar que en parsela no haya un string
            try {
                parsela = Integer.parseInt(Parsela_a.getText());
            } catch (java.lang.NumberFormatException e) {
                System.out.println("es un string y no se puede pasar");
            }

            //control que parsela este entre 128 y 1
            if (parsela <= 212 && parsela >= 1) {

                RegistrarIngresoAportante();

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar, Ingreso una parsela que no se encuentra en la base de datos.", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Boton_ingreso_alumno() {
        try {
            if (controlador.ControlarCajaAbierta() == 1) {
                int c = controlador.Controldnirepetidoingreso(documento_e.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_e.getText());

                if (documento_e.getText().equals("") || nombre_e.getText().equals("")) {
                    throw new Exception();
                }
                if (c == 0 && b == 0) {
                    if (pasar_dia.isSelected()) {

                        ingre_dia("alumno", documento_e.getText(), nombre_e.getText(), Integer.parseInt(acomp_particular.getText()));
                    } else {
                        ingre();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe abrir la caja primero", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            Tabla();
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void Boton_ingresoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingresoMousePressed
        Boton_ingreso_alumno();
        try {
            prueba();
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Boton_ingresoMousePressed

    public long cant_dias(JDateChooser fecha_1, JDateChooser fecha_2) throws java.text.ParseException, java.lang.NullPointerException {

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = myFormat.parse(calc_fecha(fecha_1));
            Date date2 = myFormat.parse(calc_fecha(fecha_2));
            long diff = date2.getTime() - date1.getTime();

            long p = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            return p;
        } catch (java.text.ParseException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una fecha de ingreso y egreso \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (java.lang.NullPointerException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error, complete todos los campos. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        return 0;
    }

    public String calc_fecha(com.toedter.calendar.JDateChooser fecha) {
        String arr[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        char c1, c2;
        if (fecha.getDate() != null) {

            c1 = fecha.getDate().toString().charAt(8);
            c2 = fecha.getDate().toString().charAt(9);

            if ("".equals(fecha.getDate().toString())) {
                System.out.println("fecha nula:");
            }

            int cont = 0;
            String l = "";

            //     System.out.println(fecha.getDate().toString().length());
            for (int i = fecha.getDate().toString().length(); i > 0; i--) {
                cont++;
                l += fecha.getDate().toString().charAt(i - 1);
                if (cont == 4) {
                    break;
                }
            }
            char k;
            String anio = "";
            for (int i = l.length(); i > 0; i--) {
                k = l.charAt(i - 1);
                anio += k;
            }
            String z = "";
            for (int i = 4; i < 7; i++) {
                z += fecha.getDate().toString().charAt(i);
            }
            int x = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals(z)) {
                    x = i + 1;
                }
            }
            String mes = "";

            if (x < 10) {
                mes = String.format("0%d", x);
            } else {
                mes = Integer.toString(x);
            }

            int dia = Integer.parseInt((c1 - '0') + "" + (c2 - '0'));
            String fecha_t;
            if (dia < 10) {
                fecha_t = anio + "-" + mes + "-" + String.format("0%d", dia);
                //      dia = Integer.parseInt(str);
            } else {
                fecha_t = anio + "-" + mes + "-" + dia;
            }
            return fecha_t;

        } else {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar una fecha de ingreso y egreso.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }
        return null;
    }

    public void ingre_dia(String categoria, String dni, String nombre, int acomp) throws SQLException, java.text.ParseException {
        LocalTime HoraActual = LocalTime.now();
        int c;
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));

        String hora_actual = hora + ":" + minutos;

        if (categoria.equals("otros")) {
            c = controlador.IngresoDiario(dni, nombre, HoraActual.format(f), categoria + "(" + descripcion_otros.getText() + ")(" + tarifa_otros.getText() + ")", acomp);
        } else {

            c = controlador.IngresoDiario(dni, nombre, HoraActual.format(f), categoria, acomp);
        }
        if (c != 1) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (categoria.equals("alumno")) {
                modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo alumno por el dia", Main.DiaActual, HoraActual.format(f), 0);
                modelo.insertardinerocaja(Main.tarifa_dia_alumnos);
                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia.isSelected()) {
                        float importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso, fecha_egreso), 2);
                        imprimirtiketacampante(calc_fecha(fecha_ingreso), calc_fecha(fecha_egreso), "Alumno", importe, nombre_e.getText(), documento_e.getText(), 0, Main.tarifa_dia_alumnos);
                    } else {

                        float importe = Main.tarifa_dia_alumnos;
                        imprimirtiketdia("Alumno", importe, nombre_e.getText(), documento_e.getText(), 0, Main.tarifa_dia_alumnos);
                    }
                }
            }
            if (categoria.equals("aportante")) {
                modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo aportante por el dia", Main.DiaActual, HoraActual.format(f), 0);
                if (!familiares.getText().equals("0")) {
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado " + familiares.getText() + " familiares de aportante dia", Main.DiaActual, HoraActual.format(f), 3);
                }
                modelo.insertardinerocaja(Main.tarifa_dia_aportantes);
                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia1.isSelected()) {
                        float importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p1, fecha_egreso_p1), 2);
                        imprimirtiketacampanteaportante(calc_fecha(fecha_ingreso_p1), calc_fecha(fecha_egreso_p1), nombre_a.getText(), documento_a.getText(), Integer.parseInt(familiares.getText()));
                    } else {

                        float importe = Main.tarifa_dia_aportantes;
                        imprimirtiketdiaaportante(nombre_a.getText(), documento_a.getText(), Integer.parseInt(familiares.getText()));
                    }
                }
                familiares.setText("0");

            }
            if (categoria.equals("particular")) {

                if (Integer.parseInt(acomp_particular.getText()) == 0) {

                    modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo particular por el dia", Main.DiaActual, HoraActual.format(f), 0);
                    modelo.insertardinerocaja(Main.tarifa_dia_particular);
                    System.out.println("tarifa individual: " + Main.tarifa_dia_particular);
                } else {
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado " + String.format("%02d", (Integer.parseInt(acomp_particular.getText()) + 1)) + " particulares por el dia", Main.DiaActual, HoraActual.format(f), 5);
                    modelo.insertardinerocaja(Main.tarifa_dia_particular * (Integer.parseInt(acomp_particular.getText()) + 1));
                    System.out.println("tarifa con acompañantes: " + Main.tarifa_dia_particular + "$ *" + (Integer.parseInt(acomp_particular.getText()) + 1) + "=" + Main.tarifa_dia_particular * (Integer.parseInt(acomp_particular.getText()) + 1));
                }
                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia2.isSelected()) {
                        float importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 2);
                        imprimirtiketacampante(calc_fecha(fecha_ingreso_p), calc_fecha(fecha_egreso_p), "Particular", importe, nombre_p.getText(), documento_p.getText(), Integer.parseInt(acomp_particular.getText()), Main.tarifa_dia_particular);
                    } else {

                        float importe = Main.tarifa_dia_particular;
                        imprimirtiketdia("Particular", importe, nombre_p.getText(), documento_p.getText(), Integer.parseInt(acomp_particular.getText()), Main.tarifa_dia_particular);
                    }
                }
            }

            if (categoria.equals("otros")) {

                modelo.InsertarRegistro(Login.usuario, "registro " + String.format("%02d", (Integer.parseInt(acomp_otros.getText()) + 1)) + " nuevo/s ingresante/s(otros) por el dia", Main.DiaActual, HoraActual.format(f), 6);
                modelo.insertardinerocaja(Float.parseFloat(tarifa_otros.getText()) * (Integer.parseInt(acomp_otros.getText()) + 1));
                System.out.println("Insertado dinero en caja, otros por dia:" + Float.parseFloat(tarifa_otros.getText()) * (Integer.parseInt(acomp_otros.getText()) + 1));
                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia4.isSelected()) {
                        float importe = (cant_dias(fecha_ingreso_otros, fecha_egreso_otros) * Float.parseFloat(tarifa_otros.getText()));
                        imprimirtiketacampanteotros(calc_fecha(fecha_ingreso_otros), calc_fecha(fecha_ingreso_otros), descripcion_otros.getText(), importe, nombre_otros.getText(), documento_otros.getText(), Integer.parseInt(acomp_otros.getText()), Float.parseFloat(tarifa_otros.getText()));
                    } else {

                        float importe = Float.parseFloat(tarifa_otros.getText());
                        imprimirtiketdiaotros(descripcion_otros.getText(), importe, nombre_otros.getText(), documento_otros.getText(), Integer.parseInt(acomp_otros.getText()), Float.parseFloat(tarifa_otros.getText()));
                    }
                }
            }

            if (categoria.equals("invitado")) {

                if (Integer.parseInt(acomp_invitados.getText()) == 0) {
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo invitado por el dia", Main.DiaActual, HoraActual.format(f), 0);
                    modelo.insertardinerocaja(Main.tarifa_dia_invitados);
                } else {
                    modelo.insertardinerocaja(Main.tarifa_dia_invitados * (Integer.parseInt(acomp_invitados.getText()) + 1));
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado " + String.format("%02d", (Integer.parseInt(acomp_invitados.getText()) + 1)) + " nuevos invitados por el dia", Main.DiaActual, HoraActual.format(f), 7);
                }

                Component jFrame = null;
                int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                if (result == 0) {

                    if (!pasar_dia3.isSelected()) {
                        float importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2), 4);
                        imprimirtiketacampante(calc_fecha(fecha_ingreso_p2), calc_fecha(fecha_egreso_p2), "Invitado", importe, nombre_p1.getText(), documento_p1.getText(), Integer.parseInt(acomp_invitados.getText()), Main.tarifa_acampar_invitados);
                    } else {

                        float importe = Main.tarifa_dia_invitados;

                        imprimirtiketdiainvitado("Invitado", importe, nombre_p1.getText(), documento_p1.getText(), Integer.parseInt(acomp_invitados.getText()), importe);
                    }
                }
            }
            setearnullalumno();
            setearnullparticular();
            setearnullaportante();
            setearnullinvitado();
            stearnullotros();

        }
    }

    private void ObtenerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ObtenerMousePressed
        try {
            if (!pasar_dia.isSelected()) {
                if (!Parcela.getText().isEmpty()) {
                    float importe = 0;

                    if (Integer.parseInt(Parcela.getText()) >= 1 && Integer.parseInt(Parcela.getText()) <= 4) {
                        importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso, fecha_egreso), 6);
                        tarifa.setText(String.format("%.2f", importe));
                    } else {
                        importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso, fecha_egreso), 1);
                        tarifa.setText(String.format("%.2f", importe));
                    }

                }

            } else {
                if (Integer.parseInt(acomp_particular.getText()) == 0) {

                    tarifa.setText(String.format("%.2f", Main.tarifa_dia_alumnos));
                } else {
                    tarifa.setText(String.format("%.2f", Main.tarifa_dia_alumnos * Integer.parseInt(acomp_particular.getText())));
                }
            }

        } catch (java.text.ParseException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una fecha de ingreso y egreso \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_ObtenerMousePressed

    private void tarifaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifaActionPerformed

    }//GEN-LAST:event_tarifaActionPerformed

    private void jPanel8ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel8ComponentHidden

    }//GEN-LAST:event_jPanel8ComponentHidden

    private void ParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ParcelaActionPerformed

    }//GEN-LAST:event_ParcelaActionPerformed

    private void documento_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_pActionPerformed

    }//GEN-LAST:event_documento_pActionPerformed

    private void Obtener1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Obtener1MousePressed

        try {

            if (!pasar_dia2.isSelected()) {
                if (!Parsela_p.getText().isEmpty()) {
                    float importe = 0;
                    //acampe
                    if (Integer.parseInt(Parsela_p.getText()) >= 1 && Integer.parseInt(Parsela_p.getText()) <= 4) {
                        importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 6);
                        tarifa1.setText(String.format("%.2f", importe));
                    } else {

                        if (Integer.parseInt(acomp_particular.getText()) == 0) {
                            importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 3);
                            tarifa1.setText(String.format("%.2f", importe));
                        } else {
                            importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p, fecha_egreso_p), 3);
                            tarifa1.setText(String.format("%.2f", importe * (Integer.parseInt(acomp_particular.getText()) + 1)));
                        }
                    }

                }

            } else {
                //por dia
                if (Integer.parseInt(acomp_particular.getText()) == 0) {

                    tarifa1.setText(String.format("%.2f", Main.tarifa_dia_particular));

                } else {
                    tarifa1.setText(String.format("%.2f", (Main.tarifa_dia_particular) * (Integer.parseInt(acomp_particular.getText()) + 1)));

                }

            }
        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Ingreso una letra donde se espera numero \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.NumberFormatException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Especifique la parcela. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NullPointerException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Obtener1MousePressed

    private void tarifa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifa1ActionPerformed

    }//GEN-LAST:event_tarifa1ActionPerformed
    public void Boton_ingreso_particular() {
        try {
            if (controlador.ControlarCajaAbierta() == 1) {
                int c = controlador.Controldnirepetidoingreso(documento_p.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_p.getText());

                if (documento_p.getText().equals("") || nombre_p.getText().equals("")) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia2.isSelected()) {

                        ingre_dia("particular", documento_p.getText(), nombre_p.getText(), Integer.parseInt(acomp_particular.getText()));
                    } else {
                        ingre_p();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe abrir la caja primero", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void Boton_ingreso_otro() {
        try {
            if (controlador.ControlarCajaAbierta() == 1) {
                int c = controlador.Controldnirepetidoingreso(documento_otros.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_otros.getText());

                if (documento_otros.getText().equals("") || nombre_otros.getText().equals("")) {
                    throw new Exception();
                }
                if (descripcion_otros.getText().isEmpty()) {
                    descripcion_otros.setText("Sin especificar");
                }
                if (c == 0 && b == 0) {
                    if (pasar_dia4.isSelected()) {

                        ingre_dia("otros", documento_otros.getText(), nombre_otros.getText(), Integer.parseInt(acomp_otros.getText()));
                    } else {
                        ingre_otros();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe abrir la caja primero", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void Boton_ingreso_pMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingreso_pMousePressed
        Boton_ingreso_particular();
        try {
            prueba();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Boton_ingreso_pMousePressed

    private void jPanel12ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel12ComponentHidden

    }//GEN-LAST:event_jPanel12ComponentHidden

    private void Obtener2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Obtener2MousePressed
        try {
            if (!pasar_dia1.isSelected()) {
                if (!Parsela_a.getText().isEmpty()) {
                    float importe = 0;

                    if (Integer.parseInt(Parsela_a.getText()) >= 1 && Integer.parseInt(Parsela_a.getText()) <= 4) {
                        importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p1, fecha_egreso_p1), 5);
                        tarifa2.setText(String.format("%.2f", importe));
                    } else {
                        importe = controlador.CalcularImporte((int) cant_dias(fecha_ingreso_p1, fecha_egreso_p1), 2);
                        tarifa2.setText(String.format("%.2f", importe));
                    }

                }

            } else {
                tarifa2.setText(String.format("%.2f", Main.tarifa_dia_aportantes));
            }

        } catch (java.text.ParseException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una fecha de ingreso y egreso \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_Obtener2MousePressed

    private void tarifa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifa2ActionPerformed

    }//GEN-LAST:event_tarifa2ActionPerformed
    public void botoningresoaportante() {
        try {
            if (controlador.ControlarCajaAbierta() == 1) {
                int c = controlador.Controldnirepetidoingreso(documento_a.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_a.getText());

                if (documento_a.getText().equals("") || nombre_a.getText().equals("") || apellido_a.getText().isEmpty() || cod_aportante.getText().isEmpty() || familiares.getText().isEmpty()) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia1.isSelected()) {
                        ingre_dia("aportante", documento_a.getText(), nombre_a.getText(), 0);
                    } else {
                        ingre_a();

                    }

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe primero abrir la caja", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            Tabla();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void Boton_ingreso_aMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingreso_aMousePressed
        botoningresoaportante();
        try {
            prueba();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_Boton_ingreso_aMousePressed
    public void BusquedaVehiculos() throws SQLException {

        DefaultTableModel modelo = new DefaultTableModel();

        ArrayList<Object> nombrecolumna = new ArrayList<Object>();

        nombrecolumna.add("marca");
        nombrecolumna.add("patente");
        nombrecolumna.add("fecha_ingreso");
        nombrecolumna.add("fecha_egreso");

        for (Object columna : nombrecolumna) {
            modelo.addColumn(columna);
        }

        ResultSet res_1;
        res_1 = controlador.MostarVehiculos();
        while (res_1.next()) {

            String documento = res_1.getString("marca");
            String nombre = res_1.getString("patente");
            String fecha_ingreso = res_1.getString("fecha_ingreso");
            String fecha_egreso = res_1.getString("fecha_egreso");

            String tab[] = {documento, nombre, fecha_ingreso, fecha_egreso};

            modelo.addRow(tab);

        }
        tabla_vehiculo.setModel(modelo);
    }
    private void Parsela_aActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Parsela_aActionPerformed

    }//GEN-LAST:event_Parsela_aActionPerformed

    private void jPanel14ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel14ComponentHidden

    }//GEN-LAST:event_jPanel14ComponentHidden

    private void Parsela_pActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Parsela_pActionPerformed

    }//GEN-LAST:event_Parsela_pActionPerformed

    private void menu_ingreso_egresoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu_ingreso_egresoMousePressed
        documento_p1.setEnabled(false);
        nombre_p1.setEnabled(false);
        LocalTime HoraActual = LocalTime.now();
        System.out.println(HoraActual.format(f));
        tarifa_casilla.setText("Tarifa por dia: " + String.valueOf(Main.tarifa_casillas));

    }//GEN-LAST:event_menu_ingreso_egresoMousePressed

    private void pasar_dia2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_dia2ActionPerformed
        if (pasar_dia2.isSelected()) {
            fecha_ingreso_p.setEnabled(false);
            fecha_egreso_p.setEnabled(false);
            Parsela_p.setEnabled(false);
        } else {
            Parsela_p.setEnabled(true);
            fecha_ingreso_p.setEnabled(true);
            fecha_egreso_p.setEnabled(true);
        }
    }//GEN-LAST:event_pasar_dia2ActionPerformed

    private void pasar_dia2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasar_dia2MousePressed

    }//GEN-LAST:event_pasar_dia2MousePressed

    private void pasar_dia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_dia1ActionPerformed
        if (pasar_dia1.isSelected()) {
            fecha_ingreso_p1.setEnabled(false);
            fecha_egreso_p1.setEnabled(false);
            Parsela_a.setEnabled(false);
        } else {
            Parsela_a.setEnabled(true);
            fecha_ingreso_p1.setEnabled(true);
            fecha_egreso_p1.setEnabled(true);
        }
    }//GEN-LAST:event_pasar_dia1ActionPerformed

    private void pasar_diaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_diaActionPerformed
        if (pasar_dia.isSelected()) {
            fecha_ingreso.setEnabled(false);
            fecha_egreso.setEnabled(false);
            Parcela.setEnabled(false);
        } else {
            fecha_ingreso.setEnabled(true);
            fecha_egreso.setEnabled(true);
            Parcela.setEnabled(true);
        }
    }//GEN-LAST:event_pasar_diaActionPerformed

    private void documento_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_pKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_documento_pKeyPressed

    private void Documento_aKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Documento_aKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                BuscarAportante();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_Documento_aKeyPressed

    private void DocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DocumentoKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                BuscarEstudiante();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_DocumentoKeyPressed

    private void dni_buscadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dni_buscadoActionPerformed
        if (dni_buscado.getText().isEmpty()) {
            try {
                Tabla();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_dni_buscadoActionPerformed

    private void jLabel32KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel32KeyPressed

    }//GEN-LAST:event_jLabel32KeyPressed
    public void buscar_egreso() throws SQLException {
        ResultSet res;
        int f = 0;
        res = controlador.MostarDocumento(dni_buscado.getText());

        if (res.next() == true) {
            f++;
            String documento = res.getString("documento");
            String nombre = res.getString("nombre");
            String categoria = res.getString("categoria");
            String importe = String.valueOf(res.getFloat("importe"));
            String fecha_egreso = res.getString("fecha_egreso");
            String fecha_ingreso = res.getString("fecha_ingreso");
            String parcela = res.getString("parsela");
            String tab[] = {documento, nombre, categoria, importe, fecha_egreso, fecha_ingreso, parcela};
            DefaultTableModel tablamodelo = (DefaultTableModel) tabla_egreso.getModel();

            tablamodelo.addRow(tab);
            filas = f;
            int rows = tablamodelo.getRowCount();
            for (int i = rows - (filas + 1); i >= 0; i--) {
                tablamodelo.removeRow(i);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontro el documento solicitado.\n Intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void prueba() throws SQLException {
        javax.swing.JLabel[] label = {j1, j2, j3, j4, j5, j6, j7, j8, j9, j10, j11, j12, j13, j14, j15, j16, j17, j18, j19, j20, j21, j22, j23, j24, j25, j26, j27, j28, j29, j30, j31, j32, j33, j34, j35, j36, j37, j38, j39, j40, j41, j42, j43, j44, j45, j46, j47, j48, j49, j50,
            j51, j52, j53, j54, j55, j56, j57, j68, j59, j60, j61, j62, j63, j64, j65, j66, j67, j68, j69, j70, j71, j72, j73, j74, j75, j76, j77, j78, j79, j80, j81, j82, j83, j84, j85, j86, j87, j88, j89, j90, j91, j92, j93, j94, j95, j96, j97, j98, j99, j100, j101, j102, j103, j104, j105, j106, j107, j108, j109, j110, j111, j112, j113, j114,
            j115, j116, j117, j118, j119, j120, j121, j122, j123, j124, j125, j126, j127, j128, j129, j130, j131, j132, j133, j134, j135, j136, j137, j138, j139, j140, j141, j142, j143, j144, j145, j146, j147, j148, j149, j150, j151, j152, j153, j154, j155, j156, j157, j158, j159, j160, j161, j162, j163, j164, j165, j166, j167, j168, j169, j170, j171, j172, j173,
            j174, j175, j176, j177, j178, j179, j180, j181, j182, j183, j184, j185, j186, j187, j188, j189, j190, j191, j192, j193, j194, j195, j196, j197, j198, j199, j200, j201, j202, j203, j204, j205, j206, j207, j208, j209, j210, j211, j212

        };
        for (int i = 0; i < label.length; i++) {
            label[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/true1x.png"))); // NOI18N
            label[i].setText(String.valueOf(i + 1));
            label[i].repaint();
        }

        ResultSet res;
        res = modelo.MostarOcupacionActual();

        while (res.next() == true) {

            for (int i = 0; i < label.length; i++) {

                if (String.valueOf(i + 1).equals(res.getString("parsela"))) {
                    label[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/false1x.png"))); // NOI18N
                    label[i].setText(String.valueOf(i + 1));
                    label[i].repaint();

                }

            }

            //    jLabel6.paintImmediately(jLabel6.getVisibleRect());
        }
    }

    public void traerinfo(int parsela) throws SQLException {
        Modelo modelo = new Modelo();
        ResultSet res;

        res = modelo.traerinfo(parsela);

        int i = 0;
        while (res.next()) {

            javax.swing.JOptionPane.showMessageDialog(this, " documento : '" + res.getString("documento") + "'" + "\n"
                    + "nombre: " + "'" + res.getString("nombre") + "'" + "\n"
                    + "categoria: " + "'" + res.getString("categoria") + "'" + "\n"
                    + "acompañantes: " + "'" + res.getString("acomp") + "'" + "\n"
                    + "importe: " + "'" + res.getString("importe") + "'" + "\n"
                    + "fecha de ingreso: " + "'" + res.getString("fecha_ingreso") + "'" + "\n"
                    + "fecha de egreso: " + "'" + res.getString("fecha_egreso") + "'" + "\n",
                    "Persona nro " + (i + 1), javax.swing.JOptionPane.INFORMATION_MESSAGE);
            i++;

        }

        if (i == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encuentra nadie en la parcela seleccionada.\n Intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void Tabla() throws SQLException {
        ResultSet res_1;
        res_1 = controlador.MostarOcupacionActual();

        Busqueda(res_1);
    }

    public void Busqueda(ResultSet res) throws SQLException {
        DefaultTableModel modelo = new DefaultTableModel();

        ArrayList<Object> nombrecolumna = new ArrayList<Object>();
        nombrecolumna.add("Documento");
        nombrecolumna.add("Nombre");
        nombrecolumna.add("Categoria");
        nombrecolumna.add("Importe");
        nombrecolumna.add("Fecha de egreso");
        nombrecolumna.add("Fecha de ingreso");
        nombrecolumna.add("Parcela");

        for (Object columna : nombrecolumna) {
            modelo.addColumn(columna);
        }

        this.tabla_egreso.setModel(modelo);

        while (res.next()) {

            String documento = res.getString("documento");
            String nombre = res.getString("nombre");
            String categoria = res.getString("categoria");
            String importe = String.valueOf(res.getFloat("importe"));
            String fecha_egreso = res.getString("fecha_egreso");
            String fecha_ingreso = res.getString("fecha_ingreso");
            String parcela = res.getString("parsela");
            String tab[] = {documento, nombre, categoria, importe, fecha_egreso, fecha_ingreso, parcela};
            modelo.addRow(tab);

        }

        tabla_egreso.setModel(modelo);
    }
    private void jLabel32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MousePressed
        try {
            buscar_egreso();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel32MousePressed

    private void jLabel33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MousePressed

        try {
            Tabla();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel33MousePressed

    private void dni_buscadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dni_buscadoKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                buscar_egreso();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (evt.getKeyChar() == KeyEvent.VK_SUBTRACT) {
            dni_buscado.setText(null);
            try {
                Tabla();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_dni_buscadoKeyPressed

    private void nombre_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_pKeyPressed

    }//GEN-LAST:event_nombre_pKeyPressed
    public void eliminar_acampante() throws SQLException {
        int fila = tabla_egreso.getSelectedRow();
        String valor = tabla_egreso.getValueAt(fila, 0).toString();
        JFrame jFrame = new JFrame();
        int result = JOptionPane.showConfirmDialog(jFrame, "Este es el acampante que desea eliminar");
        if (result == 0) {
            int control = modelo.EliminarAcampante(valor);
            if (control == 1) {
                Component rootPane = null;

                JOptionPane.showMessageDialog(rootPane, "Se elimino correctamente.");
                Tabla();

            } else {
                Component rootPane = null;
                JOptionPane.showMessageDialog(rootPane, "Ocurrio un problema, vuelva a intentar");
            }

        }
        try {
            prueba();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void jLabel34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MousePressed

        try {
            eliminar_acampante();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error, ingrese un acampante de la lista.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jLabel34MousePressed

    private void tabla_egresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_egresoKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                eliminar_acampante();

            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_tabla_egresoKeyPressed

    private void pasar_diaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_diaKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Boton_ingreso_alumno();
        }
    }//GEN-LAST:event_pasar_diaKeyPressed

    private void ParcelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ParcelaKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            Boton_ingreso_alumno();
        }
    }//GEN-LAST:event_ParcelaKeyPressed

    private void fecha_egresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egresoKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {

                int c = controlador.Controldnirepetidoingreso(documento_e.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_e.getText());

                if (documento_e.getText().equals("") || nombre_e.getText().equals("")) {
                    throw new Exception();
                }
                if (c == 0 && b == 0) {
                    if (pasar_dia.isSelected()) {

                        ingre_dia("alumno", documento_e.getText(), nombre_e.getText(), 0);
                    } else {
                        ingre();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_fecha_egresoKeyPressed

    private void fecha_ingresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingresoKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {

                int c = controlador.Controldnirepetidoingreso(documento_e.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_e.getText());

                if (documento_e.getText().equals("") || nombre_e.getText().equals("")) {
                    throw new Exception();
                }
                if (c == 0 && b == 0) {
                    if (pasar_dia.isSelected()) {

                        ingre_dia("alumno", documento_e.getText(), nombre_e.getText(), 0);
                    } else {
                        ingre();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_fecha_ingresoKeyPressed

    private void Parsela_aKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Parsela_aKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            botoningresoaportante();
        }
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }

    }//GEN-LAST:event_Parsela_aKeyPressed

    private void fecha_ingreso_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingreso_p1KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int c = controlador.Controldnirepetidoingreso(documento_a.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_a.getText());

                if (documento_a.getText().equals("") || nombre_a.getText().equals("") || apellido_a.getText().isEmpty() || cod_aportante.getText().isEmpty()) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia1.isSelected()) {
                        ingre_dia("aportante", documento_a.getText(), nombre_a.getText(), 0);
                    } else {
                        ingre_a();
                    }

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_fecha_ingreso_p1KeyPressed

    private void fecha_egreso_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egreso_p1KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            botoningresoaportante();
        }
    }//GEN-LAST:event_fecha_egreso_p1KeyPressed

    private void pasar_dia1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_dia1KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int c = controlador.Controldnirepetidoingreso(documento_a.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_a.getText());

                if (documento_a.getText().equals("") || nombre_a.getText().equals("") || apellido_a.getText().isEmpty() || cod_aportante.getText().isEmpty()) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia1.isSelected()) {
                        ingre_dia("aportante", documento_a.getText(), nombre_a.getText(), 0);
                    } else {
                        ingre_a();
                    }

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_pasar_dia1KeyPressed

    private void Boton_ingreso_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Boton_ingreso_pKeyPressed

    }//GEN-LAST:event_Boton_ingreso_pKeyPressed

    private void pasar_dia2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_dia2KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {

            Boton_ingreso_particular();
        }
    }//GEN-LAST:event_pasar_dia2KeyPressed

    private void Parsela_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Parsela_pKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {

            Boton_ingreso_particular();
        }
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_Parsela_pKeyPressed

    private void fecha_ingreso_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingreso_pKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {

            try {
                int c = controlador.Controldnirepetidoingreso(documento_p.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_p.getText());

                if (documento_p.getText().equals("") || nombre_p.getText().equals("")) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia2.isSelected()) {

                        ingre_dia("particular", documento_p.getText(), nombre_p.getText(), Integer.parseInt(acomp_particular.getText()));
                    } else {
                        ingre_p();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }//GEN-LAST:event_fecha_ingreso_pKeyPressed

    private void fecha_egreso_pKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egreso_pKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {

            try {
                int c = controlador.Controldnirepetidoingreso(documento_p.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_p.getText());

                if (documento_p.getText().equals("") || nombre_p.getText().equals("")) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia2.isSelected()) {

                        ingre_dia("particular", documento_p.getText(), nombre_p.getText(), Integer.parseInt(acomp_particular.getText()));
                    } else {
                        ingre_p();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_fecha_egreso_pKeyPressed
    public void eliminarvehiculo() throws SQLException {
        int fila = tabla_vehiculo.getSelectedRow();
        String valor = tabla_vehiculo.getValueAt(fila, 1).toString();
        JFrame jFrame = new JFrame();
        int result = JOptionPane.showConfirmDialog(jFrame, "Este es el vehiculo que desea eliminar");
        if (result == 0) {
            int control = modelo.eliminarvehiculo(valor);
            if (control == 1) {
                Component rootPane = null;

                JOptionPane.showMessageDialog(rootPane, "Se elimino correctamente.");
                BusquedaVehiculos();

            } else {
                Component rootPane = null;
                JOptionPane.showMessageDialog(rootPane, "Ocurrio un problema, vuelva a intentar");
            }

        }
    }
    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        try {
            eliminarvehiculo();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel34MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseReleased

    }//GEN-LAST:event_jLabel34MouseReleased

    private void tabla_vehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_vehiculoKeyPressed
        try {
            eliminarvehiculo();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tabla_vehiculoKeyPressed
    public void imprimirtiketdia(String categoria, float importe, String nombre, String documento, int acomp, float tarifa) throws SQLException {
        LocalTime HoraActual = LocalTime.now();
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                // .CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Categoria: " + categoria + "\n" + "Hora-ingreso: " + HoraActual.format(f) + "\n" + "Acompañantes: " + acomp + "\n" + "Solo por el dia\nVALIDO HASTA HOY A LAS 23HS\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifa + "*" + (acomp + 1) + "\n")
                .EscribirTexto("_____________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + (tarifa * (acomp + 1)) + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {
            tiket.imprimirEn("impresora termica");
            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketdiainvitado(String categoria, float importe, String nombre, String documento, int acomp, float tarifa) throws SQLException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }
        LocalTime HoraActual = LocalTime.now();
        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre del Aportante: " + nombre_a1.getText() + "        DNI: " + documento_a1.getText() + "\n" + "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Categoria: " + categoria + "\n" + "Hora-ingreso: " + HoraActual.format(f) + "\n" + "Acompañantes: " + acomp + "\n" + "Solo por el dia\nVALIDO HASTA HOY A LAS 23HS\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifa + "*" + (acomp + 1) + "\n")
                .EscribirTexto("_____________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + (tarifa * (acomp + 1)) + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {
            tiket.imprimirEn("impresora termica");
            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketdiaaportante(String nombre, String documento, int familiaress) throws SQLException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }
        LocalTime HoraActual = LocalTime.now();
        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Categoria: Aportante" + "\n" + "Hora-ingreso: " + HoraActual.format(f) + "\n" + "Familiares: " + familiaress + "\n" + "Solo por el dia\nVALIDO HASTA HOY A LAS 23HS\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $0" + "\n")
                .EscribirTexto("_____________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $0" + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {
            tiket.imprimirEn("impresora termica");
            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketacampanteaportante(String fecha_ingreso, String fecha_egreso, String nombre, String documento, int familiaress) throws SQLException, java.text.ParseException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        LocalTime HoraActual = LocalTime.now();
        // Aquí tu  serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Categoria: Aportante" + "\n" + "Familiares: " + familiaress + "\n" + "Fecha-ingreso: " + fecha_ingreso + "\n" + "Fecha-egreso: " + fecha_egreso + "\nVALIDO HASTA:" + fecha_egreso + " A LAS 10am\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $0" + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $0" + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el QR y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {

            tiket.imprimirEn("impresora termica");

            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketdiaotros(String descripcion, float importe, String nombre, String documento, int acomp, float tarifa) throws SQLException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }
        LocalTime HoraActual = LocalTime.now();
        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Descripcion: " + descripcion + "\n" + "Hora-ingreso: " + HoraActual.format(f) + "\n" + "Acompañantes: " + acomp + "\n" + "Solo por el dia\nVALIDO HASTA HOY A LAS 23HS\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifa + "*" + (acomp + 1) + "\n")
                .EscribirTexto("_____________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + (tarifa * (acomp + 1)) + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {
            tiket.imprimirEn("impresora termica");
            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketacampanteotros(String fecha_ingreso, String fecha_egreso, String descripcion, float importe, String nombre, String documento, int acomp, float tarifas) throws SQLException, java.text.ParseException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        LocalTime HoraActual = LocalTime.now();
        // Aquí tu  serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Descripcion:" + descripcion + "\n" + "Acompañantes: " + acomp + "\n" + "Fecha-ingreso: " + fecha_ingreso + "\n" + "Fecha-egreso: " + fecha_egreso + "\nVALIDO HASTA:" + fecha_egreso + " A LAS 10am\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifas + "*" + (acomp + 1) + " (x dia)" + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + importe + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el QR y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {

            tiket.imprimirEn("impresora termica");

            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketacampanteinvitado(String fecha_ingreso, String fecha_egreso, String categoria, float importe, String nombre, String documento, int acomp, float tarifas) throws SQLException, java.text.ParseException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        LocalTime HoraActual = LocalTime.now();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre del Aportante: " + nombre_a1.getText() + "\n" + "DNI APORTANTE: " + documento_a1.getText() + "\n" + "Nombre: " + nombre + "                   DNI: " + documento + "\n" + "Categoria:" + categoria + "\n" + "Acompañantes: " + acomp + "\n" + "Fecha-ingreso: " + fecha_ingreso + "\n" + "Fecha-egreso: " + fecha_egreso + "\nVALIDO HASTA:" + fecha_egreso + " A LAS 10am\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifas + "*" + (acomp + 1) + " (x dia)" + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + importe + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el QR y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {

            tiket.imprimirEn("impresora termica");

            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketacampante(String fecha_ingreso, String fecha_egreso, String categoria, float importe, String nombre, String documento, int acomp, float tarifas) throws SQLException, java.text.ParseException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        LocalTime HoraActual = LocalTime.now();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + documento + "\n" + "Categoria:" + categoria + "\n" + "Acompañantes: " + acomp + "\n" + "Fecha-ingreso: " + fecha_ingreso + "\n" + "Fecha-egreso: " + fecha_egreso + "\nVALIDO HASTA:" + fecha_egreso + " A LAS 10am\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifas + "*" + (acomp + 1) + " (x dia)" + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + importe + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el QR y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {

            tiket.imprimirEn("impresora termica");

            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }
    private void documento_aKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_aKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_documento_aKeyPressed

    private void documento_p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_p1ActionPerformed

    }//GEN-LAST:event_documento_p1ActionPerformed

    private void documento_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_p1KeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_documento_p1KeyPressed

    private void nombre_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_p1KeyPressed

    }//GEN-LAST:event_nombre_p1KeyPressed

    private void fecha_ingreso_p2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingreso_p2KeyPressed

    }//GEN-LAST:event_fecha_ingreso_p2KeyPressed

    private void fecha_egreso_p2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egreso_p2KeyPressed

    }//GEN-LAST:event_fecha_egreso_p2KeyPressed

    private void Obtener3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Obtener3MousePressed

        try {

            if (!pasar_dia3.isSelected()) {
                if (!Parsela_p1.getText().isEmpty()) {

                    float importe = 0;

                    if (Integer.parseInt(Parsela_p1.getText()) >= 1 && Integer.parseInt(Parsela_p1.getText()) <= 4) {
                        importe = ((int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2)) * Main.tarifa_acampar_invitados;

                        tarifa3.setText(String.format("%.2f", importe));
                    } else {

                        if (Integer.parseInt(acomp_invitados.getText()) <= 5) {

                            if (Integer.parseInt(acomp_invitados.getText()) == 0) {

                                importe = (Main.tarifa_acampar_invitados * ((int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2)));
                                tarifa3.setText(String.format("%.2f", importe));
                            } else {

                                importe = ((int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2)) * Main.tarifa_acampar_invitados;
                                tarifa3.setText(String.format("%.2f", importe * (Integer.parseInt(acomp_invitados.getText()) + 1)));

                            }
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(this, "Solo se permite hasta 5 invitados por aportante.\nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        }
                    }
                }

            } else {
                if (Integer.parseInt(acomp_invitados.getText()) <= 5) {

                    if (Integer.parseInt(acomp_invitados.getText()) == 0) {

                        tarifa3.setText(String.format("%.2f", Main.tarifa_dia_invitados));

                    } else {
                        tarifa3.setText(String.format("%.2f", Main.tarifa_dia_invitados * (Integer.parseInt(acomp_invitados.getText()) + 1)));

                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Solo se permite hasta 5 invitados por aportante.\nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }
            }
        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Ingreso una letra donde se espera numero \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.NumberFormatException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Especifique la parcela. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NullPointerException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Obtener3MousePressed

    private void tarifa3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifa3ActionPerformed

    }//GEN-LAST:event_tarifa3ActionPerformed
    public void RegistrarIngresoInvitados() throws java.text.ParseException, java.lang.NullPointerException, SQLException {
        try {
            LocalTime HoraActual = LocalTime.now();
            float importe = 0;
            int c;

            if (Integer.parseInt(acomp_invitados.getText()) == 0) {
                importe = (int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2) * Main.tarifa_acampar_invitados;
                tarifa3.setText(String.format("%.2f", importe));
            } else {
                importe = (int) cant_dias(fecha_ingreso_p2, fecha_egreso_p2) * (Integer.parseInt(acomp_invitados.getText()) + 1) * Main.tarifa_acampar_invitados;
            }

            if (fecha_egreso_p2.getDate() != null && fecha_ingreso_p2.getDate() != null) {

                c = controlador.IngresoParticular(documento_p1.getText(), nombre_p1.getText(), "Invitado", calc_fecha(fecha_ingreso_p2), calc_fecha(fecha_egreso_p2), Parsela_p1.getText(), importe, Integer.parseInt(acomp_invitados.getText()),HoraActual.format(f));

                if (c != 1) {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
                    String minutos = String.valueOf(calendario.get(Calendar.MINUTE));

                    String hora_actual = hora + ":" + minutos;

                    if (Integer.parseInt(acomp_invitados.getText()) == 0) {
                        modelo.InsertarRegistro(Login.usuario, "ha ingresado un nuevo invitado acampante", Main.DiaActual, HoraActual.format(f), 0);
                    } else {
                        modelo.InsertarRegistro(Login.usuario, "ha ingresado " + String.format("%02d", (Integer.parseInt(acomp_invitados.getText()) + 1)) + " nuevo/s invitado/s acampante/s.", Main.DiaActual, HoraActual.format(f), 8);
                    }

                    if (Integer.parseInt(acomp_invitados.getText()) == 0) {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja invitado: " + importe);
                    } else {
                        modelo.insertardinerocaja(importe);
                        System.out.println("importe en caja con acampañantes invitado: " + importe);
                    }

                    Component jFrame = null;
                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {

                        if (!pasar_dia3.isSelected()) {

                            imprimirtiketacampanteinvitado(calc_fecha(fecha_ingreso_p2), calc_fecha(fecha_egreso_p2), "Invitado", importe, nombre_p1.getText(), documento_p1.getText(), Integer.parseInt(acomp_invitados.getText()), Main.tarifa_acampar_invitados);
                        } else {

                            importe = Main.tarifa_dia_invitados;
                            imprimirtiketdia("Invitado", importe, nombre_p1.getText(), documento_p1.getText(), Integer.parseInt(acomp_invitados.getText()), importe);
                        }
                    }
                    setearnullinvitado();

                }

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe de ingresar una fecha de ingreso y egreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.lang.NullPointerException e) {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe completar todos los campos de ingreso.\n Error.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public void ingre_i() throws NullPointerException, SQLException {

        if (!documento_p1.getText().isEmpty() || !nombre_p1.getText().isEmpty()) {

            int parsela = 0;
            try {

                try {
                    parsela = Integer.parseInt(Parsela_p1.getText());
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("es un string y no se puede pasar");
                }

                //control que parsela este entre 128 y 1
                if (parsela <= 212 && parsela >= 1) {

                    RegistrarIngresoInvitados();

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No se pudo registrar, Ingreso una parsela que no se encuentra en la base de datos.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (java.text.ParseException ex) {
                Logger.getLogger(Ingre.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (java.lang.NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ingreso un texto donde se esperaba un numero.", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "ERROR, complete todos los campos antes de registrar un ingreso. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void botoningresoinvitado() {
        try {
            if (controlador.ControlarCajaAbierta() == 1) {
                int c = controlador.Controldnirepetidoingreso(documento_p1.getText());
                int b = controlador.Controldnirepetidoingresodiario(documento_p1.getText());

                if (documento_p1.getText().equals("") || nombre_p1.getText().equals("")) {
                    throw new Exception();
                }

                if (c == 0 && b == 0) {
                    if (pasar_dia3.isSelected()) {

                        ingre_dia("invitado", documento_p1.getText(), nombre_p1.getText(), Integer.parseInt(acomp_invitados.getText()));
                    } else {
                        ingre_i();
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ya se encuentra un acampante con ese dni en el camping.", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe abrir la caja primero", "ERROR",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            Tabla();

        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Boton_ingreso_p1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingreso_p1MousePressed
        botoningresoinvitado();
        try {
            prueba();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Boton_ingreso_p1MousePressed

    private void Boton_ingreso_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Boton_ingreso_p1KeyPressed

    }//GEN-LAST:event_Boton_ingreso_p1KeyPressed

    private void Parsela_p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Parsela_p1ActionPerformed

    }//GEN-LAST:event_Parsela_p1ActionPerformed

    private void Parsela_p1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Parsela_p1KeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_Parsela_p1KeyPressed

    private void jPanel23ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel23ComponentHidden

    }//GEN-LAST:event_jPanel23ComponentHidden

    private void pasar_dia3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasar_dia3MousePressed

    }//GEN-LAST:event_pasar_dia3MousePressed

    private void pasar_dia3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_dia3ActionPerformed
        if (pasar_dia3.isSelected()) {
            fecha_ingreso_p2.setEnabled(false);
            fecha_egreso_p2.setEnabled(false);
            Parsela_p1.setEnabled(false);
        } else {
            fecha_ingreso_p2.setEnabled(true);
            fecha_egreso_p2.setEnabled(true);
            Parsela_p1.setEnabled(true);
        }

    }//GEN-LAST:event_pasar_dia3ActionPerformed

    private void pasar_dia3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_dia3KeyPressed

    }//GEN-LAST:event_pasar_dia3KeyPressed

    private void familiaresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_familiaresKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");
            familiares.setText("0");

        }
    }//GEN-LAST:event_familiaresKeyPressed

    private void cod_aportanteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cod_aportanteKeyPressed

    }//GEN-LAST:event_cod_aportanteKeyPressed

    private void acomp_particularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_acomp_particularKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_acomp_particularKeyPressed

    private void documento_otrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_otrosActionPerformed


    }//GEN-LAST:event_documento_otrosActionPerformed

    private void documento_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_otrosKeyPressed

        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_documento_otrosKeyPressed

    private void nombre_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_otrosKeyPressed

    }//GEN-LAST:event_nombre_otrosKeyPressed

    private void fecha_ingreso_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingreso_otrosKeyPressed

    }//GEN-LAST:event_fecha_ingreso_otrosKeyPressed

    private void fecha_egreso_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egreso_otrosKeyPressed

    }//GEN-LAST:event_fecha_egreso_otrosKeyPressed

    private void Obtener4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Obtener4MousePressed

        try {

            if (!pasar_dia4.isSelected()) {
                if (!Parcela_otros.getText().isEmpty()) {

                    float importe = 0;

                    if (Integer.parseInt(Parcela_otros.getText()) >= 1 && Integer.parseInt(Parcela_otros.getText()) <= 4) {
                        importe = ((int) cant_dias(fecha_ingreso_otros, fecha_egreso_otros)) * Float.parseFloat(tarifa_otros.getText());

                        tarifa4.setText(String.format("%.2f", importe));
                    } else {

                        if (Integer.parseInt(acomp_otros.getText()) == 0) {

                            importe = (Float.parseFloat(tarifa_otros.getText()) * ((int) cant_dias(fecha_ingreso_otros, fecha_egreso_otros)));
                            tarifa4.setText(String.format("%.2f", importe));
                            System.out.println("importe::" + importe);

                        } else {

                            importe = ((int) cant_dias(fecha_ingreso_otros, fecha_egreso_otros)) * Float.parseFloat(tarifa_otros.getText());
                            tarifa4.setText(String.format("%.2f", importe * (Integer.parseInt(acomp_otros.getText()) + 1)));

                        }

                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Especifique la parcela. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }

            } else {

                if (Integer.parseInt(acomp_otros.getText()) == 0) {

                    tarifa4.setText(String.format("%.2f", Float.parseFloat(tarifa_otros.getText())));

                } else {
                    tarifa4.setText(String.format("%.2f", (Float.parseFloat(tarifa_otros.getText())) * (Integer.parseInt(acomp_otros.getText()) + 1)));

                }

            }
        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Ingreso una letra donde se espera numero \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.NumberFormatException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Especifique la parcela. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NullPointerException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Obtener4MousePressed

    private void tarifa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifa4ActionPerformed

    }//GEN-LAST:event_tarifa4ActionPerformed

    private void Boton_ingreso_p2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingreso_p2MousePressed

        Boton_ingreso_otro();
        try {
            prueba();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Boton_ingreso_p2MousePressed

    private void Boton_ingreso_p2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Boton_ingreso_p2KeyPressed

    }//GEN-LAST:event_Boton_ingreso_p2KeyPressed

    private void Parcela_otrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Parcela_otrosActionPerformed

    }//GEN-LAST:event_Parcela_otrosActionPerformed

    private void Parcela_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Parcela_otrosKeyPressed

    }//GEN-LAST:event_Parcela_otrosKeyPressed

    private void jPanel26ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel26ComponentHidden

    }//GEN-LAST:event_jPanel26ComponentHidden

    private void pasar_dia4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasar_dia4MousePressed

    }//GEN-LAST:event_pasar_dia4MousePressed

    private void pasar_dia4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_dia4ActionPerformed
        if (pasar_dia4.isSelected()) {
            fecha_ingreso_otros.setEnabled(false);
            fecha_egreso_otros.setEnabled(false);
            Parcela_otros.setEnabled(false);
        } else {
            Parcela_otros.setEnabled(true);
            fecha_ingreso_otros.setEnabled(true);
            fecha_egreso_otros.setEnabled(true);
        }
    }//GEN-LAST:event_pasar_dia4ActionPerformed

    private void pasar_dia4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_dia4KeyPressed

    }//GEN-LAST:event_pasar_dia4KeyPressed

    private void tarifa_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tarifa_otrosKeyPressed

        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }


    }//GEN-LAST:event_tarifa_otrosKeyPressed

    private void acomp_otrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_acomp_otrosKeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
    }//GEN-LAST:event_acomp_otrosKeyPressed

    private void Documento_a2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Documento_a2ActionPerformed

    }//GEN-LAST:event_Documento_a2ActionPerformed

    private void Documento_a2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Documento_a2KeyPressed
        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                buscaraportanteinvitado();
            } catch (SQLException ex) {
                Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_Documento_a2KeyPressed
    public void buscaraportanteinvitado() throws SQLException {
        ResultSet res;
        res = controlador.BuscarAportante(Documento_a2.getText());
        if (res.next() == true) {
            nombre_a1.setText(res.getString("nombre"));
            documento_a1.setText(res.getString("doc"));
            cod_aportante1.setText(res.getString("cod_aportante"));
            apellido_a1.setText(res.getString("apellido"));
            nombre_p1.setEnabled(true);
            documento_p1.setEnabled(true);

        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontro el documento solicitado.\n Intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void Buscar_a2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Buscar_a2MousePressed
        try {
            buscaraportanteinvitado();
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_Buscar_a2MousePressed

    private void documento_a1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_a1KeyPressed

    }//GEN-LAST:event_documento_a1KeyPressed

    private void cod_aportante1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cod_aportante1KeyPressed

    }//GEN-LAST:event_cod_aportante1KeyPressed

    private void descripcion_otrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descripcion_otrosActionPerformed

    }//GEN-LAST:event_descripcion_otrosActionPerformed

    private void acomp_invitadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acomp_invitadosActionPerformed

    }//GEN-LAST:event_acomp_invitadosActionPerformed

    private void acomp_invitadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_acomp_invitadosKeyPressed

        char validador = evt.getKeyChar();

        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");

        }


    }//GEN-LAST:event_acomp_invitadosKeyPressed

    private void menu_ingreso_egresoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_menu_ingreso_egresoKeyPressed

    }//GEN-LAST:event_menu_ingreso_egresoKeyPressed

    private void tarifa_otrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tarifa_otrosMousePressed
        tarifa_otros.selectAll();
    }//GEN-LAST:event_tarifa_otrosMousePressed

    private void acomp_otrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_acomp_otrosMousePressed
        acomp_otros.selectAll();
    }//GEN-LAST:event_acomp_otrosMousePressed

    private void Parcela_otrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Parcela_otrosMousePressed
        Parcela_otros.selectAll();
    }//GEN-LAST:event_Parcela_otrosMousePressed

    private void acomp_invitadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_acomp_invitadosMousePressed
        acomp_invitados.selectAll();
    }//GEN-LAST:event_acomp_invitadosMousePressed

    private void Parsela_p1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Parsela_p1MousePressed
        Parsela_p1.selectAll();
    }//GEN-LAST:event_Parsela_p1MousePressed

    private void acomp_particularMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_acomp_particularMousePressed
        acomp_particular.selectAll();
    }//GEN-LAST:event_acomp_particularMousePressed

    private void Parsela_pMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Parsela_pMousePressed
        Parsela_p.selectAll();
    }//GEN-LAST:event_Parsela_pMousePressed

    private void ParcelaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ParcelaMousePressed
        Parcela.selectAll();
    }//GEN-LAST:event_ParcelaMousePressed

    private void Parsela_aMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Parsela_aMousePressed
        Parsela_a.selectAll();
    }//GEN-LAST:event_Parsela_aMousePressed

    private void familiaresMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_familiaresMousePressed
        familiares.selectAll();
    }//GEN-LAST:event_familiaresMousePressed

    private void fecha_ingreso_p3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_ingreso_p3KeyPressed

    }//GEN-LAST:event_fecha_ingreso_p3KeyPressed

    private void fecha_egreso_p3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fecha_egreso_p3KeyPressed

    }//GEN-LAST:event_fecha_egreso_p3KeyPressed

    private void Obtener5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Obtener5MousePressed

        float importe = 0;
        try {

            if (!pasar_dia5.isSelected()) {

                importe = (Main.tarifa_casillas * ((int) cant_dias(fecha_ingreso_p3, fecha_egreso_p3)));
                tarifa5.setText(String.format("%.2f", importe));
                System.out.println("importe::" + importe);

            } else {

                importe = Main.tarifa_casillas;
                tarifa5.setText(String.format("%.2f", importe));

            }
        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Ingreso una letra donde se espera numero \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (java.lang.NumberFormatException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Especifique la parcela. \nIntente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NullPointerException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Obtener5MousePressed

    public void RegistrarCasilla() throws java.text.ParseException, SQLException {
        LocalTime HoraActual = LocalTime.now();
        float importe = 0;
        if (controlador.ControlarCajaAbierta() == 1) {
            if (!pasar_dia5.isSelected()) {

                importe = (Main.tarifa_casillas * ((int) cant_dias(fecha_ingreso_p3, fecha_egreso_p3)));

                if (!patente_c.getText().isEmpty()) {
                    Component jFrame = null;
                    modelo.insertarvehiculo(patente_c.getText(), "Casilla/Motorhome", controlador.calcularimportevehiculo(), calc_fecha(fecha_ingreso_p3), calc_fecha(fecha_egreso_p3), "10");

                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {
                        imprimirtiketcasillaacampante(calc_fecha(fecha_ingreso_p3), calc_fecha(fecha_egreso_p3), patente_c.getText(), importe, Main.tarifa_casillas);
                    }

                    //insertar en caja
                    modelo.insertardinerocaja(importe);
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado un/a nuev@ casilla/motorhome acampante.", Main.DiaActual, HoraActual.format(f), 0);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            } else {

                if (!patente_c.getText().isEmpty()) {

                    modelo.insertarvehiculo(patente_c.getText(), "Casilla/Motorhome", controlador.calcularimportevehiculo(), LocalDate.now().toString(), LocalDate.now().toString(), "22");
                    importe = Main.tarifa_casillas;

                    Component jFrame = null;
                    int result = JOptionPane.showConfirmDialog(jFrame, "Registro exitoso, desea imprimir?");

                    if (result == 0) {
                        imprimirtiketcasilladia(patente_c.getText(), importe);
                    }
                    modelo.insertardinerocaja(importe);
                    modelo.InsertarRegistro(Login.usuario, "ha ingresado un/a nuev@ casilla/motorhome por el dia", Main.DiaActual, HoraActual.format(f), 0);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos", "ERROR",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }

            }

            patente_c.setText(null);
            BusquedaVehiculos();

        } else {

            javax.swing.JOptionPane.showMessageDialog(this, "Debe abrir la caja primero", "ERROR",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

        }

    }

    public void imprimirtiketcasillaacampante(String fecha_ingreso, String fecha_egreso, String patente, float importe, float tarifas) throws SQLException, java.text.ParseException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        LocalTime HoraActual = LocalTime.now();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }

        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Patente: " + patente + "        Descripcion: Casilla/Motorhome" + "\n" + "Fecha ingreso: " + fecha_ingreso + "\n" + "Fecha egreso: " + fecha_egreso + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifas + " (x dia)" + "\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + importe + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {

            tiket.imprimirEn("impresora termica");

            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    public void imprimirtiketcasilladia(String patente, float tarifa) throws SQLException {
        String hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendario.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendario.get(Calendar.SECOND));
        String n_serial = "0";
        String hora_actual = hora + ":" + minutos;
        ResultSet res = modelo.mostrarregistros1();
        // Aquí tu serial en caso de tener uno
        if (res.next()) {
            n_serial = res.getString("id");
        }
        LocalTime HoraActual = LocalTime.now();
        String serial;
        String nro_serial = String.format("%08d", Integer.parseInt(n_serial));

        ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

        tiket.Iniciar()
                .Corte(1)
                .DeshabilitarElModoDeCaracteresChinos()
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                .Feed(1)
                .EscribirTexto("SAEBU\n")
                .EscribirTexto("Camping Universitario\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                .Feed(1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                .EscribirTexto("______________________________________________\n")
                .TextoSegunPaginaDeCodigos(2, "cp850", "Patente: " + patente + "\n" + "Hora-ingreso: " + HoraActual.format(f) + "\n" + "Solo por el dia\n")
                .EscribirTexto("______________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("Sub total: $" + tarifa + "\n")
                .EscribirTexto("_____________________________________________\n")
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                .EscribirTexto("TOTAL:  $" + tarifa + "\n")
                .EstablecerEnfatizado(true)
                .EstablecerTamanoFuente(1, 1)
                .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                .TextoSegunPaginaDeCodigos(2, "cp850", "¡Bienvenido, que tenga un buen dia!\nEscanee el qr y visite la web de SAEBU")
                .Feed(1)
                .ImprimirCodigoQr("http://saebu.unsl.edu.ar/camping-universitario", 160, ConectorPluginV3.RECUPERACION_QR_MEJOR,
                        ConectorPluginV3.TAMANO_IMAGEN_NORMAL)
                .Feed(1)
                .EstablecerTamanoFuente(1, 1)
                .EscribirTexto("PROHIBIDO EL INGRESO CON ANIMALES,\n NO HAY DEVOLUCIONES\nSistema integral de gestion de Acampantes")
                .Feed(3)
                .Corte(1)
                .Pulso(48, 30, 120);

        try {
            tiket.imprimirEn("impresora termica");
            System.out.println("Impreso correctamente");
        } catch (Exception e) {
            System.out.println("Error imprimiendo: " + e.getMessage());
        }
    }

    private void tarifa5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tarifa5ActionPerformed

    }//GEN-LAST:event_tarifa5ActionPerformed

    private void Boton_ingreso_p3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Boton_ingreso_p3MousePressed

        try {

            RegistrarCasilla();

        } catch (java.text.ParseException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Ingre.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_Boton_ingreso_p3MousePressed

    private void Boton_ingreso_p3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Boton_ingreso_p3KeyPressed

    }//GEN-LAST:event_Boton_ingreso_p3KeyPressed

    private void jPanel30ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel30ComponentHidden

    }//GEN-LAST:event_jPanel30ComponentHidden

    private void pasar_dia5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasar_dia5MousePressed

    }//GEN-LAST:event_pasar_dia5MousePressed

    private void pasar_dia5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasar_dia5ActionPerformed

        if (pasar_dia5.isSelected()) {
            fecha_ingreso_p3.setEnabled(false);
            fecha_egreso_p3.setEnabled(false);

        } else {

            fecha_ingreso_p3.setEnabled(true);
            fecha_egreso_p3.setEnabled(true);
        }


    }//GEN-LAST:event_pasar_dia5ActionPerformed

    private void pasar_dia5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasar_dia5KeyPressed


    }//GEN-LAST:event_pasar_dia5KeyPressed

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        /*
        try {
            int i = 1;
            while (i <= 50) {
                BuscarParsela(Integer.toString(i));
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Ocupacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
    }//GEN-LAST:event_jPanel3MouseEntered

    private void j212MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j212MousePressed
        try {
            traerinfo(212);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j212MousePressed

    private void j211MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j211MousePressed
        try {
            traerinfo(211);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j211MousePressed

    private void j210MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j210MousePressed
        try {
            traerinfo(210);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j210MousePressed

    private void j209MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j209MousePressed
        try {
            traerinfo(209);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j209MousePressed

    private void j208MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j208MousePressed
        try {
            traerinfo(208);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j208MousePressed

    private void j207MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j207MousePressed
        try {
            traerinfo(207);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j207MousePressed

    private void j206MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j206MousePressed
        try {
            traerinfo(206);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j206MousePressed

    private void j205MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j205MousePressed
        try {
            traerinfo(205);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j205MousePressed

    private void j204MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j204MousePressed
        try {
            traerinfo(204);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j204MousePressed

    private void j203MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j203MousePressed
        try {
            traerinfo(203);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j203MousePressed

    private void j202MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j202MousePressed
        try {
            traerinfo(202);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j202MousePressed

    private void j201MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j201MousePressed
        try {
            traerinfo(201);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j201MousePressed

    private void j200MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j200MousePressed
        try {
            traerinfo(200);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j200MousePressed

    private void j199MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j199MousePressed
        try {
            traerinfo(199);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j199MousePressed

    private void j198MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j198MousePressed
        try {
            traerinfo(198);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j198MousePressed

    private void j197MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j197MousePressed
        try {
            traerinfo(197);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j197MousePressed

    private void j196MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j196MousePressed
        try {
            traerinfo(196);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j196MousePressed

    private void j195MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j195MousePressed
        try {
            traerinfo(195);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j195MousePressed

    private void j194MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j194MousePressed
        try {
            traerinfo(194);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j194MousePressed

    private void j193MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j193MousePressed
        try {
            traerinfo(193);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j193MousePressed

    private void j192MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j192MousePressed
        try {
            traerinfo(192);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j192MousePressed

    private void j191MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j191MousePressed
        try {
            traerinfo(191);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j191MousePressed

    private void j190MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j190MousePressed
        try {
            traerinfo(190);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j190MousePressed

    private void j189MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j189MousePressed
        try {
            traerinfo(189);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j189MousePressed

    private void j188MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j188MousePressed
        try {
            traerinfo(188);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j188MousePressed

    private void j187MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j187MousePressed
        try {
            traerinfo(187);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j187MousePressed

    private void j186MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j186MousePressed
        try {
            traerinfo(186);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j186MousePressed

    private void j185MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j185MousePressed
        try {
            traerinfo(185);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j185MousePressed

    private void j184MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j184MousePressed
        try {
            traerinfo(184);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j184MousePressed

    private void j183MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j183MousePressed
        try {
            traerinfo(183);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j183MousePressed

    private void j182MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j182MousePressed
        try {
            traerinfo(182);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j182MousePressed

    private void j181MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j181MousePressed
        try {
            traerinfo(181);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j181MousePressed

    private void j180MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j180MousePressed
        try {
            traerinfo(180);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j180MousePressed

    private void j179MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j179MousePressed
        try {
            traerinfo(179);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j179MousePressed

    private void j178MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j178MousePressed
        try {
            traerinfo(178);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j178MousePressed

    private void j177MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j177MousePressed
        try {
            traerinfo(177);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j177MousePressed

    private void j176MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j176MousePressed
        try {
            traerinfo(176);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j176MousePressed

    private void j175MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j175MousePressed
        try {
            traerinfo(175);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j175MousePressed

    private void j174MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j174MousePressed
        try {
            traerinfo(174);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j174MousePressed

    private void j173MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j173MousePressed
        try {
            traerinfo(173);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j173MousePressed

    private void j172MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j172MousePressed
        try {
            traerinfo(172);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j172MousePressed

    private void j171MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j171MousePressed
        try {
            traerinfo(171);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j171MousePressed

    private void j170MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j170MousePressed
        try {
            traerinfo(170);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j170MousePressed

    private void j169MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j169MousePressed
        try {
            traerinfo(169);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j169MousePressed

    private void j168MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j168MousePressed
        try {
            traerinfo(168);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j168MousePressed

    private void j167MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j167MousePressed
        try {
            traerinfo(167);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j167MousePressed

    private void j166MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j166MousePressed
        try {
            traerinfo(166);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j166MousePressed

    private void j165MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j165MousePressed
        try {
            traerinfo(165);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j165MousePressed

    private void j164MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j164MousePressed
        try {
            traerinfo(164);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j164MousePressed

    private void j163MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j163MousePressed
        try {
            traerinfo(163);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j163MousePressed

    private void j162MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j162MousePressed
        try {
            traerinfo(162);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j162MousePressed

    private void j161MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j161MousePressed
        try {
            traerinfo(161);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j161MousePressed

    private void j160MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j160MousePressed
        try {
            traerinfo(160);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j160MousePressed

    private void j159MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j159MousePressed
        try {
            traerinfo(159);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j159MousePressed

    private void j158MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j158MousePressed
        try {
            traerinfo(158);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j158MousePressed

    private void j157MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j157MousePressed
        try {
            traerinfo(157);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j157MousePressed

    private void j156MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j156MousePressed
        try {
            traerinfo(156);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j156MousePressed

    private void j155MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j155MousePressed
        try {
            traerinfo(155);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j155MousePressed

    private void j154MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j154MousePressed
        try {
            traerinfo(154);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j154MousePressed

    private void j153MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j153MousePressed
        try {
            traerinfo(153);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j153MousePressed

    private void j152MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j152MousePressed
        try {
            traerinfo(152);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j152MousePressed

    private void j151MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j151MousePressed
        try {
            traerinfo(151);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j151MousePressed

    private void j150MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j150MousePressed
        try {
            traerinfo(150);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j150MousePressed

    private void j149MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j149MousePressed
        try {
            traerinfo(149);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j149MousePressed

    private void j148MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j148MousePressed
        try {
            traerinfo(148);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j148MousePressed

    private void j147MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j147MousePressed
        try {
            traerinfo(147);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j147MousePressed

    private void j146MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j146MousePressed
        try {
            traerinfo(146);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j146MousePressed

    private void j145MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j145MousePressed
        try {
            traerinfo(145);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j145MousePressed

    private void j144MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j144MousePressed
        try {
            traerinfo(144);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j144MousePressed

    private void j143MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j143MousePressed
        try {
            traerinfo(143);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j143MousePressed

    private void j142MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j142MousePressed
        try {
            traerinfo(142);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j142MousePressed

    private void j141MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j141MousePressed
        try {
            traerinfo(141);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j141MousePressed

    private void j140MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j140MousePressed
        try {
            traerinfo(140);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j140MousePressed

    private void j139MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j139MousePressed
        try {
            traerinfo(139);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j139MousePressed

    private void j138MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j138MousePressed
        try {
            traerinfo(138);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j138MousePressed

    private void j137MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j137MousePressed
        try {
            traerinfo(137);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j137MousePressed

    private void j136MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j136MousePressed
        try {
            traerinfo(136);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j136MousePressed

    private void j135MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j135MousePressed
        try {
            traerinfo(135);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j135MousePressed

    private void j134MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j134MousePressed
        try {
            traerinfo(134);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j134MousePressed

    private void j133MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j133MousePressed
        try {
            traerinfo(133);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j133MousePressed

    private void j132MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j132MousePressed
        try {
            traerinfo(132);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j132MousePressed

    private void j131MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j131MousePressed
        try {
            traerinfo(131);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j131MousePressed

    private void j130MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j130MousePressed
        try {
            traerinfo(130);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j130MousePressed

    private void j129MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j129MousePressed
        try {
            traerinfo(129);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j129MousePressed

    private void boton_rMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_rMousePressed

    }//GEN-LAST:event_boton_rMousePressed

    private void boton_vMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_vMousePressed

    }//GEN-LAST:event_boton_vMousePressed

    private void j128MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j128MousePressed

        try {
            traerinfo(128);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j128MousePressed

    private void j127MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j127MousePressed

        try {
            traerinfo(127);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j127MousePressed

    private void j126MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j126MousePressed

        try {
            traerinfo(126);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j126MousePressed

    private void j125MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j125MousePressed

        try {
            traerinfo(125);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j125MousePressed

    private void j124MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j124MousePressed

        try {
            traerinfo(124);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j124MousePressed

    private void j123MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j123MousePressed

        try {
            traerinfo(123);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j123MousePressed

    private void j122MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j122MousePressed

        try {
            traerinfo(122);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j122MousePressed

    private void j121MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j121MousePressed

        try {
            traerinfo(121);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j121MousePressed

    private void j120MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j120MousePressed

        try {
            traerinfo(120);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j120MousePressed

    private void j119MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j119MousePressed

        try {
            traerinfo(119);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j119MousePressed

    private void j118MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j118MousePressed

        try {
            traerinfo(118);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j118MousePressed

    private void j117MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j117MousePressed

        try {
            traerinfo(117);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j117MousePressed

    private void j116MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j116MousePressed

        try {
            traerinfo(116);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j116MousePressed

    private void j115MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j115MousePressed

        try {
            traerinfo(115);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j115MousePressed

    private void j114MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j114MousePressed

        try {
            traerinfo(114);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j114MousePressed

    private void j113MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j113MousePressed

        try {
            traerinfo(113);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j113MousePressed

    private void j112MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j112MousePressed

        try {
            traerinfo(112);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j112MousePressed

    private void j111MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j111MousePressed

        try {
            traerinfo(111);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j111MousePressed

    private void j110MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j110MousePressed

        try {
            traerinfo(110);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j110MousePressed

    private void j109MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j109MousePressed

        try {
            traerinfo(109);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j109MousePressed

    private void j108MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j108MousePressed

        try {
            traerinfo(108);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j108MousePressed

    private void j107MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j107MousePressed

        try {
            traerinfo(107);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j107MousePressed

    private void j106MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j106MousePressed

        try {
            traerinfo(106);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j106MousePressed

    private void j105MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j105MousePressed

        try {
            traerinfo(105);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j105MousePressed

    private void j104MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j104MousePressed

        try {
            traerinfo(104);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j104MousePressed

    private void j103MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j103MousePressed

        try {
            traerinfo(103);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j103MousePressed

    private void j102MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j102MousePressed

        try {
            traerinfo(102);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j102MousePressed

    private void j101MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j101MousePressed

        try {
            traerinfo(101);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j101MousePressed

    private void j100MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j100MousePressed

        try {
            traerinfo(100);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j100MousePressed

    private void j99MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j99MousePressed

        try {
            traerinfo(99);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j99MousePressed

    private void j98MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j98MousePressed

        try {
            traerinfo(98);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j98MousePressed

    private void j97MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j97MousePressed

        try {
            traerinfo(97);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j97MousePressed

    private void j96MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j96MousePressed

        try {
            traerinfo(96);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j96MousePressed

    private void j95MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j95MousePressed

        try {
            traerinfo(95);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j95MousePressed

    private void j94MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j94MousePressed

        try {
            traerinfo(94);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j94MousePressed

    private void j93MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j93MousePressed

        try {
            traerinfo(93);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j93MousePressed

    private void j92MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j92MousePressed

        try {
            traerinfo(92);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j92MousePressed

    private void j91MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j91MousePressed

        try {
            traerinfo(91);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j91MousePressed

    private void j90MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j90MousePressed

        try {
            traerinfo(90);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j90MousePressed

    private void j89MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j89MousePressed

        try {
            traerinfo(89);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j89MousePressed

    private void j88MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j88MousePressed

        try {
            traerinfo(88);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j88MousePressed

    private void j87MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j87MousePressed

        try {
            traerinfo(87);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j87MousePressed

    private void j86MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j86MousePressed

        try {
            traerinfo(86);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j86MousePressed

    private void j85MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j85MousePressed

        try {
            traerinfo(85);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j85MousePressed

    private void j84MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j84MousePressed

        try {
            traerinfo(84);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j84MousePressed

    private void j83MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j83MousePressed

        try {
            traerinfo(83);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j83MousePressed

    private void j82MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j82MousePressed

        try {
            traerinfo(82);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j82MousePressed

    private void j81MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j81MousePressed

        try {
            traerinfo(81);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j81MousePressed

    private void j80MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j80MousePressed

        try {
            traerinfo(80);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j80MousePressed

    private void j79MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j79MousePressed

        try {
            traerinfo(79);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j79MousePressed

    private void j78MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j78MousePressed

        try {
            traerinfo(78);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j78MousePressed

    private void j77MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j77MousePressed

        try {
            traerinfo(77);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j77MousePressed

    private void j76MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j76MousePressed

        try {
            traerinfo(76);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j76MousePressed

    private void j75MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j75MousePressed

        try {
            traerinfo(75);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j75MousePressed

    private void j74MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j74MousePressed

        try {
            traerinfo(74);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j74MousePressed

    private void j73MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j73MousePressed

        try {
            traerinfo(73);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j73MousePressed

    private void j72MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j72MousePressed

        try {
            traerinfo(72);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j72MousePressed

    private void j71MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j71MousePressed

        try {
            traerinfo(71);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j71MousePressed

    private void j70MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j70MousePressed

        try {
            traerinfo(70);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j70MousePressed

    private void j69MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j69MousePressed

        try {
            traerinfo(69);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j69MousePressed

    private void j68MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j68MousePressed

        try {
            traerinfo(68);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j68MousePressed

    private void j67MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j67MousePressed

        try {
            traerinfo(67);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j67MousePressed

    private void j66MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j66MousePressed

        try {
            traerinfo(66);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j66MousePressed

    private void j65MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j65MousePressed

        try {
            traerinfo(65);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j65MousePressed

    private void j64MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j64MousePressed

        try {
            traerinfo(64);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j64MousePressed

    private void j63MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j63MousePressed

        try {
            traerinfo(63);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j63MousePressed

    private void j62MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j62MousePressed

        try {
            traerinfo(62);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j62MousePressed

    private void j61MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j61MousePressed

        try {
            traerinfo(61);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j61MousePressed

    private void j60MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j60MousePressed

        try {
            traerinfo(60);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j60MousePressed

    private void j59MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j59MousePressed

        try {
            traerinfo(59);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j59MousePressed

    private void j58MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j58MousePressed

        try {
            traerinfo(58);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j58MousePressed

    private void j57MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j57MousePressed

        try {
            traerinfo(57);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j57MousePressed

    private void j56MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j56MousePressed

        try {
            traerinfo(56);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j56MousePressed

    private void j55MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j55MousePressed

        try {
            traerinfo(55);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j55MousePressed

    private void j54MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j54MousePressed

        try {
            traerinfo(54);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j54MousePressed

    private void j53MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j53MousePressed

        try {
            traerinfo(53);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j53MousePressed

    private void j52MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j52MousePressed

        try {
            traerinfo(52);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j52MousePressed

    private void j51MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j51MousePressed

        try {
            traerinfo(51);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j51MousePressed

    private void j47MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j47MousePressed

        try {
            traerinfo(47);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j47MousePressed

    private void j44MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j44MousePressed

        try {
            traerinfo(44);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j44MousePressed

    private void j22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j22MousePressed

        try {
            traerinfo(22);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j22MousePressed

    private void j1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j1MousePressed

        try {
            traerinfo(1);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j1MousePressed

    private void j26MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j26MousePressed

        try {
            traerinfo(26);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j26MousePressed

    private void j28MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j28MousePressed

        try {
            traerinfo(28);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j28MousePressed

    private void j32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j32MousePressed

        try {
            traerinfo(32);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j32MousePressed

    private void j41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j41MousePressed

        try {
            traerinfo(41);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j41MousePressed

    private void j35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j35MousePressed

        try {
            traerinfo(35);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j35MousePressed

    private void j46MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j46MousePressed

        try {
            traerinfo(46);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j46MousePressed

    private void j11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j11MousePressed

        try {
            traerinfo(11);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j11MousePressed

    private void j9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j9MousePressed

        try {
            traerinfo(9);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j9MousePressed

    private void j8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j8MousePressed

        try {
            traerinfo(8);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j8MousePressed

    private void j13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j13MousePressed

        try {
            traerinfo(13);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j13MousePressed

    private void j14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j14MousePressed
        try {
            traerinfo(14);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j14MousePressed

    private void j17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j17MousePressed

        try {
            traerinfo(17);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j17MousePressed

    private void j16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j16MousePressed

        try {
            traerinfo(16);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j16MousePressed

    private void j4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j4MousePressed

        try {
            traerinfo(4);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j4MousePressed

    private void j18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j18MousePressed

        try {
            traerinfo(18);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j18MousePressed

    private void j19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j19MousePressed

        try {
            traerinfo(19);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j19MousePressed

    private void j21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j21MousePressed

        try {
            traerinfo(21);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j21MousePressed

    private void j23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j23MousePressed

        try {
            traerinfo(23);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j23MousePressed

    private void j24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j24MousePressed

        try {
            traerinfo(24);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j24MousePressed

    private void j42MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j42MousePressed

        try {
            traerinfo(42);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j42MousePressed

    private void j30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j30MousePressed

        try {
            traerinfo(30);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j30MousePressed

    private void j43MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j43MousePressed

        try {
            traerinfo(43);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j43MousePressed

    private void j34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j34MousePressed

        try {
            traerinfo(34);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j34MousePressed

    private void j38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j38MousePressed

        try {
            traerinfo(38);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j38MousePressed

    private void j39MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j39MousePressed

        try {
            traerinfo(39);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j39MousePressed

    private void j48MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j48MousePressed

        try {
            traerinfo(48);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j48MousePressed

    private void j36MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j36MousePressed

        try {
            traerinfo(36);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j36MousePressed

    private void j37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j37MousePressed

        try {
            traerinfo(37);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j37MousePressed

    private void j33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j33MousePressed

        try {
            traerinfo(33);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j33MousePressed

    private void j25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j25MousePressed

        try {
            traerinfo(25);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j25MousePressed

    private void j2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j2MousePressed
        try {
            traerinfo(2);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j2MousePressed

    private void j15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j15MousePressed

        try {
            traerinfo(15);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j15MousePressed

    private void j6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j6MousePressed

        try {
            traerinfo(6);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j6MousePressed

    private void j50MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j50MousePressed

        try {
            traerinfo(50);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j50MousePressed

    private void j45MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j45MousePressed

        try {
            traerinfo(45);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j45MousePressed

    private void j49MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j49MousePressed

        try {
            traerinfo(49);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j49MousePressed

    private void j40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j40MousePressed

        try {
            traerinfo(40);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j40MousePressed

    private void j31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j31MousePressed

        try {
            traerinfo(31);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j31MousePressed

    private void j3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j3MousePressed
        try {
            traerinfo(3);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j3MousePressed

    private void j20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j20MousePressed

        try {
            traerinfo(20);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j20MousePressed

    private void j29MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j29MousePressed

        try {
            traerinfo(29);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j29MousePressed

    private void j7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j7MousePressed

        try {
            traerinfo(7);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j7MousePressed

    private void j27MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j27MousePressed

        try {
            traerinfo(27);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j27MousePressed

    private void j5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j5MousePressed

        try {
            traerinfo(5);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j5MousePressed

    private void j12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j12MousePressed

        try {
            traerinfo(12);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j12MousePressed

    private void j10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j10MousePressed

        try {
            traerinfo(10);
        } catch (SQLException ex) {
            Logger.getLogger(OcupacionC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_j10MousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Boton_ingreso;
    private javax.swing.JLabel Boton_ingreso_a;
    private javax.swing.JLabel Boton_ingreso_p;
    private javax.swing.JLabel Boton_ingreso_p1;
    private javax.swing.JLabel Boton_ingreso_p2;
    private javax.swing.JLabel Boton_ingreso_p3;
    private javax.swing.JLabel Buscar;
    private javax.swing.JLabel Buscar_a;
    private javax.swing.JLabel Buscar_a2;
    private javax.swing.JTextField Documento;
    private javax.swing.JTextField Documento_a;
    private javax.swing.JTextField Documento_a2;
    private javax.swing.JButton Obtener;
    private javax.swing.JButton Obtener1;
    private javax.swing.JButton Obtener2;
    private javax.swing.JButton Obtener3;
    private javax.swing.JButton Obtener4;
    private javax.swing.JButton Obtener5;
    private javax.swing.JTextField Parcela;
    private javax.swing.JTextField Parcela_otros;
    private javax.swing.JTextField Parsela_a;
    private javax.swing.JTextField Parsela_p;
    private javax.swing.JTextField Parsela_p1;
    private javax.swing.JTextField acomp_invitados;
    private javax.swing.JTextField acomp_otros;
    private javax.swing.JTextField acomp_particular;
    private javax.swing.JPanel alumnoss;
    private javax.swing.JTextField apellido_a;
    private javax.swing.JTextField apellido_a1;
    private javax.swing.JTextField apellido_e;
    private javax.swing.JPanel aportantes;
    private javax.swing.JLabel boton_r;
    private javax.swing.JLabel boton_v;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField carrera_e;
    private javax.swing.JTextField cod_aportante;
    private javax.swing.JTextField cod_aportante1;
    private javax.swing.JTextField descripcion_otros;
    private javax.swing.JTextField dni_buscado;
    private javax.swing.JTextField documento_a;
    private javax.swing.JTextField documento_a1;
    private javax.swing.JTextField documento_e;
    private javax.swing.JTextField documento_otros;
    private javax.swing.JTextField documento_p;
    private javax.swing.JTextField documento_p1;
    private javax.swing.JPanel egresos;
    private javax.swing.JTextField facultad_e;
    private javax.swing.JTextField familiares;
    private com.toedter.calendar.JDateChooser fecha_egreso;
    private com.toedter.calendar.JDateChooser fecha_egreso_otros;
    private com.toedter.calendar.JDateChooser fecha_egreso_p;
    private com.toedter.calendar.JDateChooser fecha_egreso_p1;
    private com.toedter.calendar.JDateChooser fecha_egreso_p2;
    private com.toedter.calendar.JDateChooser fecha_egreso_p3;
    private com.toedter.calendar.JDateChooser fecha_ingreso;
    private com.toedter.calendar.JDateChooser fecha_ingreso_otros;
    private com.toedter.calendar.JDateChooser fecha_ingreso_p;
    private com.toedter.calendar.JDateChooser fecha_ingreso_p1;
    private com.toedter.calendar.JDateChooser fecha_ingreso_p2;
    private com.toedter.calendar.JDateChooser fecha_ingreso_p3;
    private javax.swing.JPanel invitados;
    private javax.swing.JLabel j1;
    private javax.swing.JLabel j10;
    private javax.swing.JLabel j100;
    private javax.swing.JLabel j101;
    private javax.swing.JLabel j102;
    private javax.swing.JLabel j103;
    private javax.swing.JLabel j104;
    private javax.swing.JLabel j105;
    private javax.swing.JLabel j106;
    private javax.swing.JLabel j107;
    private javax.swing.JLabel j108;
    private javax.swing.JLabel j109;
    private javax.swing.JLabel j11;
    private javax.swing.JLabel j110;
    private javax.swing.JLabel j111;
    private javax.swing.JLabel j112;
    private javax.swing.JLabel j113;
    private javax.swing.JLabel j114;
    private javax.swing.JLabel j115;
    private javax.swing.JLabel j116;
    private javax.swing.JLabel j117;
    private javax.swing.JLabel j118;
    private javax.swing.JLabel j119;
    private javax.swing.JLabel j12;
    private javax.swing.JLabel j120;
    private javax.swing.JLabel j121;
    private javax.swing.JLabel j122;
    private javax.swing.JLabel j123;
    private javax.swing.JLabel j124;
    private javax.swing.JLabel j125;
    private javax.swing.JLabel j126;
    private javax.swing.JLabel j127;
    private javax.swing.JLabel j128;
    private javax.swing.JLabel j129;
    private javax.swing.JLabel j13;
    private javax.swing.JLabel j130;
    private javax.swing.JLabel j131;
    private javax.swing.JLabel j132;
    private javax.swing.JLabel j133;
    private javax.swing.JLabel j134;
    private javax.swing.JLabel j135;
    private javax.swing.JLabel j136;
    private javax.swing.JLabel j137;
    private javax.swing.JLabel j138;
    private javax.swing.JLabel j139;
    private javax.swing.JLabel j14;
    private javax.swing.JLabel j140;
    private javax.swing.JLabel j141;
    private javax.swing.JLabel j142;
    private javax.swing.JLabel j143;
    private javax.swing.JLabel j144;
    private javax.swing.JLabel j145;
    private javax.swing.JLabel j146;
    private javax.swing.JLabel j147;
    private javax.swing.JLabel j148;
    private javax.swing.JLabel j149;
    private javax.swing.JLabel j15;
    private javax.swing.JLabel j150;
    private javax.swing.JLabel j151;
    private javax.swing.JLabel j152;
    private javax.swing.JLabel j153;
    private javax.swing.JLabel j154;
    private javax.swing.JLabel j155;
    private javax.swing.JLabel j156;
    private javax.swing.JLabel j157;
    private javax.swing.JLabel j158;
    private javax.swing.JLabel j159;
    private javax.swing.JLabel j16;
    private javax.swing.JLabel j160;
    private javax.swing.JLabel j161;
    private javax.swing.JLabel j162;
    private javax.swing.JLabel j163;
    private javax.swing.JLabel j164;
    private javax.swing.JLabel j165;
    private javax.swing.JLabel j166;
    private javax.swing.JLabel j167;
    private javax.swing.JLabel j168;
    private javax.swing.JLabel j169;
    private javax.swing.JLabel j17;
    private javax.swing.JLabel j170;
    private javax.swing.JLabel j171;
    private javax.swing.JLabel j172;
    private javax.swing.JLabel j173;
    private javax.swing.JLabel j174;
    private javax.swing.JLabel j175;
    private javax.swing.JLabel j176;
    private javax.swing.JLabel j177;
    private javax.swing.JLabel j178;
    private javax.swing.JLabel j179;
    private javax.swing.JLabel j18;
    private javax.swing.JLabel j180;
    private javax.swing.JLabel j181;
    private javax.swing.JLabel j182;
    private javax.swing.JLabel j183;
    private javax.swing.JLabel j184;
    private javax.swing.JLabel j185;
    private javax.swing.JLabel j186;
    private javax.swing.JLabel j187;
    private javax.swing.JLabel j188;
    private javax.swing.JLabel j189;
    private javax.swing.JLabel j19;
    private javax.swing.JLabel j190;
    private javax.swing.JLabel j191;
    private javax.swing.JLabel j192;
    private javax.swing.JLabel j193;
    private javax.swing.JLabel j194;
    private javax.swing.JLabel j195;
    private javax.swing.JLabel j196;
    private javax.swing.JLabel j197;
    private javax.swing.JLabel j198;
    private javax.swing.JLabel j199;
    private javax.swing.JLabel j2;
    private javax.swing.JLabel j20;
    private javax.swing.JLabel j200;
    private javax.swing.JLabel j201;
    private javax.swing.JLabel j202;
    private javax.swing.JLabel j203;
    private javax.swing.JLabel j204;
    private javax.swing.JLabel j205;
    private javax.swing.JLabel j206;
    private javax.swing.JLabel j207;
    private javax.swing.JLabel j208;
    private javax.swing.JLabel j209;
    private javax.swing.JLabel j21;
    private javax.swing.JLabel j210;
    private javax.swing.JLabel j211;
    private javax.swing.JLabel j212;
    private javax.swing.JLabel j22;
    private javax.swing.JLabel j23;
    private javax.swing.JLabel j24;
    private javax.swing.JLabel j25;
    private javax.swing.JLabel j26;
    private javax.swing.JLabel j27;
    private javax.swing.JLabel j28;
    private javax.swing.JLabel j29;
    private javax.swing.JLabel j3;
    private javax.swing.JLabel j30;
    private javax.swing.JLabel j31;
    private javax.swing.JLabel j32;
    private javax.swing.JLabel j33;
    private javax.swing.JLabel j34;
    private javax.swing.JLabel j35;
    private javax.swing.JLabel j36;
    private javax.swing.JLabel j37;
    private javax.swing.JLabel j38;
    private javax.swing.JLabel j39;
    private javax.swing.JLabel j4;
    private javax.swing.JLabel j40;
    private javax.swing.JLabel j41;
    private javax.swing.JLabel j42;
    private javax.swing.JLabel j43;
    private javax.swing.JLabel j44;
    private javax.swing.JLabel j45;
    private javax.swing.JLabel j46;
    private javax.swing.JLabel j47;
    private javax.swing.JLabel j48;
    private javax.swing.JLabel j49;
    private javax.swing.JLabel j5;
    private javax.swing.JLabel j50;
    private javax.swing.JLabel j51;
    private javax.swing.JLabel j52;
    private javax.swing.JLabel j53;
    private javax.swing.JLabel j54;
    private javax.swing.JLabel j55;
    private javax.swing.JLabel j56;
    private javax.swing.JLabel j57;
    private javax.swing.JLabel j58;
    private javax.swing.JLabel j59;
    private javax.swing.JLabel j6;
    private javax.swing.JLabel j60;
    private javax.swing.JLabel j61;
    private javax.swing.JLabel j62;
    private javax.swing.JLabel j63;
    private javax.swing.JLabel j64;
    private javax.swing.JLabel j65;
    private javax.swing.JLabel j66;
    private javax.swing.JLabel j67;
    private javax.swing.JLabel j68;
    private javax.swing.JLabel j69;
    private javax.swing.JLabel j7;
    private javax.swing.JLabel j70;
    private javax.swing.JLabel j71;
    private javax.swing.JLabel j72;
    private javax.swing.JLabel j73;
    private javax.swing.JLabel j74;
    private javax.swing.JLabel j75;
    private javax.swing.JLabel j76;
    private javax.swing.JLabel j77;
    private javax.swing.JLabel j78;
    private javax.swing.JLabel j79;
    private javax.swing.JLabel j8;
    private javax.swing.JLabel j80;
    private javax.swing.JLabel j81;
    private javax.swing.JLabel j82;
    private javax.swing.JLabel j83;
    private javax.swing.JLabel j84;
    private javax.swing.JLabel j85;
    private javax.swing.JLabel j86;
    private javax.swing.JLabel j87;
    private javax.swing.JLabel j88;
    private javax.swing.JLabel j89;
    private javax.swing.JLabel j9;
    private javax.swing.JLabel j90;
    private javax.swing.JLabel j91;
    private javax.swing.JLabel j92;
    private javax.swing.JLabel j93;
    private javax.swing.JLabel j94;
    private javax.swing.JLabel j95;
    private javax.swing.JLabel j96;
    private javax.swing.JLabel j97;
    private javax.swing.JLabel j98;
    private javax.swing.JLabel j99;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane menu_ingreso_egreso;
    private javax.swing.JTextField nombre_a;
    private javax.swing.JTextField nombre_a1;
    private javax.swing.JTextField nombre_e;
    private javax.swing.JTextField nombre_otros;
    private javax.swing.JTextField nombre_p;
    private javax.swing.JTextField nombre_p1;
    private javax.swing.JPanel otros;
    private javax.swing.JPanel particular;
    private javax.swing.JCheckBox pasar_dia;
    private javax.swing.JCheckBox pasar_dia1;
    private javax.swing.JCheckBox pasar_dia2;
    private javax.swing.JCheckBox pasar_dia3;
    private javax.swing.JCheckBox pasar_dia4;
    private javax.swing.JCheckBox pasar_dia5;
    private javax.swing.JTextField patente_c;
    private javax.swing.JTable tabla_egreso;
    private javax.swing.JTable tabla_vehiculo;
    private javax.swing.JTextField tarifa;
    private javax.swing.JTextField tarifa1;
    private javax.swing.JTextField tarifa2;
    private javax.swing.JTextField tarifa3;
    private javax.swing.JTextField tarifa4;
    private javax.swing.JTextField tarifa5;
    private javax.swing.JLabel tarifa_casilla;
    private javax.swing.JTextField tarifa_otros;
    private javax.swing.JPanel vahiculos;
    // End of variables declaration//GEN-END:variables

}
