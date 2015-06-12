/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import blackjack.models.Player;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class PlayerPanel extends ChildPanel {

    static public class WhiteBorderLabel extends JLabel {

        public int index;

        public WhiteBorderLabel(int width, int height) {
            setSize(width, height);
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            setFont(new java.awt.Font("Dialog", 1, 25));
            setForeground(Color.WHITE);
        }
    }

    private ArrayList<WhiteBorderLabel> playerTags;
    private ArrayList<Player> players;
    private int currentIndex = 0;
    private WhiteBorderLabel detailTextLabel;
    private WhiteBorderLabel resetButton;

    public PlayerPanel(ArrayList<Player> players) {
        setLayout(null);
        this.setBackground(Color.BLACK);
        this.players = players;
        setPlayerTags(players);
        setDetailLabel();
        setResetButton();

    }

    private void setPlayerTags(ArrayList<Player> players) {
        playerTags = new ArrayList<>(4);
        players = new ArrayList<>(4);

        for (int i = 0; i < 4; ++i) {
            players.add(new Player(i));
        }
        this.players = players;
        for (int i = 0; i < 4; ++i) {
            WhiteBorderLabel playTag = new WhiteBorderLabel(150, 50);
            playTag.index = i;
            if (players.get(i).getName() == null) {
                playTag.setText("Empty");
            } else {
                playTag.setText(players.get(i).getName());
            }
            playTag.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    currentIndex = playTag.index;
                    upDateDetailString();
                    upDateBorderColor();

                }
            });
            playTag.setLocation(20, 30 + 80 * i);
            playTag.setVisible(true);
            add(playTag);
            playerTags.add(playTag);
        }
        upDateBorderColor();
        System.out.println("Players" + players.size() + "1");
    }

    private void setDetailLabel() {
        this.detailTextLabel = new WhiteBorderLabel(320, 280);
        detailTextLabel.setLocation(200, 30);

        upDateDetailString();
        add(detailTextLabel);
    }

    private void upDateDetailString() {
        for (int i = 0; i < 4; ++i) {
            if (players.get(i).getName() == null) {
                playerTags.get(i).setText("Empty");
            } else {
                playerTags.get(i).setText(players.get(i).getName());
            }
        }
        if (players.get(currentIndex).getName() == null) {
            detailTextLabel.setText("Empty Player");
        } else {
            detailTextLabel.setText(players.get(currentIndex).getHtmlString());
        }
    }

    private void upDateBorderColor() {
        for (WhiteBorderLabel borderLabel : playerTags) {
            borderLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        }
        playerTags.get(currentIndex).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
    }

    public void setResetButton() {
        this.resetButton = new WhiteBorderLabel(70, 40);
        resetButton.setLocation(550, 270);
        resetButton.setText("reset");
        resetButton.setVisible(true);

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                String newName;
                while (true) {
                    newName = JOptionPane.showInputDialog("输入名字");
                    if (newName != null) {
                        if (newName.length() == 0 || newName.length() > 10) {
                            JOptionPane.showMessageDialog(null, newName, "不合法的输入", JOptionPane.WARNING_MESSAGE);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                if (newName != null) {
                    players.set(currentIndex, new Player(newName));
                    upDateDetailString();
                }

            }
        });
        add(resetButton);
    }

    public int getIndex() {
        return this.currentIndex;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }
    

}
