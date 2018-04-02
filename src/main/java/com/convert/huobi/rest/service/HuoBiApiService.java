package com.convert.huobi.rest.service;

import com.convert.huobi.rest.domain.balance.Account;
import com.convert.huobi.rest.domain.match.Match;
import com.convert.huobi.rest.domain.match.MatchData;
import com.convert.huobi.rest.domain.order.OrderData;
import com.convert.huobi.rest.domain.order.Orders;
import com.convert.huobi.rest.domain.price.Balance;
import com.convert.huobi.rest.domain.price.Kind;
import com.convert.huobi.rest.domain.support.CoinKind;
import com.convert.huobi.rest.domain.support.CoinPrice;
import com.convert.huobi.rest.utils.CryptoUtils;
import com.convert.huobi.rest.utils.HttpUtilManager;
import com.convert.huobi.rest.utils.JsonUtil;
import com.convert.huobi.rest.utils.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.convert.huobi.rest.common.HuoBiConst.*;

@Service
public class HuoBiApiService {

    private static final Logger LOG = LoggerFactory.getLogger(HuoBiApiService.class);
    private static final HttpUtilManager HTTP_CLIENT = HttpUtilManager.getInstance();

    /**
     * 获取我的账户所有货币对应的总ETH数量
     *
     * @return ethCount
     */
    public String[] getMyTotal(String accessKey, String secretKey) {
        try {
            if (accessKey == null || secretKey == null) {
                return null;
            }
            String[] num = new String[2];
            final BigDecimal[] totalCoinUnitPrice = {new BigDecimal(0)};
            String usdtEthPrice = getEthToUsdtPrice();
            List<Kind> kindList = getMyBalance(accessKey, secretKey);
            kindList.stream().forEach(o -> {
                if (!USDT.equals(o.getCurrency())) {
                    String coinUnitPrice = getSupportCoinToUsdtPrice(o.getCurrency());
                    BigDecimal myCoinCount = new BigDecimal(o.getBalance());
                    BigDecimal myCoinPrice = new BigDecimal(coinUnitPrice);
                    BigDecimal usdtPrice = myCoinCount.multiply(myCoinPrice);
                    totalCoinUnitPrice[0] = totalCoinUnitPrice[0].add(usdtPrice);
                } else {
                    String balanceUsdt = o.getBalance();
                    totalCoinUnitPrice[0] = totalCoinUnitPrice[0].add(new BigDecimal(balanceUsdt));
                }
            });//usdt数量
            num[0] = totalCoinUnitPrice[0].toPlainString();
            BigDecimal myEth = totalCoinUnitPrice[0].divide(new BigDecimal(usdtEthPrice), 18, BigDecimal.ROUND_CEILING);
            //eth数量
            num[1] = myEth.toPlainString();
            return num;
        } catch (Exception e) {
            LOG.error("Get all my eth error:{}", e.getMessage(), e);
            return null;
        }
    }

