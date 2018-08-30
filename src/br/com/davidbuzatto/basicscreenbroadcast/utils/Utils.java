/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author David
 */
public class Utils {

    public static FontMetrics createFontMetrics( Font font ) {
        
        BufferedImage bi = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB_PRE );
        
        Graphics g = bi.getGraphics();
        FontMetrics fm = g.getFontMetrics( font );
        g.dispose();
        
        bi = null;
        
        return fm;
        
    }
    
    public static List<byte[]> bufferedImageListToByteArrayList( List<BufferedImage> imgs ) throws IOException {
        
        List<byte[]> imagesInBytes = new ArrayList<>();
        
        for ( BufferedImage img : imgs ) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( img, "png", baos );
            baos.flush();
            imagesInBytes.add( baos.toByteArray() );
            baos.close();
        }
        
        return imagesInBytes;
        
    }
    
    public static byte[] bufferedImageToByteArray( BufferedImage img ) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( img, "png", baos );
        baos.flush();
        byte[] imageInBytes = baos.toByteArray();
        baos.close();
        
        return imageInBytes;
        
    }
    
    public static BufferedImage byteArrayToBufferedImage( byte[] imgData ) throws IOException {
        
        InputStream in = new ByteArrayInputStream( imgData );
        BufferedImage buffImg = ImageIO.read( in );
        
        return buffImg;
        
    }

}
