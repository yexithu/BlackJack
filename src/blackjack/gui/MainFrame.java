/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Game;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class MainFrame extends JFrame {

    public static Game game;
    private BetPanel betPanel;

    public MainFrame() {
        game = new Game("Martin");
        setSize(656, 399);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setBetPanel();
        setVisible(true);
    }

    private void setBetPanel() {
        betPanel = new BetPanel();
        
        betPanel.setBetFinishedListener(new BetPanel.BetFinishedListener() {
            @Override
            public void onBetFinished(int betNum) {
                game.refresh();//重置游戏状态，新开一局游戏
                game.shuffle();//洗牌
                game.bet(betNum);
            }
        });

        add(betPanel);
    }
    
}
