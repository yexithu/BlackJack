/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class PlayPanel extends JPanel {

    private ArrayList<Poker> playerDefaultHands;
    private ArrayList<Poker> bankerHands;

    private PlayerActionListener playerActionListener;
    private boolean isSpilt;

    private int totalBetValue = 0;
    private int totalLeftValue = 0;

    private JLabel betValueLabel;
    private JLabel leftValueLabel;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        ImageIcon background = new ImageIcon("res/playBackground.png");
        g.drawImage(background.getImage(), 0, 0, this);
        
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

    
    void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }
    
    public interface PlayerActionListener {
        void onPlayerStand();
        void onPlayerHit();
        void onPlayerSpilt();
        void onPlayerSurrand();       
        void onPlayerDouble();        
        void onPlayerTakeInsure();       
    }

}
