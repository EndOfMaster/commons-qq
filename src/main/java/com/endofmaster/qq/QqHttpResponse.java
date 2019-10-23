package com.endofmaster.qq;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.json.JsonUtils;
import com.endofmaster.commons.util.validate.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static com.endofmaster.qq.QqHttpClient.MAPPER;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class QqHttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(QqHttpResponse.class);

    private int statusCode;
    private String reasonPhrase;
    private String contentType;
    private InputStream body;

    public QqHttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public <T extends QqResponse> T parse(Class<T> tClass) throws QqException {
        try {
            if (statusCode >= 200 && statusCode < 300) {
                String paramsStr = StreamUtils.copyToString(body, Charset.forName("UTF-8"));
                logger.debug("qq请求返回paramsStr：" + paramsStr);
                String json = paramsStr;
                if (!JsonUtils.isJson(paramsStr)) {
                    if (paramsStr.contains("=") && paramsStr.contains("&")) {
                        Map<String, String> params = ParamUtils.parseKvString(paramsStr);
                        json = MAPPER.writeValueAsString(params);
                    } else {
                        json = getJson(paramsStr);
                    }
                }
                T result = MAPPER.readValue(json, tClass);
                if (!result.successful()) {
                    logger.error("QQ错误码：" + result.getCode() + ",错误内容：" + result.getMsg());
                    throw new QqServerException(result.getMsg());
                }
                return result;
            } else {
                throw new QqServerException("Failed to parse body, invalid status code");
            }
        } catch (IOException e) {
            throw new QqClientException(e);
        }
    }

    private String getJson(String str) {
        int i = str.indexOf("{");
        int j = str.lastIndexOf("}") + 1;
        return str.substring(i, j);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }

}
