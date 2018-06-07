package com.endofmaster.qq;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ZM.Wang
 */
public class QqUtils {

    public static String getAuthorizeUrl(String appId, String redirectUrl, String scope, String state, String display) throws UnsupportedEncodingException {
        String url = "https://graph.qq.com/oauth2.0/authorize?client_id=" + appId + "&redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8") + "&response_type=code&scope=" + scope + "&state=" + URLEncoder.encode(state, "UTF-8");
        if (StringUtils.isBlank(display)) {
            return url;
        } else {
            return url + "&display=" + display;
        }
    }
}
