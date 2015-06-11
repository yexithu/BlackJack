/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import blackjack.gui.Animation;
import blackjack.util.ConsoleScanner;

import java.util.ArrayList;

/**
 * @author Martin
 */
//游戏过程的抽象表示
public class Game {

    //代表游戏结果的枚举类型
    public static enum State {

        NULL, PLAYER_WIN, BANKER_WIN, PUSH;
    }
    private final Player Player;
    private final Banker Banker;
    private final Deck Deck;
    private final Setting Setting;
    private State Result;//游戏结果（对于没有分牌的简单情况）
    private ArrayList<State> Results;//游戏结果组（有分牌的情况）
    private int pool, bet;//pool：赌池，bet：赌注
    private GameActionListener gameActionListener;

    public Game(String Name) {
        Player = new Player(Name);
        Banker = new Banker();
        Setting = blackjack.models.Setting.example();
        Deck = new Deck(Setting.getDeckNum());
        Results = new ArrayList();
        Deck.shuffle();//第一次运行前先洗牌
    }

    public void initial() {
        Card[] cards = new Card[4];
        for (int i = 0; i < 4; ++i) {
            cards[i] = Deck.getCard();
        }
        Player.deal(cards[0]);
        Banker.deal(cards[1]);
        Player.deal(cards[2]);
        Banker.deal(cards[3]);
        Player.setBJ();
        Banker.setBJ();
        Banker.displayFirstCard();
        Player.display();
        Results.clear();
        Results.add(State.NULL);
        gameActionListener.onInitial(cards);
        new Animation.expectantTaskManager(1500, new Animation.expectantTaskManager.ExpectantTask() {
            @Override
            public void expectantTask() {
                if (Banker.isPeek()) {
                    gameActionListener.onBankerPeek();
                    gameActionListener.showChoiceDialog();
                    if (Banker.isBJ()) {
                        gameActionListener.onBankerDisplayCard();
                        if (Player.isBJ()) {
                            Results.set(0, State.PUSH);
                            gameActionListener.onShowResult(0, State.PUSH);
                        } else {
                            gameActionListener.showTagMessage(1, 1);
                            Results.set(0, State.BANKER_WIN);
                            gameActionListener.onShowResult(0, State.BANKER_WIN);
                        }
                    }
                } else if (Player.isBJ()) {
                    gameActionListener.showTagMessage(0, 1);
                    Results.set(0, State.PLAYER_WIN);
                    gameActionListener.onShowResult(0, State.PLAYER_WIN);
                } else {
                    //设置Choice Listener
                }
            }
        });
    }

    private void bankerAct() {
        gameActionListener.onBankerDisplayCard();
        while (!Banker.isBust() && Banker.continueHit()) {
            Card c = Deck.getCard();
            Banker.deal(c);
            gameActionListener.onDeal(3, c);
        }
        if (Banker.isBust()) {
            gameActionListener.showTagMessage(1, 0);
            if (Player.getHandNum() == 1) {
                Results.set(0, State.PLAYER_WIN);
                gameActionListener.onShowResult(0, State.PLAYER_WIN);
            } else {
                for (int i = 1; i < 3; i++) {
                    if (Results.get(i) == State.NULL) {
                        Results.set(i, State.PLAYER_WIN);
                        gameActionListener.onShowResult(i, State.PLAYER_WIN);
                    }
                }
            }
        }
    }

    public void refresh() {
        Result = State.NULL;
        Player.clear();
        Banker.clear();
        Player.setInsure(false);
        Player.setSurrender(false);
        Player.setSplit(false);
        Player.setBJ(false);
        Banker.setBJ(false);
        pool = 0;
    }

    public boolean isShuffle() {
        return Deck.isShuffle(Setting.getPenetration());
    }

    public void shuffle() {
        if (isShuffle()) {
            Deck.shuffle();
            if (Setting.getBurnCardNum() > 0) {
                burn();
            }
        }
    }

    public void burn() {
        int i = Setting.getBurnCardNum();
        Deck.burn(i);
    }

    public void bet(int betNum) {
        Player.changeCounter(-betNum);
        bet = betNum;
        pool = bet;
    }

    public void playerInsure() {
        Player.changeCounter(-pool / 2);
        Player.setInsure(true);
    }

    public void playerHit(int index) {
        Card c = Deck.getCard();
        Player.deal(index, c);
        gameActionListener.onDeal(index, c);
        playerBustControl(index);
    }

    public void playerSurrend(int index) {
        Player.setSurrenders(index, true);
        Results.set(index, State.BANKER_WIN);
        if (index == 1) {
            Card c = Deck.getCard();
            Player.deal(2, c);
            gameActionListener.onChangeSet(c);
        } else {
            gameActionListener.onShowResult(index, State.BANKER_WIN);
        }
    }

    public void playerStand(int index) {
        if (index == 1) {
            Card c = Deck.getCard();
            Player.deal(2, c);
            gameActionListener.onChangeSet(c);
        } else {
            bankerAct();
            if (Results.get(index) == State.NULL) {
                Results.set(index, compare(index));
                gameActionListener.onShowResult(index, Results.get(index));
            }
            if (index == 2) {
                if (Results.get(1) == State.NULL) {
                    Results.set(1, compare(1));
                    gameActionListener.onShowResult(1, Results.get(1));
                }
            }
        }
    }