    public String getMyTotalUsdt(String accessKey, String secretKey) {
        try {
            final BigDecimal[] totalCoinUnitPrice = {new BigDecimal(0)};
            String usdtEthPrice = getEthToUsdtPrice();
            List<Kind> kindList = getMyBalance(accessKey, secretKey);
            kindList.stream().forEach(o -> {
                if (!USDT.equals(o.getCurrency())) {
                    String coinUnitPrice = getSupportCoinToUsdtPrice(o.getCurrency());
                    BigDecimal myCoinCount = new BigDecimal(o.getBalance());
                    BigDecimal myCoinPrice = new BigDecimal(coinUnitPrice);
                    BigDecimal usdtPrice = myCoinCount.multiply(myCoinPrice);
                    totalCoinUnitPrice[0] = totalCoinUnitPrice[0].add(usdtPrice);
                } else {
                    String balanceUsdt = o.getBalance();
                    totalCoinUnitPrice[0] = totalCoinUnitPrice[0].add(new BigDecimal(balanceUsdt));
                }
            });
            BigDecimal bigDecimal = totalCoinUnitPrice[0];
            return bigDecimal.toPlainString();
        } catch (Exception e) {
            LOG.error("Get all my eth error:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据订单ID获取详情
     *
     * @param accessKey key
     * @param secretKey key
     * @param orderId   订单ID
     * @return json
     */
    private String getOrdersById(String accessKey, String secretKey, String orderId) {
        try {
            String url = String.format(OEDERS_BY_ID, orderId);
            Map<String, String> signMap = getCommonParam(accessKey);
            String sign = CryptoUtils.buildSign(METHOD_GET, SIGN_URL, url, signMap, secretKey);
            signMap.put(SIGN_MAP_SIGNATURE, sign);

            return HTTP_CLIENT.requestHttpGet(HUOBI_URL, url, signMap);
        } catch (Exception e) {
            LOG.error("fetch orders error:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询当前委托、历史委托
     *
     * @param accessKey key
     * @param secretKey key
     * @param symbol    qtumusdt
     * @return list
     */
    public List<OrderData> getOrdersInfo(String accessKey, String secretKey, String symbol) {
        String ordersInfoJson = queryOrders(accessKey, secretKey, symbol, null, null, null, "filled", null, null, null);
        ordersInfoJson = ordersInfoJson.replaceAll("-", "");
        Orders orders = (Orders) JsonUtil.fromJson(ordersInfoJson, Orders.class);
        if (OK.equals(orders.getStatus())) {
            return orders.getData();
        }
        return null;
    }

    /**
     * 查询当前成交、成交历史
     *
     * @param accessKey key
     * @param secretKey key
     * @param symbol    qtumusdt
     * @return list
     */
    public List<MatchData> getMatchResultInfo(String accessKey, String secretKey, String symbol) {
        String matchResultJson = queryMatchresults(accessKey, secretKey, symbol, null, null, null, null, null, null);
        matchResultJson = matchResultJson.replaceAll("-", "");
        Match match = (Match) JsonUtil.fromJson(matchResultJson, Match.class);
        if (OK.equals(match.getStatus())) {
            return match.getData();
        }
        return null;
    }

    private String queryOrders(String accessKey, String secretKey, String symbol, String types, String startDate, String endDate, String states, String from, String direct, String size) {
        try {
            Map<String, String> signMap = getCommonParam(accessKey);
            setParam(signMap, SIGN_MAP_SYMBOL, symbol);
            setParam(signMap, SIGN_MAP_TYPES, types);
            setParam(signMap, SIGN_MAP_START_DATE, startDate);
            setParam(signMap, SIGN_MAP_END_DATE, endDate);
            setParam(signMap, SIGN_MAP_STATES, states);
            setParam(signMap, SIGN_MAP_FROM, from);
            setParam(signMap, SIGN_MAP_DIRECT, direct);
            setParam(signMap, SIGN_MAP_SIZE, size);
            String sign = CryptoUtils.buildSign(METHOD_GET, SIGN_URL, ORDERS_URL, signMap, secretKey);
            signMap.put(SIGN_MAP_SIGNATURE, sign);

            return HTTP_CLIENT.requestHttpGet(HUOBI_URL, ORDERS_URL, signMap);
        } catch (Exception e) {
            LOG.error("fetch ordersInfo error");
        }
        return null;
    }

    private String queryMatchresults(String accessKey, String secretKey, String symbol, String types, String startDate, String endDate, String from, String direct, String size) {
        try {
            Map<String, String> signMap = getCommonParam(accessKey);
            setParam(signMap, SIGN_MAP_SYMBOL, symbol);
            setParam(signMap, SIGN_MAP_TYPES, types);
            setParam(signMap, SIGN_MAP_START_DATE, startDate);
            setParam(signMap, SIGN_MAP_END_DATE, endDate);
            setParam(signMap, SIGN_MAP_FROM, from);
            setParam(signMap, SIGN_MAP_DIRECT, direct);
            setParam(signMap, SIGN_MAP_SIZE, size);
            String sign = CryptoUtils.buildSign(METHOD_GET, SIGN_URL, MATCH_RESULT_URL, signMap, secretKey);
            signMap.put(SIGN_MAP_SIGNATURE, sign);

            return HTTP_CLIENT.requestHttpGet(HUOBI_URL, MATCH_RESULT_URL, signMap);
        } catch (Exception e) {
            LOG.error("fetch match result error");
        }
        return null;
    }

    /**
     * 获取自己有余额的账户的信息
     *
     * @return 账户集合
     */
    private List<Kind> getMyBalance(String accessKey, String secretKey) {
        try {
            String accountId = getMySpotAccountId(accessKey, secretKey);
            String json = getAccountsBalance(accessKey, secretKey, accountId);

            Balance balance = (Balance) JsonUtil.fromJson(json, Balance.class);
            if (OK.equals(balance.getStatus())) {
                List<Kind> kindList = new ArrayList<>();
                balance.getData().getList().stream().forEach(o -> {
                    Kind kind = JsonUtil.objectToEntity(o, Kind.class);
                    BigDecimal bd = new BigDecimal(kind.getBalance());
                    if (bd.compareTo(ZERO) > 0) {
                        kindList.add(kind);
                    }
                });
                return kindList;
            }
        } catch (Exception e) {
            LOG.error("Get my balance error:{}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取自己现金账户id
     *
     * @return accountId
     */
    private String getMySpotAccountId(String accessKey, String secretKey) {
        try {
            String accounts = getAccounts(accessKey, secretKey);
            Account account = (Account) JsonUtil.fromJson(accounts, Account.class);
            List<Long> list = new ArrayList<>();
            if (OK.equals(account.getStatus())) {
                account.getData().stream().forEach(o -> {
                    if (ACCOUNT_SPOT.equals(o.getType())) {
                        list.add(o.getId());
                    }
                });
            }
            return list.get(0).toString();
        } catch (Exception e) {
            LOG.error("Get my spot accountId error:[}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取账户json
     *
     * @param accessKey key
     * @param secretKey key
     * @return accountJson
     */
    private String getAccounts(String accessKey, String secretKey) {
        try {
            Map<String, String> signMap = getCommonParam(accessKey);
            String sign = CryptoUtils.buildSign(METHOD_GET, SIGN_URL, ACCOUNT_URL, signMap, secretKey);
            signMap.put(SIGN_MAP_SIGNATURE, sign);
            return HTTP_CLIENT.requestHttpGet(HUOBI_URL, ACCOUNT_URL, signMap);
        } catch (Exception e) {
            LOG.error("fetch accounts info error", e.getMessage(), e);
        }
        return null;
    }

    private String getAccountsBalance(String accessKey, String secretKey, String accountId) {
        try {
            String url = String.format(ACCOUNT_BALANCE, accountId);
            Map<String, String> signMap = getCommonParam(accessKey);
            String sign = CryptoUtils.buildSign(METHOD_GET, SIGN_URL, url, signMap, secretKey);
            signMap.put(SIGN_MAP_SIGNATURE, sign);

            return HTTP_CLIENT.requestHttpGet(HUOBI_URL, url, signMap);
        } catch (Exception e) {
            LOG.error("fetch account balance error");
        }
        return null;
    }

    /**
     * 获取支持币种对应的usdt价格
     *
     * @param coinName 货币
     * @return 货币转换usdt的价格
     */
    private String getSupportCoinToUsdtPrice(String coinName) {
        try {
            if (Arrays.asList(SUPPORT_USDT_COIN).contains(coinName)) {
                String supportCoinToUsdtPriceUrl = SUPPORT_COIN_TO_JOINT_PRICE_URL_PRE + coinName + USDT;
                System.out.println(supportCoinToUsdtPriceUrl);
                String supportCoinToUsdtPriceJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, supportCoinToUsdtPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToUsdtPriceJson, CoinPrice.class);
                if (OK.equals(coinPrice.getStatus())) {
                    return coinPrice.getTick().getClose();
                }
            }
            LOG.info("USDT不支持{}兑换", coinName);

            if (Arrays.asList(SUPPORT_ETH_COIN).contains(coinName)) {
                String supportCoinToEthPriceUrl = SUPPORT_COIN_TO_JOINT_PRICE_URL_PRE + coinName + ETH;
                String supportCoinToEthPriceJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, supportCoinToEthPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToEthPriceJson, CoinPrice.class);
                if (OK.equals(coinPrice.getStatus())) {
                    String price = coinPrice.getTick().getClose();
                    BigDecimal ethPrice = new BigDecimal(price);
                    String p = getEthToUsdtPrice();
                    if (p == null) {
                        throw new Exception("Get ETH to USDT error");
                    }
                    BigDecimal coinToUsdtPrice = ethPrice.multiply(new BigDecimal(p));
                    return coinToUsdtPrice.toString();
                }
            }
            LOG.info("USDT不支持{}兑换", coinName);

            if (Arrays.asList(SUPPORT_BTC_COIN).contains(coinName)) {
                String supportCoinToBtcPriceUrl = SUPPORT_COIN_TO_JOINT_PRICE_URL_PRE + coinName + BTC;
                String supportCoinToBtcPriceJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, supportCoinToBtcPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToBtcPriceJson, CoinPrice.class);
                if (OK.equals(coinPrice.getStatus())) {
                    String price = coinPrice.getTick().getClose();
                    BigDecimal btcPrice = new BigDecimal(price);
                    String p = getBtcToUsdtPrice();
                    if (p == null) {
                        throw new Exception("Get BTC to USDT error");
                    }
                    BigDecimal coinToUsdtPrice = btcPrice.multiply(new BigDecimal(p));
                    return coinToUsdtPrice.toString();
                }
            }
            LOG.info("USDT不支持{}兑换", coinName);
            return "0";
        } catch (Exception e) {
            LOG.error("Get support coin to USDT price error:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取货币所有支持币种
     *
     * @return coinList
     */
    private List getSupportCoinList() {
        try {
            String supportCoinJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, SUPPORT_COIN_URL);
            CoinKind coinKind = (CoinKind) JsonUtil.fromJson(supportCoinJson, CoinKind.class);
            if (OK.equals(coinKind.getStatus())) {
                return coinKind.getData();
            }
        } catch (Exception e) {
            LOG.error("Get support coin list error:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * ETH转换USDT价格
     *
     * @return price
     */
    private String getEthToUsdtPrice() {
        try {
            String usdtEthPriceJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, ETH_USDT_PRICE_URL);
            CoinPrice cp = (CoinPrice) JsonUtil.fromJson(usdtEthPriceJson, CoinPrice.class);
            if (OK.equals(cp.getStatus())) {
                String usdtEthPrice = cp.getTick().getClose();
                return usdtEthPrice;
            }
        } catch (Exception e) {
            LOG.error("Get ETH to USDT price error:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * BTC转换USDT价格
     *
     * @return price
     */
    private String getBtcToUsdtPrice() {
        try {
            String usdtEthPriceJson = HTTP_CLIENT.requestHttpGet(HUOBI_URL, BTC_USDT_PRICE_URL);
            CoinPrice cp = (CoinPrice) JsonUtil.fromJson(usdtEthPriceJson, CoinPrice.class);
            if (OK.equals(cp.getStatus())) {
                String usdtBtcPrice = cp.getTick().getClose();
                return usdtBtcPrice;
            }
        } catch (Exception e) {
            LOG.error("Get BTC to USDT price error:{}", e.getMessage(), e);
        }
        return null;
    }

    private static Map<String, String> getCommonParam(String accessKey) {
        Map<String, String> signMap = new HashMap<String, String>();
        signMap.put(SIGN_MAP_ACCESS_KEY, accessKey);
        signMap.put(SIGN_MAP_SIGNATURE_METHOD, SIGNATURE_METHOD);
        signMap.put(SIGN_MAP_SIGNATURE_VERSION, SIGNATURE_VERSION);
        signMap.put(SIGN_MAP_TIMESTAMP, ParamUtils.getUTCDate());
        return signMap;
    }

    private static void setParam(Map paramMap, String key, String value) {
        if (value != null) {
            paramMap.put(key, value);
        }
    }


}