
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import java.util.ArrayList;

/**
 *
 * @author Martin
 */
//手牌堆
public class Hands {

    protected ArrayList<Hand> HandArray;//用于表示一个手牌对象的数组

    public Hands() {
        HandArray = new ArrayList();
        HandArray.add(new Hand());
    }

    //判断能否分牌
    public boolean isSplit(int i) {
        return HandArray.get(i).canSplit();
    }

    //判断是否软点
    public boolean isSoft(int i) {
        return HandArray.get(i).isSoft();
    }

    //判断是否爆牌
    public boolean isBust(int i) {
        return HandArray.get(i).isBust();
    }

    //获取手牌总点数
    public int getTotal(int i) {
        return HandArray.get(i).getTotal();
    }

    //发牌
    public void deal(int i, Card c) {
        HandArray.get(i).deal(c);
    }

    //进行分牌操作
    public void split() {
        HandArray.add(new Hand());
        HandArray.add(new Hand());
        deal(1, HandArray.get(0).getCard(0));
        deal(2, HandArray.get(0).getCard(1));
    }

    //获取手牌堆中的手牌总数
    public int getHandNum() {
        return HandArray.size();
    }
}
