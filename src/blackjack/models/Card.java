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

        SPADE("S", 3), HEART("H", 0), PLAM("P", 1), DIAMOND("D", 2);
        public final String Name;
        public final int index;

        private Pattern(String Name, int index) {
            this.Name = Name;
            this.index = index;
        }

        @Override
        public String toString() {
            return Name;
        }
    }

    //表示点数的枚举类型
    public static enum Figure {

        ACE("A", 1, 12), TWO("2", 2, 0), THREE("3", 3, 1), FOUR("4", 4, 2), FIVE("5", 5, 3), SIX("6", 6, 4), SEVEN("7", 7, 5), EIGHT("8", 8, 6), NINE("9", 9, 7), TEN("10", 10, 8), JACK("J", 10, 9), QUEEN("Q", 10, 10), KNIGHT("K", 10, 11);
        public final String Name;
        public final int Value;
        public final int index;

        private Figure(String Name, int Value, int index) {
            this.Name = Name;
            this.Value = Value;
            this.index = index;
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

    public int getTypeIndex() {
        return this.Type.index;
    }

    public int getValueIndex() {
        return this.Value.index;
    }
}
