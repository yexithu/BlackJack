/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Card;
import blackjack.models.Game;
import blackjack.models.PlayerSet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
    private final PlayerSet playerSet;
    private int currentPlayerIndex = 0;

    public MainFrame() {
//        game = new Game("Martin");
        JFrame temp = this;
        playerSet = new PlayerSet();
        setSize(650, 400);
        setMenuPanel();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("BlackJack");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(temp, "是否确认关闭？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void setBetPanel() {
        betPanel = new BetPanel();
        betPanel.setLeftValue(game.getPlayerCounter());
        add(betPanel);
        paintComponents(getGraphics());
        betPanel.setBetFinishedListener(new BetPanel.BetFinishedListener() {
            @Override
            public void onBetFinished(int betNum) {
                if (game.checkBet(betNum)) {
                    game.refresh();//重置游戏状态，新开一局游戏
                    game.shuffle();//洗牌
                    game.bet(betNum);
                    remove(betPanel);
                    repaint();
                    setPlayPanel(betNum);
                    game.initial();
                } else {
                    JOptionPane.showMessageDialog(betPanel, "Bet Limit: " + game.getBetMin() + " - " + game.getBetMax(), "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        betPanel.setOnExitListener(new ChildPanel.ExitClickedListener() {

            @Override
            public void onExitClicked() {
                game.save();
                remove(betPanel);
                setMenuPanel();
                paintComponents(getGraphics());
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
                game.save();
                remove(playPanel);
                repaint();
                setBetPanel();
            }
        });

        playPanel.setOnExitListener(new ChildPanel.ExitClickedListener() {

            @Override
            public void onExitClicked() {
                if (JOptionPane.showConfirmDialog(playPanel, "已下赌注将会损失，确认退出？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    game.save();
                    remove(playPanel);
                    setMenuPanel();
                    paintComponents(getGraphics());
                }
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
                game = new Game();
                playerSet.initialSet();
                if (game.setPlayer(currentPlayerIndex)) {
                    remove(menuPanel);
                    setBetPanel();
                    paintComponents(getGraphics());
                    setGame();
                    paintComponents(getGraphics());
                }
            }

            @Override
            public void onPlayer() {
                remove(menuPanel);
                setPlayerPanel();

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

    private void setPlayerPanel() {
        playerSet.initialSet();
        playerPanel = new PlayerPanel(playerSet.getSet());
        add(playerPanel);
        paintComponents(getGraphics());

        playerPanel.setOnExitListener(new ChildPanel.ExitClickedListener() {

            @Override
            public void onExitClicked() {
                currentPlayerIndex = playerPanel.getIndex();
                playerSet.setSet(playerPanel.getPlayers());
                remove(playerPanel);
                setMenuPanel();
                paintComponents(getGraphics());
            }
        });
    }
}
