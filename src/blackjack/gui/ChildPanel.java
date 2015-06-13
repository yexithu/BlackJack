/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class ChildPanel extends JPanel {

    private JLabel exitLabel;

    public ChildPanel() {
        setExitLabel();
    }

    public void setExitLabel() {
        this.exitLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(PlayerPanel.class.getResource("/res/exit.png"));
        exitLabel.setIcon(imageIcon);
        exitLabel.setSize(72, 72);
        exitLabel.setLocation(550, 170);
        exitLabel.setVisible(true);
        add(exitLabel);
    }

    public interface ExitClickedListener {

        public void onExitClicked();
    }

    public void setOnExitListener(ExitClickedListener exitClickedListener) {
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                exitClickedListener.onExitClicked();
            }
        });
    }
}
