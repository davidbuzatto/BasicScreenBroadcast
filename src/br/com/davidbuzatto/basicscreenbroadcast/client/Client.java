/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.net.SocketFactory;

/**
 *
 * @author David
 */
public class Client {
    
    public static void main(String[] args) throws IOException {
        
        Socket s = SocketFactory.getDefault().createSocket( "localhost", 8085 );
        ImageInputStream i = ImageIO.createImageInputStream( s.getInputStream() );
        BufferedImage bm = ImageIO.read(i);
        
        new Test( bm ).setVisible(true);
        
        
    }
    
}
