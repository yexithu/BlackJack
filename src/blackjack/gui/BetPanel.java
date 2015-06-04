/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 *
 * @author Re
 */
public class BetPanel extends JPanel{
    
    static private ArrayList<Token> defaultTokens;
    static private ArrayList<Token> tableTokens;
    private JPanel overLapPanel;
    public BetPanel() {
        setLayout(null);
        setDefaultToken(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon backgroundImage = new ImageIcon("res/betBackground.png");
        g.drawImage(backgroundImage.getImage(), 0, 0, this);
        
        for(Token tableToken: tableTokens) {
            if(tableToken != null) {
                this.remove(tableToken);
                this.add(tableToken);
            }
        }
        
        for(Token defaultToken: defaultTokens) {
            this.remove(defaultToken);
            this.add(defaultToken);
        }
        
    }
    
    private void setDefaultToken(JPanel thisPanel) {
        CardGui cardGui = new CardGui(518, 4, new Card(Card.Pattern.DIAMOND, Card.Figure.KNIGHT));
        add(cardGui);
        setLayout(null);
        defaultTokens = new ArrayList<>();
        tableTokens = new ArrayList<>();
        
        Token token1 = new Token(1, 100, 85);
        Token token5 = new Token(5, 100, 185);
        Token token25 = new Token(25, 100, 285);
        Token token100 = new Token(100, 210, 85);
        Token token500 = new Token(500, 210, 185);
        Token token1000 = new Token(1000, 210, 285);
        defaultTokens.add(token1);
        defaultTokens.add(token5);
        defaultTokens.add(token25);
        defaultTokens.add(token100);
        defaultTokens.add(token500);
        defaultTokens.add(token1000);
        
        
        
        for (Token defaultToken : defaultTokens) {
            defaultToken.setClickedListener(new Token.TokenClickedListener() {

            @Override
            public void onTokensClicked(int parValue) {
               createBetToken(defaultToken);
            };
        });
        }

        for (Token defaultToken : defaultTokens) {
            thisPanel.add(defaultToken);
        }  
    }
    
    private void createBetToken(Token defaultToken) {
        int randomX = 380, randomY = 185;
        Random random = new Random();
        randomX += random.nextInt(40) - 20;
        randomY += random.nextInt(40) - 20;
        Animation.CardAnimation addedToken = new Animation.CardAnimation(this, defaultToken.parValue, defaultToken.centerX, defaultToken.centerY, randomX, randomY);
        tableTokens.add(0, addedToken.getToken());
        new Thread(addedToken).start(); 
    }
}

