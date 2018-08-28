/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui;

import br.com.davidbuzatto.basicscreenbroadcast.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author David
 */
public class BroadcastAreaSelectPanel extends JPanel {
    
    private class DrawRectangle {
        Rectangle rect;
        Color borderColor;
        Color fillColor;
    }
    
    private enum DragType {
        E_RESIZE,
        W_RESIZE,
        N_RESIZE,
        S_RESIZE,
        NE_RESIZE,
        NW_RESIZE,
        SE_RESIZE,
        SW_RESIZE,
        MOVE,
        NONE
    }
    
    private static final Cursor CROSSHAIR_CURSOR = new Cursor( Cursor.CROSSHAIR_CURSOR );
    private static final Cursor MOVE_CURSOR = new Cursor( Cursor.MOVE_CURSOR );
    private static final Cursor E_RESIZE_CURSOR = new Cursor( Cursor.E_RESIZE_CURSOR );
    private static final Cursor W_RESIZE_CURSOR = new Cursor( Cursor.W_RESIZE_CURSOR );
    private static final Cursor N_RESIZE_CURSOR = new Cursor( Cursor.N_RESIZE_CURSOR );
    private static final Cursor S_RESIZE_CURSOR = new Cursor( Cursor.S_RESIZE_CURSOR );
    private static final Cursor NE_RESIZE_CURSOR = new Cursor( Cursor.NE_RESIZE_CURSOR );
    private static final Cursor NW_RESIZE_CURSOR = new Cursor( Cursor.NW_RESIZE_CURSOR );
    private static final Cursor SE_RESIZE_CURSOR = new Cursor( Cursor.SE_RESIZE_CURSOR );
    private static final Cursor SW_RESIZE_CURSOR = new Cursor( Cursor.SW_RESIZE_CURSOR );
    
    private static final Color DEFAULT_COORD_LABEL_COLOR = 
            Color.BLACK;
    private static final Color DEFAULT_COORD_LABEL_BACKGOUND = 
            new Color( 255, 255, 255, 100 );
    
    private static final Color DEFAULT_RECT_BORDER_COLOR = 
            new Color( 0, 51, 102, 100 );
    private static final Color DEFAULT_RECT_FILL_COLOR = 
            new Color( 0, 102, 153, 100 );
    
    private static final Color SELECTED_RECT_BORDER_COLOR = 
            new Color( 24, 130, 27, 100 );
    private static final Color SELECTED_RECT_FILL_COLOR = 
            new Color( 98, 200, 98, 100 );
    
    private static final Color DELETE_RECT_BORDER_COLOR = 
            new Color( 200, 51, 102, 100 );
    private static final Color DELETE_RECT_FILL_COLOR = 
            new Color( 200, 102, 153, 100 );
    
    private List<DrawRectangle> selectedRectDrawList;
    private List<Rectangle> selectedRectScreenList;
    
    private DrawRectangle selectedRectDraw;
    private Rectangle selectedRectScreen;
    
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private int xDiff;
    private int yDiff;
    
    private int xStartScreen;
    private int yStartScreen;
    private int xEndScreen;
    private int yEndScreen;
    private int xDiffScreen;
    private int yDiffScreen;
    
    private BasicStroke stroke;
    private Font font;
    private FontMetrics fontMetrics;
    
    private DrawRectangle draggedRectDraw;
    private Rectangle draggedRectScreen;
    private DragType dragType;
    
