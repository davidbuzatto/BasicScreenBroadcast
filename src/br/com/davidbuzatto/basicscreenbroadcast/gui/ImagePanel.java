/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui;

import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastArea;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class ImagePanel extends JPanel {

    private BroadcastArea broadcastArea;
    private BufferedImage image;
    private Point cursorPosition;
    
    public ImagePanel() {
        setDoubleBuffered( true );
    }
    
    @Override
    protected void paintComponent( Graphics g ) {
        
        super.paintComponent( g );
        
        if ( image != null ) {
            g.drawImage( image, 0, 0, getWidth(), getHeight(), null );
        } else {
            g.setColor( Color.WHITE );
            g.drawString( "please wait... starting transmission.", 0, 20 );
        }
        
        if ( cursorPosition != null ) {
            g.setColor( Color.RED );
            g.fillRect( cursorPosition.x, cursorPosition.y, 5, 5 );
        }
        
        g.setColor( Color.WHITE );
        
        if ( cursorPosition != null && broadcastArea != null ) {
            
            Point insidepPoint = new Point();
            insidepPoint.x = cursorPosition.x + broadcastArea.getRectangle().x;
            insidepPoint.y = cursorPosition.y + broadcastArea.getRectangle().y;
            
            if ( broadcastArea.getRectangle().contains( insidepPoint ) ) {
                g.setColor( Color.RED );
            }
            
        } else {
            g.setColor( Color.WHITE );
        }
        
        g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
        
    }

    public void setBroadcastArea( BroadcastArea broadcastArea ) {
        this.broadcastArea = broadcastArea;
    }
    
    public void setImage( BufferedImage image ) {
        this.image = image;
    }

    public void setCursorPosition( Point cursorPosition ) {
        this.cursorPosition = cursorPosition;
    }
    
}
