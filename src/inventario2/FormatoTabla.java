/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario2;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author sys515
 */
public class FormatoTabla extends DefaultTableCellRenderer{

    private int columna_patron ;

    public FormatoTabla(int Colpatron)
    {
        this.columna_patron = Colpatron;
    }

    
    public Component getTableCellRendererComponent ( JTable table, Object value, boolean selected, boolean focused, int row, int column )
    {        
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        //Si la celda corresponde a una fila con estado FALSE, se cambia el color de fondo a rojo
        if( Integer.parseInt(String.valueOf(table.getValueAt(row,4)))<=Integer.parseInt(String.valueOf(table.getValueAt(row,5))))
        {
            setBackground(Color.red);
        }

        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
 }

}