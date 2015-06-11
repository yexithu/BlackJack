/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import static blackjack.gui.BetPanel.cardGui;
import blackjack.models.Card;
import blackjack.models.Game;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
    private ArrayList<JLabel> messageTags;
    private PlayerActionListener playerActionListener;
    private boolean isSpilt;
    private int currenSet = 0;
    private static Poker cardGui;
    private int totalBetValue = 0;
    private int totalLeftValue = 0;
    private JLabel betValueLabel;
    private JLabel leftValueLabel;
    private JButton spiltButton;
    private int pokerIndex = 0;
    Hashtable<Integer, ArrayList<Poker>> hands;

    public PlayPanel() {
        setLayout(null);
        setHandPokers();
        setDefaultCard();
        setSplitButton();
        setMessageTags();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon background = new ImageIcon("res/playBackground.png");
        g.drawImage(background.getImage(), 0, 0, this);
        for (JLabel tagLabel : messageTags) {
            this.remove(tagLabel);
            this.add(tagLabel);
        }
        Iterator interator = hands.keySet().iterator();
        while (interator.hasNext()) {
            Integer next = (Integer) interator.next();
            ArrayList<Poker> pokers = hands.get(next);
            for (Poker poker : pokers) {
                this.remove(poker);
                this.add(poker);
                poker.isCoverred = pokers.get(0) != poker;
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
                playerActionListener.onPlayerHit(currenSet);
            }
        });
    }

    void initial(Card[] cards) {
        dealCard(0, cards[0], true);
        int midtime = 200;
        new Animation.expectantTaskManager(midtime, new Animation.expectantTaskManager.ExpectantTask() {
            @Override
            public void expectantTask() {
                dealCard(3, cards[1], true);
            }
        });
        new Animation.expectantTaskManager(midtime * 2, new Animation.expectantTaskManager.ExpectantTask() {
            @Override
            public void expectantTask() {
                dealCard(0, cards[2], true);
            }
        });
        new Animation.expectantTaskManager(midtime * 3, new Animation.expectantTaskManager.ExpectantTask() {
            @Override
            public void expectantTask() {
                dealCard(3, cards[3], false);
                if (cards[0].getValue() == cards[2].getValue()) {
                    changeSplitButtonState();
                }
            }
        });
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

        void onPlayerStand(int index);

        void onPlayerHit(int index);

        void onPlayerSpilt();

        void onPlayerSurrand(int index);

        void onPlayerDouble(int index);

        void onPlayerTakeInsure();

        void onGameOver();
    }

    public void dealCard(int index, Card card, boolean toTurn) {
        System.out.println("DealIndex" + index);
        Poker tempPoker = new Poker(Poker.defultX, Poker.defaultY, card, true);
        tempPoker.setClickedListener(getDealedCardListener(index));
        hands.get(index).add(0, tempPoker);
        dealAnimation(index, tempPoker, toTurn);
    }

    public void dealAnimation(int index, Poker poker, boolean toTurn) {
        int endX = 0, endY = 190;
        if (index == 3) {
            endY = 15;
        }
        endX = (hands.get(index).size() - 1) * 25 + 5;
        if (toTurn) {
            Animation.PokerTurnMove pokerTurnMove = new Animation.PokerTurnMove(this, poker, endX, endY);
        } else {
            new Animation.PokerMove(this, poker, endX, endY);
        }
    }

    public void bankerDisplayCard() {
        new Animation.PokerTurn(this, hands.get(3).get(0));
    }

    public void bankerPeekCard() {
        new Animation.PokerPeeked(hands.get(3).get(0));
    }

    public void showMessageDialog(String input) {
        JOptionPane.showMessageDialog(this, input, "Hint", JOptionPane.QUESTION_MESSAGE);
    }

    public void showChoiceDialog() {

        int result = JOptionPane.showConfirmDialog(this, "Take Insurerance?");
        if (result == JOptionPane.YES_OPTION) {
            playerActionListener.onPlayerTakeInsure();
        }
    }

    public void showResultDialog(int index, Game.State state) {
        JOptionPane.showMessageDialog(this, String.valueOf(state), "Hint", JOptionPane.PLAIN_MESSAGE);
        if (index == 0 || index == 1) {
            playerActionListener.onGameOver();
        } else {
            for (JLabel tagLabel : messageTags) {
                tagLabel.setVisible(false);
                tagLabel.setEnabled(false);
            }
            setChanged();
        }
    }

    public void pokerSetBack() {

        new Animation.PokerSpilt(this, hands.get(0).get(0), 1);
    }

    public void setSplitButton() {
        spiltButton = new JButton();
        spiltButton.setSize(60, 35);
        spiltButton.setLocation(150, 200);
        spiltButton.setText("Split");
        spiltButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                playerSplit();
                playerActionListener.onPlayerSpilt();
            }
        });
        add(spiltButton);
        spiltButton.setVisible(false);
        spiltButton.setEnabled(false);
    }

    void changeSplitButtonState() {
        spiltButton.setEnabled(!spiltButton.isEnabled());
        spiltButton.setVisible(!spiltButton.isVisible());
    }

    private void playerSplit() {
        changeSplitButtonState();
        currenSet = 1;
        hands.get(1).add(hands.get(0).get(1));
        hands.get(2).add(hands.get(0).get(0));
        new Animation.PokerSpilt(this, hands.get(0).get(0), 1);
    }

    private void setHandPokers() {
        playerDefaultHands = new ArrayList<>();
        bankerHands = new ArrayList<>();
        playerFirstHands = new ArrayList<>();
        playerSecondHands = new ArrayList<>();
        hands = new Hashtable<>(4);
        hands.put(0, playerDefaultHands);
        hands.put(1, playerFirstHands);
        hands.put(2, playerSecondHands);
        hands.put(3, bankerHands);
    }

    private Poker.CardClickedListener getDealedCardListener(int index) {
        if (index == 3) {
            return new Poker.CardClickedListener() {
                @Override
                public void onCardClicked() {
                    playerActionListener.onPlayerDouble(currenSet);
                }
            };
        } else {
            return new Poker.CardClickedListener() {
                @Override
                public void onCardClicked() {
                    System.out.println("PlayerClicked");
                    playerActionListener.onPlayerStand(currenSet);
                }
            };
        }
    }

    public void showTageMessage(int index, int type) {
        JLabel tag = messageTags.get(index * 2 + type);
        tag.setVisible(true);
        tag.setEnabled(true);
    }

    public void setChanged() {

        currenSet = 3 - currenSet;
        for (Poker poker : hands.get(3 - currenSet)) {
            new Animation.PokerSpilt(this, poker, 1);
        }
        for (Poker poker : hands.get(currenSet)) {
            new Animation.PokerSpilt(this, poker, -1);
        }
    }

    private void setMessageTags() {
        messageTags = new ArrayList<>(4);
        ImageIcon imageBust = new ImageIcon("res/Bust.png");
        ImageIcon imageBj = new ImageIcon("res/BlackJack.png");
        JLabel playerBust = new JLabel(imageBust);
        playerBust.setSize(100, 50);
        playerBust.setLocation(90, 250);
        JLabel playerBj = new JLabel(imageBj);
        playerBj.setSize(150, 50);
        playerBj.setLocation(240, 270);
        JLabel bankerBust = new JLabel(imageBust);
        bankerBust.setSize(100, 50);
        bankerBust.setLocation(90, 60);
        JLabel bankerBj = new JLabel(imageBj);
        bankerBj.setSize(150, 50);
        bankerBj.setLocation(240, 20);
        messageTags.add(playerBust);
        messageTags.add(playerBj);
        messageTags.add(bankerBust);
        messageTags.add(bankerBj);
        for (JLabel label : messageTags) {
            label.setVisible(false);
            label.setEnabled(false);
        }
    }
}
