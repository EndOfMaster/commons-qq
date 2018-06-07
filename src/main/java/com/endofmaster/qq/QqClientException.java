package com.endofmaster.qq;

/**
 * @author YQ.Huang
 */
public class QqClientException extends QqException {

    public QqClientException(String message) {
        super(message);
    }

    public QqClientException(Exception e){
        super(e);
    }

}
