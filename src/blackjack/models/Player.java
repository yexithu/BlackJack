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
    private final int index;//对应的存档序号
    private int Counter;//筹码数
    private boolean Insure, Surrender, Split;//保险标志，投降标志，分牌标志
    private ArrayList<Boolean> Surrenders;//投降标志组
    private int DealCount;//游戏局数计数
    private int Win, Lose, Push;//输赢平计数

    //根据
    public Player(String Name, int index) {
        this.Name = Name;
        Counter = 1000;
        Insure = false;
        Surrender = false;
        this.index = index;
    }

    //生成一个只有存档序号的Player对象
    public Player(int index) {
        this.index = index;
        Name = null;
    }

    //获得玩家当前拥有筹码数
    public int getCounter() {
        return Counter;
    }

    //更新玩家筹码数
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

    public String getName() {
        return this.Name;
    }

    //获取玩家统计信息
    public String getHtmlString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>" + "玩家名: ").append(Name).append("<br/>");
        stringBuilder.append("资产: ").append(String.valueOf(this.Counter)).append("<br/>");
        stringBuilder.append("游戏局数: ").append(String.valueOf(this.DealCount)).append("<br/>");
        stringBuilder.append("胜利局数: ").append(String.valueOf(this.Win)).append("<br/>");
        stringBuilder.append("平局数: ").append(String.valueOf(this.Push)).append("<br/>");
        stringBuilder.append("失败局数: ").append(String.valueOf(this.Lose)).append("<br/>").append("</html>");
        return stringBuilder.toString();
    }
}
