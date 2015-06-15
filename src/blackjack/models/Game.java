/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import blackjack.gui.Animation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @author Martin
 */
//游戏过程的抽象表示
public class Game {

    //代表游戏结果的枚举类型
    public static enum State {

        NULL, PLAYER_WIN, BANKER_WIN, PUSH;
    }
    private Player Player;
    private final Banker Banker;
    private final Deck Deck;
    private final Setting Setting;
    private ArrayList<State> Results;//游戏结果组（有分牌的情况）
    private int pool, bet;//pool：赌池，bet：赌注
    private GameActionListener gameActionListener;//游戏事件监听器

    public Game() {
        Player = null;
        Banker = new Banker();
        Setting = blackjack.models.Setting.example();
        Deck = new Deck(Setting.getDeckNum());
        Results = new ArrayList();
        Deck.shuffle();//第一次运行前先洗牌
    }

    //检测是否已存在存档，如果没有则提示新建
    public boolean setPlayer(int index) {
        boolean flag = true;
        Player = PlayerSet.readPlayer(index);
        if (Player.getName() == null) {
            String newName;
            while (true) {
                newName = JOptionPane.showInputDialog("检测到您未设置账户，请输入新账户名");
                if (newName != null) {
                    if (newName.length() == 0) {
                        JOptionPane.showMessageDialog(null, "输入为空", "不合法的输入", JOptionPane.WARNING_MESSAGE);
                    } else if (newName.length() > 10) {
                        JOptionPane.showMessageDialog(null, "账户名长度不能超过10", "不合法的输入", JOptionPane.WARNING_MESSAGE);
                    } else {
                        break;
                    }
                } else {
                    flag = false;
                    break;
                }
            }
            Player = new Player(newName, index);
        }
        return flag;
    }

    //初始化游戏
    public void initial() {
        Card[] cards = new Card[4];
        for (int i = 0; i < 4; ++i) {
            cards[i] = Deck.getCard();
        }
        Player.deal(0, cards[0]);
        Banker.deal(cards[1]);
        Player.deal(0, cards[2]);
        Banker.deal(cards[3]);
        Player.setBJ();
        Banker.setBJ();
        Results.clear();
        Results.add(State.NULL);
        gameActionListener.onInitial(cards);
        new Animation.expectantTaskManager(1200, new Animation.expectantTaskManager.ExpectantTask() {
            @Override
            public void expectantTask() {
                if (Banker.isPeek()) {
                    gameActionListener.onBankerPeek();
                    if (Banker.isInsureable()) {
                        gameActionListener.showChoiceDialog();
                    }
                    if (Banker.isBJ()) {
                        gameActionListener.onBankerDisplayCard();
                        if (Player.isBJ()) {
                            Results.set(0, State.PUSH);
                            result(0);
                        } else {
                            gameActionListener.showTagMessage(1, 1);
                            Results.set(0, State.BANKER_WIN);
                            result(0);
                        }
                    }
                }
                if (Results.get(0) == State.NULL) {
                    if (Player.isBJ()) {
                        gameActionListener.showTagMessage(0, 1);
                        Results.set(0, State.PLAYER_WIN);
                        result(0);
                    }
                }
            }
        });
    }

    //重置游戏
    public void refresh() {
        Player.clear();
        Banker.clear();
        Player.setInsure(false);
        Player.setSurrender(false);
        Player.setBJ(false);
        Banker.setBJ(false);
        pool = 0;
    }

