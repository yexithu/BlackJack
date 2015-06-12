/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Player;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class PlayerPanel extends JPanel{
    static public class WhiteBorderLabel extends JLabel{
        public WhiteBorderLabel(int width, int height) {
            setSize(width, height);
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            
            setFont(new java.awt.Font("Dialog",   1,   25));
            setForeground(Color.WHITE);
            setText("Test");
        }
    }

    private ArrayList<WhiteBorderLabel> playerTags;
    private ArrayList<Player> players;
    private int currentIndex = 0;
    public PlayerPanel(ArrayList<Player> players) {
        setLayout(null);
        this.setBackground(Color.BLACK);
        this.players = players;
        setPlayerTags(players);
        
    }
    
    private void setPlayerTags(ArrayList<Player> players) {
        playerTags = new ArrayList<>(4);
        players = new ArrayList<>(4);
        
        for (int i = 0; i < 4; ++i) {
            players.add(new Player(i));
        }
        for(int i = 0; i < 4; ++i) {
            WhiteBorderLabel playTag = new WhiteBorderLabel(150, 50);
            if(players.get(i).getName() == null) {
               playTag.setText("Empty");
            }
            else {
                playTag.setText(players.get(i).getName());
            }
            
            playTag.setLocation(20, 30 + 80 * i);
            playTag.setVisible(true);
            add(playTag);
            playerTags.add(playTag);
        }
    }
    
    
}
