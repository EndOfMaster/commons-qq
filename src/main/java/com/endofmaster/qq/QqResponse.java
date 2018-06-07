package com.endofmaster.qq;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class QqResponse {

    private int code;

    private int ret;

    private String msg;

    public boolean successful() {
        return code == 0 || ret >= 0;
    }

    public int getCode() {
        return code;
    }

    public QqResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public QqResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getRet() {
        return ret;
    }

    public QqResponse setRet(int ret) {
        this.ret = ret;
        return this;
    }

    @Override
    public String toString() {
        return "QqResponse{" +
                "code=" + code +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                '}';
    }
}
