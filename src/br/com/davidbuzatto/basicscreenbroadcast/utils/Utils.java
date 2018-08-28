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

}
