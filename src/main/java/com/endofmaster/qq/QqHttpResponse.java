package com.endofmaster.qq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.endofmaster.qq.QqHttpClient.MAPPER;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class QqHttpResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(QqHttpResponse.class);

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
                String json = MAPPER.writeValueAsString(body);
                LOGGER.debug("qq请求返回json：" + json);
                T result = MAPPER.readValue(json, tClass);
                if (!result.successful()) {
                    LOGGER.error("QQ错误码：" + result.getCode() + ",错误内容：" + result.getMsg());
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
