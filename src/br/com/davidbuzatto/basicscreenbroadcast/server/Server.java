/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.server;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.net.ServerSocketFactory;

/**
 *
 * @author David
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException, IOException {
        
        Robot r = new Robot();
        BufferedImage img = r.createScreenCapture( new Rectangle( Toolkit.getDefaultToolkit().getScreenSize() ) );
        
        ServerSocket s = ServerSocketFactory.getDefault().createServerSocket( 8085 );
        Socket d = s.accept();
        
        ImageIO.write( img, "bmp", d.getOutputStream() );
        
    }
    
}
