/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.model;

import java.awt.Rectangle;

/**
 *
 * @author David
 */
public class BroadcastArea implements Cloneable {
    
    private String name;
    private Rectangle rectangle;

    public BroadcastArea() {
        this( "empty", new Rectangle( 0, 0, 20, 20 ) );
    }

    public BroadcastArea( String name, Rectangle rectangle ) {
        this.rectangle = rectangle;
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
    
    public int getArea() {
        return rectangle.width * rectangle.height;
    }

    @Override
    public Object clone() {

        BroadcastArea c = null;
        
        try {
            
            c = (BroadcastArea) super.clone();

            c.rectangle = new Rectangle(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height );

            c.name = name;
            
        } catch ( CloneNotSupportedException exc ) {
            System.out.println( "Clone not supported in " + getClass().getName() );
        }
        
        return c;

    }
    
    @Override
    public String toString() {
        return String.format( 
                "<html><font color='%s'>%s - left: %d; top: %d; right: %d; bottom: %d | Area: %d</font></html>",
                getArea() >= 2500 ? "#A8D7FF" : "#FFA8A8",
                name,
                rectangle.x, 
                rectangle.y, 
                rectangle.x + rectangle.width, 
                rectangle.y + rectangle.height,
                getArea() );
    }
    
}
