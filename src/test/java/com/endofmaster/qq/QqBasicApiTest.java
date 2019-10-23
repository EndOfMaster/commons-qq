package com.endofmaster.qq;


import com.endofmaster.qq.basic.QqAuthUserInfo;
import com.endofmaster.qq.basic.QqBasicApi;
import org.junit.Test;

public class QqBasicApiTest {

    private QqBasicApi basicApi;

    public QqBasicApiTest(){
        QqHttpClient client =new QqHttpClient();
        this.basicApi=new QqBasicApi("101764280","","redirectUrl",client);
    }

    @Test
    public void getUserInfo(){
        QqAuthUserInfo qqUserInfo = basicApi.getOauth2UserInfo("BC0EBE3DA853EB0BF520BDA03EA94FF4", "83D7BEF3321C98AFA3A9EF01473EA219");
        System.err.println(qqUserInfo);
    }

}