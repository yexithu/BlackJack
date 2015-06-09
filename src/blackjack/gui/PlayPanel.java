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

    public PlayPanel() {
        setLayout(null);
        setHandPokers();
        setDefaultCard();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        ImageIcon background = new ImageIcon("res/playBackground.png");
        g.drawImage(background.getImage(), 0, 0, this);
        
    }
    
    private void setDefaultCard() {
        cardGui = new Poker(518, 4, new Card(Card.Pattern.DIAMOND, Card.Figure.KNIGHT), true);
        cardGui.isCoverred = false;
        add(cardGui);
        
        cardGui.setClickedListener(new Poker.CardClickedListener() {
            @Override
            public void onCardClicked() {
                System.out.println("pokerClicked");
                //player-ActionListener.onPlayerHit();
                Card[] cards = new Card[4];
                for (int i = 0; i < cards.length; i++) {
                    cards[i] =  new Card(Card.Pattern.DIAMOND, Card.Figure.KNIGHT);
                    
                }
                
                initial(cards);
            }
        });
    }
    
    private void initial(Card[] cards) {
        Poker playerFirst = new Poker(Poker.defultX, Poker.defaultY, cards[0], true);
        playerDefaultHands.add(playerFirst);
        dealAnimation(0, new Poker(cardGui), true);
        
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
        
    }

    public void dealAnimation(int index, Poker poker, boolean toTurn) {
        int endX = 0, endY = 0;
        if(index == 0) {
            endX = (playerDefaultHands.size() - 1) * 25 + 5;endY = 190;            
        }
        if(index == 1) {
            endX = (bankerHands.size() - 1) * 25 + 5;endY = 30;            
        }
        if(index == 2) {
            endX = (playerFirstHands.size() - 1) * 25 + 5;endY = 190;            
        }
        else {
            endX = (playerSecondHands.size() - 1) * 25 + 5;endY = 190;            
        }
        
        if(toTurn) {
                Animation.PokerTurnMove pokerTurnMove = new Animation.PokerTurnMove(this, poker, endX, endY);
            } 
        else {
                new Animation.PokerMove(this, poker, endX, endY);
        }
    }
    
    private  void setHandPokers() {
        playerDefaultHands = new ArrayList<>();
        bankerHands = new ArrayList<>();
        playerFirstHands = new ArrayList<>();
        playerSecondHands = new ArrayList<>();
    }
}
