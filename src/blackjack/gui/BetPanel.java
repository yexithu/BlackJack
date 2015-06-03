/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class BetPanel extends JPanel{
    
    private BufferedImage pokersSet;
    private BufferedImage tokensSet;
    
    public BetPanel() {
        Token token1 = new Token(1, 100, 100);
        Token token2 = new Token(5, 200, 100);
        Token token3 = new Token(25, 300, 100);
        Token token4 = new Token(100, 100, 200);
        Token token5 = new Token(500, 200, 200);
        Token token6 = new Token(1000, 300, 200);
        

//        label.setLocation(0,0);
//        label.setSize(token.getIconWidth(), token.getIconHeight());
//        System.out.println(label.getWidth()+" " +  label.getHeight());
       // label.setBounds(0, 0, label.getWidth(), label.getHeight());
        setLayout(null);
        add(token1);
        add(token2);
        add(token3);
        add(token4);
        add(token5);
        add(token6);
        
//        add(label);

    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        setBackground(Color.BLACK);

//        ImageIcon backgroundImage = new ImageIcon("res/bettable.png");
//        g.drawImage(backgroundImage.getImage(),320,120,this);
          
//        g.drawImage(token.getImage(), 0, 0,this);

        
    }
    
}

