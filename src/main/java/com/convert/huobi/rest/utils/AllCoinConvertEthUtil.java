package com.convert.huobi.rest.utils;

import com.convert.huobi.rest.domain.support.CoinPrice;
import com.convert.huobi.rest.service.V1RestApi;
import com.convert.huobi.rest.domain.balance.Account;
import com.convert.huobi.rest.domain.price.Balance;
import com.convert.huobi.rest.domain.price.Kind;
import com.convert.huobi.rest.domain.support.CoinKind;
import com.convert.huobi.rest.service.impl.V1RestApiImpl;
import org.apache.http.HttpException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-13 9:57
 **/
public class AllCoinConvertEthUtil {

    private static final BigDecimal ZERO = new BigDecimal(0);
    private static final String HUOBI_URL = "https://api.huobi.pro";
    private static final HttpUtilManager httpUtil = HttpUtilManager.getInstance();

    private static String[] supportUsdtCoin = {"ht", "btc", "bch", "eth", "xrp", "ltc", "xem", "eos", "dash", "neo", "trx", "qtum", "etc", "omg", "hsr", "zec", "snt", "gnt", "ven", "cvc", "storj", "smt", "itc", "mds", "nas", "elf", "iost", "theta", "let", "dta", "zil", "ruff", "ela"};
    private static String[] supportEthCoin = {"ht", "eos", "trx", "icx", "lsk", "qtum", "omg", "hsr", "salt", "gnt", "cmt", "btm", "pay", "powr", "bat", "dgd", "ven", "qash", "gas", "mana", "eng", "cvc", "mco", "rdn", "chat", "srn", "link", "act", "tnb", "qsp", "req", "appc", "rcn", "smt", "adx", "tnt", "ost", "itc", "lun", "gnx", "evx", "mds", "snc", "propy", "eko", "nas", "wax", "wicc", "topc", "swftc", "dbc", "elf", "aidoc", "qun", "iost", "yee", "dat", "theta", "let", "dta", "utk", "mee", "zil", "soc", "ruff", "ocn", "ela", "zla", "stk", "wpr", "mtn", "mtx", "edu", "blz", "abt", "ont"};
    private static String[] supportBtcCoin = {"ht", "bch", "eth", "xrp", "ltc", "xem", "eos", "dash", "neo", "trx", "icx", "lsk", "qtum", "etc", "btg", "omg", "hsr", "zec", "snt", "salt", "gnt", "cmt", "btm", "pay", "knc", "powr", "bat", "dgd", "ven", "qash", "zrx", "gas", "mana", "eng", "cvc", "mco", "mtl", "rdn", "storj", "chat", "srn", "link", "act", "tnb", "qsp", "req", "rpx", "appc", "rcn", "smt", "adx", "tnt", "ost", "itc", "lun", "gnx", "ast", "evx", "mds", "snc", "propy", "eko", "nas", "bcd", "wax", "wicc", "topc", "swftc", "dbc", "elf", "aidoc", "qun", "iost", "yee", "dat", "theta", "let", "dta", "utk", "mee", "zil", "soc", "ruff", "ocn", "ela", "bcx", "sbtc", "bifi", "zla", "stk", "wpr", "mtn", "mtx", "edu", "blz", "abt", "ont"};

