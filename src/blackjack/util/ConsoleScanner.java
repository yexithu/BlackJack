/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.util;

import blackjack.exceptions.InvalidInputException;
import java.util.Scanner;
import java.util.Arrays;

/**
 *
 * @author Martin
 */
//控制台输入控制
public class ConsoleScanner {

    private static final Scanner in = new Scanner(System.in);//通用的Scanner对象

    //获得Yes/No选择
    public static boolean getYorN() {
        String key;
        boolean match = false, loop = true;
        while (loop) {
            try {
                key = in.nextLine();
                switch (key) {
                    case "Y":
                    case "y":
                        match = true;
                        break;
                    case "N":
                    case "n":
                        break;
                    default:
                        throw new InvalidInputException();
                }
                loop = false;
            } catch (InvalidInputException e) {
                System.out.println(e);
            }
        }
        return match;
    }

    //获得在多个小数选项中用户的选择
    public static double getDouble(Double... a) {
        Double key = 0e0;
        Arrays.sort(a);
        boolean loop = true;
        while (loop) {
            try {
                key = new Double(in.nextLine());
                if (Arrays.binarySearch(a, key) < 0) {
                    throw new InvalidInputException();
                } else {
                    loop = false;
                }
            } catch (InvalidInputException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
        return key;
    }

    //获得在多个整数选项中用户的选择
    public static int getInt(Integer... a) {
        Integer key = 0;
        Arrays.sort(a);
        boolean loop = true;
        while (loop) {
            try {
                key = new Integer(in.nextLine());
                if (Arrays.binarySearch(a, key) < 0) {
                    throw new InvalidInputException();
                } else {
                    loop = false;
                }
            } catch (InvalidInputException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
        return key;
    }

    //获得在给定小数范围中中用户的选择
    public static double getRangeDouble(double min, double max) {
        Double key = 0e0;
        boolean loop = true;
        while (loop) {
            try {
                key = new Double(in.nextLine());
                if (key > max || key < min) {
                    throw new InvalidInputException();
                } else {
                    loop = false;
                }
            } catch (InvalidInputException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
        return key;
    }

    //获得在给定小数范围中中用户的选择
    public static int getRangeInt(int min, int max) {
        int key = 0;
        boolean loop = true;
        while (loop) {
            try {
                key = new Integer(in.nextLine());
                if (key > max || key < min) {
                    throw new InvalidInputException();
                } else {
                    loop = false;
                }
            } catch (InvalidInputException e) {
                System.out.println(e);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
        return key;
    }

    //获取在多个字符串选项中用户的选择
    public static String getChoice(String... a) {
        String key = null;
        Arrays.sort(a);
        boolean loop = true;
        while (loop) {
            try {
                key = in.nextLine();
                if (Arrays.binarySearch(a, key) < 0) {
                    throw new InvalidInputException();
                } else {
                    loop = false;
                }
            } catch (InvalidInputException e) {
                System.out.println(e);
            }
        }
        return key;
    }
}
