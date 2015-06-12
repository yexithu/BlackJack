
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

    protected ArrayList<Hand> HandArray;

    public Hands() {
        HandArray = new ArrayList();
        HandArray.add(new Hand());
    }

    public boolean isSplit(int i) {
        return HandArray.get(i).canSplit();
    }

    public boolean isSoft(int i) {
        return HandArray.get(i).isSoft();
    }

    public boolean isBust(int i) {
        return HandArray.get(i).isBust();
    }

    public int getTotal(int i) {
        return HandArray.get(i).getTotal();
    }

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

    public int getHandNum() {
        return HandArray.size();
    }
}
