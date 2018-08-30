/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.server;

import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastArea;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastDataByte;
import br.com.davidbuzatto.basicscreenbroadcast.utils.Utils;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ServerSocketFactory;

/**
 *
 * @author David
 */
public class Server {

    private static Robot robot;
    private int port;
    private int fps;
    private ServerSocket serverSocket;
    
    private List<Socket> clientSockets;
    private List<BroadcastArea> broadcastAreas;
    
    private Socket newClientSocket;
    private ServerConnectionThread serverConnectionThread;
    private ServerDataThread serverDataThread;
    
    private ExecutorService executorService;

    private ObjectOutputStream oos;

    public Server( int port, int fps, List<BroadcastArea> broadcastAreas ) throws AWTException, IOException {

        if ( robot == null ) {
            try {
                robot = new Robot();
            } catch ( AWTException exc ) {
                throw new AWTException( "Can't create Robot!" );
            }
        }
        
        this.port = port;
        this.fps = fps;
        
        this.clientSockets = Collections.synchronizedList( new ArrayList<Socket>() );
        
        this.broadcastAreas = Collections.synchronizedList( new ArrayList<BroadcastArea>() );
        setBroadcastAreas( broadcastAreas );

        this.serverSocket = ServerSocketFactory.getDefault().createServerSocket( port );
        
        this.serverConnectionThread = new ServerConnectionThread();
        this.serverDataThread = new ServerDataThread();
        
        this.executorService = new ThreadPoolExecutor( 10, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( 10 ) );

    }

    public void start() {
        executorService.execute( this.serverConnectionThread );
        executorService.execute( this.serverDataThread );
    }
    
    public void stop() throws IOException {
        serverSocket.close();
        serverConnectionThread.stop();
        serverDataThread.stop();
        executorService.shutdown();
    }
    
    private void stopServer() {
        try {
            stop();
        } catch ( IOException exci ) {
            System.err.println( "Stop server didn't work as intended!" );
            System.err.println( exci.getMessage() );
        }
    }
    
    public void setBroadcastAreas( List<BroadcastArea> broadcastAreas ) {
        
        for ( BroadcastArea ba : broadcastAreas ) {
            this.broadcastAreas.add( ba );
        }
        
    }
    
    private class ServerConnectionThread implements Runnable {

        private boolean running = true;
        
        @Override
        public void run() {
            
            System.out.println( "Server Connection Thread is Running!" );
            
            while ( running ) {
                try {
                    System.out.println( "Server Connection Thread is waiting for a new connection..." );
                    newClientSocket = serverSocket.accept();
                } catch ( IOException exc ) {
                    
                    System.err.println( "Can't accept a new connection!" );
                    System.err.println( exc.getMessage() );
                    stopServer();
                    
                }
            }
            
        }
        
        public void stop() {
            running = false;
        }

    }

    private class ServerDataThread implements Runnable {

        private boolean running = true;

        @Override
        public void run() {

            System.out.println( "Server Data Thread is Running!" );
            
            while ( running ) {

                List<BufferedImage> imagesToSend = new ArrayList<>();
                
                for ( BroadcastArea ba : broadcastAreas ) {
                    imagesToSend.add( robot.createScreenCapture( ba.getRectangle() ) );
                }

                for ( Socket cs : clientSockets ) {
                    
                    //System.out.println( "Sending data to client: " + cs.toString() );
                    
                    try {
                        
                        if ( oos == null ) {
                            oos = new ObjectOutputStream( cs.getOutputStream() );
                        }
                        
                        /*BroadcastData d = new BroadcastData( 
                                broadcastAreas, 
                                imagesToSend, 
                                MouseInfo.getPointerInfo().getLocation() );*/
                        
                        BroadcastDataByte d = new BroadcastDataByte( 
                                broadcastAreas, 
                                Utils.bufferedImageListToByteArrayList( imagesToSend ), 
                                MouseInfo.getPointerInfo().getLocation() );
                            
                        oos.writeObject( d );
                        oos.flush();
                        
                    } catch ( IOException exc ) {
                        
                        System.err.println( "Can't send data to client." );
                        System.err.println( exc.getMessage() );
                        stopServer();
                        
                    }
                    
                }
                
                if ( newClientSocket != null && !clientSockets.contains( newClientSocket ) ) {
                    clientSockets.add( newClientSocket );
                }
                
                try {
                    Thread.sleep( fpsToMilis( fps ) );
                } catch ( InterruptedException exc ) {
                    
                    System.err.println( "Can't sleep " + getClass().getName() );
                    stopServer();
                    
                }

            }

        }

        private long fpsToMilis( int fps ) {
            return 1000L / fps;
        }
        
        public void stop() throws IOException {
            running = false;
            if ( oos != null ) {
                oos.close();
            }
        }

    }

}
