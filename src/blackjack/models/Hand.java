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

    private final ArrayList<Card> Cards;//用Card对象的ArrayList表示手牌
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

    public Card getCard(int index) {
        return Cards.get(index);
    }

    //发手牌
    public void deal(Card c) {
        Cards.add(c);
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
