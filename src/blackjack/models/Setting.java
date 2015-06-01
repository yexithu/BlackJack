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
//游戏设置
public class Setting {

    private double Penetration;//渗透率
    private int BetMax;//赌注上限
    private int BetMin;//赌注下限
    private int DeckNum;//牌堆所包含的扑克牌副数
    private double BJOdds;//BlackJack赔率
    private int BurnCardNum;//烧牌数目

    public Setting(double Penetration, int BetMax, int BetMin, int DeckNum, double BJOdds, int BurnCardNum) {
        this.Penetration = Penetration;
        this.BJOdds = BJOdds;
        this.BetMax = BetMax;
        this.BetMin = BetMin;
        this.DeckNum = DeckNum;
        this.BurnCardNum = BurnCardNum;
    }

    public static Setting example() {
        return new Setting(0.6, 500, 10, 2, 1.5, 0);
    }

    /**
     * @return the Penetration
     */
    public double getPenetration() {
        return Penetration;
    }

    /**
     * @param Penetration the Penetration to set
     */
    public void setPenetration(double Penetration) {
        this.Penetration = Penetration;
    }

    /**
     * @return the BetMax
     */
    public int getBetMax() {
        return BetMax;
    }

    /**
     * @param BetMax the BetMax to set
     */
    public void setBetMax(int BetMax) {
        this.BetMax = BetMax;
    }

    /**
     * @return the BetMin
     */
    public int getBetMin() {
        return BetMin;
    }

    /**
     * @param BetMin the BetMin to set
     */
    public void setBetMin(int BetMin) {
        this.BetMin = BetMin;
    }

    /**
     * @return the DeckNum
     */
    public int getDeckNum() {
        return DeckNum;
    }

    /**
     * @param DeckNum the DeckNum to set
     */
    public void setDeckNum(int DeckNum) {
        this.DeckNum = DeckNum;
    }

    /**
     * @return the BJOdds
     */
    public double getBJOdds() {
        return BJOdds;
    }

    /**
     * @param BJOdds the BJOdds to set
     */
    public void setBJOdds(double BJOdds) {
        this.BJOdds = BJOdds;
    }

    /**
     * @return the BurnCard
     */
    public int getBurnCard() {
        return getBurnCardNum();
    }

    /**
     * @param BurnCard the BurnCard to set
     */
    public void setBurnCard(int BurnCard) {
        this.setBurnCardNum(BurnCard);
    }

    /**
     * @return the BurnCardNum
     */
    public int getBurnCardNum() {
        return BurnCardNum;
    }

    /**
     * @param BurnCardNum the BurnCardNum to set
     */
    public void setBurnCardNum(int BurnCardNum) {
        this.BurnCardNum = BurnCardNum;
    }
}
