package com.endofmaster.qq;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class QqResponse {

    private int code;
    private int ret;
    private String msg;
    private Long error;
    @JsonProperty("error_description")
    private String description;

    public boolean successful() {
        return code == 0 || ret >= 0 || error == null;
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

    public Long getError() {
        return error;
    }

    public QqResponse setError(Long error) {
        this.error = error;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QqResponse setDescription(String description) {
        this.description = description;
        return this;
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
