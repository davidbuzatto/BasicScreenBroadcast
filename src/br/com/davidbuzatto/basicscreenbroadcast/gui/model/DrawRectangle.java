/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.model;

import static br.com.davidbuzatto.basicscreenbroadcast.utils.Constants.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author David
 */
public class DrawRectangle implements Cloneable {

    private String name;
    private Rectangle rectangle;
    private Color borderColor;
    private Color fillColor;

    public void draw( Graphics2D g2d ) {

        g2d.setColor( fillColor );
        g2d.fillRect( rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height );

        g2d.setColor( borderColor );
        g2d.drawRect( rectangle.x,
                rectangle.y,
                rectangle.width - 1,
                rectangle.height - 1 );

        String startCoord = String.format( "[l: %d; t: %d]", rectangle.x, rectangle.y );
        String endCoord = String.format( "[r: %d; b: %d]", rectangle.x + rectangle.width, rectangle.y + rectangle.height );

        g2d.setColor( DEFAULT_COORD_LABEL_BACKGOUND );

        g2d.fillRoundRect( rectangle.x + 3, rectangle.y + 3,
                DRAW_RECT_FONT_METRICS.stringWidth( startCoord ) + 7,
                DRAW_RECT_FONT_METRICS.getHeight() + 2, 10, 10 );

        g2d.fillRoundRect( rectangle.x + rectangle.width - DRAW_RECT_FONT_METRICS.stringWidth( endCoord ) - 11,
                rectangle.y + rectangle.height - DRAW_RECT_FONT_METRICS.getHeight() - 6,
                DRAW_RECT_FONT_METRICS.stringWidth( endCoord ) + 7,
                DRAW_RECT_FONT_METRICS.getHeight() + 2, 10, 10 );

        if ( name != null ) {
            
            g2d.fillRoundRect( 
                    rectangle.x + rectangle.width / 2 - DRAW_RECT_FONT_METRICS.stringWidth( name ) / 2 - 4, 
                    rectangle.y + rectangle.height / 2 - DRAW_RECT_FONT_METRICS.getHeight() / 2,
                    DRAW_RECT_FONT_METRICS.stringWidth( name ) + 7,
                    DRAW_RECT_FONT_METRICS.getHeight() + 2, 10, 10 );
            
        }
        
        g2d.setColor( DEFAULT_COORD_LABEL_COLOR );
        g2d.setStroke( DRAW_RECT_STROKE );
        g2d.setFont( DRAW_RECT_FONT );

        g2d.drawString( startCoord,
                rectangle.x + 7,
                rectangle.y + DRAW_RECT_FONT_METRICS.getHeight() );

        g2d.drawString( endCoord,
                rectangle.x + rectangle.width - DRAW_RECT_FONT_METRICS.stringWidth( endCoord ) - 7,
                rectangle.y + rectangle.height - DRAW_RECT_FONT_METRICS.getHeight() + 7 );
        
        if ( name != null ) {
            g2d.drawString( name, 
                    rectangle.x + rectangle.width / 2 - DRAW_RECT_FONT_METRICS.stringWidth( name ) / 2,
                    rectangle.y + rectangle.height / 2 + DRAW_RECT_FONT_METRICS.getHeight() / 2 - 2 );
        }

    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle( Rectangle rectangle ) {
        this.rectangle = rectangle;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor( Color borderColor ) {
        this.borderColor = borderColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor( Color fillColor ) {
        this.fillColor = fillColor;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        DrawRectangle c = null;
        
        try {
            
            c = (DrawRectangle) super.clone();

            c.name = name;
            c.rectangle = new Rectangle(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height );

            c.borderColor = borderColor;
            c.fillColor = fillColor;
            
        } catch ( CloneNotSupportedException exc ) {
            System.out.println( "Clone not supported in " + getClass().getName() );
        }

        return c;

    }

}