    //庄家要牌操作
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
            } else {
                for (int index = 1; index < 3; index++) {
                    if (Results.get(index) == State.NULL) {
                        Results.set(index, State.PLAYER_WIN);
                    }
                }
            }
        }
    }

    //根据渗透率判断是否需要洗牌
    public boolean isShuffle() {
        return Deck.isShuffle(Setting.getPenetration());
    }

    //洗牌操作
    public void shuffle() {
        if (isShuffle()) {
            Deck.shuffle();
            gameActionListener.onShowMessageDialog("The Deck Has Been Shuffled!");
            if (Setting.getBurnCardNum() > 0) {
                burn();
            }
        }
    }

    //烧牌操作
    public void burn() {
        int i = Setting.getBurnCardNum();
        Deck.burn(i);
    }

    //检查玩家下注是否在赌注限制范围内
    public boolean checkBet(int betNum) {
        return !(betNum < Setting.getBetMin() || betNum > Setting.getBetMax());
    }

    //玩家下注操作
    public void bet(int betNum) {
        Player.changeCounter(-betNum);
        bet = betNum;
        pool = bet;
    }

    //玩家买保险操作
    public void playerInsure() {
        Player.changeCounter(-getPool() / 2);
        Player.setInsure(true);
    }

    //玩家要牌操作
    public void playerHit(int index) {
        Card c = Deck.getCard();
        Player.deal(index, c);
        gameActionListener.onDeal(index, c);
        playerBustControl(index);
    }

    //玩家投降操作
    public void playerSurrend(int index) {
        Player.setSurrenders(index, true);
        Results.set(index, State.BANKER_WIN);
        if (index == 1) {
            Card c = Deck.getCard();
            Player.deal(2, c);
            gameActionListener.onChangeSet(c);
        } else {
            result(index);
        }
    }

    //玩家停牌操作
    public void playerStand(int index) {
        if (index == 1) {
            Card c = Deck.getCard();
            Player.deal(2, c);
            gameActionListener.onChangeSet(c);
        } else {
            bankerAct();
            if (Results.get(index) == State.NULL) {
                Results.set(index, compare(index));
            }
            result(index);
            if (index == 2) {
                if (Results.get(1) == State.NULL) {
                    Results.set(1, compare(1));
                }
                result(1);
            }
        }
    }

    //计算输赢平
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

    //计算游戏结果，更新数据
    public void result(int index) {
        try {
            switch (Results.get(index)) {
                case PLAYER_WIN:
                    if (Player.isBJ()) {
                        Player.changeCounter((int) (getPool() * 2.5));
                    } else {
                        Player.changeCounter(getPool() * 2);
                    }
                    break;
                case BANKER_WIN:
                    if (Player.isInsure() && Banker.isBJ()) {
                        Player.changeCounter(getPool() / 2);
                    }
                    if (Player.isSurrender()) {
                        Player.changeCounter(getPool() / 2);
                    }
                    break;
                case PUSH:
                    Player.changeCounter(getPool());
                    break;
                default:
                    throw new Exception();
            }
            Player.update(Results.get(index));
            gameActionListener.onShowResult(index, Results.get(index));
        } catch (Exception e) {
            gameActionListener.onShowMessageDialog("Unknown Error! Game State is NULL!");
        }
    }

    //玩家加倍操作
    public void playerDouble(int index) {
        Player.changeCounter(-bet);
        pool += bet;
        Card c = Deck.getCard();
        gameActionListener.onDouble(index, c);
        Player.deal(index, c);
        new Animation.expectantTaskManager(700, new Animation.expectantTaskManager.ExpectantTask() {

            @Override
            public void expectantTask() {
                playerBustControl(index);
                if (Results.get(index) == State.NULL) {
                    if (index == 1) {
                        Card c = Deck.getCard();
                        Player.deal(2, c);
                        gameActionListener.onChangeSet(c);
                    } else {
                        bankerAct();
                        if (Results.get(index) == State.NULL) {
                            Results.set(index, compare(index));
                        }
                        result(index);
                        if (index == 2) {
                            if (Results.get(1) == State.NULL) {
                                Results.set(1, compare(1));
                            }
                            result(1);
                        }
                    }
                }
            }
        });
    }

    //玩家爆牌判断和对应操作
    private void playerBustControl(int index) {
        if (Player.isBust(index)) {
            gameActionListener.showTagMessage(0, 0);
            Results.set(index, State.BANKER_WIN);
            if (index == 1) {
                new Animation.expectantTaskManager(700, new Animation.expectantTaskManager.ExpectantTask() {

                    @Override
                    public void expectantTask() {
                        Card c = Deck.getCard();
                        gameActionListener.onChangeSet(c);
                        new Animation.expectantTaskManager(300, new Animation.expectantTaskManager.ExpectantTask() {

                            @Override
                            public void expectantTask() {
                                Player.deal(2, c);
                            }
                        });
                    }
                });
            } else {
                result(index);
                if (index == 2) {
                    if (Results.get(1) == State.NULL) {
                        bankerAct();
                        if (Results.get(1) == State.NULL) {
                            Results.set(1, compare(1));
                        }
                    }
                    result(1);
                }
            }
        }
    }

    //分牌操作
    public void split() {
        Results.add(State.NULL);
        Results.add(State.NULL);
        Player.split();
        Player.changeCounter(-bet);
        pool += bet;
        Card c = Deck.getCard();
        Player.deal(1, c);
        gameActionListener.onDeal(1, c);
    }

//    public void splitInitial() {
//        Results = new ArrayList();
//        for (int i = 0; i < Player.getHandNum(); i++) {
//            Results.add(State.NULL);
//        }
//        Player.initialSurrenders();
//    } 
    //获得当前游戏中玩家拥有的筹码数
    public int getPlayerCounter() {
        return Player.getCounter();
    }

    //获得当前游戏设置的赌注下限
    public int getBetMax() {
        return Setting.getBetMax();
    }

    //获得当前游戏设置的赌注下限
    public int getBetMin() {
        return Setting.getBetMin();
    }

    /**
     * @return the pool
     */
    //获得当前游戏中赌池中的筹码数目
    public int getPool() {
        return pool;
    }

    //保存游戏记录到指定存档
    public void save() {
        if (Player != null) {
            PlayerSet.writePlayer(Player);
        }
    }

    //设置游戏事件监听器
    public void setGameActionListener(GameActionListener gameActionListener) {
        this.gameActionListener = gameActionListener;
    }

    //游戏事件监听器
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
