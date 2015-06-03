/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
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
public class Token extends JLabel{
    
    public int parValue;
    public int centerX, centerY;
    public int radius = 40;
    
    public static BufferedImage tokensBufferedImage;
    
    private ImageIcon tokenIcon;
    private TokenClickedListener tokenClickedListener;
    
    static {
        try {    
            tokensBufferedImage = ImageIO.read(new FileInputStream("res/tokens.png"));
        } catch (IOException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(tokensBufferedImage.getType());
    }
    
    public Token(int parValue, int centerX, int centerY) {
        this.parValue = parValue;
        this.centerX = centerX;
        this.centerY = centerY;
        tokenIcon = new ImageIcon();
        tokenIcon.setImage(getSpecificToken());
        
        this.setIcon(tokenIcon);
        this.setSize(tokenIcon.getIconWidth(), tokenIcon.getIconHeight());
        this.setLocation(centerX - radius, centerY - radius);
    }
    
    private Image getSpecificToken( ) {
        int startX = 0;
        switch(this.parValue) {
            case 1:
                startX = 0;
                break;
            case 5:
                startX = 1;
                break;
            case 25:
                startX = 2;
                break;
            case 100:
                startX = 3;
                break;
            case 500:
                startX = 4;
                break;
            case 1000:
                startX = 5;
                break;                
        }
        int length = tokensBufferedImage.getHeight();
        BufferedImage originImage = tokensBufferedImage.getSubimage(startX * length, 0, length, length);
        originImage.getScaledInstance(2 * radius, 2 * radius, java.awt.Image.SCALE_SMOOTH);
        return (originImage.getScaledInstance(2 * radius, 2 * radius, java.awt.Image.SCALE_SMOOTH));        
    }
    
    private void setMouseAdapter( ) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
             public void mousePressed(MouseEvent me){
                int mouseX = me.getPoint().x;
                int mouseY = me.getPoint().y;
                if(Point.distance(mouseX, mouseY, radius, radius) < radius) {
                    tokenClickedListener.onTokensClicked(parValue);
                }
             }
         };
        this.addMouseListener(mouseAdapter);
    }
    
    
    
    /*
    Token按钮接口
    */
    public interface TokenClickedListener {
        /*
        图片被点击时调用
        */
        public void onTokensClicked(int parValue);
    }
    
    /*
    调用该方法为Token增加Listener
    */
    public void setClickedListener(TokenClickedListener listener) {
        tokenClickedListener = listener;
        this.setMouseAdapter();
    }
}