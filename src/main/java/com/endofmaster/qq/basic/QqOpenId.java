package com.endofmaster.qq.basic;

import com.endofmaster.qq.QqResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ZM.Wang
 */
public class QqOpenId extends QqResponse {

    @JsonProperty("openid")
    private  String openId;

    public String getOpenId() {
        return openId;
    }

    public QqOpenId setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    @Override
    public String toString() {
        return "QqOpenId{" +
                "openId='" + openId + '\'' +
                '}';
    }
}
