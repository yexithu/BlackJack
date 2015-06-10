/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import static blackjack.gui.BetPanel.cardGui;
import blackjack.models.Card;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class PlayPanel extends JPanel {

    private ArrayList<Poker> playerDefaultHands;
    private ArrayList<Poker> bankerHands;
    private ArrayList<Poker> playerFirstHands;
    private ArrayList<Poker> playerSecondHands;

    private PlayerActionListener playerActionListener;
    private boolean isSpilt;
    private int currenSet = 0;
    private static Poker cardGui;
    private int totalBetValue = 0;
    private int totalLeftValue = 0;
    private JLabel betValueLabel;
    private JLabel leftValueLabel;
    
    Hashtable<Integer, ArrayList<Poker>> hands;

    public PlayPanel() {
        setLayout(null);
        setHandPokers();
        setDefaultCard();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon background = new ImageIcon("res/playBackground.png");
        g.drawImage(background.getImage(), 0, 0, this);
        
        Iterator interator = hands.keySet().iterator();
        
        while (interator.hasNext()) {
            Integer next =(Integer) interator.next();
            ArrayList<Poker> pokers = hands.get(next);
            for (Poker poker : pokers) {
                this.remove(poker);
                this.add(poker);
                if(pokers.get(0) == poker)
                    poker.isCoverred = false;
                else
                    poker.isCoverred = true;
            }           
        }
        
        this.remove(cardGui);
        this.add(cardGui);

    }

    private void setDefaultCard() {
        cardGui = new Poker(518, 4, new Card(Card.Pattern.DIAMOND, Card.Figure.KNIGHT), true);
        cardGui.isCoverred = false;
        add(cardGui);

        cardGui.setClickedListener(new Poker.CardClickedListener() {
            @Override
            public void onCardClicked() {
                playerActionListener.onPlayerHit();
              
            }
        });
    }

    void initial(Card[] cards) {
        dealCard(0, cards[0], true);
        dealCard(1, cards[1], true);
        dealCard(0, cards[2], true);
        dealCard(1, cards[3], false);

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

    private void dealCard(int index, Card card, boolean toTurn) {
        Poker tempPoker = new Poker(Poker.defultX, Poker.defaultY, card, true);
        hands.get(index).add(0, tempPoker);
        dealAnimation(index, tempPoker, toTurn);
    }

    public void dealAnimation(int index, Poker poker, boolean toTurn) {
        int endX = 0, endY = 0;
        if (index == 0) {
            endY = 190;
        } else if (index == 1) {
            endY = 30;
        } else if (index == 2) {
            endY = 190;
        } else {
            endY = 190;
        }
        endX = (hands.get(index).size() - 1) * 25 + 5;
        if (toTurn) {
            Animation.PokerTurnMove pokerTurnMove = new Animation.PokerTurnMove(this, poker, endX, endY);
        } else {
            new Animation.PokerMove(this, poker, endX, endY);
        }
    }

    public void showMessageDialog(String input) {
        JOptionPane.showMessageDialog(null, input, "Hint", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showChoiceDialog() {
        int result = JOptionPane.showConfirmDialog(null, "Take Insurerance?");
        if(result == JOptionPane.YES_OPTION) {
            playerActionListener.onPlayerTakeInsure();
        }
    }
    private void setHandPokers() {
        playerDefaultHands = new ArrayList<>();
        bankerHands = new ArrayList<>();
        playerFirstHands = new ArrayList<>();
        playerSecondHands = new ArrayList<>();
        
        hands = new Hashtable<>(4);
        hands.put(0, playerDefaultHands);
        hands.put(1, bankerHands);
        hands.put(2, playerFirstHands);
        hands.put(3, playerSecondHands);
    }
}
