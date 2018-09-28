/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario2;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chr97lubuntu
 */
public class recuperar extends javax.swing.JFrame {
Conexion con = new Conexion();
   
    Connection Consulta = con.conexion();
    boolean verificado;
    /**
     * Creates new form recuperar
     */
    public recuperar() {
        initComponents();
          this.setDefaultCloseOperation(this.HIDE_ON_CLOSE); 
           Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();    
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        admin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        passadmin = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        usuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        recordar = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(17, 111, 172));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Usuario administrador");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanel1.add(admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 200, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Contraseña administrador");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 210, -1));
        jPanel1.add(passadmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 200, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Usuario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 190, -1));
        jPanel1.add(usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 200, -1));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Contraseña: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        recordar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        recordar.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(recordar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 200, 20));

        jButton1.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconoso/icons8-contraseña-1-50.png"))); // NOI18N
        jButton1.setText("Recuperar contraseña");
        jButton1.setContentAreaFilled(false);
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/iconoso/icons8-contraseña-1-filled-50.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String adminis = admin.getText();
        char claves[]=passadmin.getPassword();
        String clavedef=new String(claves);
        
        
        String puest=" ";
        
        try{
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Usuario, Contraseña, Privilegios FROM Usuarios");
            while (Ca.next()) {
               // System.out.println(Ca.getString(1)+" -- "+Ca.getString(2));
               // System.out.println(us+"***"+clavedef);
               if((adminis.equals(Ca.getString(1)))&&(clavedef.equals(Ca.getString(2)))){
                   verificado=true;
                   //System.out.println("entre");
                   puest=Ca.getString(3);
               }
                              
            }
            
        }catch(SQLException ex){
            Logger.getLogger(Ingreso.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(verificado==true){
           // System.out.println("Bienvenido "+us);
            if(puest.equals("Vendedor")){
            sinpermisos sp = new sinpermisos();
            sp.setVisible(true);
            }
            else{
               String nueva = contra(usuario.getText());
                
               if(nueva.equals("")){
                   
               }
               else{
                   recordar.setVisible(false);
                   recordar.setText(nueva);
                   recordar.setVisible(true);
               }
            }
            
        }
        else{
           // JOptionPane.showMessageDialog(null, "Datos incorrectos");
            //System.out.println("no");
            //JOptionPane.showMessageDialog(null," Aprendiendo a poner imagenes ", "Imagen Java", JOptionPane.PLAIN_MESSAGE, icono);
           inva in = new inva();
           in.setVisible(true);
        }
        
        
        
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    public String contra(String us){
        String st = "";
        
          try{
            Statement sx = Consulta.createStatement();
            ResultSet Ca = sx.executeQuery("SELECT Contraseña, Privilegios FROM Usuarios where Usuario='"+us+"'");
            while (Ca.next()) {
               // System.out.println(Ca.getString(1)+" -- "+Ca.getString(2));
               st=Ca.getString(1);
                              
            }
            
        }catch(SQLException ex){
            Logger.getLogger(Ingreso.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return st;
        
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
            java.util.logging.Logger.getLogger(recuperar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(recuperar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(recuperar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(recuperar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new recuperar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField admin;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passadmin;
    private javax.swing.JLabel recordar;
    private javax.swing.JTextField usuario;
    // End of variables declaration//GEN-END:variables
}