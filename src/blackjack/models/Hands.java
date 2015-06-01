
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

    public void display(int i) {
        System.out.println("Now displaying No." + (i + 1) + " hand:");
        HandArray.get(i).display();
    }

    //进行分牌操作
    public void split(int i, Card c1, Card c2) { //c1,c2新发的两张牌
        HandArray.add(i + 1, new Hand());//在手牌堆中新建一个手牌对象
        deal(i + 1, HandArray.get(i).deleteLast(i));//在第i副手牌中删除最后一张牌，置入第i+1副手牌中
        deal(i, c1);//在第i副手牌中发一张新牌
        deal(i + 1, c2);//在第i+1副手牌中发一张新牌
    }

    public int getHandNum() {
        return HandArray.size();
    }
}
