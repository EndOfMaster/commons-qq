package com.endofmaster.qq.basic;

import com.endofmaster.qq.QqResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通过用户授权获取的用户信息
 *
 * @author YQ.Huang
 * @author ZM.Wang
 */
public class QqAuthUserInfo extends QqResponse {

    @JsonProperty("nickname")
    private String nickName;

    @JsonProperty("gender")
    private String sex;

    @JsonProperty("figureurl_qq_1")
    private String headImgUrl;

    @JsonProperty("figureurl_qq_2")
    private String headImg100Url;

    @JsonProperty("figureurl_2")
    private String spaceHeadImg100Url;

    public String getNickName() {
        return nickName;
    }

    public QqAuthUserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public QqAuthUserInfo setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public QqAuthUserInfo setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public String getHeadImg100Url() {
        return headImg100Url;
    }

    public QqAuthUserInfo setHeadImg100Url(String headImg100Url) {
        this.headImg100Url = headImg100Url;
        return this;
    }

    public String getSpaceHeadImg100Url() {
        return spaceHeadImg100Url;
    }

    public QqAuthUserInfo setSpaceHeadImg100Url(String spaceHeadImg100Url) {
        this.spaceHeadImg100Url = spaceHeadImg100Url;
        return this;
    }

    @Override
    public String toString() {
        return "QqAuthUserInfo{" +
                "nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", headImg100Url='" + headImg100Url + '\'' +
                ", spaceHeadImg100Url='" + spaceHeadImg100Url + '\'' +
                '}';
    }
}
