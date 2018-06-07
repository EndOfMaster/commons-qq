package com.endofmaster.qq;

/**
 * @author ZM.Wang
 */
public class QqException extends RuntimeException {

    public QqException(String errorMsg) {
        super(errorMsg);
    }

    public QqException(Throwable e) {
        super(e);
    }
}
