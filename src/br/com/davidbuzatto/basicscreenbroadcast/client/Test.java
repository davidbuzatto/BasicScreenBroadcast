/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class Test extends JFrame {
    
    private BufferedImage im;
    
    public Test( BufferedImage im ) {
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 500, 500 );
        this.im = im;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        if ( im != null ) {
            g.drawImage( im, 0, 0, null );
        }
    }
    
}
