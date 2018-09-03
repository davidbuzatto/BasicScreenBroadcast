/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.client;

import br.com.davidbuzatto.basicscreenbroadcast.gui.ImageWindow;
import br.com.davidbuzatto.basicscreenbroadcast.gui.MainWindow;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastArea;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastDataByte;
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
 *
 * @author David
 */
public class Client {
    
    private String host;
    private int port;
    
    private Socket clientSocketFromServer;
    private ObjectInputStream ois;
    private List<ImageWindow> imageWindows;
    
    private ServerDataThread clientDataThread;
    private ExecutorService executorService;

    private MainWindow mainWindow;
    
    public Client( String host, int port, MainWindow mainWindow ) throws IOException {

        this.port = port;
        this.host = host;
        this.mainWindow = mainWindow;
        
        imageWindows = new ArrayList<>();
        
        clientSocketFromServer = SocketFactory.getDefault().createSocket( host, port );
        clientDataThread = new ServerDataThread();
        executorService = new ThreadPoolExecutor( 1, 1, 1, 
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( 1 ) );

    }

    public void start() {
        imageWindows.clear();
        executorService.execute( this.clientDataThread );
    }
    
    public void stop() throws IOException {
        
        for ( ImageWindow iw : imageWindows ) {
            iw.setVisible( false );
            iw.dispose();
        }
        
        clientSocketFromServer.close();
        clientDataThread.stop();
        executorService.shutdown();
        
    }
    
    private void stopClient() {
        try {
            stop();
        } catch ( IOException exc ) {
            Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                    "--- I/O Exception - Stop client didn't work as intended! ---\n", Color.RED );
            Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.RED );
        }
    }

    private class ServerDataThread implements Runnable {

        private boolean running = true;

        @Override
        public void run() {

            Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                    "Client Data Thread is Running!\n", 
                    Constants.OK_OUTPUT_MESSAGE_COLOR );
            
            while ( running ) {

                try {
                    
                    if ( ois == null ) {
                        ois = new ObjectInputStream( clientSocketFromServer.getInputStream() );
                    }
                    
                    /*BroadcastData data = (BroadcastData) ois.readObject();
                    
                    if ( imageWindows.isEmpty() ) {
                        for ( BroadcastArea ba : data.getBroadcastAreas() ) {
                            ImageWindow iw = new ImageWindow();
                            iw.setVisible( true );
                            imageWindows.add( iw );
                        }
                    }
                    
                    int i = 0;
                    for ( ImageWindow iw : imageWindows ) {
                        iw.setData( 
                                data.getBroadcastAreas().get( i ), 
                                data.getImages().get( i ),
                                data.getCursorPosition() );
                        i++;
                    }*/
                    
                    BroadcastDataByte data = (BroadcastDataByte) ois.readObject();
                    
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
                    
                    try {
                        Thread.sleep( 15 );
                    } catch ( InterruptedException exc ) {

                        Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                                "--- InterruptedException - Can't sleep! ---\n", Color.RED );
                        Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.RED );
                        stopClient();

                    }
                    
                } catch ( IOException | ClassNotFoundException exc ) {
                    
                    Utils.insertFormattedTextJTextPane( mainWindow.getTxtPaneOutputAndError(), 
                            "--- I/O Exception - Can't read image! ---\n", Color.RED );
                    Utils.insertFormattedExceptionTextJTextPane( mainWindow.getTxtPaneOutputAndError(), exc, Color.RED );
                    stopClient();
                    
                }

            }

        }
        
        public void stop() {
            running = false;
        }

    }
    
}
