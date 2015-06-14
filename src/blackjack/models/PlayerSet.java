/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.models;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */
//用于对玩家信息的文件存储和读取
public class PlayerSet {

    private ArrayList<Player> set;//固定的4个存档位置

    public PlayerSet() {
        set = new ArrayList(4);
    }

    //初始化PlayerSet，检查存档文件是否存在，如不存在则新建空存档文件
    public void initialSet() {
        for (int index = 0; index < 4; index++) {
            File file = new File("res/player" + index + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                writePlayer(new Player(index));
            }
        }
    }

    //一次性写入所有存档
    public void writeSet() {
        for (Player p : set) {
            writePlayer(p);
        }
    }

    //一次性读取所有存档
    public void readSet() {
        set.clear();
        for (int index = 0; index < 4; index++) {
            set.add(readPlayer(index));
        }
    }

    //写入给定Player对象对应的存档
    public static void writePlayer(Player p) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("res/player" + p.getIndex() + ".txt"));
            out.writeObject(p);
        } catch (FileNotFoundException e) {
            System.out.println("Player File Not Found!");
        } catch (IOException e) {
            System.out.println("Unknown IO Error!");
        }
    }

    //读取给定index位置的存档，恢复为Player对象并传出
    public static Player readPlayer(int index) {
        Player p = new Player(index);
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("res/player" + index + ".txt"));
            p = (Player) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Player File Not Found!");
        } catch (IOException e) {
            System.out.println("Unknown IO Error!");
        } catch (ClassNotFoundException e) {
            System.out.println("Player File Corrupted!");
        }
        return p;
    }

    /**
     * @return the set
     */
    public ArrayList<Player> getSet() {
        readSet();
        return set;
    }

    public void setSet(ArrayList<Player> players) {
        this.set = players;
        writeSet();
    }
}