    public static void main(String[] args) {
        try {
            System.out.println(getMyTotalEth());
//            getSupportUsdtCoin();
//            getSupportBtcCoin();
//            getSupportEthCoin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取我的账户所有货币对应的总ETH数量
     *
     * @return ethCount
     */
    public static String getMyTotalEth() {
        final BigDecimal[] totalCoinUnitPrice = {new BigDecimal(0)};
        String ethToUsdtPrice = getEthToUsdtPrice();
        List<Kind> kindList = getMyBalance();
        kindList.stream().forEach(o -> {
            if (!"usdt".equals(o.getCurrency())) {
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
        BigDecimal myEth = totalCoinUnitPrice[0].divide(new BigDecimal(ethToUsdtPrice), 18, BigDecimal.ROUND_CEILING);
        return myEth.toString();
    }

    /**
     * 获取自己现金账户id
     *
     * @return accountId
     */
    public static String getMySpotAccountId() {
        try {
            V1RestApi api = new V1RestApiImpl();
            String accountJson = api.accounts();
            System.out.println(accountJson);
            Account account = (Account) JsonUtil.fromJson(accountJson, Account.class);
            List<Long> list = new ArrayList<>();
            if ("ok".equals(account.getStatus())) {
                account.getData().stream().forEach(o -> {
                    if ("spot".equals(o.getType())) {
                        list.add(o.getId());
                    }
                });
            }
            return list.get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取自己账户金额json
     *
     * @return json
     */
    public static String getAccountBalanceJson() {
        try {
            String accountId = getMySpotAccountId();
            V1RestApi api = new V1RestApiImpl();
            String json = api.accountsBalance(accountId);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取自己有余额的账户的信息
     *
     * @return 账户集合
     */
    public static List<Kind> getMyBalance() {
        try {
            String json = getAccountBalanceJson();
            Balance balance = (Balance) JsonUtil.fromJson(json, Balance.class);
            if ("ok".equals(balance.getStatus())) {
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
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取支持币种对应的usdt价格
     *
     * @param coinName 货币
     * @return 货币转换usdt的价格
     */
    private static String getSupportCoinToUsdtPrice(String coinName) {
        try {
            if (Arrays.asList(supportUsdtCoin).contains(coinName)) {
                String supportCoinToUsdtPriceUrl = "/market/detail?symbol=" + coinName + "usdt";
                String supportCoinToUsdtPriceJson = httpUtil.requestHttpGet(HUOBI_URL, supportCoinToUsdtPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToUsdtPriceJson, CoinPrice.class);
                if ("ok".equals(coinPrice.getStatus())) {
                    return coinPrice.getTick().getClose();
                }
            }
            System.out.println("usdt不支持" + coinName);
            if (Arrays.asList(supportEthCoin).contains(coinName)) {
                String supportCoinToEthPriceUrl = "/market/detail?symbol=" + coinName + "eth";
                String supportCoinToEthPriceJson = httpUtil.requestHttpGet(HUOBI_URL, supportCoinToEthPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToEthPriceJson, CoinPrice.class);
                if ("ok".equals(coinPrice.getStatus())) {
                    String price = coinPrice.getTick().getClose();
                    BigDecimal ethPrice = new BigDecimal(price);
                    String p = getEthToUsdtPrice();
                    BigDecimal coinToUsdtPrice = ethPrice.multiply(new BigDecimal(p));
                    return coinToUsdtPrice.toString();
                }
            }
            System.out.println("eth不支持" + coinName);
            if (Arrays.asList(supportBtcCoin).contains(coinName)) {
                String supportCoinToBtcPriceUrl = "/market/detail?symbol=" + coinName + "btc";
                String supportCoinToBtcPriceJson = httpUtil.requestHttpGet(HUOBI_URL, supportCoinToBtcPriceUrl);
                CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(supportCoinToBtcPriceJson, CoinPrice.class);
                if ("ok".equals(coinPrice.getStatus())) {
                    String price = coinPrice.getTick().getClose();
                    BigDecimal btcPrice = new BigDecimal(price);
                    String p = getBtcToUsdtPrice();
                    BigDecimal coinToUsdtPrice = btcPrice.multiply(new BigDecimal(p));
                    return coinToUsdtPrice.toString();
                }
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取货币所有支持币种
     *
     * @return coinList
     */
    private static List getSupportCoinList() {
        try {
            String supportCoinUrl = "/v1/common/currencys";
            String supportCoinJson = httpUtil.requestHttpGet(HUOBI_URL, supportCoinUrl);
            CoinKind coinKind = (CoinKind) JsonUtil.fromJson(supportCoinJson, CoinKind.class);
            if ("ok".equals(coinKind.getStatus())) {
                return coinKind.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ETH转换USDT价格
     *
     * @return price
     */
    private static String getEthToUsdtPrice() {
        try {
            String usdtEthPriceUrl = "/market/detail?symbol=ethusdt";
            String usdtEthPriceJson = httpUtil.requestHttpGet(HUOBI_URL, usdtEthPriceUrl);
            CoinPrice cp = (CoinPrice) JsonUtil.fromJson(usdtEthPriceJson, CoinPrice.class);
            String usdtEthPrice = cp.getTick().getClose();
            return usdtEthPrice;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BTC转换USDT价格
     *
     * @return price
     */
    private static String getBtcToUsdtPrice() {
        try {
            String usdtEthPriceUrl = "/market/detail?symbol=btcusdt";
            String usdtEthPriceJson = httpUtil.requestHttpGet(HUOBI_URL, usdtEthPriceUrl);
            CoinPrice cp = (CoinPrice) JsonUtil.fromJson(usdtEthPriceJson, CoinPrice.class);
            String usdtBtcPrice = cp.getTick().getClose();
            return usdtBtcPrice;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------------- 以下是获取上面3个json的方法 ------------------------------------ //

    /**
     * 支持USDT的货币Json
     *
     * @return json
     */
    public static String getSupportUsdtCoinJson() {
        try {
            List supportCoins = new ArrayList<>();
            List list = getSupportCoinList();
            list.stream().forEach(coin -> {
                String url = "/market/detail?symbol=" + coin + "usdt";
                try {
                    String json = httpUtil.requestHttpGet(HUOBI_URL, url);
                    CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(json, CoinPrice.class);
                    if ("ok".equals(coinPrice.getStatus())) {
                        supportCoins.add(coin);
                    }
                } catch (HttpException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            String j = JsonUtil.toJson(supportCoins);
            System.out.println("***" + j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 支持ETH的货币Json
     *
     * @return json
     */
    public static String getSupportEthCoinJson() {
        try {
            List supportCoins = new ArrayList<>();
            List list = getSupportCoinList();
            list.stream().forEach(coin -> {
                String url = "/market/detail?symbol=" + coin + "eth";
                try {
                    String json = httpUtil.requestHttpGet(HUOBI_URL, url);
                    CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(json, CoinPrice.class);
                    if ("ok".equals(coinPrice.getStatus())) {
                        supportCoins.add(coin);
                    }
                } catch (HttpException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            String j = JsonUtil.toJson(supportCoins);
            System.out.println("***" + j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 支持BTC的货币Json
     *
     * @return json
     */
    public static String getSupportBtcCoinJson() {
        try {
            List supportCoins = new ArrayList<>();
            List list = getSupportCoinList();
            list.stream().forEach(coin -> {
                String url = "/market/detail?symbol=" + coin + "btc";
                try {
                    String json = httpUtil.requestHttpGet(HUOBI_URL, url);
                    CoinPrice coinPrice = (CoinPrice) JsonUtil.fromJson(json, CoinPrice.class);
                    if ("ok".equals(coinPrice.getStatus())) {
                        supportCoins.add(coin);
                    }
                } catch (HttpException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            String j = JsonUtil.toJson(supportCoins);
            System.out.println("***" + j);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