    public BroadcastAreaSelectPanel() {
    
        setBackground( new Color( 0, 0, 0, 0 ) );
        setOpaque( false );
        setCursor( CROSSHAIR_CURSOR );
        
        selectedRectDrawList = new ArrayList<>();
        selectedRectScreenList = new ArrayList<>();
        
        stroke = new BasicStroke( 2 );
        font = new Font( "SansSerif", Font.BOLD, 12 );
        fontMetrics = Utils.createFontMetrics( font );
        
        dragType = DragType.NONE;
        
        addMouseListener( new MouseAdapter() {
            
            @Override
            public void mousePressed( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    xStart = e.getX();
                    yStart = e.getY();
                    xStartScreen = e.getXOnScreen();
                    yStartScreen = e.getYOnScreen();
                    
                    draggedRectDraw = null;
                    
                    for ( int i = selectedRectDrawList.size() - 1; i >= 0; i-- ) {
                        
                        DrawRectangle r = selectedRectDrawList.get( i );
                        
                        if ( r.rect.contains( e.getPoint() ) ) {
                            
                            draggedRectDraw = r;
                            draggedRectScreen = selectedRectScreenList.get( i );
                            
                            draggedRectDraw.borderColor = SELECTED_RECT_BORDER_COLOR;
                            draggedRectDraw.fillColor = SELECTED_RECT_FILL_COLOR;
                            
                            xDiff = xStart - draggedRectDraw.rect.x;
                            yDiff = yStart - draggedRectDraw.rect.y;
                            
                            xDiffScreen = xStartScreen - draggedRectScreen.x;
                            yDiffScreen = yStartScreen - draggedRectScreen.y;
                            
                            break;
                            
                        }
                        
                    }
                    
                    repaint();
                
                } else if ( SwingUtilities.isRightMouseButton( e ) ) {
                    
                    boolean found = false;
                    int i = 0;
                    
                    for ( DrawRectangle r : selectedRectDrawList ) {
                        if ( r.rect.contains( e.getPoint() ) ) {
                            r.borderColor = DELETE_RECT_BORDER_COLOR;
                            r.fillColor = DELETE_RECT_FILL_COLOR;
                            found = true;
                            break;
                        }
                        i++;
                    }
                    
                    repaint();
                    
                    if ( found ) {
                        
                        if ( JOptionPane.showConfirmDialog( 
                                null, 
                                "Do you really want to remove this Broadcast Area?", 
                                "Remove Broadcast Area", 
                                JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {
                            
                            selectedRectDrawList.remove( i );
                            selectedRectScreenList.remove( i );
                            
                        } else {
                            selectedRectDrawList.get( i ).borderColor = DEFAULT_RECT_BORDER_COLOR;
                            selectedRectDrawList.get( i ).fillColor = DEFAULT_RECT_FILL_COLOR;
                        }
                    }
                    
                    repaint();
                    
                }
                
            }

            @Override
            public void mouseReleased( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    if ( draggedRectDraw != null ) {
                        
                        draggedRectDraw.borderColor = DEFAULT_RECT_BORDER_COLOR;
                        draggedRectDraw.fillColor = DEFAULT_RECT_FILL_COLOR;
                        
                        draggedRectDraw = null;
                        draggedRectScreen = null;
                        
                        dragType = DragType.NONE;
                        
                    } else {
                        
                        buildSelectedRects( e );

                        selectedRectDrawList.add( selectedRectDraw );
                        selectedRectScreenList.add( selectedRectScreen );

                        selectedRectDraw = null;
                        selectedRectScreen = null;
                        
                    }
                    
                    repaint();
                    
                }
                
            }
            
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            
            @Override
            public void mouseDragged( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    if ( draggedRectDraw != null ) {
                        
                        int xaDraw = draggedRectDraw.rect.x;
                        int yaDraw = draggedRectDraw.rect.y;
                        int xaScreen = draggedRectScreen.x;
                        int yaScreen = draggedRectScreen.y;
                                
                        switch ( dragType ) {
                            
                            case MOVE:
                                draggedRectDraw.rect.x = e.getX() - xDiff;
                                draggedRectDraw.rect.y = e.getY() - yDiff;
                                draggedRectScreen.x = e.getXOnScreen() - xDiffScreen;
                                draggedRectScreen.y = e.getYOnScreen() - yDiffScreen;
                                break;
                                
                            case E_RESIZE:
                                resizeE( e );
                                break;
                                
                            case W_RESIZE:
                                resizeW( e, xaDraw, xaScreen );
                                break;
                                
                            case S_RESIZE:
                                resizeS( e );
                                break;
                                
                            case N_RESIZE:
                                resizeN( e, yaDraw, yaScreen );
                                break;
                                
                            case NE_RESIZE:
                                resizeN( e, yaDraw, yaScreen );
                                resizeE( e );
                                break;
                                
                            case NW_RESIZE:
                                resizeN( e, yaDraw, yaScreen );
                                resizeW( e, xaDraw, xaScreen );
                                break;
                                
                            case SE_RESIZE:
                                resizeS( e );
                                resizeE( e );
                                break;
                                
                            case SW_RESIZE:
                                resizeS( e );
                                resizeW( e, xaDraw, xaScreen );
                                break;
                                
                        }
                        
                    } else {
                        buildSelectedRects( e );
                    }
                    
                }
                
                repaint();
                
            }
            
            @Override
            public void mouseMoved( MouseEvent e ) {
                
                dragType = DragType.NONE;
                Cursor cursor = CROSSHAIR_CURSOR;
                
                for ( int i = selectedRectDrawList.size() - 1; i >= 0; i-- ) {
                        
                    Rectangle r = selectedRectDrawList.get( i ).rect;   
                    
                    if ( r.contains( e.getPoint() ) ) {
                        
                        boolean left = e.getX() <= r.x + 10;
                        boolean right = e.getX() >= r.x + r.width - 10;
                        boolean top = e.getY() <= r.y + 10;
                        boolean bottom = e.getY() >= r.y + r.height - 10;
                        
                        if ( left && top ) {
                            cursor = NW_RESIZE_CURSOR;
                            dragType = DragType.NW_RESIZE;
                        } else if ( left && bottom ) {
                            cursor = SW_RESIZE_CURSOR;
                            dragType = DragType.SW_RESIZE;
                        } else if ( right && top ) {
                            cursor = NE_RESIZE_CURSOR;
                            dragType = DragType.NE_RESIZE;
                        } else if ( right && bottom ) {
                            cursor = SE_RESIZE_CURSOR;
                            dragType = DragType.SE_RESIZE;
                        } else if ( left ) {
                            cursor = W_RESIZE_CURSOR;
                            dragType = DragType.W_RESIZE;
                        } else if ( right ) {
                            cursor = E_RESIZE_CURSOR;
                            dragType = DragType.E_RESIZE;
                        } else if ( top ) {
                            cursor = N_RESIZE_CURSOR;
                            dragType = DragType.N_RESIZE;
                        } else if ( bottom ) {
                            cursor = S_RESIZE_CURSOR;
                            dragType = DragType.S_RESIZE;
                        } else {
                            cursor = MOVE_CURSOR;
                            dragType = DragType.MOVE;
                        }
                        
                        break;
                        
                    }
                    
                }
                
                setCursor( cursor );
                
            }
            
        });
    
    }
    
    private void buildSelectedRects( MouseEvent e ) {
        
        xEnd = e.getX();
        yEnd = e.getY();
        xEndScreen = e.getXOnScreen();
        yEndScreen = e.getYOnScreen();

        selectedRectDraw = new DrawRectangle();
        selectedRectDraw.rect = new Rectangle(
                xStart < xEnd ? xStart : xEnd, 
                yStart < yEnd ? yStart : yEnd, 
                xStart < xEnd ? xEnd - xStart : xStart - xEnd, 
                yStart < yEnd ? yEnd - yStart : yStart - yEnd );
        selectedRectDraw.borderColor = DEFAULT_RECT_BORDER_COLOR;
        selectedRectDraw.fillColor = DEFAULT_RECT_FILL_COLOR;

        selectedRectScreen = new Rectangle(
                xStartScreen < xEndScreen ? xStartScreen : xEndScreen, 
                yStartScreen < yEndScreen ? yStartScreen : yEndScreen, 
                xStartScreen < xEndScreen ? xEndScreen - xStartScreen : xStartScreen - xEndScreen, 
                yStartScreen < yEndScreen ? yEndScreen - yStartScreen : yStartScreen - yEndScreen );
        
    }
    
    @Override
    protected void paintComponent( Graphics g ) {
        
        super.paintComponent( g );
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint( 
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON );
        
        if ( selectedRectDraw != null ) {
            drawRect( g2d, selectedRectDraw );
        }
        
        for ( DrawRectangle r : selectedRectDrawList ) {
            drawRect( g2d, r );
        }
        
        g2d.dispose();
        
    }

    private void drawRect( Graphics2D g2d, DrawRectangle r ) {
        
        Rectangle rect = r.rect;
        
        g2d.setColor( r.fillColor );
        g2d.fillRect( 
                rect.x, 
                rect.y, 
                rect.width,
                rect.height );

        g2d.setColor( r.borderColor );
        g2d.drawRect( 
                rect.x, 
                rect.y, 
                rect.width - 1,
                rect.height - 1 );
        
        
        String startCoord = String.format( "[l: %d; t: %d]", rect.x, rect.y );
        String endCoord = String.format( "[r: %d; b: %d]", rect.x + rect.width, rect.y + rect.height );
        
        
        g2d.setColor( DEFAULT_COORD_LABEL_BACKGOUND );
        
        g2d.fillRoundRect( rect.x + 3, rect.y + 3, 
                fontMetrics.stringWidth( startCoord ) + 7, 
                fontMetrics.getHeight() + 2, 10, 10 );
        
        g2d.fillRoundRect( 
                rect.x + rect.width - fontMetrics.stringWidth( endCoord ) - 11, 
                rect.y + rect.height - fontMetrics.getHeight() - 6, 
                fontMetrics.stringWidth( endCoord ) + 7, 
                fontMetrics.getHeight() + 2, 10, 10 );
        
        g2d.setColor( DEFAULT_COORD_LABEL_COLOR );
        g2d.setStroke( stroke );
        g2d.setFont( font );
        
        g2d.drawString( startCoord, 
                rect.x + 7, 
                rect.y + fontMetrics.getHeight() );
        
        g2d.drawString( endCoord, 
                rect.x + rect.width - fontMetrics.stringWidth( endCoord ) - 7, 
                rect.y + rect.height - fontMetrics.getHeight() + 7 );
        
    }
    
    public List<Rectangle> getSelectedRectScreenList() {
        return selectedRectScreenList;
    }
    
    public void setSelectedRectScreenList( List<Rectangle> selectedRectScreenList, BroadcastAreaSelectDialog parentDialog ) {
        
        int xOffset = parentDialog.getX();
        int yOffset = parentDialog.getY();
        
        for ( Rectangle r : selectedRectScreenList ) {
            
            Rectangle nr = new Rectangle();
            DrawRectangle dr = new DrawRectangle();
            
            nr = new Rectangle( r.x, r.y, r.width, r.height );
            
            dr.rect = new Rectangle( r.x + xOffset, r.y + yOffset, r.width, r.height );
            dr.borderColor = DEFAULT_RECT_BORDER_COLOR;
            dr.fillColor = DEFAULT_RECT_FILL_COLOR;
            
            this.selectedRectScreenList.add( nr );
            this.selectedRectDrawList.add( dr );
            
        }
        
    }

    private void resizeE( MouseEvent e ) {
        if ( e.getX() - draggedRectDraw.rect.x > 20 ) {
            draggedRectDraw.rect.width = e.getX() - draggedRectDraw.rect.x;
            draggedRectScreen.width = e.getXOnScreen() - draggedRectScreen.x;
        }
    }
    
    private void resizeW( MouseEvent e, int xaDraw, int xaScreen ) {
        if ( draggedRectDraw.rect.x + draggedRectDraw.rect.width - e.getX() > 20 ) {
            draggedRectDraw.rect.x = e.getX() - xDiff;
            draggedRectDraw.rect.width += xaDraw - draggedRectDraw.rect.x;
            draggedRectScreen.x = e.getXOnScreen()- xDiffScreen;
            draggedRectScreen.width += xaScreen - draggedRectScreen.x;
        }
    }

    private void resizeS( MouseEvent e ) {
        if ( e.getY() - draggedRectDraw.rect.y > 20 ) {
            draggedRectDraw.rect.height = e.getY() - draggedRectDraw.rect.y;
            draggedRectScreen.height = e.getYOnScreen() - draggedRectScreen.y;
        }
    }
    
    private void resizeN( MouseEvent e, int yaDraw, int yaScreen ) {
        if ( draggedRectDraw.rect.y + draggedRectDraw.rect.height - e.getY() > 20 ) {
            draggedRectDraw.rect.y = e.getY() - yDiff;
            draggedRectDraw.rect.height += yaDraw - draggedRectDraw.rect.y;
            draggedRectScreen.y = e.getYOnScreen()- yDiffScreen;
            draggedRectScreen.height += yaScreen - draggedRectScreen.y;
        }
    }

}