    private State compare(int index) {
        State result;
        if (Player.getTotal(index) < Banker.getTotal()) {
            result = State.BANKER_WIN;
        } else if (Player.getTotal(index) > Banker.getTotal()) {
            result = State.PLAYER_WIN;
        } else {
            result = State.PUSH;
        }
        return result;
    }

    public void result() {
        try {
            switch (Result) {
                case PLAYER_WIN:
                    System.out.println("You Win!\n");
                    if (Player.isBJ()) {
                        Player.changeCounter((int) (pool * 2.5));
                    } else {
                        Player.changeCounter(pool * 2);
                    }
                    break;
                case BANKER_WIN:
                    System.out.println("You lose!\n");
                    if (Player.isInsure() && Banker.isBJ()) {
                        Player.changeCounter(pool / 2);
                        System.out.println("Insurance paid!\n");
                    }
                    if (Player.isSurrender()) {
                        Player.changeCounter(pool / 2);
                    }
                    break;
                case PUSH:
                    System.out.println("Push!\n");
                    Player.changeCounter(pool);
                    break;
                default:
                    throw new Exception();
            }
            Player.showCounter();
            Player.update(Result);
        } catch (Exception e) {
            System.out.println("Unknown error!");
        }
    }

    public void result(int i) {
        try {
            switch (Results.get(i)) {
                case PLAYER_WIN:
                    System.out.println("For your No." + i + " hand, you Win!\n");
                    if (Player.isBJ()) {
                        Player.changeCounter((int) (pool * 2.5));
                    } else {
                        Player.changeCounter(pool * 2);
                    }
                    break;
                case BANKER_WIN:
                    System.out.println("For your No." + i + " hand, you lose!\n");
                    if (Player.isInsure() && Banker.isBJ()) {
                        Player.changeCounter(pool / 2);
                        System.out.println("Insurance paid!\n");
                    }
                    if (Player.isSurrender()) {
                        Player.changeCounter(pool / 2);
                    }
                    break;
                case PUSH:
                    System.out.println("For your No." + i + " hand, push!\n");
                    Player.changeCounter(pool);
                    break;
                default:
                    throw new Exception();
            }
            Player.update(Result);
        } catch (Exception e) {
            System.out.println("Unknown error!");
        }
    }

    public void playerDouble(int index) {
        Player.changeCounter(-bet);
        pool += bet;
        Card c = Deck.getCard();
        gameActionListener.onDouble(index, c);
        Player.deal(index, c);
        playerBustControl(index);
        if (Results.get(index) == State.NULL) {
            if (index == 1) {
                c = Deck.getCard();
                Player.deal(2, c);
                gameActionListener.onChangeSet(c);
            } else {
                bankerAct();
                if (Results.get(index) == State.NULL) {
                    Results.set(index, compare(index));
                    gameActionListener.onShowResult(index, Results.get(index));
                }
                if (index == 2) {
                    if (Results.get(1) == State.NULL) {
                        Results.set(1, compare(1));
                        gameActionListener.onShowResult(1, Results.get(1));
                    }
                }
            }
        }
    }

    private void playerBustControl(int index) {
        if (Player.isBust(index)) {
            gameActionListener.showTagMessage(0, 0);
            Results.set(index, State.BANKER_WIN);
            if (index == 1) {
                Card c = Deck.getCard();
                Player.deal(2, c);
                gameActionListener.onChangeSet(c);
            } else {
                gameActionListener.onShowResult(index, Results.get(index));
                if (index == 2) {
                    if (Results.get(1) == State.NULL) {
                        bankerAct();
                        if (Results.get(1) == State.NULL) {
                            Results.set(1, compare(1));
                            gameActionListener.onShowResult(1, Results.get(1));
                        }
                    }
                }
            }
        }
    }

    public void split() {
        Results.add(State.NULL);
        Results.add(State.NULL);
        Player.split();
        Card c = Deck.getCard();
        Player.deal(1, c);
        gameActionListener.onDeal(1, c);
    }

    public void splitInitial() {
        Results = new ArrayList();
        for (int i = 0; i < Player.getHandNum(); i++) {
            Results.add(State.NULL);
        }
        Player.initialSurrenders();
    }

    public void playerBust(int index) {
        gameActionListener.showTagMessage(0, 0);
        Results.set(index, State.BANKER_WIN);
        gameActionListener.onShowResult(index, State.BANKER_WIN);
    }

    public void setGameActionListener(GameActionListener gameActionListener) {
        this.gameActionListener = gameActionListener;
    }

    public interface GameActionListener {

        void onInitial(Card[] cards);
        /*
         Index传玩家0或者庄家1玩家手牌1庄家手牌，2玩家分牌第一幅，3玩家分牌第二幅
         */

        void onDouble(int index, Card card);

        void onDeal(int index, Card card);

        void onBankerDisplayCard();

        void onShowResult(int index, State state);

        void onBankerPeek();

        void showChoiceDialog();

        void onShowMessageDialog(String input);

        void showTagMessage(int index, int type);

        void onChangeSet(Card c);
    }
}
