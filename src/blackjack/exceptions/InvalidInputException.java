/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.exceptions;

/**
 *
 * @author Martin
 */
//自定义错误类型：不合法的输入
public class InvalidInputException extends Exception{
    public InvalidInputException(){
        super("Error! Invalid Input! Please check and input again.");
    }
}
