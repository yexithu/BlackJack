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
        for (int index = 1; index < 5; index++) {
            set.add(new Player(index));
        }
    }

    public void writeSet() {
        for (Player p : getSet()) {
            try {
                File file = new File("res/player" + p.getIndex() + ".txt");
                file.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }
    }

    public void readSet() {
        getSet().clear();
        try {
            for (int index = 1; index < 5; index++) {
                File file = new File("res/player" + index + ".txt");
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                getSet().add((Player) in.readObject());
            }
        } catch (FileNotFoundException e) {

        } catch (IOException | ClassNotFoundException e) {
            
        }
    }

    /**
     * @return the set
     */
    public ArrayList<Player> getSet() {
        return set;
    }
    
    public void setSet(ArrayList<Player> players) {
        this.set = players;
    }
}
