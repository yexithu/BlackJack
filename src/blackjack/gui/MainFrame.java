/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author Re
 */
public class MainFrame extends JFrame{
    
    public MainFrame() {
        BetPanel betPanel = new BetPanel();
        add(betPanel);
        
        setSize(656, 399);
    }    
}