/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import java.util.Collections;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */
//一个牌堆的抽象表示
public class Deck implements Cloneable {

    private final ArrayList<Card> Cards;//用Card对象的ArrayList表示牌堆
    private final int Num;//属性：牌堆所包含的扑克牌副数
    private int Count;//属性：发牌计数器

    public Deck(int DeckNum) {
        Num = DeckNum;
        Count = 0;
        Cards = new ArrayList();
        Cards.ensureCapacity(Num * 52);
        for (int i = 0; i < Num; i++) { //牌堆初始化
            int j = 0;
            for (Card.Pattern p : Card.Pattern.values()) {
                int k = 0;
                for (Card.Figure f : Card.Figure.values()) {
                    int l = i * 52 + j * 13 + k;
                    Cards.add(new Card(p, f));
                    k++;
                }
                j++;
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Deck o = null;
        try {
            o = (Deck) super.clone();
            for (int i = 0; i < Cards.size(); i++) {
                o.Cards.set(i, (Card) Cards.get(i).clone());
            }
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    //根据渗透率判断是否需要洗牌
    public boolean isShuffle(double Penetration) {
        return Count > Num * 52 * Penetration;
    }

    //洗牌堆
    public void shuffle() {
        Count = 0;//重置发牌计数器
        Collections.shuffle(Cards);
    }

    //烧牌：洗牌后弃掉牌堆顶的若干张牌
    public void burn(int BurnCardNum) {
        Count += BurnCardNum;
    }

    //模拟从牌堆顶获取一张牌
    public Card getCard() {
        return Cards.get(Count++);//发牌后计数器+1
    }

    /*
     public void showDeck() {
     for (int i = 0; i < 52 * Num; i++) {
     System.out.println(Cards.get(i).toString());
     }
     }*/
}
