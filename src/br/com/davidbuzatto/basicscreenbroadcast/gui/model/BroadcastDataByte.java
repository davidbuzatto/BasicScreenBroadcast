/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author David
 */
public class BroadcastDataByte implements Serializable {

    private String command;
    private String commandParams;
    
    private List<BroadcastArea> broadcastAreas;
    private Point cursorPosition;
    private List<byte[]> images;

    public BroadcastDataByte( 
            String command,
            String commandParams,
            List<BroadcastArea> broadcastAreas, 
            List<byte[]> images, 
            Point cursorPosition ) {
        this.command = command;
        this.commandParams = commandParams;
        this.broadcastAreas = broadcastAreas;
        this.images = images;
        this.cursorPosition = cursorPosition;
    }

    public List<BroadcastArea> getBroadcastAreas() {
        return broadcastAreas;
    }

    public void setBroadcastAreas( List<BroadcastArea> broadcastAreas ) {
        this.broadcastAreas = broadcastAreas;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages( List<byte[]> images ) {
        this.images = images;
    }

    public Point getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition( Point cursorPosition ) {
        this.cursorPosition = cursorPosition;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand( String command ) {
        this.command = command;
    }

    public String getCommandParams() {
        return commandParams;
    }

    public void setCommandParams( String commandParams ) {
        this.commandParams = commandParams;
    }

}
