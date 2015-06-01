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
public class Hand implements Cloneable {

    private final ArrayList<Card> Cards;//用Card对象的ArrayList表示牌堆
    private boolean Soft = false, BJ = false;//Soft:手牌点数是否为软点 BJ：手牌是否是BlackJack

    public Hand() {
        Cards = new ArrayList();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Hand o = null;
        try {
            o = (Hand) super.clone();
            o.Cards.addAll((ArrayList) Cards.clone());
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
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

    //发手牌
    public void deal(Card c) {
        Cards.add(c);
    }

    //删除最后一张手牌（分牌用）
    public Card deleteLast(int n) {
        return Cards.remove(Cards.size() - 1);
    }

    //显示手牌
    public void display() {
        for (Card c : Cards) {
            System.out.println(c);
        }
        System.out.println("\nThe value of this hand: " + getTotal() + "\n");
    }

    //显示第一张手牌
    public void displayFirstCard() {
        System.out.println(Cards.get(0) + "\n");
    }

    //判断手牌能否分牌
    public boolean canSplit() {
        return Cards.get(0).equals(Cards.get(1));
    }

    public boolean isSoft() {
        return Soft;
    }

    /**
     * @return the BJ
     */
    public boolean isBJ() {
        return BJ;
    }

    public void setBJ() {
        setBJ(getTotal() == 21);
    }

    /**
     * @param BJ the BJ to set
     */
    public void setBJ(boolean BJ) {
        this.BJ = BJ;
    }

    public boolean isBust() {
        return getTotal() > 21;
    }

}
