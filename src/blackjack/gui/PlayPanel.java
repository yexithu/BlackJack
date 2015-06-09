/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class PlayPanel extends JPanel {

    private ArrayList<Poker> playerDefaultHands;
    private ArrayList<Poker> bankerHands;

    boolean isSpilt;

    private int totalBetValue = 0;
    private int totalLeftValue = 0;

    private JLabel betValueLabel;
    private JLabel leftValueLabel;

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

    public interface PlayerActionListener {

        void onPlayerStand();

        void onPlayerHit();

        void onPlayerSpilt();

        void onPlayerSurrand();
        
        void onPlayerDouble();
        
        void onPlayerTakeInsure();
        
    }

}
