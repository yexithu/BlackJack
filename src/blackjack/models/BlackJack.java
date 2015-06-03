/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import blackjack.gui.MainFrame;

/**
 *
 * @author Martin
 */
public class BlackJack extends MainFrame{

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        //Game g=new Game("Martin");
        BlackJack blackJack = new BlackJack();
        blackJack.setVisible(true);
        blackJack.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //g.run();

    }
}
