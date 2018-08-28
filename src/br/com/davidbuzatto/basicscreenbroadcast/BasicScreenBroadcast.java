/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast;

import br.com.davidbuzatto.basicscreenbroadcast.gui.MainWindow;
import com.bulenkov.darcula.DarculaLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 *
 * @author David
 */
public class BasicScreenBroadcast {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Darcula look and feel */
        try {
            BasicLookAndFeel darcula = new DarculaLaf();
            UIManager.setLookAndFeel(darcula);
        } catch ( UnsupportedLookAndFeelException exc ) {
            
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
        
    }
    
}
