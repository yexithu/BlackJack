/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

/**
 *
 * @author Martin
 */
//一张扑克牌的抽象表示
public class Card implements Cloneable {

    //表示花色的枚举类型
    public static enum Pattern {

        SPADE("S"), HEART("H"), PLAM("P"), DIAMOND("D");
        public final String Name;

        private Pattern(String Name) {
            this.Name = Name;
        }

        @Override   
        public String toString() {
            return Name;
        }
    }

    //表示点数的枚举类型
    public static enum Figure {

        ACE("A", 1), TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9), TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KNIGHT("K", 10);
        public final String Name;
        public final int Value;

        private Figure(String Name, int Value) {
            this.Name = Name;
            this.Value = Value;
        }

        @Override
        public String toString() {
            return Name;
        }
    }

    protected Pattern Type;//属性：扑克牌的花色
    protected Figure Value;//属性：扑克牌的点数

    public Card(Card.Pattern p, Card.Figure f) {
        Type = p;
        Value = f;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    public int getValue() {
        return Value.Value;
    }

    public boolean equals(Card c) {
        return this.getValue() == c.getValue();
    }

    @Override
    public String toString() {
        return Type + "-" + Value;
    }
}
