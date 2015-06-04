/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Re
 */
public class CardGui extends JLabel{
    
    public static int width = 110, height = 155;
    public int positionX, positionY;
    public Card card;
    
    static private BufferedImage cardsBufferedImage;
    
    private ImageIcon cardIcon;
    private CardClickedListener cardClickedListener;
    private MouseAdapter mouseAdapter;
    
    static {
        try {    
            cardsBufferedImage = ImageIO.read(new FileInputStream("res/cards.png"));
        } catch (IOException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CardGui(int posX, int posY, Card card) {
        this.positionX = posX;
        this.positionY = posY;
        this.card = card;
        
        cardIcon = new ImageIcon();
        cardIcon.setImage(getSpecificCard());
        
        setIcon(cardIcon);
        setSize(cardIcon.getIconWidth(), cardIcon.getIconHeight());
        setLocation(positionX, positionY);
    } 
    
    
    public void setClickedListener(CardClickedListener cardListener) {
        this.cardClickedListener = cardListener;
        mouseAdapter = new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent me) {
                cardClickedListener.onCardClicked();
            }
        };
        this.addMouseListener(mouseAdapter);
    }
    
    
    
    public void removeMouseAdapter() {
        this.removeMouseListener(mouseAdapter);
    }
    
    
    /*
    调用setClickedListener增加监听
    */
    public interface CardClickedListener{
        void onCardClicked();
    }
    
    private Image getSpecificCard() {
        int startX = 0, startY = 0;
        int wid, hei;
        wid = cardsBufferedImage.getWidth() / 16;
        hei = cardsBufferedImage.getHeight() / 4;
        
        BufferedImage subBufferedImage = cardsBufferedImage.getSubimage(startX, startY, wid, hei);
        return (subBufferedImage.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
    }
    
}

