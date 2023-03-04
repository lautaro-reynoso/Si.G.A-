/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Paneles_rotativos;

import Clases_tiketera.ConectorPluginV3;
import Main.Modelo;
import Main.Controlador;
import Main.Main;
import Paneles_principales.Login;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mateo
 */
public class Inicio extends javax.swing.JPanel {

    int filas = 0, filas_dia = 0, filas_vehiculos = 0, filas_parcelas = 0, filas_egresantes = 0;
    Controlador controlador = new Controlador();
    Modelo modelo = new Modelo();
    public DateTimeFormatter f = DateTimeFormatter.ofPattern("HH':'mm");

    /**
     * Creates new form Inicio
     */
    public Inicio() {
        initComponents();

    }

    public void buscarDocumento() throws SQLException {

        ResultSet res_1;

        res_1 = controlador.MostarDocumento(busca_documento2.getText());
        Busqueda(res_1);

    }

    public void buscarDocumento_e() throws SQLException {

        ResultSet res_1;

        res_1 = controlador.MostrarEgresoPorDocumento(busca_documento_e.getText());
        BusquedaEgresantes(res_1);

    }

    public void buscarDocumentoDia() throws SQLException {
        ResultSet res_2;

        res_2 = controlador.MostarDocumentoDia(busca_documento1.getText());
        BusquedaDia(res_2);

    }

    public void Busqueda(ResultSet res) throws SQLException {
        int f = 0;
        DefaultTableModel modelo = new DefaultTableModel();

        ArrayList<Object> nombrecolumna = new ArrayList<Object>();
        nombrecolumna.add("Documento");
        nombrecolumna.add("Nombre");
        nombrecolumna.add("Categoria");
        nombrecolumna.add("Importe");
        nombrecolumna.add("Fecha ingreso");
        nombrecolumna.add("Hora de ingreso");
        nombrecolumna.add("Fecha egreso");

        nombrecolumna.add("Parcela");
        nombrecolumna.add("Acompañantes");
        for (Object columna : nombrecolumna) {
            modelo.addColumn(columna);
        }
        this.tabla_acampantes.setModel(modelo);
        while (res.next()) {

            String documento = res.getString("documento");
            String nombre = res.getString("nombre");
            String categoria = res.getString("categoria");
            String importe = String.valueOf(res.getFloat("importe"));
            String fecha_egreso = res.getString("fecha_egreso");
            String hora_ingreso = res.getString("hora");
            String fecha_ingreso = res.getString("fecha_ingreso");

            String parcela = res.getString("parsela");
            String acomp = res.getString("acomp");
            String tab[] = {documento, nombre, categoria, importe, fecha_ingreso, hora_ingreso, fecha_egreso, parcela, acomp};
            modelo.addRow(tab);

        }
        tabla_acampantes.setModel(modelo);
    }

    public void BusquedaDia(ResultSet res) throws SQLException {
        DefaultTableModel modelo = new DefaultTableModel();

        ArrayList<Object> nombrecolumna = new ArrayList<Object>();
        nombrecolumna.add("Documento");
        nombrecolumna.add("Nombre");
        nombrecolumna.add("Hora ingreso");
        nombrecolumna.add("Categoria");
        nombrecolumna.add("Acompañantes");
        for (Object columna : nombrecolumna) {
            modelo.addColumn(columna);
        }
        this.tabla_x_dia.setModel(modelo);

        while (res.next()) {

            String documento = res.getString("dni");
            String nombre = res.getString("nombre");
            String hora_ingreso = res.getString("hora_ingreso");
            String categoria = res.getString("categoria");
            String acomp = res.getString("acomp");
            String tab[] = {documento, nombre, hora_ingreso, categoria, acomp};

            modelo.addRow(tab);

        }
        tabla_x_dia.setModel(modelo);
    }

    public void BusquedaVehiculos(ResultSet res) throws SQLException {

    }

