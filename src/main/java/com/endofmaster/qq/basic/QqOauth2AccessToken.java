package com.endofmaster.qq.basic;

import com.endofmaster.qq.QqResponse;

/**
 * @author YQ.Huang
 */
public class QqOauth2AccessToken extends QqResponse {

    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}
