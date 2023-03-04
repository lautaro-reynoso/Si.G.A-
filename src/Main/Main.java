/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import com.formdev.flatlaf.FlatLightLaf;
import Paneles_principales.Login;
import Paneles_principales.esperar;
import Paneles_principales.noconexion;
import java.net.UnknownHostException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

/**
 *
 * @author ig:lauti_reynosoo
 */
public class Main {

    public static Controlador controlador = new Controlador();
    public static Conexion conexion = new Conexion();
    public static String privilegio;
    public static String DiaActual = LocalDate.now().toString();

    public static int HoraActual = LocalTime.now().getHour();

    public static float tarfia_acampar_alumnos, tarfia_acampar_aportantes, tarfia_acampar_particular,
            tarifa_dia_alumnos, tarifa_dia_aportantes, tarifa_dia_particular, tarifa_dia_invitados, tarifa_acampar_invitados, tarifa_cabaña_aportante, tarifa_cabaña_noaportante, tarifa_casillas;
    public static Calendar calendario = Calendar.getInstance();

    public static void main(String args[]) throws SQLException, ClassNotFoundException, UnknownHostException {

        System.out.println(HoraActual);
        
       
        try {
            int i = 0;
            conexion.Conectar2(i);

        } catch (java.lang.NullPointerException e) {
            System.out.println("problemas");
        }

        controlador.setear_tarifas();

        //System.out.println(calendario.get(Calendar.HOUR_OF_DAY));
        /*   String hora = null;

        //ha ingresado un nuevo particular por el dia
        String acomp2 = "ha ingresado un nuevo particular por el dia.";
        System.out.println(acomp2.length());

        String acomp3 = "ha ingresado 01 nuevo/s invitado/s acampante/s.";
        System.out.println(acomp3.length());
        System.out.println(01 + 3);
        String a = "ha ingresado un nuevo particular acampante";
        String b = "ha ingresado un nuevo alumno acampante";
        String c = "ha ingresado un nuevo aportante acampante";
        String d = "ha ingresado un nuevo invitado acampante";

        String f = "ha ingresado un nuevo alumno por el dia";
        String acomp = "ha ingresado 04 particulares por el dia";
        System.out.println(acomp.length() + "/" + f.length());

        String e = "ha ingresado un nuevo particular por el dia";
        String acomp4 = "ha ingresado 01 nuevos invitados por el dia";
        System.out.println(e.length() + "/" + acomp4.length());

        String g = "ha ingresado un nuevo aportante por el dia";
        String h = "ha ingresado un nuevo invitado por el dia";

        System.out.println(a.length() + "/" + b.length() + "/" + c.length() + "/" + d.length() + "/" + g.length() + "/" + h.length());
        
        String i = "ha ingresado un/a nuev@ casilla/motorhome acampante.";
        String j = "ha ingresado un/a nuev@ casilla/motorhome por el dia";
        System.out.println(i.length());*/
 /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        FlatLightLaf.setup();

        /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
         */
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        }
        );

    }

}
