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
public class PlayerSet {

    private ArrayList<Player> set;

    public PlayerSet() {
        set = new ArrayList();
    }

    public void initialSet() {
        for (int index = 0; index < 4; index++) {
            File file = new File("res/player" + index + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                writePlayer(new Player(index));
            }
        }
    }

    public void writeSet() {
        for (Player p : set) {
            writePlayer(p);
        }
    }

    public void readSet() {
        set.clear();
        for (int index = 0; index < 4; index++) {
            set.add(readPlayer(index));
        }
    }

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
