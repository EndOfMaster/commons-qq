package com.endofmaster.qq.basic;


import com.endofmaster.qq.*;

import java.io.UnsupportedEncodingException;

/**
 * @author YQ.Huang
 */
public class QqBasicApi {
    private final String appId;
    private final String appSecret;
    private final String redirectUrl;
    private final QqHttpClient client;

    public QqBasicApi(String appId, String appSecret, String redirectUrl, QqHttpClient client) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.redirectUrl = redirectUrl;
        this.client = client;
    }

    public String getBaseAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return QqUtils.getAuthorizeUrl(appId, redirectUrl, "get_user_info", state, null);
    }

    public String getMobileAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return QqUtils.getAuthorizeUrl(appId, redirectUrl, "get_user_info", state, "mobile");
    }

    public QqOauth2AccessToken getOauth2AccessToken(String code) throws QqException {
        QqHttpRequest request = new QqHttpRequest("https://graph.qq.com/oauth2.0/token")
                .setArg("client_id", appId)
                .setArg("client_secret", appSecret)
                .setArg("code", code)
                .setArg("redirect_uri", redirectUrl)
                .setArg("grant_type", "authorization_code");
        QqHttpResponse response = client.execute(request);
        return response.parse(QqOauth2AccessToken.class);
    }

    public QqAuthUserInfo getOauth2UserInfo(String openId, String oauth2AccessToken) throws QqException {
        QqHttpRequest request = new QqHttpRequest("https://graph.qq.com/user/get_user_info")
                .setArg("access_token", oauth2AccessToken)
                .setArg("oauth_consumer_key", appId)
                .setArg("openid", openId);
        QqHttpResponse response = client.execute(request);
        return response.parse(QqAuthUserInfo.class);
    }

    public String getAppId() {
        return appId;
    }
}
