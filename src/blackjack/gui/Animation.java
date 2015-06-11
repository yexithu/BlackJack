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

    static public class TokenMove implements Runnable {

        final private JPanel panel;
        private Token token;
        static private double maxScaleRate = 1.3;
        static private int runTime = 400;
        public int value;
        public int startX;
        public int startY;
        public int endX;
        public int endY;
        public boolean isAdd;

        public TokenMove(JPanel pan, Token token, int ex, int ey, boolean isAdd) {
            this.panel = pan;
            this.value = token.parValue;
            this.startX = token.centerX;
            this.startY = token.centerY;
            this.token = token;
            this.endX = ex;
            this.endY = ey;
            this.isAdd = isAdd;
            panel.add(token);
            new Thread(this).start();
        }

        @Override
        public void run() {
            double rate = 1;
            token.pauseClickedListener();
//            BetPanel.defaultTokens.get(token.parValue).pauseClickedListener();
            for (int i = 0; i < 3; ++i) {
                rate += (maxScaleRate - 1) / 3;
                int afterX = (int) (token.getX() - Token.radius * (rate - 1));
                int afterY = (int) (token.getY() - Token.radius * (rate - 1));
                token.setLocation(afterX, afterY);
                token.setTokenSize((int) (Token.radius * rate));
                //panel.repaint();
                try {
                    Thread.sleep(runTime / 10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            token.setTokenSize(Token.radius);
            int paveMentX = (endX - token.getX() - Token.radius) / 7;
            int paveMentY = (endY - token.getY() - Token.radius) / 7;
            for (int i = 0; i < 7; ++i) {
                token.setLocation(token.getX() + paveMentX, token.getY() + paveMentY);
                threadSleep(runTime / 10);
            }
            token.centerX = endX;
            token.centerY = endY;
            token.continueClickedListener();
//            BetPanel.defaultTokens.get(token.parValue).continueClickedListener();

            if (isAdd) {
                token.setTokenSize(Token.radius - BetPanel.tableTokens.size() / 3);
            } else {
                panel.remove(token);
                BetPanel.defaultTokens.get(token.parValue).setEnabled(true);
                BetPanel.defaultTokens.get(token.parValue).setVisible(true);
                panel.repaint();
            }
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

        static private int runTime = 400;
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
            panel.add(this.poker);
            new Thread(this).start();
        }

        @Override
        public void run() {
            int perFrame = runTime / 10;
            double angle = 0;
            boolean originSide = poker.isBack;
            for (int i = 0; i < 10; ++i) {
                angle += Math.PI / 10;
                double rate = Math.cos(angle);
                rate = Math.abs(rate);
                double temp = maxScaleRate - 1;
                double scaleRate = maxScaleRate - Math.abs((temp / 5) * i - temp);
                if (angle >= Math.PI / 2 && poker.isBack == originSide) {
                    poker.turnOver();
                }
                poker.setCardSize((int) (Poker.width * rate * scaleRate), (int) (Poker.height * scaleRate));
                poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
                threadSleep(perFrame);
            }
            poker.setCardSize(Poker.width, Poker.height);
            poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
        }
    }

    static class PokerTurnMove implements Runnable {

        static private int turnRunTime = 300;
        static private int moveRunTime = 300;
        final JPanel panel;
        private Poker poker;
        private int centerX;
        private int centerY;
        private int endX;
        private int endY;
        double maxScaleRate = 1.3;

        public PokerTurnMove(JPanel panel, Poker poker, int eX, int eY) {
            this.panel = panel;
            this.poker = poker;
            this.centerX = poker.getX() + poker.getWidth() / 2;
            this.centerY = poker.getY() + poker.getHeight() / 2;
            this.endX = eX;
            this.endY = eY;
            panel.add(this.poker);
            new Thread(this).start();
        }

        @Override
        public void run() {
            int perFrame = turnRunTime / 10;
            double angle = 0;
            boolean originSide = poker.isBack;
            for (int i = 0; i < 10; ++i) {
                angle += Math.PI / 10;
                double rate = Math.cos(angle);
                rate = Math.abs(rate);
                double temp = maxScaleRate - 1;
                double scaleRate = (maxScaleRate - 1) * i / 10 + 1;
                if (angle >= Math.PI / 2 && poker.isBack == originSide) {
                    poker.turnOver();
                }
                poker.setCardSize((int) (Poker.width * rate * scaleRate), (int) (Poker.height * scaleRate));
                poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
                threadSleep(perFrame);

            }
            poker.setCardSize(Poker.width, Poker.height);
            poker.setLocation(centerX - poker.getWidth() / 2, centerY - poker.getHeight() / 2);
            perFrame = moveRunTime / 10;
            int pavementX = (endX - poker.getX()) / 10;
            int pavementY = (endY - poker.getY()) / 10;
            for (int i = 0; i < 10; ++i) {
                poker.setLocation(poker.getX() + pavementX, poker.getY() + pavementY);
                threadSleep(perFrame);
            }
            poker.setLocation(endX, endY);
        }
    }

    public static class PokerMove implements Runnable {

        static int runTime = 400;
        static double maxScaleRate = 1.3;
        final JPanel panel;
        private Poker poker;
        private int endX;
        private int endY;
        private int centerX;
        private int centerY;

        public PokerMove(JPanel panel, Poker poker, int eX, int eY) {
            this.panel = panel;
            this.poker = poker;
            this.endX = eX;
            this.endY = eY;
            this.centerX = poker.getX() + poker.getWidth() / 2;
            this.centerY = poker.getY() + poker.getHeight() / 2;
            panel.add(this.poker);
            new Thread(this).start();
        }

        @Override
        public void run() {
            double rate = 1;
            for (int i = 0; i < 3; ++i) {
                rate += (maxScaleRate - 1) / 3;
                int afterX = (int) (poker.getX() - Poker.width * (rate - 1) / 2);
                int afterY = (int) (poker.getY() - Poker.height * (rate - 1) / 2);
                poker.setLocation(afterX, afterY);
                poker.setCardSize((int) (Poker.width * rate), (int) (Poker.height * rate));
                //panel.repaint();
                threadSleep(runTime / 10);
            }
            poker.setCardSize(Poker.width, Poker.height);
            int paveMentX = (endX - poker.getX()) / 7;
            int paveMentY = (endY - poker.getY()) / 7;
            for (int i = 0; i < 7; ++i) {
                poker.setLocation(poker.getX() + paveMentX, poker.getY() + paveMentY);
                threadSleep(runTime / 10);
            }
            poker.setLocation(endX, endY);
        }
    }

    static public void threadSleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static class PokerPeeked implements Runnable {

        static int runTime = 500;
        static double maxScaleRate = 1.3;
        public Poker poker;

        public PokerPeeked(Poker poker) {
            this.poker = poker;
            new Thread(this).start();
        }

        @Override
        public void run() {
            int perFrame = runTime / 10;
            int centerX = poker.getX() + Poker.width / 2;
            int topX = poker.getX();
            int topY = poker.getY();
            double rateLine = 2 * (maxScaleRate - 1) / 10;
            for (int i = 0; i < 10; ++i) {
                double scaleRate = maxScaleRate - Math.abs(maxScaleRate - 1 - rateLine * i);
                int endX = (int) (centerX - Poker.width * scaleRate / 2);
                int endY = (int) (topY - 3 * Poker.height * (scaleRate - 1) / 4);
                poker.setCardSize((int) (Poker.width * scaleRate), (int) (Poker.height * scaleRate));
                poker.setLocation(endX, endY);
                threadSleep(perFrame);
            }
            poker.setCardSize(Poker.width, Poker.height);
            poker.setLocation(topX, topY);
        }

    }

    public static class expectantTaskManager implements Runnable {

        private int time;
        private ExpectantTask expectantTask;

        public interface ExpectantTask {

            void expectantTask();
        }

        public expectantTaskManager(int time, ExpectantTask expectantTask) {
            this.expectantTask = expectantTask;
            this.time = time;
            new Thread(this).start();
        }

        @Override
        public void run() {
            threadSleep(time);
            expectantTask.expectantTask();
        }
    }

    public static class PokerSpilt implements Runnable {

        static int offsetX = 300, offsetY = -50;
        static int runTime = 300;
        static double maxScaleRate = 1.5;
        //1 远处 , -1 进出
        int flag;
        JPanel panel;
        Poker poker;

        public PokerSpilt(JPanel panel, Poker poker, int flag) {
            this.panel = panel;
            this.poker = poker;
            this.flag = flag;
            new Thread(this).start();
        }

        @Override
        public void run() {
            int offX = offsetX * flag;
            int offY = offsetY * flag;
            int endX = poker.getX() + offX;
            int endY = poker.getY() + offY;
            int perFrame = runTime / 10;
            for (int i = 0; i < 10; ++i) {
                double rate = 1;
                if (flag == 1) {
                    rate = 1 - ((1 - 1 / maxScaleRate) * i / 10);
                } else {
                    rate = (1 + ((maxScaleRate - 1) * i / 10)) / maxScaleRate;
                }
                poker.setLocation(poker.getX() + offX / 10, poker.getY() + offY / 10);
                poker.setCardSize((int) (Poker.width * rate), (int) (Poker.height * rate));
                threadSleep(perFrame);
            }
            poker.setLocation(endX, endY);
        }
    }
}
