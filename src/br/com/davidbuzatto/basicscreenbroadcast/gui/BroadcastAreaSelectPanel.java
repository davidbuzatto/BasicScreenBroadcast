/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.basicscreenbroadcast.gui;

import static br.com.davidbuzatto.basicscreenbroadcast.utils.Constants.*;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.BroadcastArea;
import br.com.davidbuzatto.basicscreenbroadcast.gui.model.DrawRectangle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

/**
 *
 * @author David
 */
public class BroadcastAreaSelectPanel extends JPanel {
    
    private DrawRectangle selectedDrawRect;
    private BroadcastArea selectedBroadcastArea;
    
    private DrawRectangle draggedDrawRect;
    private BroadcastArea draggedBroadcastArea;
    private DragType dragType;
    
    private List<DrawRectangle> selectedDrawRectList;
    private List<BroadcastArea> selectedBroadcastAreaList;
    
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
    
    private JPopupMenu popup;
    private JMenuItem itemChangeName;
    private JMenuItem itemRemove;
    private int searchIndex;
    
    public BroadcastAreaSelectPanel() {
    
        setBackground( new Color( 0, 0, 0, 0 ) );
        setOpaque( false );
        setCursor( CROSSHAIR_CURSOR );
        
        popup = new JPopupMenu();
        itemChangeName = new JMenuItem( "Change Name" );
        itemChangeName.setIcon( new ImageIcon( getClass().getResource( 
                "/br/com/davidbuzatto/basicscreenbroadcast/gui/icons/pencil.png" ) ) );
        itemRemove = new JMenuItem( "Remove" );
        itemRemove.setIcon( new ImageIcon( getClass().getResource( 
                "/br/com/davidbuzatto/basicscreenbroadcast/gui/icons/delete.png" ) ) );
        
        popup.add( itemChangeName );
        popup.add( new JSeparator() );
        popup.add( itemRemove );
        
        selectedDrawRectList = new ArrayList<>();
        selectedBroadcastAreaList = new ArrayList<>();
        
        dragType = DragType.NONE;
        
        itemChangeName.addActionListener( new ActionListener() {
            
            @Override
            public void actionPerformed( ActionEvent e ) {
                
                String name = JOptionPane.showInputDialog( "Whats the name new of the selected Broadcast Area?" );
                if ( name == null || name.trim().isEmpty() ) {
                    name = "empty";
                }

                name = name.trim();
                selectedDrawRectList.get( searchIndex ).setName( name );
                selectedBroadcastAreaList.get( searchIndex ).setName( name );
                
                selectedDrawRectList.get( searchIndex ).setBorderColor( DEFAULT_RECT_BORDER_COLOR );
                selectedDrawRectList.get( searchIndex ).setFillColor( DEFAULT_RECT_FILL_COLOR );
                
                repaint();
                
            }
            
        });
        
        itemRemove.addActionListener( new ActionListener() {
            
            @Override
            public void actionPerformed( ActionEvent e ) {
                
                if ( JOptionPane.showConfirmDialog( 
                        null, 
                        "Do you really want to remove this Broadcast Area?", 
                        "Remove Broadcast Area", 
                        JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ) {

                    selectedDrawRectList.remove( searchIndex );
                    selectedBroadcastAreaList.remove( searchIndex );

                } else {
                    selectedDrawRectList.get( searchIndex ).setBorderColor( DEFAULT_RECT_BORDER_COLOR );
                    selectedDrawRectList.get( searchIndex ).setFillColor( DEFAULT_RECT_FILL_COLOR );
                }
                
                repaint();
                
            }
            
        });
        
        
        addMouseListener( new MouseAdapter() {
            
            @Override
            public void mousePressed( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    xStart = e.getX();
                    yStart = e.getY();
                    xStartScreen = e.getXOnScreen();
                    yStartScreen = e.getYOnScreen();
                    
                    draggedDrawRect = null;
                    
                    for ( int i = selectedDrawRectList.size() - 1; i >= 0; i-- ) {
                        
                        DrawRectangle r = selectedDrawRectList.get( i );
                        
                        if ( r.getRectangle().contains( e.getPoint() ) ) {
                            
                            draggedDrawRect = r;
                            draggedBroadcastArea = selectedBroadcastAreaList.get( i );
                            
                            draggedDrawRect.setBorderColor( SELECTED_RECT_BORDER_COLOR );
                            draggedDrawRect.setFillColor( SELECTED_RECT_FILL_COLOR );
                            
                            xDiff = xStart - draggedDrawRect.getRectangle().x;
                            yDiff = yStart - draggedDrawRect.getRectangle().y;
                            
                            xDiffScreen = xStartScreen - draggedBroadcastArea.getRectangle().x;
                            yDiffScreen = yStartScreen - draggedBroadcastArea.getRectangle().y;
                            
                            break;
                            
                        }
                        
                    }
                    
                    repaint();
                
                } else if ( SwingUtilities.isRightMouseButton( e ) ) {
                    
                    boolean found = false;
                    searchIndex = 0;
                    
                    for ( DrawRectangle r : selectedDrawRectList ) {
                        if ( r.getRectangle().contains( e.getPoint() ) ) {
                            r.setBorderColor( DELETE_RECT_BORDER_COLOR );
                            r.setFillColor( DELETE_RECT_FILL_COLOR );
                            found = true;
                            break;
                        }
                        searchIndex++;
                    }
                    
                    repaint();
                    
                    if ( found ) {
                        popup.show( (Component) e.getSource(), e.getXOnScreen(), e.getYOnScreen());
                    }
                    
                    repaint();
                    
                }
                
            }

            @Override
            public void mouseReleased( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    if ( draggedDrawRect != null ) {
                        
                        draggedDrawRect.setBorderColor( DEFAULT_RECT_BORDER_COLOR );
                        draggedDrawRect.setFillColor( DEFAULT_RECT_FILL_COLOR );
                        
                        draggedDrawRect = null;
                        draggedBroadcastArea = null;
                        
                        dragType = DragType.NONE;
                        
                    } else {
                        
                        if ( selectedDrawRect != null && 
                                selectedDrawRect.getRectangle().width >= 50 && 
                                selectedDrawRect.getRectangle().height >= 50 ) {
                        
                            buildSelectedRects( e );

                            String name = JOptionPane.showInputDialog( "Whats the name of the created Broadcast Area?" );
                            if ( name == null || name.trim().isEmpty() ) {
                                name = "empty";
                            }
                            
                            name = name.trim();
                            selectedDrawRect.setName( name );
                            selectedBroadcastArea.setName( name );

                            selectedDrawRectList.add( selectedDrawRect );
                            selectedBroadcastAreaList.add( selectedBroadcastArea );
                            
                        } else {
                            
                            JOptionPane.showMessageDialog( (JPanel) e.getSource(), 
                                    "A Broadcast Area should have at least 50 "
                                            + "pixels of with and height!", 
                                    "ERROR", JOptionPane.ERROR_MESSAGE );
                            
                        }

                        selectedDrawRect = null;
                        selectedBroadcastArea = null;
                        
                    }
                    
                    repaint();
                    
                }
                
            }
            
        });
        
