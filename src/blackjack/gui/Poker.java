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
public class Poker extends JLabel{
    
    public static int width = 110, height = 155;
    public int positionX, positionY;
    public Card card;
    public boolean isBack = false;
    
    
    static private BufferedImage cardsBufferedImage;
    
    private ImageIcon cardIcon;
    private CardClickedListener cardClickedListener;
    private MouseAdapter mouseAdapter;
    private BufferedImage originImage;
    
    static {
        try {    
            cardsBufferedImage = ImageIO.read(new FileInputStream("res/cards.png"));
        } catch (IOException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Poker(int posX, int posY) {
        this.positionX = posX;
        this.positionY = posY;
        cardIcon = new ImageIcon();
    }

    public Poker(int posX, int posY, Card card) {
        this(posX, posY);
        this.card = card;
        setOriginImage();
        setCardSize(width, height);
    } 
    
    public Poker(int posX, int posY, Card card, boolean bool) {
        this(posX, posY, card);
        this.isBack = bool;
        setOriginImage();
        setCardSize(width, height);
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
    
    private void setOriginImage() {

        int startX = 0, startY = 0;
        int wid, hei;
        wid = cardsBufferedImage.getWidth() / 16;
        hei = cardsBufferedImage.getHeight() / 4;
        if(isBack) {
            startX = 13 * wid;
            startY = 0 * hei;
        }
        else {
            startX = card.getValueIndex() * wid;
            startY = card.getTypeIndex() * hei;
        }

        originImage = cardsBufferedImage.getSubimage(startX, startY, wid, hei);
    }
    
    public void setCardSize(int width, int height) {
        setOriginImage();
        width = Math.max(width, 1);
        cardIcon.setImage(originImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
        
        setIcon(cardIcon);
        setSize(cardIcon.getIconWidth(), cardIcon.getIconHeight());
        setLocation(positionX, positionY);
    }
    
    public void turnOver() {
        this.isBack = !this.isBack;
        setOriginImage();
        setCardSize(this.getWidth(), this.getHeight());
    }
}

