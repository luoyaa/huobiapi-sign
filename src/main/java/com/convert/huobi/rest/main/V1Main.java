package com.convert.huobi.rest.main;

import com.convert.huobi.rest.domain.order.Orders;
import com.convert.huobi.rest.service.V1RestApi;
import com.convert.huobi.rest.domain.balance.Account;
import com.convert.huobi.rest.service.impl.V1RestApiImpl;
import com.convert.huobi.rest.utils.AllCoinConvertEthUtil;
import com.convert.huobi.rest.utils.JsonUtil;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-12 11:31
 **/
public class V1Main {

    public static void main(String[] args) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List getOrdersInfo(String symbol, String states) {
        try {
            V1RestApi api = new V1RestApiImpl();
            String ordersInfoJson = api.orders(symbol, null, null,null, states,null,null,null);

            ordersInfoJson = ordersInfoJson.replaceAll("-", "");
            Orders orders = (Orders) JsonUtil.fromJson(ordersInfoJson, Orders.class);
            if ("ok".equals(orders.getStatus())) {
                System.err.println(orders.getData());
                return orders.getData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getStopAccountsId(String accountJson) {
        Account account = (Account) JsonUtil.fromJson(accountJson, Account.class);
        List<Long> list = new ArrayList<Long>();
        if ("ok".equals(account.getStatus())) {
            account.getData().stream().forEach(o -> {
                if ("spot".equals(o.getType())) {
                    list.add(o.getId());
                }
            });
            return list.get(0).toString();
        }
        return null;
    }

    private static String getAccounts() throws IOException, HttpException {
        V1RestApi api = new V1RestApiImpl();
        return api.accounts();
    }

    private static String getAccountsBalance(String accountId) throws IOException, HttpException {
        V1RestApi api = new V1RestApiImpl();
        return api.accountsBalance(accountId);
    }

    private static String getOrders(String symbol) throws IOException, HttpException {
        V1RestApi api = new V1RestApiImpl();
        return api.orders(symbol, null, null, null, "filled", null, null, null);
    }

    private static String getMatchresults(String symbol) throws IOException, HttpException {
        V1RestApi api = new V1RestApiImpl();
        return api.matchresults(symbol, null, null, null, null, null, null);
    }

    private static String getMyTotalEth(){
        return AllCoinConvertEthUtil.getMyTotalEth();
    }
}
