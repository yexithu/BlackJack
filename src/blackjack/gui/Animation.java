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
        }
        
         /**
         *
         * @return
         */
        public Token getToken() {
            System.out.println("InRun" + String.valueOf(this.token == null));
            return this.token;
                
        }
    }
}
    