        addMouseMotionListener( new MouseMotionAdapter() {
            
            @Override
            public void mouseDragged( MouseEvent e ) {
                
                if ( SwingUtilities.isLeftMouseButton( e ) ) {
                    
                    if ( draggedDrawRect != null ) {
                        
                        int xaDraw = draggedDrawRect.getRectangle().x;
                        int yaDraw = draggedDrawRect.getRectangle().y;
                        int xaScreen = draggedBroadcastArea.getRectangle().x;
                        int yaScreen = draggedBroadcastArea.getRectangle().y;
                                
                        switch ( dragType ) {
                            
                            case MOVE:
                                draggedDrawRect.getRectangle().x = e.getX() - xDiff;
                                draggedDrawRect.getRectangle().y = e.getY() - yDiff;
                                draggedBroadcastArea.getRectangle().x = e.getXOnScreen() - xDiffScreen;
                                draggedBroadcastArea.getRectangle().y = e.getYOnScreen() - yDiffScreen;
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
                
                for ( int i = selectedDrawRectList.size() - 1; i >= 0; i-- ) {
                        
                    Rectangle r = selectedDrawRectList.get( i ).getRectangle();   
                    
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

        selectedDrawRect = new DrawRectangle();
        selectedDrawRect.setRectangle( new Rectangle(
                xStart < xEnd ? xStart : xEnd, 
                yStart < yEnd ? yStart : yEnd, 
                xStart < xEnd ? xEnd - xStart : xStart - xEnd, 
                yStart < yEnd ? yEnd - yStart : yStart - yEnd ) );
        selectedDrawRect.setBorderColor( DEFAULT_RECT_BORDER_COLOR );
        selectedDrawRect.setFillColor( DEFAULT_RECT_FILL_COLOR );

        Rectangle selectedBroadcastAreaRect = new Rectangle(
                xStartScreen < xEndScreen ? xStartScreen : xEndScreen, 
                yStartScreen < yEndScreen ? yStartScreen : yEndScreen, 
                xStartScreen < xEndScreen ? xEndScreen - xStartScreen : xStartScreen - xEndScreen, 
                yStartScreen < yEndScreen ? yEndScreen - yStartScreen : yStartScreen - yEndScreen );
        
        selectedBroadcastArea = new BroadcastArea( "e", selectedBroadcastAreaRect );
        
    }
    
    @Override
    protected void paintComponent( Graphics g ) {
        
        super.paintComponent( g );
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint( 
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON );
        
        if ( selectedDrawRect != null ) {
            selectedDrawRect.draw( g2d );
        }
        
        for ( DrawRectangle r : selectedDrawRectList ) {
            r.draw( g2d );
        }
        
        g2d.dispose();
        
    }
    
    public List<BroadcastArea> getSelectedBroadcastAreaList() {
        return selectedBroadcastAreaList;
    }
    
    public void setBroadcastAreaList( List<BroadcastArea> selectedBroadcastAreaList, BroadcastAreaSelectDialog parentDialog ) {
        
        int xOffset = parentDialog.getX();
        int yOffset = parentDialog.getY();
        
        for ( BroadcastArea r : selectedBroadcastAreaList ) {
            
            BroadcastArea sba = (BroadcastArea) r.clone();
            
            DrawRectangle dr = new DrawRectangle();
            dr.setName( sba.getName() );
            dr.setRectangle( new Rectangle( 
                    r.getRectangle().x + xOffset, 
                    r.getRectangle().y + yOffset, 
                    r.getRectangle().width, 
                    r.getRectangle().height ) );
            dr.setBorderColor( DEFAULT_RECT_BORDER_COLOR );
            dr.setFillColor( DEFAULT_RECT_FILL_COLOR );
            
            this.selectedBroadcastAreaList.add( sba );
            this.selectedDrawRectList.add( dr );
            
        }
        
    }

    private void resizeE( MouseEvent e ) {
        if ( e.getX() - draggedDrawRect.getRectangle().x > 50 ) {
            draggedDrawRect.getRectangle().width = e.getX() - draggedDrawRect.getRectangle().x;
            draggedBroadcastArea.getRectangle().width = 
                    e.getXOnScreen() - draggedBroadcastArea.getRectangle().x;
        }
    }
    
    private void resizeW( MouseEvent e, int xaDraw, int xaScreen ) {
        if ( draggedDrawRect.getRectangle().x + draggedDrawRect.getRectangle().width - e.getX() > 50 ) {
            draggedDrawRect.getRectangle().x = e.getX() - xDiff;
            draggedDrawRect.getRectangle().width += xaDraw - draggedDrawRect.getRectangle().x;
            draggedBroadcastArea.getRectangle().x = e.getXOnScreen()- xDiffScreen;
            draggedBroadcastArea.getRectangle().width += 
                    xaScreen - draggedBroadcastArea.getRectangle().x;
        }
    }

    private void resizeS( MouseEvent e ) {
        if ( e.getY() - draggedDrawRect.getRectangle().y > 50 ) {
            draggedDrawRect.getRectangle().height = e.getY() - draggedDrawRect.getRectangle().y;
            draggedBroadcastArea.getRectangle().height = 
                    e.getYOnScreen() - draggedBroadcastArea.getRectangle().y;
        }
    }
    
    private void resizeN( MouseEvent e, int yaDraw, int yaScreen ) {
        if ( draggedDrawRect.getRectangle().y + draggedDrawRect.getRectangle().height - e.getY() > 50 ) {
            draggedDrawRect.getRectangle().y = e.getY() - yDiff;
            draggedDrawRect.getRectangle().height += yaDraw - draggedDrawRect.getRectangle().y;
            draggedBroadcastArea.getRectangle().y = e.getYOnScreen()- yDiffScreen;
            draggedBroadcastArea.getRectangle().height += 
                    yaScreen - draggedBroadcastArea.getRectangle().y;
        }
    }

}
