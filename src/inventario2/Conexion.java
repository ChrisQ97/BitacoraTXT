/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario2;

/**
 *
 * @author sys515
 */
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sys515
 */
public class Conexion {

    Connection cn;
    Statement st;

    public Connection conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //cn = DriverManager.getConnection("jdbc:mysql://localhost/InventarioPEPS/UEPS?useSSL=false", "root", "InventarioPEPS");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/InventarioPEPS/UEPS?useSSL=false", "root", "1997");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("no conecta localhost::3306");
        }
        return cn;
    }

    Statement createStatement() {
        throw new UnsupportedOperationException("No soportado");
    }
}
