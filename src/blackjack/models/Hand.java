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
//一副手牌的抽象表示
public class Hand {

    private final ArrayList<Card> Cards;//用Card对象的ArrayList表示手牌
    private boolean Soft = false, BJ = false;//Soft:手牌点数是否为软点 BJ：手牌是否是BlackJack

    public Hand() {
        Cards = new ArrayList();
    }

    //清空手牌
    public void clear() {
        Cards.clear();
    }

    //计算手牌点数
    public int getTotal() {
        boolean CheckSoft = false;
        int num = 0;
        for (Card c : Cards) {
            if ("A".equals(c.Value.Name)) {
                CheckSoft = true;
            }
            num += c.Value.Value;
        }
        if (CheckSoft && num <= 11) { //对软点情况的处理
            num += 10;
            Soft = true;
        }
        return num;
    }

    //获得第一张手牌的点数
    public int getFirstCard() {
        return Cards.get(0).Value.Value;
    }

    //获得指定位置的手牌
    public Card getCard(int index) {
        return Cards.get(index);
    }

    //向手牌中发牌
    public void deal(Card c) {
        Cards.add(c);
    }

    //判断手牌能否分牌
    public boolean canSplit() {
        return Cards.get(0).equals(Cards.get(1));
    }

    //判断是否软点
    public boolean isSoft() {
        return Soft;
    }

    /**
     * @return the BJ
     */
    public boolean isBJ() {
        return BJ;
    }

    //游戏开始时设置BJ标志
    public void setBJ() {
        setBJ(getTotal() == 21);
    }

    /**
     * @param BJ the BJ to set
     */
    public void setBJ(boolean BJ) {
        this.BJ = BJ;
    }

    //判断是否爆掉
    public boolean isBust() {
        return getTotal() > 21;
    }
}
