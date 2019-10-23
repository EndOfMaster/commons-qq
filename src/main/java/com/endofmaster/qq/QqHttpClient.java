package com.endofmaster.qq;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.p12cert.P12Cert;
import com.endofmaster.commons.util.p12cert.P12CertUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA;

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

    private final HttpClient httpClient;

    public QqHttpClient() {
        this(null, null);
    }

    public QqHttpClient(final InputStream cert, final String certPassword) {
        this(3000, 3000, cert, certPassword);
    }

    public QqHttpClient(final int connectTimeout, final int socketTimeout, final InputStream cert, final String certPassword) {
        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            if (cert != null) {
                SSLConnectionSocketFactory sslSocketFactory = buildSsl(cert, certPassword);
                httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
            }
            httpClient = httpClientBuilder.setDefaultRequestConfig(config).build();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new QqException(e);
        }
    }

    public QqHttpResponse execute(QqHttpRequest qqHttpRequest) throws QqException {
        HttpUriRequest httpRequest = buildHttpUriRequest(qqHttpRequest);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            QqHttpResponse qqHttpResponse = new QqHttpResponse(httpResponse.getStatusLine().getStatusCode());
            qqHttpResponse.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
            qqHttpResponse.setBody(httpResponse.getEntity().getContent());
            qqHttpResponse.setContentType(httpResponse.getEntity().getContentType().getValue());
            return qqHttpResponse;
        } catch (IOException e) {
            throw new QqException(e);
        }
    }

    public InputStream download(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return httpResponse.getEntity().getContent();
        } catch (IOException e) {
            throw new QqException(e);
        }
    }

    private HttpUriRequest buildHttpUriRequest(QqHttpRequest qqHttpRequest) throws QqException {
        RequestBuilder requestBuilder = RequestBuilder.create(qqHttpRequest.getMethod())
                .setUri(qqHttpRequest.getUrl());

        for (String headerKey : qqHttpRequest.getHeaders().keySet()) {
            requestBuilder.addHeader(headerKey, qqHttpRequest.getHeaders().get(headerKey));
        }

        if ("POST".equalsIgnoreCase(qqHttpRequest.getMethod()) || "PUT".equalsIgnoreCase(qqHttpRequest.getMethod())) {
            Map<String, Object> map = new HashMap<>();
            for (QqHttpRequest.Arg arg : qqHttpRequest.getArgs()) {
                map.put(arg.key, arg.value);
            }
            if ("json".equalsIgnoreCase(qqHttpRequest.getDataType())) {
                try {
                    String json = MAPPER.writeValueAsString(map);
                    logger.debug("qq请求json：" + json);
                    StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                    requestBuilder.setEntity(entity);
                } catch (JsonProcessingException e) {
                    throw new QqException(e);
                }
            } else {
                //目前其他模式只有form
                try {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    for (String key : map.keySet()) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            builder.addTextBody(key, value.toString());
                        } else {
                            byte[] data = StreamUtils.copyToByteArray((InputStream) value);
                            builder.addBinaryBody(key, data, MULTIPART_FORM_DATA, key);
                        }
                    }
                    builder.setMode(HttpMultipartMode.RFC6532);
                    requestBuilder.setEntity(builder.build());
                } catch (IOException e) {
                    throw new QqException(e);
                }
            }
        } else {
            for (QqHttpRequest.Arg arg : qqHttpRequest.getArgs()) {
                requestBuilder.addParameter(arg.key, arg.value.toString());
            }
            logger.debug("qq请求连接：" + requestBuilder.getUri().toString());
        }
        return requestBuilder.build();
    }

    private SSLConnectionSocketFactory buildSsl(InputStream cert, String certPassword) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        P12Cert p12Cert = P12CertUtils.load(cert, certPassword.toCharArray());
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(p12Cert.getKeyStore(), certPassword.toCharArray()).build();
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

}
