package com.endofmaster.qq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author YQ.Huang
 */
public class QqHttpClient {

    static final ObjectMapper MAPPER = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setVisibility(new VisibilityChecker.Std(NONE, NONE, NONE, NONE, ANY));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public QqHttpResponse execute(QqHttpRequest qqHttpRequest) throws QqException {
        StringBuilder params = new StringBuilder();
        for (QqHttpRequest.Arg arg : qqHttpRequest.getArgs()) {
            params.append(arg.key).append("=").append(arg.value.toString()).append("&");
        }
        String paramsStr = params.substring(0, params.length() - 1);
        String url = qqHttpRequest.getUrl() + "?" + paramsStr;
        logger.debug("qq请求最终拼接url：" + url);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            QqHttpResponse qqHttpResponse = new QqHttpResponse(httpResponse.getStatusLine().getStatusCode());
            qqHttpResponse.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
            qqHttpResponse.setBody(httpResponse.getEntity().getContent());
            qqHttpResponse.setContentType(httpResponse.getEntity().getContentType().getValue());
            return qqHttpResponse;
        } catch (IOException e) {
            throw new QqException(e);
        }
    }

}
