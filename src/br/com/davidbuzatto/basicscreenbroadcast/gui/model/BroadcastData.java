/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Does not work...
 * 
 * @author David
 */
public class BroadcastData implements Serializable {

    private List<BroadcastArea> broadcastAreas;
    private Point cursorPosition;
    transient private List<BufferedImage> images;

    public BroadcastData( List<BroadcastArea> broadcastAreas, 
            List<BufferedImage> images, 
            Point cursorPosition ) {
        this.broadcastAreas = broadcastAreas;
        this.images = images;
        this.cursorPosition = cursorPosition;
    }

    private void writeObject( ObjectOutputStream out ) throws IOException {
        
        out.defaultWriteObject();
        out.writeInt( images.size() );
        
        for ( BufferedImage img : images ) {
            ImageIO.write( img, "png", out );
        }
        
    }

    private void readObject( ObjectInputStream in ) throws IOException, ClassNotFoundException {
        
        in.defaultReadObject();
        final int imageCount = in.readInt();
        
        images = new ArrayList<BufferedImage>( imageCount );
        
        for ( int i = 0; i < imageCount; i++ ) {
            images.add( ImageIO.read( in ) );
        }
        
    }

    public List<BroadcastArea> getBroadcastAreas() {
        return broadcastAreas;
    }

    public void setBroadcastAreas( List<BroadcastArea> broadcastAreas ) {
        this.broadcastAreas = broadcastAreas;
    }

    public List<BufferedImage> getImages() {
        return images;
    }

    public void setImages( List<BufferedImage> images ) {
        this.images = images;
    }

    public Point getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition( Point cursorPosition ) {
        this.cursorPosition = cursorPosition;
    }

}
