/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui.client;

import br.com.davidbuzatto.basicscreenbroadcast.gui.ImageWindow;
import br.com.davidbuzatto.basicscreenbroadcast.gui.MainWindow;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastArea;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastData;
import br.com.davidbuzatto.basicscreenbroadcast.utils.Constants;
import br.com.davidbuzatto.basicscreenbroadcast.utils.Utils;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;

/**
 * Warning!
 * This class is tightly coupled with the MainWindow class.
 * 
 * @author David
 */
public class Client {
    
    private String host;
    private int port;
    
    private Socket socket;
    private ObjectInputStream ois;
    private List<ImageWindow> imageWindows;
    
    private ClientDataThread clientDataThread;
    private ExecutorService executorService;

    private MainWindow mainWindow;
    
    public Client( String host, int port, MainWindow mainWindow ) throws IOException {

        this.port = port;
        this.host = host;
        this.mainWindow = mainWindow;
        
        imageWindows = new ArrayList<>();
        
        socket = SocketFactory.getDefault().createSocket( host, port );
        ois = new ObjectInputStream( socket.getInputStream() );
        
        clientDataThread = new ClientDataThread();
        executorService = new ThreadPoolExecutor( 1, 1, 1, 
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( 1 ) );

    }

    public void start() {
        imageWindows.clear();
        executorService.execute( this.clientDataThread );
    }
    
    private void stopClient() {
        try {
            stop();
        } catch ( IOException exc ) {
            Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                    "\n--------------------\n" +
                    "CLIENT> --- I/O Exception - Stop client didn't work as intended! ---\n", Color.RED );
            Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.ORANGE );
        }
    }
    
    public void stop() throws IOException {
        
        for ( ImageWindow iw : imageWindows ) {
            iw.setVisible( false );
            iw.dispose();
        }
        
        mainWindow.getBtnClientConnect().setEnabled( true );
        mainWindow.getBtnClientDisconnect().setEnabled( false );
                
        socket.close();
        ois.close();
        
        clientDataThread.stop();
        executorService.shutdown();
        
    }

    private class ClientDataThread implements Runnable {

        private boolean running = true;

        @Override
        public void run() {

            Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                    "CLIENT> Client Data Thread is Running!\n", 
                    Constants.OK_OUTPUT_MESSAGE_COLOR );
            
            while ( running ) {

                try {
                    
                    BroadcastData data = (BroadcastData) ois.readObject();
                    
                    switch ( data.getCommand() ) {
                        
                        case "dataTransfer":
                            
                            if ( imageWindows.isEmpty() ) {
                                for ( BroadcastArea ba : data.getBroadcastAreas() ) {
                                    ImageWindow iw = new ImageWindow();
                                    iw.setVisible( true );
                                    iw.setBroadcastArea( ba );
                                    imageWindows.add( iw );
                                }
                            }

                            int i = 0;
                            for ( ImageWindow iw : imageWindows ) {
                                iw.setData(
                                        Utils.byteArrayToBufferedImage( data.getImages().get( i ) ),
                                        data.getCursorPosition() );
                                i++;
                            }
                            
                            break;
                            
                        default:
                            System.err.println( "Command " + data.getCommand() + " does not exists!" );
                            break;
                        
                    }
                    
                    try {
                        Thread.sleep( 15 );
                    } catch ( InterruptedException exc ) {

                        Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                                "\n--------------------\n" +
                                "CLIENT> --- InterruptedException - Can't sleep! ---\n", Color.RED );
                        Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.ORANGE );
                        stopClient();

                    }
                    
                } catch ( IOException | ClassNotFoundException exc ) {
                    
                    Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                            "\n--------------------\n" +
                            "CLIENT> --- I/O Exception - Can't read image! ---\n", Color.RED );
                    Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.ORANGE );
                    stopClient();
                    
                }

            }

        }
        
        public void stop() {
            running = false;
        }

    }
    
}
