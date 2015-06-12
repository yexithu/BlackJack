/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class MenuPanel extends JPanel{
    static class BigTextLabel extends JPanel{
        static ImageIcon imageIcon = new ImageIcon("res/mainMenuButton.png");
        JLabel jLabel;
        public BigTextLabel(String text) {
            setLayout(null);
            jLabel = new JLabel(text);
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jLabel.setVerticalAlignment(JLabel.CENTER);
            jLabel.setSize(130, 80);
            jLabel.setFont(new java.awt.Font("Dialog",   1,   25));
            jLabel.setForeground(Color.WHITE);
            this.add(jLabel);
            this.setSize(130, 80);
            this.setVisible(true);
        }
        
        public void paintComponent(Graphics g) {
            g.drawImage(imageIcon.getImage(), 0, 0, this);
        }
    }
    
    private BigTextLabel startLabel;
    private BigTextLabel helpLabel;
    private BigTextLabel playerLabel;
    private BigTextLabel aboutLabel;
    private MainMenuClickedListener mainMenuClickedListener;
    static private ImageIcon backgroundIcon = new ImageIcon("res/mainPanelBackGround.png");
    public MenuPanel() {
        setLayout(null);
        setLabels();
    }
    
    private void setLabels() {
        startLabel = new BigTextLabel("Start");
        add(startLabel);
        startLabel.setLocation(60, 50);
        helpLabel = new BigTextLabel("Help");
        add(helpLabel);
        helpLabel.setLocation(60, 230);
        playerLabel = new BigTextLabel("Player");
        add(playerLabel);
        playerLabel.setLocation(410, 50);
        aboutLabel = new BigTextLabel("About");
        add(aboutLabel);
        aboutLabel.setLocation(410, 230);
                
    }
    
    public void setMainMenuListeners(MainMenuClickedListener mainMenuClickedListener) {
        this.mainMenuClickedListener = mainMenuClickedListener;
        startLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                mainMenuClickedListener.onStart();
            }
        });
        helpLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                mainMenuClickedListener.onHelp();
            }
        });
        playerLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                mainMenuClickedListener.onStart();
            }
        });
        aboutLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                mainMenuClickedListener.onAbout();
            }
        });
        
    }
    
    public void paintComponent(Graphics g) {
            g.drawImage(backgroundIcon.getImage(), 0, 0, this);
    }
    
    public interface MainMenuClickedListener{
        public void onStart() ;
        public void onPlayer();
        public void onHelp();
        public void onAbout();
    }
}
