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

    public void run() {

        //到此运行至bet()
        //MainFrame 调用返回四张牌，放在数组里
        initial();//发初始牌等初始化, 

        //玩家拿到BlackJack的提示
        if (Player.isBJ()) {
            System.out.println("You get BJ!\n");
        }
        //庄家需要偷窥的情况
        if (Banker.isPeek()) {
            System.out.println("The banker peeks~\n");
            //买保险
            if (isPlayerInsure()) {
                playerInsure();
            }
            //庄家拿到BlackJack的情况
            if (Banker.isBJ()) {
                Banker.display();
                System.out.println("Banker get BJ!\n");
                if (Player.isBJ()) {
                    Result = State.PUSH;
//                        result();
                } else {
                    Result = State.BANKER_WIN;
//                        result();
                }
            } else {
                System.out.println("\nNothing happened~\n");
            }
        }
        //如果庄家不是BJ，游戏继续
        if (!Banker.isBJ()) {
            //如果玩家拿到BJ的情况
            if (Player.isBJ()) {
                Banker.display();
                Result = State.PLAYER_WIN;
//                    result();
            } else { //如果玩家没拿到BJ，游戏继续
                playerSplit();//分牌
                //如果玩家进行了分牌，游戏按照分牌后的流程进行
                if (Player.isSplit()) {
                    splitInitial();//分牌后的初始化操作
                    splitProcess();//分牌后的游戏流程
                } else if (isPlayerDouble()) { //如果玩家没有分牌，游戏按没有分牌的简单情形继续
                    playerDouble();//加倍
                    if (Player.isBust()) {
                        playerBust();//玩家爆牌的处理
                    } else {
                        bankerAct();//如果加倍后没有爆牌，庄家行动
                        Result = compare(Result);//更新游戏结果
//                            result();
                    }
                } else {
                    playerChoice();//如果玩家没有加倍，玩家选择要牌/停牌/投降
                    if (!Player.isBust() && !Player.isSurrender()) {
                        bankerAct();//如果玩家没有爆牌也没有投降，庄家行动
                        Result = compare(Result);//更新游戏结果
//                            result();
                    }
                }
            }
        }
        if (!Player.isSplit()) {
            result();//如果玩家没有分牌，按照简单情形计算游戏结果
        }

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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //生吞了啊啊啊！
            }
            Card c = Deck.getCard();
            Banker.deal(c);
            gameActionListener.onDeal(3, c);
        }
        if (Banker.isBust()) {
            if (Player.getHandNum() == 1) {
                Results.set(0, State.PLAYER_WIN);
                gameActionListener.onShowResult(0, State.PLAYER_WIN);
            } else {
                for (int i = 1; i < 3; i++) {
                    if (Results.get(i) == State.NULL) {
                        Results.set(i, compare(i));
                        gameActionListener.onShowResult(i, Results.get(i));
                    }
                }
            }
        }
    }

    private State compare(State result) {
        if (result == State.NULL) {
            if (Player.getTotal() < Banker.getTotal()) {
                result = State.BANKER_WIN;
            } else if (Player.getTotal() > Banker.getTotal()) {
                result = State.PLAYER_WIN;
            } else {
                result = State.PUSH;
            }
        }
        return result;
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

    public boolean start() {
        System.out.println("Do you want to start a new game?Y/N");
        return ConsoleScanner.getYorN();
    }

    public boolean isShuffle() {
        return Deck.isShuffle(Setting.getPenetration());
    }

    public void shuffle() {
        if (isShuffle()) {
            Deck.shuffle();
            System.out.println("The deck has been shuffled!");
            if (Setting.getBurnCardNum() > 0) {
                burn();
            }
        }
    }

    public void burn() {
        int i = Setting.getBurnCardNum();
        Deck.burn(i);
        System.out.println("Burn " + i + " cards\n");
    }

    public void bet(int betNum) {
        System.out.println("How much do you want to bet?");
        Player.changeCounter(-betNum);
        bet = betNum;
        pool = bet;
    }

    public boolean isPlayerInsure() {
        System.out.println("Banker may get BJ, do you want to buy insurance?Y/N");
        return ConsoleScanner.getYorN();
    }

    public void playerInsure() {
        Player.changeCounter(-pool / 2);
        Player.setInsure(true);
//        System.out.println("Insurance bought!");
    }

    public boolean isPlayerDouble() {
        System.out.println("Do you want to double?Y/N");
        return ConsoleScanner.getYorN();
    }

    public void playerDouble() {
        Player.changeCounter(-pool);
        pool *= 2;
        Player.deal(Deck.getCard());
        Player.display();
    }

    public void playerChoice() {
        boolean loop = true;
        while (loop) {
            System.out.println("Your choice? Hit:H Stand:S R:Surrender");
            switch (ConsoleScanner.getChoice("H", "h", "S", "s", "R", "r")) {
                case "H":
                case "h":
                    Player.deal(Deck.getCard());
                    Player.display();
                    break;
                case "S":
                case "s":
                    loop = false;
                    break;
                case "R":
                case "r":
                    Player.setSurrender(true);
                    Result = State.BANKER_WIN;
                    System.out.println("You have surrendered!");
//                    result();
                    loop = false;
                    break;
            }
            if (Player.isBust()) {
                System.out.println("Bust!\n");
                Result = State.BANKER_WIN;
                loop = false;
//                result();
            }
        }
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
            Results.set(index, compare(index));
            gameActionListener.onShowResult(index, Results.get(index));
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

    private void playerBust() {
        System.out.println("Bust!\n");
        Result = State.BANKER_WIN;
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
                Results.set(index, compare(index));
                gameActionListener.onShowResult(index, Results.get(index));
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
            }
        }
    }

    public void split() {
        Results.add(State.NULL);
        Results.add(State.NULL);
        Player.split();
        Card c = Deck.getCard();
        Player.deal(1, c);
        gameActionListener.onDeal(0, c);
    }

    public void playerSplit() {
        int i = 0;
        do {
            if (Player.isSplit(i)) {
                Player.split(i, Deck.getCard(), Deck.getCard());
                if (!Player.isSplit()) {
                    Player.setSplit(true);
                }
                i--;
            }
            i++;
        } while (i < Player.getHandNum());
    }

    public void splitInitial() {
        Results = new ArrayList();
        for (int i = 0; i < Player.getHandNum(); i++) {
            Results.add(State.NULL);
        }
        Player.initialSurrenders();
    }

    public void splitProcess() {
        int i = 0;
        boolean flag = false;
        //让玩家顺序处理他的各副手牌
        do {
            Player.display(i);
            if (isPlayerDouble()) {
                playerDouble(i);
                if (Player.isBust(i)) {
                    playerBust(i);
                }
            } else {
                playerChoice(i);
            }
            i++;
        } while (i < Player.getHandNum());
        //判断是否庄家还需行动,如果玩家所有手牌都已爆牌或投降，则庄家不必行动，标志置否
        for (i = 0; i < Player.getHandNum(); i++) {
            if (!(Player.isBust(i) || Player.getSurrenders(i))) {
                flag = true;
            }
        }
        if (flag) {
            bankerAct();
        }
        //顺序计算玩家各副手牌游戏结果
        for (i = 0; i < Player.getHandNum(); i++) {
            if (Results.get(i) == State.NULL) {
                Results.set(i, compare(Results.get(i)));
            }
            result(i);
        }
        Player.showCounter();
    }

    public void playerChoice(int i) {
        boolean loop = true;
        while (loop) {
            System.out.println("Your choice? Hit:H Stand:S R:Surrender");
            switch (ConsoleScanner.getChoice("H", "h", "S", "s", "R", "r")) {
                case "H":
                case "h":
                    Player.deal(i, Deck.getCard());
                    Player.display(i);
                    break;
                case "S":
                case "s":
                    loop = false;
                    break;
                case "R":
                case "r":
                    Player.setSurrenders(i, true);
                    Results.set(i, State.BANKER_WIN);
                    System.out.println("You have surrendered!");
                    loop = false;
                    break;
            }
            if (Player.isBust(i)) {
                System.out.println("Bust!\n");
                Results.set(i, State.BANKER_WIN);
                loop = false;
            }
        }

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
