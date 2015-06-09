/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
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
    private PlayPanel playPanel;
    
    
    public MainFrame() {
        
        game = new Game("Martin");
        setSize(656, 399);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setGame();
        setBetPanel();
        //setPlayPanel();
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
                
                remove(betPanel);
                repaint();
                setPlayPanel();
            }
        });

        add(betPanel);
    }
    
    private void setPlayPanel() {
        playPanel = new PlayPanel();
        add(playPanel);
        paintComponents(getGraphics());

        
        playPanel.setPlayerActionListener(new PlayPanel.PlayerActionListener() {

            @Override
            public void onPlayerStand() {

            }

            @Override
            public void onPlayerHit() {
 
            }

            @Override
            public void onPlayerSpilt() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onPlayerSurrand() {

            }

            @Override
            public void onPlayerDouble() {
            }

            @Override
            public void onPlayerTakeInsure() {
            }
        });
    }
    
    private void setGame() {
        game.setGameActionListener(new Game.GameActionListener() {

            @Override
            public void onInitial(Card[] cards) {

            }

            @Override
            public void onDeal(int index, Card card) {

            }

            @Override
            public void onBankerDisplayCard() {

            }

            @Override
            public void onShowResult(Game.State state) {

            }

            @Override
            public void onBankerPeek() {

            }

            @Override
            public void showChoiceDialog() {

            }
        });
    }
    
}
