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
//庄家的抽象表示
public class Banker extends Hand{

    @Override
    public void display() {
        System.out.println("\nBanker get:");
        super.display();
    }

    @Override
    public void displayFirstCard() {
        System.out.println("\nBanker get:");
        super.displayFirstCard();
    }

    //根据庄家的明牌判断庄家是否要进行偷窥
    public boolean isPeek() {
        return getFirstCard() == 1 || getFirstCard() == 10;
    }

    //根据庄家的明牌判断玩家是否能买保险
    public boolean isInsureable() {
        return getFirstCard() == 1;
    }

    //判断庄家是否继续要牌
    public boolean continueHit() {
        return getTotal() < 17 || (getTotal() == 17 && isSoft());
    }

}
