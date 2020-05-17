package edu.gdpu.exception;

/**
 * @author ilanky
 * @date 2020年 05月16日 04:00:31
 */
public class PasswordIncorrectException extends Exception{
    public PasswordIncorrectException(String msg){
        super(msg);
    }
}
