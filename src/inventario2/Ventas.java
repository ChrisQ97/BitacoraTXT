/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario2;

import com.mxrck.autocompleter.AutoCompleterCallback;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
/*
fecha actual en el calendario,codigo y nombre
*/
/**
 *
 * @author sys515
 */
public class Ventas extends javax.swing.JFrame {

    private Boolean valor=false;
    Conexion con = new Conexion();
    Connection Consulta = con.conexion();
    Connection Insertar = con.conexion();
    private int año=0;
    private int mes=0;
    private int dia=0;
    DefaultTableModel modelo = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }; //para la tabla
    private String nitglobal = null;
    private String US=null;
    private String Codigo;
   TextAutoCompleter textAutoCompleter ;
        
    /**
     * Creates new form Ventas
     */
    
    private void SYN()
    {
        try {
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("select id,Serie,INCREMENTO from Resoluciones where Activo=1 && INCREMENTO!=RangoF order by Fecha ASC Limit 1");
            while(Ca.next())
            {
                Numero.setText(Ca.getString(3));
                Serie.setText(Ca.getString(2));
                id.setText(Ca.getString(1));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
  
    public Ventas() {
        initComponents();
                this.setTitle("Ventas- Sistema Inventario BTZ");

        //Producto.requestFocus();
       textAutoCompleter = new TextAutoCompleter(Pr);
       textAutoCompleter.setCaseSensitive(false);
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Codigo,Nombre,Marca,Medida FROM Producto where Existencia!=0");
            while (Ca.next()) {

             
                textAutoCompleter.addItem(Ca.getString(1)+","+Ca.getString(2)+","+Ca.getString(3)+","+Ca.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        textAutoCompleter.setMode(0);
        this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
        SYN();
        Cantidad.setText("");
        Fech.requestFocus();
        id.setVisible(false);
        id.setEnabled(false);
//        AutoCompleteDecorator.decorate(Producto);
        AutoCompleteDecorator.decorate(Nit);
        
         java.util.Date fecha = new Date();
        Fech.setDate(fecha);
        modelo.setRowCount(0);
        modelo.addColumn("Cantidad");
        modelo.addColumn("Unidad");
        modelo.addColumn("Nombre");
        modelo.addColumn("Marca");
        modelo.addColumn("Precio Producto");
        modelo.addColumn("Precio Unitario");
        Factura.setModel(modelo);
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Codigo,Nombre,Marca,Medida FROM Producto");
            while (Ca.next()) {

//                Producto.addItem(Ca.getString(1)+","+Ca.getString(2)+","+Ca.getString(3)+","+Ca.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Nit FROM Cliente");
            while (Ca.next()) {

                Nit.addItem(Ca.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        Factura.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 2) {
                    int e=ext(String.valueOf(Factura.getValueAt(Factura.getSelectedRow(), 2)),
                            String.valueOf(Factura.getValueAt(Factura.getSelectedRow(), 3)));
                    String mensaje=mensaje(String.valueOf(Factura.getValueAt(Factura.getSelectedRow(), 0)));
                    
                    if(Integer.parseInt(mensaje)>e)
                    {
                         JOptionPane.showMessageDialog(null, "Excede La existencia");

                    }
                    if(mensaje.equals("0"))
                    {
                         JOptionPane.showMessageDialog(null, "No se permite que ingrese 0");
                        
                    }
                    else
                    {
                        Double x=(Double.parseDouble(String.valueOf(Factura.getValueAt(Factura.getSelectedRow(), 5))))*Double.parseDouble(mensaje);
                        Factura.setValueAt(mensaje, row, 0);
                        Factura.setValueAt(String.valueOf(x), row, 4);
                        sumar_columnas();
                    }
                 
                }
            }
        });
        LU();
    }
    private void sumar_columnas()
    {
        if(Factura.getRowCount()!=0)
        {
            BigDecimal n=BigDecimal.valueOf(0);
            int c=0;
            for (int i = 0; i < Factura.getRowCount(); i++) 
            {
               c = c+Integer.valueOf(Factura.getValueAt(i, 0).toString().trim());
               n=n.add(BigDecimal.valueOf(Double.valueOf(Factura.getValueAt(i, 4).toString().trim())));
               
               
            }
            TC.setText(String.valueOf(c));
            TC1.setText(String.valueOf(n));
        }
        else
        {
            TC.setText("0");
            TC1.setText("0.00");
        }
    }
    
    private void LU()
    {
        User.removeAllItems();
        try {
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("select Usuario from Usuarios ");
            while(Ca.next())
            {
                User.addItem(Ca.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String mensaje(String x)
    {
        int numero=0;
        try
        {
        
            numero=Integer.parseInt(javax.swing.JOptionPane.showInputDialog("introduce numero"));
            return String.valueOf(numero);
        }
        catch(NumberFormatException e)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Solo se permiten numeros");
 
        }
        return x;
    }
    private int CuantosLotes(String Codigo) {
        int cantidad = 0;
        int NoLotes = 0;
        cantidad = Integer.parseInt(Cantidad.getText());

        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Cantidad FROM Lote  where Producto_id='" + Codigo + "' ORDER BY Fecha ASC");
            while (Ca.next()) {
                if (cantidad > 0) {
                    cantidad = cantidad - Integer.parseInt(Ca.getString(1));
                    NoLotes++;

                }
            }
            return NoLotes;
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private int CuantosLotes2(String id,String n) {
        int cantidad = 0;
        int NoLotes = 0;
        cantidad = Integer.parseInt(n);

        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Cantidad FROM Lote  where Producto_id='" + id + "' ORDER BY Fecha ASC");
            while (Ca.next()) {
                if (cantidad > 0) {
                    cantidad = cantidad - Integer.parseInt(Ca.getString(1));
                    NoLotes++;

                }
            }
            return NoLotes;
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        Nombre = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelcliente = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Nit = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        NombreM = new javax.swing.JLabel();
        ApellidoM = new javax.swing.JLabel();
        N = new javax.swing.JLabel();
        NY = new javax.swing.JLabel();
        addcli = new javax.swing.JButton();
        panelfactura = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Serie = new javax.swing.JLabel();
        Numero = new javax.swing.JLabel();
        Fech = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JLabel();
        panelproducto = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        NombrePM = new javax.swing.JLabel();
        NombreP = new javax.swing.JLabel();
        MarcaM = new javax.swing.JLabel();
        Marca = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Existencia = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Cantidad = new javax.swing.JTextField();
        addfila = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        Uni = new javax.swing.JLabel();
        Pr = new javax.swing.JTextField();
        breturn = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        TC1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        TC = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Factura = new rojerusan.RSTableMetro();
        jButton1 = new javax.swing.JButton();
        Usuario = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        User = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        PW = new javax.swing.JPasswordField();

        jMenuItem1.setText("Eliminar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(189, 189, 189));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 65, -1, -1));
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 57, 66, -1));
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 78, 64, -1));

        panelcliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelcliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Cliente");
        panelcliente.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 1, -1, -1));

        Nit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NitActionPerformed(evt);
            }
        });
        panelcliente.add(Nit, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 28, 146, -1));

        jLabel6.setText("Nit");
        panelcliente.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 36, -1, -1));

        NombreM.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        NombreM.setText("Nombre:");
        panelcliente.add(NombreM, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 72, -1, -1));

        ApellidoM.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        ApellidoM.setText("Apellido:");
        panelcliente.add(ApellidoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 72, -1, -1));

        N.setText("jLabel14");
        panelcliente.add(N, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 72, -1, -1));

        NY.setText("jLabel15");
        panelcliente.add(NY, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 72, -1, -1));

        addcli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/clientead11.png"))); // NOI18N
        addcli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addcliActionPerformed(evt);
            }
        });
        panelcliente.add(addcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 93, 39, -1));

        jPanel1.add(panelcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 270, 140));

        panelfactura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Fecha");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Factura");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Serie");

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Numero");

        Serie.setText("jLabel15");

        Numero.setText("jLabel15");

        Fech.setDateFormatString("yyyy-MM-d");

        id.setText("jLabel19");

        javax.swing.GroupLayout panelfacturaLayout = new javax.swing.GroupLayout(panelfactura);
        panelfactura.setLayout(panelfacturaLayout);
        panelfacturaLayout.setHorizontalGroup(
            panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfacturaLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addContainerGap(292, Short.MAX_VALUE))
            .addGroup(panelfacturaLayout.createSequentialGroup()
                .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelfacturaLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(32, 32, 32)
                        .addComponent(Fech, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelfacturaLayout.createSequentialGroup()
                        .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addGap(41, 41, 41)
                        .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Numero)
                            .addGroup(panelfacturaLayout.createSequentialGroup()
                                .addComponent(Serie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(id)))))
                .addGap(64, 64, 64))
        );
        panelfacturaLayout.setVerticalGroup(
            panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfacturaLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelfacturaLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3))
                    .addGroup(panelfacturaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Fech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelfacturaLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(Serie)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelfacturaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Numero))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        jPanel1.add(panelfactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        panelproducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelproducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Producto");
        panelproducto.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        NombrePM.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        NombrePM.setText("Nombre");
        panelproducto.add(NombrePM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        NombreP.setText("-----");
        NombreP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NombrePKeyTyped(evt);
            }
        });
        panelproducto.add(NombreP, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 90, -1));

        MarcaM.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        MarcaM.setText("Marca");
        panelproducto.add(MarcaM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 55, -1));

        Marca.setText("-----");
        panelproducto.add(Marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 80, -1));

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Existencia");
        panelproducto.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        Existencia.setText("-----");
        panelproducto.add(Existencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 90, -1));

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("Cantidad");
        panelproducto.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, -1, -1));

        Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CantidadKeyTyped(evt);
            }
        });
        panelproducto.add(Cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 70, -1));

        addfila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/masa10.png"))); // NOI18N
        addfila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfilaActionPerformed(evt);
            }
        });
        panelproducto.add(addfila, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 30, 30));

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setText("Unidad");
        panelproducto.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        Uni.setText("-----");
        panelproducto.add(Uni, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 90, -1));

        Pr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PrKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PrKeyPressed(evt);
            }
        });
        panelproducto.add(Pr, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 180, -1));

        jPanel1.add(panelproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 340, 240));

        breturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/regresara7.png"))); // NOI18N
        breturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                breturnActionPerformed(evt);
            }
        });
        jPanel1.add(breturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 100, 40, -1));

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel14.setText("Total");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 440, -1, -1));

        TC1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        TC1.setText("0.0");
        jPanel1.add(TC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 440, 70, -1));

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel13.setText("Total");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 440, -1, -1));

        TC.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        TC.setText("0");
        jPanel1.add(TC, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 440, 60, -1));

        Factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Factura.setComponentPopupMenu(jPopupMenu1);
        Factura.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane2.setViewportView(Factura);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 650, 270));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cajara23.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 72, -1));

        Usuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel16.setText("Vendedor");

        User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserActionPerformed(evt);
            }
        });

        jLabel17.setText("Usuario");

        javax.swing.GroupLayout UsuarioLayout = new javax.swing.GroupLayout(Usuario);
        Usuario.setLayout(UsuarioLayout);
        UsuarioLayout.setHorizontalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(24, 24, 24)
                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(User, 0, 146, Short.MAX_VALUE)
                    .addComponent(jLabel16)
                    .addComponent(PW))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        UsuarioLayout.setVerticalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(User, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addComponent(PW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1.add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, -1, 140));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1033, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void llenarPM(String Codigo) {
        
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Nombre,Marca,Existencia ,Medida FROM Producto Where Codigo='" + Codigo + "'");
            while (Ca.next()) {

                Uni.setText(Ca.getString(4));
                NombreP.setText(Ca.getString(1));
                Marca.setText(Ca.getString(2));
                Existencia.setText(Ca.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private int ext(String N,String M)
    {
         try {
            int y=0;
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Existencia FROM Producto Where Nombre='" +N + "' && Marca='"+M+"'");
            while (Ca.next()) {
                y=Integer.parseInt(Ca.getString(1));
                
            }
            return y;
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    private void llenarCl(String Codigo) {

        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT NombreC,Apellido FROM Cliente Where Nit='" + Codigo + "'");
            while (Ca.next()) {

                N.setText(Ca.getString(1));
                NY.setText(Ca.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String id(String Codigo) {
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT id FROM Producto Where Codigo='" + Codigo + "'");
            while (Ca.next()) {

                return Ca.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String id2(String Nombre, String Marca) {
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT id FROM Producto Where Nombre='" + Nombre + "'&& Marca='" + Marca + "'");
            while (Ca.next()) {

                return Ca.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }   
//    private int RetornoIdLoteNuevo() {
//        try {
//
//            Statement sx = Consulta.createStatement();
//            ResultSet Ca = sx.executeQuery("SELECT NoLote FROM Lote WHERE Producto_id ='" + id((String) Producto.getSelectedItem()) + "'&& Disponible=true &&NoLote= (SELECT MAX(NoLote) FROM Lote WHERE Producto_id ='" + id((String) Producto.getSelectedItem()) + "')");
//
//            while (Ca.next()) {
//
//                return Integer.parseInt(Ca.getString(1));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return 0;
//    }

    private String Facturacion(int x, String Codigo) {
        BigDecimal cantidad = BigDecimal.valueOf(0.0);
        cantidad = BigDecimal.valueOf(Double.parseDouble(Cantidad.getText()));
        BigDecimal PrecioTotal = BigDecimal.valueOf(0.0);
        try {

            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Cantidad,PrecioUnitario FROM Lote  where Disponible=true && Producto_id='" + Codigo + "' ORDER BY Fecha ASC Limit " + x + "");
            while (Ca.next()) {
                
                if (x == 1) {
                    PrecioTotal = PrecioTotal.add(BigDecimal.valueOf(Double.parseDouble(Cantidad.getText())).multiply(BigDecimal.valueOf( Double.parseDouble(Ca.getString(2))))).setScale(2, BigDecimal.ROUND_DOWN);

                
                } else {

                    if (Double.parseDouble(String.valueOf(cantidad)) < Double.parseDouble(Ca.getString(1))) {

                        PrecioTotal = PrecioTotal.add(cantidad.multiply(BigDecimal.valueOf(Double.parseDouble(Ca.getString(2))))).setScale(2, BigDecimal.ROUND_DOWN);                                ;

                    }
                    if (Double.parseDouble(String.valueOf(cantidad)) == Double.parseDouble(Ca.getString(1))) {
                

                        PrecioTotal = PrecioTotal.add(BigDecimal.valueOf(Double.parseDouble(Ca.getString(1))).multiply(BigDecimal.valueOf(Double.parseDouble(Ca.getString(2))))).setScale(2, BigDecimal.ROUND_DOWN); 
                                
                    }
                    if (Double.parseDouble(String.valueOf(cantidad)) > Double.parseDouble(Ca.getString(1))) {

                        cantidad = cantidad.subtract(BigDecimal.valueOf(Double.parseDouble(Ca.getString(1)))).setScale(2, BigDecimal.ROUND_DOWN);
                        PrecioTotal = PrecioTotal.add((BigDecimal.valueOf(Double.parseDouble(Ca.getString(1))).multiply(BigDecimal.valueOf(Double.parseDouble(Ca.getString(2)))))).setScale(2, BigDecimal.ROUND_DOWN);;
                                

                    }

                }
            }
            return String.valueOf(PrecioTotal);
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(null, "Precio Total " + PrecioTotal);
        return null;

    }

    private int getidPro(String Nom, String Marca) {
        int id3 = 0;
        try {
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT id FROM Producto WHERE Nombre='" + Nom + "'&& Marca='" + Marca + "'");
            while (Ca.next()) {
                id3 = Integer.parseInt(Ca.getString(1));
            }
            return id3;

        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    private Boolean Consultar(String id,String N)
    {   
        Boolean Valor=false;
        try {
            Statement st = Consulta.createStatement();
            ResultSet rd = st.executeQuery("SELECT Cantidad FROM Lote WHERE id ='" +id+ "' && NoLote='"+N+"'");
            while(rd.next())
            {
                if(Integer.parseInt(rd.getString(1))==0)
                {
                    Valor=true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Valor;
    }
    private void poner0(String id,String N)
    {
        try {
            PreparedStatement r=Insertar.prepareStatement("UPDATE Lote set Cantidad=0, Disponible=false where Disponible=true && Producto_id=" + id + ""
                                + "&& NoLote=" + N + "");
            r.executeUpdate();
            r.close();
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void crearLotesNuevos(String idF, String ve[], String idP) {
       
       
        
        
        
        double cantidad = Double.parseDouble(ve[0]);
        try {
            
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT NoLote,Cantidad,PrecioUnitario,PrecioTotal,Descripcion FROM Lote  where Disponible=true&& Producto_id='"
                    + idP + "' ORDER BY Fecha ASC Limit "
                    + CuantosLotes2(idP,ve[0]) + "");
            
            
             
            while (Ca.next()) {
                if (CuantosLotes2(idP,ve[0]) == 1) {
                    double aux = cantidad - Double.parseDouble(Ca.getString(2));
                    cantidad = cantidad - aux;
                    
                    
                    
                    
                    PreparedStatement CrearLot = Insertar.prepareStatement("INSERT INTO LoteVenta(NoLote,Cantidad,PrecioUnitario,PrecioTotal,Descripcion,Producto_id,FacturaVenta_id,Fecha) "
                            + "VALUES(?,?,?,?,?,?,?,?)");
                    CrearLot.setString(1, Ca.getString(1));
                    CrearLot.setString(2, ve[0]);
                    CrearLot.setString(3, Ca.getString(3));
                    double precioTo = 0;
                    precioTo = (Double.parseDouble(ve[0])) * (Double.parseDouble(Ca.getString(3)));
                    CrearLot.setString(4, String.valueOf(precioTo));
                    CrearLot.setString(5, Ca.getString(5));
                    CrearLot.setString(6, idP);
                    CrearLot.setString(7, idF);
                    CrearLot.setString(8, año+"-"+mes+"-"+dia);
                    CrearLot.executeUpdate();
                    
                    if (cantidad == 0) {
                        PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Lote set Cantidad=0, Disponible=false where Disponible=true && Producto_id=" + idP + ""
                                + "&& NoLote=" + Ca.getString(1) + "");
                        Actualizar.executeUpdate();
                        Actualizar.close();
                    } else {

                        PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Lote set Cantidad=Cantidad-'" + ve[0] + "'where Disponible=true && Producto_id=" + idP + ""
                                + "&& NoLote=" + Ca.getString(1) + "");
                        Actualizar.executeUpdate();
                        Actualizar.close();
                        if(Consultar(idP,Ca.getString(1)))
                        {
                            poner0(idP,Ca.getString(1));
                        }
                        
                    }
                    CrearLot.close();

                } else {
                    if (cantidad < Double.parseDouble(Ca.getString(2))) {
                        PreparedStatement CrearLot = Insertar.prepareStatement("INSERT INTO LoteVenta(NoLote,Cantidad,PrecioUnitario,PrecioTotal,Descripcion,Producto_id,FacturaVenta_id,Fecha) "
                                + "VALUES(?,?,?,?,?,?,?,?)");
                        CrearLot.setString(1, Ca.getString(1));
                        CrearLot.setString(2, String.valueOf(cantidad));
                        CrearLot.setString(3, Ca.getString(3));
                        double precioTo = 0;
                        precioTo = cantidad * (Double.parseDouble(Ca.getString(3)));
                        CrearLot.setString(4, String.valueOf(precioTo));
                        CrearLot.setString(5, Ca.getString(5));
                        CrearLot.setString(6, idP);
                        CrearLot.setString(7, idF);
                        CrearLot.setString(8, año+"-"+mes+"-"+dia);
                        CrearLot.executeUpdate();
                        CrearLot.close();
                        PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Lote set Cantidad=Cantidad-'" + String.valueOf(cantidad) + "'where Disponible=true && Producto_id=" + idP + ""
                                + "&& NoLote=" + Ca.getString(1) + "");
                        Actualizar.executeUpdate();
                        Actualizar.close();

                    }
                    if (cantidad == Double.parseDouble(Ca.getString(2))) {
                        PreparedStatement CrearLot = Insertar.prepareStatement("INSERT INTO LoteVenta(NoLote,Cantidad,PrecioUnitario,PrecioTotal,Descripcion,Producto_id,FacturaVenta_id,Fecha) "
                                + "VALUES(?,?,?,?,?,?,?,?)");
                        CrearLot.setString(1, Ca.getString(1));
                        CrearLot.setString(2, String.valueOf(cantidad));
                        CrearLot.setString(3, Ca.getString(3));
                        double precioTo = 0;
                        precioTo = cantidad * (Double.parseDouble(Ca.getString(3)));
                        CrearLot.setString(4, String.valueOf(precioTo));
                        CrearLot.setString(5, Ca.getString(5));
                        CrearLot.setString(6, idP);
                        CrearLot.setString(7, idF);
                        CrearLot.setString(8, año+"-"+mes+"-"+dia);
                        CrearLot.executeUpdate();
                        CrearLot.close();
                        PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Lote set Cantidad=0, Disponible=false where Disponible=true && Producto_id=" + idP + ""
                                + "&& NoLote=" + Ca.getString(1) + "");
                        Actualizar.executeUpdate();
                        Actualizar.close();
                    }
                    if (cantidad > Double.parseDouble(Ca.getString(2))) {

                        cantidad = cantidad - Double.parseDouble(Ca.getString(2));

                        PreparedStatement CrearLot = Insertar.prepareStatement("INSERT INTO LoteVenta(NoLote,Cantidad,PrecioUnitario,PrecioTotal,Descripcion,Producto_id,FacturaVenta_id,Fecha) "
                                + "VALUES(?,?,?,?,?,?,?,?)");
                        CrearLot.setString(1, Ca.getString(1));
                        CrearLot.setString(2, Ca.getString(2));
                        CrearLot.setString(3, Ca.getString(3));
                        CrearLot.setString(4, Ca.getString(4));
                        CrearLot.setString(5, Ca.getString(5));
                        CrearLot.setString(6, idP);
                        CrearLot.setString(7, idF);
                        CrearLot.setString(8, año+"-"+mes+"-"+dia);
                        CrearLot.executeUpdate();
                        CrearLot.close();
                        PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Lote set Cantidad=0,Disponible=false where Disponible=true && Producto_id=" + idP + ""
                                + "&& NoLote=" + Ca.getString(1) + "");

                        Actualizar.executeUpdate();
                        Actualizar.close();

                    }
                }
            }
           
            

        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
        private Boolean vef()
    {
       
        
        
        char claves[]=PW.getPassword();
        String clavedef=new String(claves);
        int c=0;
        try{
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Usuario, Contraseña FROM Usuarios");
            while (Ca.next()) 
            {
                c++;
                if(c!=0)
                {
               // System.out.println(Ca.getString(1)+" -- "+Ca.getString(2));
               // System.out.println(us+"***"+clavedef);
               if((US.equals(Ca.getString(1)))&&(clavedef.equals(Ca.getString(2))))
                {
                   return true;
                }
                }
                else
                {
                   return false;
                }
                              
            }
            
        }catch(SQLException ex){
            Logger.getLogger(Ingreso.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        return false;
    }

    private int getidProve(String nit) {
        int nit2 = 0;
        try {
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT id FROM Cliente WHERE Nit='" + nit + "'");
            while (Ca.next()) {
                nit2 = Integer.parseInt(Ca.getString(1));

            }
            return nit2;
        } catch (SQLException ex) {
            Logger.getLogger(Compras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private String crearFactura(String Serie, String Numero,String id) {
        int idUsuario = 0;
        try {
            PreparedStatement CrearLot = Insertar.prepareStatement("INSERT INTO FacturaVenta(Total,Serie,Numero,Cliente_id,Resoluciones_id,Fecha,Cantidad_Prod,Usuarios_id,Anulado"
                    + ") VALUES(?,?,?,?,?,?,?,?,0)", Statement.RETURN_GENERATED_KEYS);
            CrearLot.setString(1, TC1.getText());
            CrearLot.setString(2, Serie);
            CrearLot.setString(3, Numero);
            CrearLot.setString(4, String.valueOf(getidProve(nitglobal)));
            CrearLot.setString(5, idRes());   
            CrearLot.setString(6, año+"-"+mes+"-"+dia);
            CrearLot.setString(7,TC.getText());
            CrearLot.setString(8,id);
            CrearLot.executeUpdate();

            try (ResultSet rs = CrearLot.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new RuntimeException("no devolvió el ID");
                }

                idUsuario = rs.getInt(1);
                CrearLot.close();

            }
            return String.valueOf(idUsuario);
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    private Boolean CompararEntrada(String Nombre,String Marca)
    {
        
        String x[] = new String[2];
        if(Factura.getRowCount()!=0)
        {
        for (int i = 0; i < Factura.getRowCount(); i++)
        {
            
                x[0] = Factura.getValueAt(i, 2).toString().trim();
                x[1] = Factura.getValueAt(i, 3).toString().trim();
                if(x[0].equals(Nombre)&& x[1].equals(Marca))
                {
                    return false;
                }
               
                
           
        }
        }
        else{
            return true;

        }
        return true;
        
    }
    private void addfilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addfilaActionPerformed
        if(valor==true)
        {
        if (Cantidad.getText().equals("")||Cantidad.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Ingrese la Cantidad que desea comprar de: " + NombreP.getText());

        } else {
            if(CompararEntrada(NombreP.getText(),Marca.getText())==false){
              JOptionPane.showMessageDialog(null, "Ya tiene este Producto Registrado en la Factura");

            }
            else{
            if (Integer.parseInt(Cantidad.getText()) <= Integer.parseInt(Existencia.getText())) {
                String Completo =Codigo;// (String) Producto.getSelectedItem();
                String Fact=Facturacion(CuantosLotes(id(Completo)), id(Completo));
                Double r=Double.parseDouble(Fact);
                Double PrecioUnita=r/(Double.parseDouble(Cantidad.getText()));
                BigDecimal PrecioUnitar=BigDecimal.valueOf(PrecioUnita).setScale(2, BigDecimal.ROUND_UP);
                modelo.addRow(new Object[]{Cantidad.getText(),Uni.getText(), NombreP.getText(), Marca.getText(),
                    Facturacion(CuantosLotes(id(Completo)), id(Completo)), PrecioUnitar});
                BigDecimal To = BigDecimal.valueOf(Double.parseDouble(TC1.getText())).add(BigDecimal.valueOf(Double.parseDouble(Facturacion(CuantosLotes(id(Completo)), id(Completo))))).setScale(2, BigDecimal.ROUND_DOWN);
                TC1.setText(String.valueOf(To));
                 int aux=0;
            aux=Integer.valueOf(TC.getText());
            aux=aux+Integer.valueOf(Cantidad.getText());
            TC.setText(String.valueOf(aux));
            } else {
                JOptionPane.showMessageDialog(null, "Excede la cantidad de Inventario");

            }
           
        }
        }
      Cantidad.setText("");
        }
        else
        {
          JOptionPane.showMessageDialog(null, "Seleccione producto");

        }
               
// TODO add your handling code here:
    }//GEN-LAST:event_addfilaActionPerformed
    private String idRes()
    {   
        String id=null;
        try {
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("select id from Resoluciones where INCREMENTO!=RangoF ORDER BY Fecha ASC Limit 1");
            while(Ca.next())
            {
                id=Ca.getString(1);
            }
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
    private int Fecha2(String f)
    {
        Date date = new Date();
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       String fecha3=dateFormat.format(date);
       
       
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        
        Date fecha1 = null;
        Date fecha2 = null;
        try {

            fecha1 = formatoDelTexto.parse(fecha3);
            fecha2 = formatoDelTexto.parse(f);

        } catch (ParseException ex) {

            ex.printStackTrace();

        }
        return fecha1.compareTo(fecha2);
    }
    
    private String ahora(){
          String retornar="";
        try {
          
            Statement fechaa = Insertar.createStatement();
            ResultSet fech = fechaa.executeQuery("SELECT Now();");
            while (fech.next()) {
                retornar = fech.getString(1);
            }
            return retornar;
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retornar;
                
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         CrearExcel arch = new CrearExcel();
         ManejoDeArchivos txt = new ManejoDeArchivos();
        try
        {
        año = Fech.getCalendar().get(Calendar.YEAR);
         mes = Fech.getCalendar().get(Calendar.MONTH) + 1;
         dia = Fech.getCalendar().get(Calendar.DAY_OF_MONTH);
        }                                        

    
        catch(NullPointerException ex) {
        }
        if(año==0&&dia==0&&mes==00)   
        {
            JOptionPane.showMessageDialog(this, "Al menos selecciona una fecha válida!", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            String Fecha3=año+"-"+mes+"-"+dia;
            if(Fecha2(Fecha3)>=0)
            {
                if (Serie.getText().equals("") || Numero.getText().equals("")) 
                {
                    JOptionPane.showMessageDialog(null, "Campo(s) Vacio(s)");

                }
                else 
                {
                    if(Factura.getRowCount()!=0)
                    {
                        if(PW.getText().equals("********"))
                        {
                            JOptionPane.showMessageDialog(null, "Ingrese Contraseña de Vendedor");
                        }
                        else
                        {   
                            if(vef()==true)
                            {
                                try {
                                    // desactivamos autocommit
                                    Insertar.setAutoCommit(false);
                                    /*
                                    
                                    inicia la transaccion para crear lotes de venta
                                    */
                                    //fecha
                                    String serverhora = ahora();
                                    String tabla = "LoteVenta";
                                   
                                   
                                    //arch.modificarFinal(serverhora, tabla, "Activa");
                                    txt.escribirTransacciontres(serverhora, "Activa", tabla);
                                    //System.out.println("transaccion normal");
                                    Statement starttransaction = Insertar.createStatement();
                                    starttransaction.execute("START TRANSACTION;");
                                    String idVend=idVe(US,PW.getText());
                                    String x[] = new String[6];
                                    String idFac = crearFactura(Serie.getText(), Numero.getText(),idVend);
                                    aumentar();
                                    for (int i = 0; i < Factura.getRowCount(); i++)
                                    {
                                        for (int j = 0; j < Factura.getColumnCount(); j++)
                                        {
                                            x[j] = Factura.getValueAt(i, j).toString().trim();
                                            
                                        }
                                        
                                        crearLotesNuevos(idFac, x, id2(x[2], x[3]));
                                    }
                                    //arch.modificar("Parcialmente Comprometida");
                                    serverhora = ahora();
                                    txt.escribirTransacciondos(serverhora, "Parcialmente Comprometida");
                                    
                                    /*
                                    arch.mod
                                    aqui terminamos la transaccion de Insertar
                                    */
                                    int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro?");
                                    if(resp == 0){
                                    Insertar.commit();
                                    //arch.modificar("Comprometida");
                                    
                                    serverhora = ahora();
                                    txt.escribirTransacciondos(serverhora, "Comprometida");
                                    
                                    JOptionPane.showMessageDialog(null, "Venta Registrada");
                                    }
                                    else{
                                    Insertar.rollback();
                                    //arch.modificar("Abortada");
                                    serverhora = ahora();
                                    txt.escribirTransacciondos(serverhora, "Abortada");
                                    
                                    JOptionPane.showMessageDialog(null, "Operación Descartada");
                                    }
                                    Insertar.close();
                                    
                                    SubVentas xx = new SubVentas();
                                    xx.setVisible(true);
                                    dispose();
                                    
                                    
                                } catch (SQLException ex) {
                                    Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
                                    //arch.modificar("Fallida");
                                    String serverhora = ahora();
                                    txt.escribirTransacciondos(serverhora, "Fallida");
                                    
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "Contraseña Incorrecto");
                            }
                        }
                    }
                    else
                    {
                     JOptionPane.showMessageDialog(null, "Factura Vacia");

                    }
            
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Fecha incoherente: "+Fecha3);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    private void aumentar()
    {
        try {
            PreparedStatement Actualizar = Insertar.prepareStatement("UPDATE Resoluciones set INCREMENTO=INCREMENTO+1 where id='"+id.getText()+"' ");
            Actualizar.executeUpdate();
            Actualizar.close();
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    private String idVe(String U,String P)
    {
         String id=null;
        try {
           
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT id FROM Usuarios where Usuario='"+U+"' && Contraseña='"+P+"'");
            while (Ca.next()) {

               id=Ca.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;

    }
    private void CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CantidadKeyTyped
        int k = (int) evt.getKeyChar();
        if (k >= 97 && k <= 122 || k >= 65 && k <= 90) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar letras!!!", "Ventana Error Datos", JOptionPane.ERROR_MESSAGE);
        }
        if (k == 241 || k == 209) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar letras!!!", "Ventana Error Datos", JOptionPane.ERROR_MESSAGE);
        }
        if (k >= 33 && k <= 47) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar Simbolos!!!", "Ventana Error Datos", JOptionPane.ERROR_MESSAGE);
        }
        if (k == 10) {
            Cantidad.transferFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_CantidadKeyTyped

    private void NitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NitActionPerformed
        nitglobal = (String) Nit.getSelectedItem();
        String Completo = (String) Nit.getSelectedItem();
        llenarCl(Completo);

    }//GEN-LAST:event_NitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        SubVentas y = new SubVentas();
        y.setVisible(true);
        dispose();  // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void addcliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addcliActionPerformed
        // TODO add your handling code here:
        Clientes cl = new Clientes();
        cl.setVisible(true);
        dispose();

    }//GEN-LAST:event_addcliActionPerformed

    private void breturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_breturnActionPerformed
        // TODO add your handling code here:
        SubVentas men = new SubVentas();
        men.setVisible(true);
        dispose();


    }//GEN-LAST:event_breturnActionPerformed

    private void UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserActionPerformed
        US=(String) User.getSelectedItem();
        // TODO add your handling code here:
    }//GEN-LAST:event_UserActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
           int seleccionar = 0;
        seleccionar = Factura.getSelectedRow();
        if (seleccionar == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un fila valida");
        } else {
           modelo.removeRow(seleccionar);
        }
        sumar_columnas();

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void PrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrKeyPressed
        
        // TODO add your handling code here:
    }//GEN-LAST:event_PrKeyPressed

    private void PrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PrKeyTyped
      int k = (int) evt.getKeyChar();
        if (k == 10) {
           
           
      
            if(textAutoCompleter.itemExists(textAutoCompleter.getItemSelected()))
            {
                llenar((String)textAutoCompleter.getItemSelected());
                valor=true;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No existe el producto");

            }
                        
        }           // TODO add your handling code here:
    }//GEN-LAST:event_PrKeyTyped

    private void NombrePKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombrePKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_NombrePKeyTyped
    private void llenar(String datos)
    {
        String[] parts = datos.split(",");
       String part1 = parts[0]; 
       Codigo=part1;
       llenarPM(Codigo);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ApellidoM;
    private javax.swing.JTextField Cantidad;
    private javax.swing.JLabel Existencia;
    private rojerusan.RSTableMetro Factura;
    private com.toedter.calendar.JDateChooser Fech;
    private javax.swing.JLabel Marca;
    private javax.swing.JLabel MarcaM;
    private javax.swing.JLabel N;
    private javax.swing.JLabel NY;
    private javax.swing.JComboBox<String> Nit;
    private javax.swing.JLabel Nombre;
    private javax.swing.JLabel NombreM;
    private javax.swing.JLabel NombreP;
    private javax.swing.JLabel NombrePM;
    private javax.swing.JLabel Numero;
    private javax.swing.JPasswordField PW;
    private javax.swing.JTextField Pr;
    private javax.swing.JLabel Serie;
    private javax.swing.JLabel TC;
    private javax.swing.JLabel TC1;
    private javax.swing.JLabel Uni;
    private javax.swing.JComboBox<String> User;
    private javax.swing.JPanel Usuario;
    private javax.swing.JButton addcli;
    private javax.swing.JButton addfila;
    private javax.swing.JButton breturn;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelcliente;
    private javax.swing.JPanel panelfactura;
    private javax.swing.JPanel panelproducto;
    // End of variables declaration//GEN-END:variables
}
