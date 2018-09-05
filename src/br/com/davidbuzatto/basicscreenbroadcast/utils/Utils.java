/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.utils;

import java.awt.Color;
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
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.Utilities;

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
            if ( img != null ) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write( img, "png", baos );
                baos.flush();
                imagesInBytes.add( baos.toByteArray() );
                baos.close();
            }
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
    
    public static void insertFormattedTextJTextPane( JTextPane textPane, String message, Color color ) {
        
        try {
            
            Document document = textPane.getDocument();
            
            SimpleAttributeSet attr = new SimpleAttributeSet();
            
            StyleConstants.setForeground( attr, color );
            StyleConstants.setItalic( attr, false );
            StyleConstants.setBold( attr, true );
            
            document.insertString( document.getLength(), message, attr );
            
        } catch ( BadLocationException exc ) {
            System.err.println( exc.getMessage() );
            exc.printStackTrace();
        }
        
    }
    
    public static void insertFormattedExceptionTextJTextPane( JTextPane textPane, Exception exc, Color color ) {
        
        try {
            
            Document document = textPane.getDocument();
            
            SimpleAttributeSet attr = new SimpleAttributeSet();
            
            StyleConstants.setForeground( attr, color );
            StyleConstants.setItalic( attr, false );
            StyleConstants.setBold( attr, true );
            
            document.insertString( document.getLength(), "    Message: " + exc.getMessage(), attr );
            document.insertString( document.getLength(), "\n      Cause: " + exc.getCause(), attr );
            document.insertString( document.getLength(), "\n      Class: " + exc.getClass(), attr );
            document.insertString( document.getLength(), "\nStack Trace: \n", attr );
            
            StyleConstants.setBold( attr, false );
            document.insertString( document.getLength(), processException( exc ), attr );
            
        } catch ( BadLocationException ex ) {
            System.err.println( ex.getMessage() );
            exc.printStackTrace();
        }
        
    }
    
    private static String processException( Exception exc ) {
        
        StringBuilder sb = new StringBuilder();
        
        for ( StackTraceElement e : exc.getStackTrace() ) {
            String mData = e.toString().trim();
            if ( !mData.isEmpty() ) {
                sb.append( "    " ).append( mData ).append( "\n" );
            }
        }
        
        return sb.toString();
        
    }
    
}
