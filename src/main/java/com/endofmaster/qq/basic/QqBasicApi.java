package com.endofmaster.qq.basic;


import com.endofmaster.qq.QqException;
import com.endofmaster.qq.QqHttpClient;
import com.endofmaster.qq.QqHttpRequest;
import com.endofmaster.qq.QqHttpResponse;
import com.endofmaster.qq.QqUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author YQ.Huang
 */
public class QqBasicApi {
    private final String appId;
    private final String appKey;
    private final String redirectUrl;
    private final QqHttpClient client;

    public QqBasicApi(String appId, String appKey, String redirectUrl, QqHttpClient client) {
        this.appId = appId;
        this.appKey = appKey;
        this.redirectUrl = redirectUrl;
        this.client = client;
    }

    public String getBaseAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return QqUtils.getAuthorizeUrl(appId, redirectUrl, "get_user_info", state, null);
    }

    public String getMobileAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return QqUtils.getAuthorizeUrl(appId, redirectUrl, "get_user_info", state, "mobile");
    }

    public QqOauth2AccessToken getOauth2AccessToken(String code) throws QqException, UnsupportedEncodingException {
        QqHttpRequest request = new QqHttpRequest("https://graph.qq.com/oauth2.0/token")
                .setArg("client_id", appId)
                .setArg("client_secret", appKey)
                .setArg("code", code)
                .setArg("redirect_uri", URLEncoder.encode(redirectUrl,"UTF-8"))
                .setArg("grant_type", "authorization_code");
        QqHttpResponse response = client.execute(request);
        return response.parse(QqOauth2AccessToken.class);
    }

    public QqOpenId getOauth2OpenId(String oauth2AccessToken) {
        QqHttpRequest request = new QqHttpRequest("https://graph.qq.com/oauth2.0/me")
                .setArg("access_token", oauth2AccessToken);
        QqHttpResponse response = client.execute(request);
        return response.parse(QqOpenId.class);
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
