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
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author David
 */
public class Utils {

    private static final Object LOCK_FOR_INSERTION = new Object();
    private static ImageWriter JPG_WRITER = ImageIO.getImageWritersByFormatName( "jpg" ).next();
    private static JPEGImageWriteParam JPG_PARAM = new JPEGImageWriteParam( null );
    
    static {
        JPG_PARAM.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
    }
    
    public static FontMetrics createFontMetrics( Font font ) {
        
        BufferedImage bi = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB_PRE );
        
        Graphics g = bi.getGraphics();
        FontMetrics fm = g.getFontMetrics( font );
        g.dispose();
        
        bi = null;
        
        return fm;
        
    }
    
    public static List<byte[]> bufferedImageListToByteArrayList( List<BufferedImage> imgs, String format, int imageCompression ) throws IOException {
        
        List<byte[]> imagesInBytes = new ArrayList<>();
        
        for ( BufferedImage img : imgs ) {
            if ( img != null ) {
                imagesInBytes.add( bufferedImageToByteArray( img, format, imageCompression ) );
            }
        }
        
        return imagesInBytes;
        
    }
    
    public static byte[] bufferedImageToByteArray( BufferedImage img, String format, int imageCompression ) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        if ( format.equals( "png" ) ) {
            ImageIO.write( img, format, baos );
        } else {
            
            MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream( baos );
            
            JPG_PARAM.setCompressionQuality( imageCompression / 100f );
            JPG_WRITER.setOutput( mcios );
            JPG_WRITER.write( null, new IIOImage( img, null, null ), JPG_PARAM );
            
            mcios.close();
            
        }
        
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
        
        synchronized ( LOCK_FOR_INSERTION ) {
            
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
        
    }
    
    public static void insertFormattedExceptionTextJTextPane( JTextPane textPane, Exception exc, Color color ) {
        
        synchronized ( LOCK_FOR_INSERTION ) {
            
            try {

                Document document = textPane.getDocument();

                SimpleAttributeSet attr = new SimpleAttributeSet();

                StyleConstants.setForeground( attr, color );
                StyleConstants.setItalic( attr, false );
                StyleConstants.setBold( attr, true );

                document.insertString( document.getLength(), "    Message: " + exc.getMessage(), attr );
                document.insertString( document.getLength(), "\n      Cause: " + exc.getCause(), attr );
                document.insertString( document.getLength(), "\n      Class: " + exc.getClass(), attr );
                document.insertString( document.getLength(), "\nStack Trace: ", attr );

                StyleConstants.setBold( attr, false );
                document.insertString( document.getLength(), processException( exc ), attr );

            } catch ( BadLocationException ex ) {
                System.err.println( ex.getMessage() );
                exc.printStackTrace();
            }
            
        }
        
    }
    
    private static String processException( Exception exc ) {
        
        StringBuilder sb = new StringBuilder();
        boolean firstOk = false;
        
        for ( StackTraceElement e : exc.getStackTrace() ) {
            
            String mData = e.toString().trim();
            
            if ( firstOk ) {
                sb.append( "             " );
            } else {
                firstOk = true;
            }
            
            if ( !mData.isEmpty() ) {
                sb.append( mData ).append( "\n" );
            }
            
        }
        
        return sb.toString();
        
    }
    
}
