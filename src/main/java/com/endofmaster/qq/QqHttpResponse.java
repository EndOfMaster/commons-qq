package com.endofmaster.qq;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.validate.ParamUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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

    @SuppressWarnings("unchecked")
    public <T extends QqResponse> T parse(Class<T> tClass) throws QqException {
        try {
            if (statusCode >= 200 && statusCode < 300) {
                String paramsStr = StreamUtils.copyToString(body, Charset.forName("UTF-8"));
                logger.debug("qq请求返回paramsStr：" + paramsStr);
                T result;
                if (paramsStr.charAt(0) == '{') {
                    result = MAPPER.readValue(paramsStr, tClass);
                } else {
                    Map<String, String> params = ParamUtils.parseKvString(paramsStr);
                    Object obj = tClass.newInstance();
                    BeanUtils.populate(obj, params);
                    result = (T) obj;
                }
                if (!result.successful()) {
                    logger.error("QQ错误码：" + result.getCode() + ",错误内容：" + result.getMsg());
                    throw new QqServerException(result.getMsg());
                }
                return result;
            } else {
                throw new QqServerException("Failed to parse body, invalid status code");
            }
        } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
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
