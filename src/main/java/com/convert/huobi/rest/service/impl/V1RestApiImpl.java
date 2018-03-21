package com.convert.huobi.rest.service.impl;

import com.convert.huobi.rest.common.Const;
import com.convert.huobi.rest.service.V1RestApi;
import com.convert.huobi.rest.utils.CryptoUtils;
import com.convert.huobi.rest.utils.HttpUtilManager;
import com.convert.huobi.rest.utils.ParamUtils;

import org.apache.http.HttpException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luoxuri
 * @create 2018-03-12 11:25
 **/
public class V1RestApiImpl implements V1RestApi {

    private static String HUOBI_URL = "https://api.huobi.pro";
    private static String SIGN_URL = "api.huobi.pro";

    public String symbols() throws Exception{
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = "/v1/common/symbols";

        Map<String, String> signMap = getCommonParam();
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);

        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);
    }

    @Override
    public String accounts() throws HttpException, IOException {
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = "/v1/account/accounts";
        Map<String, String> signMap = getCommonParam();
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);
        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);

    }

    @Override
    public String accountsBalance(String accountId) throws HttpException, IOException {
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = String.format("/v1/account/accounts/%s/balance", accountId);
        Map<String, String> signMap = getCommonParam();
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);

        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);

    }

    @Override
    public String orders(String orderId) throws HttpException, IOException {
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = String.format("/v1/order/orders/%s", orderId);
        Map<String, String> signMap = getCommonParam();
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);

        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);
    }

    @Override
    public String orders(String symbol, String types, String startDate, String endDate, String states, String from, String direct, String size) throws HttpException, IOException {
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = "/v1/order/orders";
        Map<String, String> signMap = getCommonParam();
        setParam(signMap,"symbol",symbol);
        setParam(signMap,"types",types);
        setParam(signMap,"start-date",startDate);
        setParam(signMap,"end-date",endDate);
        setParam(signMap,"states",states);
        setParam(signMap,"from",from);
        setParam(signMap,"direct",direct);
        setParam(signMap,"size",size);
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);

        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);
    }

    @Override
    public String matchresults(String symbol, String types, String startDate, String endDate, String from, String direct, String size) throws HttpException, IOException {
        HttpUtilManager httpUtil = HttpUtilManager.getInstance();
        String url = "/v1/order/matchresults";
        Map<String, String> signMap = getCommonParam();
        setParam(signMap,"symbol",symbol);
        setParam(signMap,"types",types);
        setParam(signMap,"start-date",startDate);
        setParam(signMap,"end-date",endDate);
        setParam(signMap,"from",from);
        setParam(signMap,"direct",direct);
        setParam(signMap,"size",size);
        String sign = CryptoUtils.buildSign("GET", SIGN_URL, url, signMap);
        signMap.put("Signature", sign);

        return httpUtil.requestHttpGet(HUOBI_URL, url, signMap);
    }

    private Map<String, String> getCommonParam() {
        Map<String, String> signMap = new HashMap<String, String>();
        signMap.put("AccessKeyId", Const.Access_Key);
        signMap.put("SignatureMethod", Const.SignatureMethod);
        signMap.put("SignatureVersion", Const.SignatureVersion);
        signMap.put("Timestamp", ParamUtils.getUTCDate());
        return signMap;
    }

    private void setParam(Map paramMap, String key, String value) {
        if (value != null) {
            paramMap.put(key, value);
        }
    }
}
