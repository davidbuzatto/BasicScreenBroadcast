/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;

/**
 *
 * @author David
 */
public class Constants {
    
    public static final Cursor CROSSHAIR_CURSOR = new Cursor( Cursor.CROSSHAIR_CURSOR );
    public static final Cursor MOVE_CURSOR = new Cursor( Cursor.MOVE_CURSOR );
    public static final Cursor E_RESIZE_CURSOR = new Cursor( Cursor.E_RESIZE_CURSOR );
    public static final Cursor W_RESIZE_CURSOR = new Cursor( Cursor.W_RESIZE_CURSOR );
    public static final Cursor N_RESIZE_CURSOR = new Cursor( Cursor.N_RESIZE_CURSOR );
    public static final Cursor S_RESIZE_CURSOR = new Cursor( Cursor.S_RESIZE_CURSOR );
    public static final Cursor NE_RESIZE_CURSOR = new Cursor( Cursor.NE_RESIZE_CURSOR );
    public static final Cursor NW_RESIZE_CURSOR = new Cursor( Cursor.NW_RESIZE_CURSOR );
    public static final Cursor SE_RESIZE_CURSOR = new Cursor( Cursor.SE_RESIZE_CURSOR );
    public static final Cursor SW_RESIZE_CURSOR = new Cursor( Cursor.SW_RESIZE_CURSOR );
    
    public static final Color DEFAULT_COORD_LABEL_COLOR = 
            Color.BLACK;
    public static final Color DEFAULT_COORD_LABEL_BACKGOUND = 
            new Color( 255, 255, 255, 100 );
    
    public static final Color DEFAULT_RECT_BORDER_COLOR = 
            new Color( 0, 51, 102, 100 );
    public static final Color DEFAULT_RECT_FILL_COLOR = 
            new Color( 0, 102, 153, 100 );
    
    public static final Color SELECTED_RECT_BORDER_COLOR = 
            new Color( 24, 130, 27, 100 );
    public static final Color SELECTED_RECT_FILL_COLOR = 
            new Color( 98, 200, 98, 100 );
    
    public static final Color DELETE_RECT_BORDER_COLOR = 
            new Color( 200, 51, 102, 100 );
    public static final Color DELETE_RECT_FILL_COLOR = 
            new Color( 200, 102, 153, 100 );
    
    public static final BasicStroke DRAW_RECT_STROKE = new BasicStroke( 2 );
    public static final Font DRAW_RECT_FONT = new Font( "SansSerif", Font.BOLD, 12 );
    public static final FontMetrics DRAW_RECT_FONT_METRICS = Utils.createFontMetrics( DRAW_RECT_FONT );
    
}
