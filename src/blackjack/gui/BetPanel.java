/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
import blackjack.models.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
public class BetPanel extends JPanel {

    static public ArrayList<Token> tableTokens;
    static Hashtable<Integer, Integer> leftTokensNumber = new Hashtable<>(6);
    static Hashtable<Integer, Token> defaultTokens = new Hashtable<>(6);
    static Poker cardGui;
    private int totalBetValue = 0;
    private int totalLeftValue = 1000;
    private JLabel betValueLabel;
    private JLabel leftValueLabel;
    private BetFinishedListener betFinishedListener;

    public BetPanel() {
        setLayout(null);
        setValueLabels();
        setDefaultToken(this);
        setDefaultCard();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundImage = new ImageIcon(BetPanel.class.getResource("/res/betBackground.png"));
        g.drawImage(backgroundImage.getImage(), 0, 0, this);
        for (Token tableToken : tableTokens) {
            if (tableToken != null) {
                this.remove(tableToken);
                this.add(tableToken);
            }
        }

        Iterator iterator = defaultTokens.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = (Integer) iterator.next();
            this.remove(defaultTokens.get(next));
            this.add(defaultTokens.get(next));
        }

    }

    private void setDefaultToken(JPanel thisPanel) {
        tableTokens = new ArrayList<>();
        Token token1 = new Token(1, 100, 85);
        Token token5 = new Token(5, 100, 185);
        Token token25 = new Token(25, 100, 285);
        Token token100 = new Token(100, 210, 85);
        Token token500 = new Token(500, 210, 185);
        Token token1000 = new Token(1000, 210, 285);
        defaultTokens.put(1, token1);
        defaultTokens.put(5, token5);
        defaultTokens.put(25, token25);
        defaultTokens.put(100, token100);
        defaultTokens.put(500, token500);
        defaultTokens.put(1000, token1000);
        Iterator iterator = defaultTokens.keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = (Integer) iterator.next();
            Token defaultToken = defaultTokens.get(next);
            thisPanel.add(defaultToken);
            leftTokensNumber.put(defaultToken.parValue, 10);
            defaultToken.setClickedListener(new Token.TokenClickedListener() {
                @Override
                public void onTokensClicked(int parValue) {
                    if (totalLeftValue - defaultToken.parValue >= 0) {
                        createBetToken(defaultToken);
                    }
                }
            });
        }
    }

    private void setDefaultCard() {
        cardGui = new Poker(518, 4, new Card(Card.Pattern.DIAMOND, Card.Figure.KNIGHT), true);
        cardGui.isCoverred = false;
        add(cardGui);
        cardGui.setClickedListener(new Poker.CardClickedListener() {
            @Override
            public void onCardClicked() {
                System.out.println("pokerClicked");
                betFinishedListener.onBetFinished(totalBetValue);
            }
        });
    }

    private void withDrawToken(Token token) {
        if (tableTokens.get(0) == token) {
            new Animation.TokenMove(this, new Token(token), defaultTokens.get(token.parValue).centerX, defaultTokens.get(token.parValue).centerY, false);
            this.remove(token);
            tableTokens.remove(0);
            totalBetValue -= token.parValue;
            totalLeftValue += token.parValue;
            leftTokensNumber.replace(token.parValue, leftTokensNumber.get(token.parValue) + 1);
            setBetValue();
            setLeftValue();
        }
    }

    private void createBetToken(Token defaultToken) {
        int randomX = 380, randomY = 185;
        Random random = new Random();
        randomX += random.nextInt(36) - 18;
        randomY += random.nextInt(36) - 18;
        Token newToken = new Token(defaultToken);
        Animation.TokenMove addedToken = new Animation.TokenMove(this, newToken, randomX, randomY, true);
        setLeftToken(newToken);
        tableTokens.add(0, addedToken.getToken());
        leftTokensNumber.replace(defaultToken.parValue, (leftTokensNumber.get(defaultToken.parValue) - 1));
        totalBetValue += defaultToken.parValue;
        totalLeftValue -= defaultToken.parValue;
        setBetValue();
        setLeftValue();
        if ((int) leftTokensNumber.get(defaultToken.parValue) == 0) {
            defaultToken.setVisible(false);
            defaultToken.setEnabled(false);
        }
    }

    private void setLeftToken(Token token) {
        token.setClickedListener(new Token.TokenClickedListener() {

            @Override
            public void onTokensClicked(int parValue) {
                withDrawToken(token);
            }
        });
    }

    private void setLeftValue() {
        this.leftValueLabel.setText(String.valueOf(totalLeftValue));
    }

    public void setLeftValue(int value) {
        this.totalLeftValue = value;
        setLeftValue();
    }

    private void setBetValue() {
        this.betValueLabel.setText(String.valueOf(totalBetValue));
    }

    private void setValueLabels() {
        this.betValueLabel = new JLabel();
        betValueLabel.setSize(50, 25);
        betValueLabel.setLocation(550, 240);
        betValueLabel.setVisible(true);
        betValueLabel.setForeground(Color.WHITE);
        betValueLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        this.add(betValueLabel);
        setBetValue();
        this.leftValueLabel = new JLabel();
        leftValueLabel.setSize(50, 25);
        leftValueLabel.setLocation(550, 280);
        leftValueLabel.setVisible(true);
        leftValueLabel.setForeground(Color.WHITE);
        leftValueLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        this.add(leftValueLabel);
        setLeftValue();
    }

    public interface BetFinishedListener {

        void onBetFinished(int bet);
    }

    public void setBetFinishedListener(BetFinishedListener listener) {
        this.betFinishedListener = listener;
    }
}
