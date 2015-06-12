/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
import blackjack.models.Game;
import blackjack.models.PlayerSet;
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
    private MenuPanel menuPanel;
    private BetPanel betPanel;
    private PlayPanel playPanel;
    private PlayerPanel playerPanel;
    private PlayerSet playerSet;
    private int currentPlayerIndex = 0;
    public MainFrame() {
        game = new Game("Martin");
        playerSet = new PlayerSet();
        playerSet.readSet();
        System.out.println(playerSet.getSet().size());
        setSize(646, 389);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMenuPanel();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("BlackJack");
        setVisible(true);
    }

    private void setBetPanel() {
        betPanel = new BetPanel();
        betPanel.setLeftValue(game.getPlayerCounter());
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
                setPlayPanel(betNum);
                game.initial();
            }
        });
    }

    private void setPlayPanel(int betNum) {
        playPanel = new PlayPanel();
        playPanel.setBetValue(betNum);
        playPanel.setLeftValue(game.getPlayerCounter());
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

    private void setMenuPanel() {
        menuPanel = new MenuPanel();
        menuPanel.setVisible(true);
        add(menuPanel);

        paintComponents(getGraphics());
        menuPanel.setMainMenuListeners(new MenuPanel.MainMenuClickedListener() {

            @Override
            public void onStart() {
                remove(menuPanel);
                setBetPanel();
                paintComponents(getGraphics());
                setGame();
                paintComponents(getGraphics());
            }

            @Override
            public void onPlayer() {
                remove(menuPanel);
                playerPanel = new PlayerPanel(playerSet.getSet());
                add(playerPanel);
                paintComponents(getGraphics());
            }

            @Override
            public void onHelp() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onAbout() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        paintComponents(getGraphics());
    }
}
