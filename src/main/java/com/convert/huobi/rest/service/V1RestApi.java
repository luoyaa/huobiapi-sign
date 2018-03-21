package com.convert.huobi.rest.service;

import org.apache.http.HttpException;

import java.io.IOException;

/**
 * @author luoxuri
 * @create 2018-03-12 11:23
 **/
public interface V1RestApi {

    public String accounts() throws HttpException, IOException;

    public String accountsBalance(String accountId) throws HttpException, IOException;

    public String orders(String orderId) throws HttpException, IOException;

    public String orders(String symbol, String types, String startDate, String endDate, String states,String from, String direct,String size) throws HttpException, IOException;

    public String matchresults(String symbol, String types, String startDate, String endDate, String from, String direct,String size) throws HttpException, IOException;

}
