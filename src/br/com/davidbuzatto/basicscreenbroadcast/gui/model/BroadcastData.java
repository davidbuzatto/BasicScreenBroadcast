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
 * This class is used to transfer data between client and server.
 * The application stabilish a protocol with these commands/parameters.
 * 
 * +--------------+---------------------------------------------------+
 * | command      | behavior                                          |
 * +--------------+---------------------------------------------------+
 * | connect      | client requests to connect on server.             |
 * +--------------+---------------------------------------------------+
 * | connected    | server accepts the connection requested and       |
 * |              | informs the client.                               |
 * +--------------+---------------------------------------------------+
 * | disconnect   | client requests to disconnect from server.        |
 * +--------------+---------------------------------------------------+
 * | disconnected | server disconnects a client.                      |
 * +--------------+---------------------------------------------------+
 * | dataTransfer | server sends data to client (after a successfull  |
 * |              | connection).                                      |
 * +--------------+---------------------------------------------------+
 * 
 * @author David
 */
public class BroadcastData implements Serializable {
    
    private String command;
    private String commandParams;
    
    private List<BroadcastArea> broadcastAreas;
    private Point cursorPosition;
    private List<byte[]> images;

    public BroadcastData(){
    }
    
    public BroadcastData( 
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

    public static class BroadcastDataFactory {
        
        public static BroadcastData create() {
            return new BroadcastData();
        }
        
        public static BroadcastData createConnect() {
            BroadcastData bd = create();
            bd.setCommand( "connect" );
            return bd;
        }
        
        public static BroadcastData createConnected() {
            BroadcastData bd = create();
            bd.setCommand( "connected" );
            return bd;
        }
        
        public static BroadcastData createDisconnect() {
            BroadcastData bd = create();
            bd.setCommand( "disconnect" );
            return bd;
        }
        
        public static BroadcastData createDisconnected() {
            BroadcastData bd = create();
            bd.setCommand( "disconnected" );
            return bd;
        }
        
        public static BroadcastData createDataTransfer() {
            BroadcastData bd = create();
            bd.setCommand( "dataTransfer" );
            return bd;
        }
        
    }
    
}
