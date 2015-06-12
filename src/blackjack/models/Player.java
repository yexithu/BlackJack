/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */
//玩家的抽象表示
public class Player extends Hands implements Serializable {

    private final String Name;//名字
    private final int index;
    private static int count = 0;
    private int Counter;//筹码数
    private boolean Insure, Surrender, Split;//保险标志，投降标志，分牌标志
    private ArrayList<Boolean> Surrenders;//投降标志组
    private int DealCount;//游戏局数计数
    private int Win, Lose, Push;//输赢平计数

    public Player(String n) {
        Name = n;
        Counter = 1000;
        Insure = false;
        Surrender = false;
        index = count++;
    }

    public Player(int index) {
        this.index = index;
        Name = null;
    }

    public int getCounter() {
        return Counter;
    }

    public void changeCounter(int change) {
        Counter += change;
    }

    //根据游戏状态更新用户统计数据
    public void update(Game.State s) {
        switch (s) {
            case PLAYER_WIN:
                Win += 1;
                break;
            case BANKER_WIN:
                Lose += 1;
                break;
            case PUSH:
                Push += 1;
                break;
        }
        DealCount += 1;
    }

    /**
     * @return the Insure
     */
    public boolean isInsure() {
        return Insure;
    }

    /**
     * @param Insure the Insure to set
     */
    public void setInsure(boolean Insure) {
        this.Insure = Insure;
    }

    /**
     * @return the Surrender
     */
    public boolean isSurrender() {
        return Surrender;
    }

    /**
     * @param Surrender the Surrender to set
     */
    public void setSurrender(boolean Surrender) {
        this.Surrender = Surrender;
    }

    /**
     * @param i
     * @return the Surrenders
     */
    public boolean getSurrenders(int i) {
        return Surrenders.get(i);
    }

    /**
     * @param i
     * @param Surrender
     */
    public void setSurrenders(int i, boolean Surrender) {
        Surrenders.set(i, Surrender);
    }

    //分牌前初始化投降标志组
    public void initialSurrenders() {
        Surrenders = new ArrayList();
        for (int i = 0; i < getHandNum(); i++) {
            Surrenders.add(false);
        }
    }

    public boolean isBJ() {
        return HandArray.get(0).isBJ();
    }

    public boolean isBust() {
        return HandArray.get(0).isBust();
    }

    public void deal(Card c) {
        HandArray.get(0).deal(c);
    }

    public void setBJ() {
        HandArray.get(0).setBJ();
    }

    public void setBJ(boolean BJ) {
        HandArray.get(0).setBJ(BJ);
    }

    public int getTotal() {
        return HandArray.get(0).getTotal();
    }

    public void clear() {
        HandArray = new ArrayList();
        HandArray.add(new Hand());
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }
}
