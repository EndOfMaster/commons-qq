package com.endofmaster.qq.basic;

import com.endofmaster.qq.QqResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author YQ.Huang
 */
public class WxIpList extends QqResponse {

    @JsonProperty("ip_list")
    private List<String> ipList;

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    @Override
    public String toString() {
        return "WxIpList{" +
                "ipList=" + ipList +
                '}';
    }
}
