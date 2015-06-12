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
        setSize(646, 389);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setGame();
        setBetPanel();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("BlackJack");
        setVisible(true);
    }

    private void setBetPanel() {
        betPanel = new BetPanel();
        add(betPanel);
        paintComponents(getGraphics());
        betPanel.setBetFinishedListener(new BetPanel.BetFinishedListener() {
            @Override
            public void onBetFinished(int betNum) {
                game.refresh();//重置游戏状态，新开一局游戏
                game.shuffle();//洗牌
                game.bet(betNum);
                remove(betPanel);
                repaint();
                setPlayPanel();
                game.initial();
            }
        });
    }

    private void setPlayPanel() {
        playPanel = new PlayPanel();
        add(playPanel);
        paintComponents(getGraphics());

        playPanel.setPlayerActionListener(new PlayPanel.PlayerActionListener() {

            @Override
            public void onPlayerStand(int index) {
                System.out.println("PlayerStand");
                game.playerStand(index);
            }

            @Override
            public void onPlayerHit(int index) {
                game.playerHit(index);
            }

            @Override
            public void onPlayerSpilt() {
                game.split();
            }

            @Override
            public void onPlayerSurrand(int index) {
                game.playerSurrend(index);
            }

            @Override
            public void onPlayerDouble(int index) {
                game.playerDouble(index);
            }

            @Override
            public void onPlayerTakeInsure() {
                game.playerInsure();
            }

            @Override
            public void onGameOver() {
                remove(playPanel);
                repaint();
                setBetPanel();
            }
        });
    }

    private void setGame() {
        game.setGameActionListener(new Game.GameActionListener() {

            @Override
            public void onInitial(Card[] cards) {
                playPanel.initial(cards);
            }

            @Override
            public void onDouble(int index, Card card) {
                playPanel.dealCard(index, card, true);
            }

            @Override
            public void onDeal(int index, Card card) {
                playPanel.dealCard(index, card, true);
            }

            @Override
            public void onBankerDisplayCard() {
                playPanel.bankerDisplayCard();
            }

            @Override
            public void onShowResult(int index, Game.State state) {
                playPanel.showResultDialog(index, state);
            }

            @Override
            public void onBankerPeek() {
                playPanel.bankerPeekCard();
            }

            @Override
            public void showChoiceDialog() {
                playPanel.showChoiceDialog();
            }

            @Override
            public void onShowMessageDialog(String input) {
                playPanel.showMessageDialog(input);
            }

            @Override
            public void showTagMessage(int index, int type) {
                playPanel.showTageMessage(index, type);
            }//index 0Player 1Banker type 0Bust 1BJ

            @Override
            public void onChangeSet(Card c) {
                playPanel.setChanged();
                playPanel.dealCard(2, c, true);
            }
        });
    }
}
