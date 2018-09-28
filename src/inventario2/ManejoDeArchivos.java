/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventario2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chr
 */
public class ManejoDeArchivos {
    public static void main(String[] arg){
        File file = new File("bitacoratxt.txt");
        PrintWriter escribir;
        
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                escribir = new PrintWriter(file);
                escribir.println("");
                escribir.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            
        }
    }
    public void escribirTransaccion(String Server, String Status, String Table){
               File file = new File("bitacoratxt.txt");
        PrintWriter escribir;
        
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                escribir = new PrintWriter(file);
                escribir.println("       >>>>> Transaccion "+Table+" <<<< ");
                escribir.println(Server+" >> "+Status);
                escribir.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            
        } 
    }

    public void escribirTransacciontres(String Server, String Status, String Table){
            File archivo;
        FileWriter escribir;
        PrintWriter linea;
        //System.out.println("tres");
        archivo = new File("bitacoratxt.txt");
        String texto ="        >>>>> Transaccion "+Table+" <<<<" ;
        String textodos = Server+" >> "+Status;
        if(!archivo.exists()){
            System.out.println("tres no existe");
            try {
                archivo.createNewFile();
                
                escribir = new FileWriter(archivo,true);
                linea = new PrintWriter(escribir);
                linea.println(texto);
                linea.println(textodos);
                linea.close();
                escribir.close();
                
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("    tres si existe");
              try {
                              
                escribir = new FileWriter(archivo,true);
                linea = new PrintWriter(escribir);
                linea.println(texto);
                linea.println(textodos);
                linea.close();
                escribir.close();
                
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
    }}
    public void escribirTransacciondos(String Server, String Status){
      //  System.out.println("dos");
        File archivo;
        FileWriter escribir;
        PrintWriter linea;
        String texto = Server+" >> "+Status;
        archivo = new File("bitacoratxt.txt");
        if(!archivo.exists()){
            System.out.println("dos no existe");
            try {
                archivo.createNewFile();
                escribir = new FileWriter(archivo,true);
                linea = new PrintWriter(escribir);
                linea.println(texto);
                linea.close();
                escribir.close();
                
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("dos existe");
              try {
               // archivo.createNewFile();
                escribir = new FileWriter(archivo,true);
                linea = new PrintWriter(escribir);
                linea.println(texto);
                linea.close();
                escribir.close();
                
            } catch (IOException ex) {
                Logger.getLogger(ManejoDeArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
     
     
     
    
    
}
