/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Re
 */
public class Animation {
    
    static private int runTime = 400;
    
    
    static public class CardAnimation implements Runnable{
        
        final private JPanel panel;
        private Token token;
        static private double scaleRate = 0.25;
        public int value;
        public int startX;
        public int startY;
        public int endX;
        public int endY;
        
        public CardAnimation(JPanel pan, int val, int sx, int sy, int ex, int ey) {
            this.panel = pan;
            this.value = val;
            this.startX = sx;
            this.startY = sy;
            this.endX = ex;
            this.endY = ey;
            
            token = new Token(value, startX, startY);
            panel.add(token);
        }
        
        @Override
        public void run() {
            panel.repaint();
            
            double rate = 1;
            for(int i = 0; i < 3; ++i) {
                rate += 0.1;
                int afterX = (int) (token.getX() - Token.radius * scaleRate);
                int afterY = (int) (token.getY() - Token.radius * scaleRate);
                
                token.setLocation(afterX, afterY);
                token.setTokenSize((int)(Token.radius * rate));
                //panel.repaint();
                 try {
                    Thread.sleep(runTime / 10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            token.setTokenSize(Token.radius);
            int paveMentX = (endX - token.getX() - Token.radius) / 7;
            int paveMentY = (endY - token.getY()- Token.radius) / 7;  
            for(int i = 0; i < 7; ++i) {
                token.setLocation(token.getX() + paveMentX, token.getY() + paveMentY);
                try {
                    Thread.sleep(runTime / 10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            token.setTokenSize(Token.radius - BetPanel.tableTokens.size() / 3);
        }
        
         /**
         *
         * @return
         */
        public Token getToken() {
            return this.token;               
        }
    }
    
    static public class PokerTurn implements Runnable {

        final private JPanel panel;
        private Poker poker;
        private int centerX;
        private int centerY;
        private double maxScaleRate = 1.15;
        public PokerTurn(JPanel panel, Poker poker) {
            this.panel = panel;
            this.poker = poker;
            this.centerX = poker.getX() + poker.getWidth() / 2;
            this.centerY = poker.getY() + poker.getHeight() / 2;
        }
        @Override
        public void run() {
            int perFrame = runTime / 10;
            double angle = 0;
            boolean originSide = poker.isBack;
            System.out.println("Test" + Math.cos(60));
            for(int i = 0; i < 10; ++i) {
                angle += Math.PI / 10;
                double rate = Math.cos(angle);
                rate = Math.abs(rate);
                double temp = maxScaleRate - 1;
                double scaleRate = maxScaleRate -Math.abs((temp / 5)* i - temp);
                System.out.println("Number" + i + "  " + (int)(Poker.height * scaleRate));
                if(angle >= Math.PI / 2 && poker.isBack == originSide) {
                    System.out.println("Turn");
                    poker.turnOver();
                }
                
                
                poker.setCardSize((int) (Poker.width * rate * scaleRate), (int)(Poker.height * scaleRate));
                poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
                try {
                    Thread.sleep(perFrame);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            poker.setCardSize(Poker.width, Poker.height);
            poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
        }
        
    }
}
    