    public void BusquedaParcelas(ResultSet res) throws SQLException {

        int f = 0;
        while (res.next()) {
            f++;
            String parcela = res.getString("parsela");
            String nombre = res.getString("nombre");
            String documento = res.getString("documento");

            String tab[] = {parcela, nombre, documento};
            DefaultTableModel tablamodelo = (DefaultTableModel) tabla_ocupacion.getModel();

            tablamodelo.addRow(tab);

            filas_parcelas = f;
            int rows = tablamodelo.getRowCount();
            for (int i = rows - (filas_parcelas + 1); i >= 0; i--) {
                tablamodelo.removeRow(i);
            }

        }

    }

    public void BusquedaParcelasPorNro() throws SQLException {

        ResultSet res_2;

        res_2 = controlador.BuscarParsela(busca_parcela.getText());
        Busqueda(res_2);
    }

    public void Tabla() throws SQLException {
        ResultSet res_1, res_2, res_3;
        res_1 = controlador.MostarOcupacionActual();

        Busqueda(res_1);

        res_2 = controlador.MostarOcupacionActualDia();
        BusquedaDia(res_2);
        ;
        //  String id = String.valueOf(s.getString("id"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        cant_p = new javax.swing.JLabel();
        consultar_btn = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_x_dia = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        busca_documento1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        parsela_btn = new javax.swing.JLabel();
        busca_parcela = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        busca_documento2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_acampantes = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_egresantes = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        busca_documento_e = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_ocupacion = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        parcela_s = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        parcela_e = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabbedPane1MousePressed(evt);
            }
        });

        cant_p.setText("Personas en el camping: Actualizar Tablas");

        consultar_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/images/pngwing.com (1).png"))); // NOI18N
        consultar_btn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        consultar_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        consultar_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                consultar_btnMousePressed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion de personas que pasan el dia"));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Personas que se encuentran en el Camping para pasar el dia"));

        tabla_x_dia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documento", "Nombre", "Hora de ingreso", "Categoría", "Acompañantes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_x_dia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_x_diaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_x_dia);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar por DNI"));

        busca_documento1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busca_documento1ActionPerformed(evt);
            }
        });
        busca_documento1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                busca_documento1KeyPressed(evt);
            }
        });

        jLabel6.setText("BUSCAR");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel6MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(busca_documento1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(busca_documento1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion de Acampantes"));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar por su Parcela"));

        parsela_btn.setText("BUSCAR");
        parsela_btn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        parsela_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        parsela_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                parsela_btnMousePressed(evt);
            }
        });

        busca_parcela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busca_parcelaActionPerformed(evt);
            }
        });
        busca_parcela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                busca_parcelaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(busca_parcela)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(parsela_btn))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(busca_parcela, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parsela_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Busque  por su DNI"));

        busca_documento2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busca_documento2ActionPerformed(evt);
            }
        });
        busca_documento2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                busca_documento2KeyPressed(evt);
            }
        });

        jLabel4.setText("BUSCAR");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 90, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addComponent(busca_documento2))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(busca_documento2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Acampantes Actualmente en el Camping"));

        tabla_acampantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documento", "Nombre", "Categoria", "Importe", "Fecha de egreso", "Hora de ingreso", "Fecha de ingreso", "Parcela", "Acompañantes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_acampantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabla_acampantesMousePressed(evt);
            }
        });
        tabla_acampantes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_acampantesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_acampantes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(jLabel5)
                        .addContainerGap(1037, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cant_p)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(consultar_btn)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cant_p, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(consultar_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("OCUPACION ACTUAL", jPanel1);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Egresantes de hoy"));

        tabla_egresantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Documento", "Fecha de ingreso"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tabla_egresantes);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Busque  por su DNI"));

        busca_documento_e.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busca_documento_eActionPerformed(evt);
            }
        });
        busca_documento_e.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                busca_documento_eKeyPressed(evt);
            }
        });

        jLabel7.setText("BUSCAR");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel7MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(busca_documento_e, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(busca_documento_e, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1096, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Reubicar Acampante"));

        tabla_ocupacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parcela (editar)", "Nombre", "Documento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_ocupacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabla_ocupacionMousePressed(evt);
            }
        });
        tabla_ocupacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_ocupacionKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(tabla_ocupacion);

        jLabel11.setText("PARCELA SELECCIONADA:");

        parcela_s.setText("ELEGIR");

        jLabel10.setText("EDITAR PARCELA:");

        jButton2.setText("Confirmar rehubicación");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(parcela_s)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parcela_e, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(parcela_s)
                    .addComponent(jLabel10)
                    .addComponent(parcela_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jTabbedPane1.addTab("EGRESANTES/REUBICACION", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void consultar_btnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consultar_btnMousePressed
        contar_personas_camping();
    }//GEN-LAST:event_consultar_btnMousePressed
    public void contar_personas_camping() {
    
        int total_acampantes = 0;
        int total_pordia = 0;
        try {

            Tabla();

            ArrayList list = new ArrayList();
            for (int i = 0; i < tabla_acampantes.getModel().getRowCount(); i++) {
                list.add(tabla_acampantes.getModel().getValueAt(i, 8)); //get the all row values at column index 0

                total_acampantes += (Integer.parseInt(list.get(i).toString())+1);
            }
            
            
            ArrayList list_2 = new ArrayList();
            for (int i = 0; i < tabla_x_dia.getModel().getRowCount(); i++) {
                list_2.add(tabla_x_dia.getModel().getValueAt(i, 4)); //get the all row values at column index 0

                total_pordia += (Integer.parseInt(list_2.get(i).toString())+1);
            }
            
        

            cant_p.setText("Cantidad de personas en el camping: " + (total_acampantes + total_pordia));

        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void busca_parcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busca_parcelaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_busca_parcelaActionPerformed

    public void TablaVehiculos() throws SQLException {
        ResultSet res_1;
        res_1 = controlador.MostarVehiculos();

        BusquedaVehiculos(res_1);

    }

    public void TablaParcelas() throws SQLException {
        ResultSet res_1;
        res_1 = controlador.MostarOcupacionActual();

        BusquedaParcelas(res_1);

    }


    private void busca_documento1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busca_documento1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_busca_documento1ActionPerformed

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed

        try {
            buscarDocumentoDia();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jLabel6MousePressed

    private void busca_documento2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busca_documento2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_busca_documento2ActionPerformed

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed

        try {
            buscarDocumento();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jLabel4MousePressed

    private void parsela_btnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_parsela_btnMousePressed

        try {
            BusquedaParcelasPorNro();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_parsela_btnMousePressed

    private void busca_parcelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busca_parcelaKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                BusquedaParcelasPorNro();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        char validador = evt.getKeyChar();
        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");
            busca_parcela.setText(null);
        }
    }//GEN-LAST:event_busca_parcelaKeyPressed

    private void busca_documento2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busca_documento2KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                buscarDocumento();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_busca_documento2KeyPressed

    private void busca_documento1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busca_documento1KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                buscarDocumentoDia();
            } catch (SQLException ex) {
                Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        char validador = evt.getKeyChar();
        if (Character.isLetter(validador)) {
            getToolkit().beep();
            evt.consume();
            Component rootPane = null;

            JOptionPane.showMessageDialog(rootPane, "Ingrese solo números!  ");
            busca_documento1.setText(null);
        }
    }//GEN-LAST:event_busca_documento1KeyPressed

    public void RehubicacionParcelas() {
        if (!parcela_e.getText().isEmpty()) {

            if (Integer.parseInt(parcela_e.getText()) <= 0 || Integer.parseInt(parcela_e.getText()) > 128) {
                javax.swing.JOptionPane.showMessageDialog(this, "No existe tal parcela.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else {

                //  System.out.println(parcela);
                try {

                    controlador.ModificarParcela(Integer.parseInt(parcela_s.getText()), Integer.parseInt(parcela_e.getText()));
                    parcela_s.setText("ELEGIR");
                    javax.swing.JOptionPane.showMessageDialog(this, "La parcela se actualizó con éxito.", "ACTUALIZADO", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                } catch (java.lang.NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(this, "hubo un error en el ingreso de datos, porfavor eliga una parcela a modificar.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                }

            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Antes de modificar, complete el campo de la parcela a modificar.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        try {
            RehubicacionParcelas();

        } catch (java.lang.NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Hubo un error, intente nuevamente.", "ERROR", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            parcela_e.setText(null);

        }
        try {
            TablaParcelas();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton2MousePressed

    public void TablaEgresantes() throws SQLException {

        ResultSet res_1;
        res_1 = controlador.MostarEgreso();

        BusquedaEgresantes(res_1);

    }

    public void BusquedaEgresantes(ResultSet res) throws SQLException {

        int f = 0;
        while (res.next()) {
            f++;
            String egreso = res.getString("fecha_ingreso");
            String nombre = res.getString("nombre");
            String documento = res.getString("documento");

            String tab[] = {nombre, documento, egreso};
            DefaultTableModel tablamodelo = (DefaultTableModel) tabla_egresantes.getModel();

            tablamodelo.addRow(tab);

            filas_egresantes = f;
            int rows = tablamodelo.getRowCount();
            for (int i = rows - (filas_egresantes + 1); i >= 0; i--) {
                tablamodelo.removeRow(i);
            }

        }

    }


    private void jTabbedPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MousePressed

        try {
            /*
            TablaVehiculos();
             */
            TablaEgresantes();
            TablaParcelas();

            Tabla();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTabbedPane1MousePressed

    private void tabla_ocupacionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_ocupacionMousePressed

        String parcela = tabla_ocupacion.getModel().getValueAt(tabla_ocupacion.getSelectedRow(), 0).toString();

        parcela_s.setText(parcela);
    }//GEN-LAST:event_tabla_ocupacionMousePressed

    private void tabla_ocupacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_ocupacionKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            String parcela = tabla_ocupacion.getModel().getValueAt(tabla_ocupacion.getSelectedRow(), 0).toString();

            parcela_s.setText(parcela);
        }

    }//GEN-LAST:event_tabla_ocupacionKeyPressed

    private void busca_documento_eActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busca_documento_eActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_busca_documento_eActionPerformed

    private void busca_documento_eKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_busca_documento_eKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_busca_documento_eKeyPressed

    private void jLabel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MousePressed
        try {
            buscarDocumento_e();
        } catch (SQLException ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jLabel7MousePressed

    private void tabla_acampantesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_acampantesMousePressed

    }//GEN-LAST:event_tabla_acampantesMousePressed

    private void tabla_acampantesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_acampantesKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            String dni = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 0).toString();
            String nombre = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 1).toString();
            String categoria = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 2).toString();
            String acomp = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 8).toString();
            String fecha_ingreso = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 4).toString();
            String fecha_egreso = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 6).toString();
            String importe = tabla_acampantes.getModel().getValueAt(tabla_acampantes.getSelectedRow(), 3).toString();
            LocalTime HoraActual = LocalTime.now();
            String serial;
            String nro_serial = "0234";

            ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

            tiket.Iniciar()
                    .Corte(1)
                    .DeshabilitarElModoDeCaracteresChinos()
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                    //.CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                    .Feed(1)
                    .EscribirTexto("SAEBU\n")
                    .EstablecerEnfatizado(true)
                    .EscribirTexto("Camping Universitario\nTIKET DUPLICADO\n")
                    .EstablecerEnfatizado(false)
                    .Feed(1)
                    .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                    .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                    .Feed(1)
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                    .EscribirTexto("______________________________________________\n")
                    .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + dni + "\n" + "Categoria:" + categoria + "\n" + "Acompañantes: " + acomp + "\n" + "Fecha-ingreso: " + fecha_ingreso + "\n" + "Fecha-egreso: " + fecha_egreso + "\nVALIDO HASTA:" + fecha_egreso + " a las 10am")
                    .EscribirTexto("______________________________________________\n")
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                    //.EscribirTexto("Sub total: $" + tarifas + "*" + (acomp + 1) + " (x dia)" + "\n")
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
    }//GEN-LAST:event_tabla_acampantesKeyPressed

    private void tabla_x_diaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_x_diaKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            String dni = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 0).toString();
            String nombre = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 1).toString();
            String categoria = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 3).toString();
            String acomp = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 4).toString();
            String hora_ingreso = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 2).toString();
            //String fecha_egreso = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 4).toString();
            //String importe = tabla_x_dia.getModel().getValueAt(tabla_x_dia.getSelectedRow(), 3).toString();
            LocalTime HoraActual = LocalTime.now();
            float total = 0;
            if (categoria.equals("invitado")) {
                total = (Float.parseFloat(acomp) * Main.tarifa_dia_invitados) + Main.tarifa_dia_invitados;
                System.out.println(total);
            }
            if (categoria.equals("particular")) {
                total = (Float.parseFloat(acomp) * Main.tarifa_dia_particular) + Main.tarifa_dia_particular;
                System.out.println(total);
            }
            if (categoria.equals("alumno")) {
                total = (Float.parseFloat(acomp) * Main.tarifa_dia_alumnos) + Main.tarifa_dia_alumnos;
                System.out.println(total);
            }
            if (categoria.equals("aportante")) {
                total = (Float.parseFloat(acomp) * Main.tarifa_dia_aportantes) + Main.tarifa_dia_aportantes;
                System.out.println(total);
            }

            String serial;
            String nro_serial = "0234";

            ConectorPluginV3 tiket = new ConectorPluginV3(ConectorPluginV3.URL_PLUGIN_POR_DEFECTO, "0006-" + nro_serial);

            tiket.Iniciar()
                    .Corte(1)
                    .DeshabilitarElModoDeCaracteresChinos()
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_CENTRO)
                    // .CargarImagenLocalEImprimir("C:\\Users\\mateo\\Desktop\\Nueva carpeta\\SIGA-main\\src\\com\\images\\icon-2_1.png", 0, 216)
                    .Feed(1)
                    .EscribirTexto("SAEBU\n")
                    .EstablecerEnfatizado(true)
                    .EscribirTexto("Camping Universitario\nTIKET DUPLICADO\n")
                    .Feed(1)
                    .TextoSegunPaginaDeCodigos(2, "cp850", "Serial: " + "0006-" + nro_serial + "\nCajero: " + Login.usuario + "\n")
                    .EscribirTexto("Fecha y hora: " + Main.DiaActual + " " + HoraActual.format(f) + "hs")
                    .Feed(1)
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_IZQUIERDA)
                    .EscribirTexto("______________________________________________\n")
                    .TextoSegunPaginaDeCodigos(2, "cp850", "Nombre: " + nombre + "        DNI: " + dni + "\n" + "Categoria: " + categoria + "\n" + "Hora-ingreso: " + hora_ingreso + "\n" + "Acompañantes: " + acomp + "\n" + "Solo por el dia\n VALIDO HASTA HOY a las 23hs\n")
                    .EscribirTexto("______________________________________________\n")
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                    //.EscribirTexto("Sub total: $" + tarifa + "*" + (acomp + 1) + "\n")
                    .EscribirTexto("_____________________________________________\n")
                    .EstablecerAlineacion(ConectorPluginV3.ALINEACION_DERECHA)
                    .EscribirTexto("TOTAL:  $" + total + "\n")
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
    }//GEN-LAST:event_tabla_x_diaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField busca_documento1;
    private javax.swing.JTextField busca_documento2;
    private javax.swing.JTextField busca_documento_e;
    private javax.swing.JTextField busca_parcela;
    private javax.swing.JLabel cant_p;
    private javax.swing.JLabel consultar_btn;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField parcela_e;
    private javax.swing.JLabel parcela_s;
    private javax.swing.JLabel parsela_btn;
    private javax.swing.JTable tabla_acampantes;
    private javax.swing.JTable tabla_egresantes;
    private javax.swing.JTable tabla_ocupacion;
    private javax.swing.JTable tabla_x_dia;
    // End of variables declaration//GEN-END:variables
}
