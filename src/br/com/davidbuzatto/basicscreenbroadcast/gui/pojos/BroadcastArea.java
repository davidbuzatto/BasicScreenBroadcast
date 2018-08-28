/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.pojos;

import java.awt.Rectangle;

/**
 *
 * @author David
 */
public class BroadcastArea {
    
    private Rectangle rectangle;

    public BroadcastArea() {
        rectangle = new Rectangle( 0, 0, 10, 10 );
    }

    public BroadcastArea( Rectangle rectangle ) {
        this.rectangle = rectangle;
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
    public String toString() {
        return String.format( 
                "<html><font color='%s'>Broadcast Area Delimiter - left: %d; top: %d; right: %d; bottom: %d | Area: %d</font></html>",
                getArea() > 200 ? "#A8D7FF" : "#FFA8A8",
                rectangle.x, 
                rectangle.y, 
                rectangle.x + rectangle.width, 
                rectangle.y + rectangle.height,
                getArea() );
    }
    
}
