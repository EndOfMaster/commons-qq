package com.endofmaster.qq;


import com.endofmaster.qq.basic.QqAuthUserInfo;
import com.endofmaster.qq.basic.QqBasicApi;
import org.junit.jupiter.api.Test;

public class QqBasicApiTest {

    private QqBasicApi basicApi;

    public QqBasicApiTest(){
        QqHttpClient client =new QqHttpClient();
        this.basicApi=new QqBasicApi("101764280","","redirectUrl",client);
    }

    @Test
    public void getUserInfo(){
        QqAuthUserInfo qqUserInfo = basicApi.getOauth2UserInfo("******", "*****");
        System.err.println(qqUserInfo);
    }

}