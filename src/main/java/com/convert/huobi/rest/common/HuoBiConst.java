package com.convert.huobi.rest.common;

import java.math.BigDecimal;

/**
 * @author luoxuri
 * @create 2018-03-15 14:26
 **/
public class HuoBiConst {

    public static final BigDecimal ZERO = new BigDecimal(0);

    public static final String HUOBI_URL = "https://api.huobi.pro";
    public static String SIGN_URL = "api.huobi.pro";
    public static String SIGNATURE_METHOD = "HmacSHA256";
    public static String SIGNATURE_VERSION = "2";

    /**
     * 支持USDT/ETH/BTC交易的货币
     */
    public static String[] SUPPORT_USDT_COIN = {"ht","btc","bch","eth","xrp","ltc","xem","eos","dash","neo","trx","qtum","etc","omg","hsr","zec","snt","gnt","ven","cvc","storj","smt","itc","mds","nas","elf","iost","theta","let","dta","zil","ruff","ela"};
    public static String[] SUPPORT_ETH_COIN = {"ht","eos","trx","icx","lsk","qtum","omg","hsr","salt","gnt","cmt","btm","pay","powr","bat","dgd","ven","qash","gas","mana","eng","cvc","mco","rdn","chat","srn","link","act","tnb","qsp","req","appc","rcn","smt","adx","tnt","ost","itc","lun","gnx","evx","mds","snc","propy","eko","nas","wax","wicc","topc","swftc","dbc","elf","aidoc","qun","iost","yee","dat","theta","let","dta","utk","mee","zil","soc","ruff","ocn","ela","zla","stk","wpr","mtn","mtx","edu","blz","abt","ont"};
    public static String[] SUPPORT_BTC_COIN = {"ht","bch","eth","xrp","ltc","xem","eos","dash","neo","trx","icx","lsk","qtum","etc","btg","omg","hsr","zec","snt","salt","gnt","cmt","btm","pay","knc","powr","bat","dgd","ven","qash","zrx","gas","mana","eng","cvc","mco","mtl","rdn","storj","chat","srn","link","act","tnb","qsp","req","rpx","appc","rcn","smt","adx","tnt","ost","itc","lun","gnx","ast","evx","mds","snc","propy","eko","nas","bcd","wax","wicc","topc","swftc","dbc","elf","aidoc","qun","iost","yee","dat","theta","let","dta","utk","mee","zil","soc","ruff","ocn","ela","bcx","sbtc","bifi","zla","stk","wpr","mtn","mtx","edu","blz","abt","ont"};

    public static String USDT = "usdt";
    public static String BTC = "btc";
    public static String ETH = "eth";

    public static String OK = "ok";
    public static String ACCOUNT_SPOT = "spot";

    public static String BTC_USDT_PRICE_URL = "/market/detail?symbol=btcusdt";
    public static String ETH_USDT_PRICE_URL = "/market/detail?symbol=ethusdt";
    public static String SUPPORT_COIN_URL = "/v1/common/currencys";
    public static String SUPPORT_COIN_TO_JOINT_PRICE_URL_PRE = "/market/detail?symbol=";
    public static String ACCOUNT_BALANCE = "/v1/account/accounts/%s/balance";
    public static String ACCOUNT_URL = "/v1/account/accounts";
    public static String MATCH_RESULT_URL = "/v1/order/matchresults";
    public static String ORDERS_URL = "/v1/order/orders";
    public static String OEDERS_BY_ID = "/v1/order/orders/%s";

    public static String METHOD_GET = "GET";
    public static String METHOD_POST = "POST";

    public static String SIGN_MAP_SIGNATURE = "Signature";
    public static String SIGN_MAP_SYMBOL = "symbol";
    public static String SIGN_MAP_TYPES = "types";
    public static String SIGN_MAP_START_DATE = "start-date";
    public static String SIGN_MAP_END_DATE = "end-date";
    public static String SIGN_MAP_STATES = "states";
    public static String SIGN_MAP_FROM = "from";
    public static String SIGN_MAP_DIRECT = "direct";
    public static String SIGN_MAP_SIZE = "size";

    public static String SIGN_MAP_ACCESS_KEY = "AccessKeyId";
    public static String SIGN_MAP_SIGNATURE_METHOD = "SignatureMethod";
    public static String SIGN_MAP_SIGNATURE_VERSION = "SignatureVersion";
    public static String SIGN_MAP_TIMESTAMP = "Timestamp";

    public static String SUCCESS = "操作成功";
    public static String FAILED = "操作失败";

    /**
     * 火币所有交易对
     */
    public static String HUOBI_SYMBOLS = "['naseth','eoseth','zecusdt','evxbtc','mdseth','smteth','trxeth','thetausdt','luneth','smtusdt','bchbtc','letusdt','gnxeth','mtlbtc','chateth','qtumusdt','sntbtc','wprbtc','elfeth','zilbtc','utkbtc','sbtcbtc','tntbtc','neousdt','mcobtc','osteth','bt2btc','hsreth','topceth','salteth','aidoceth','waxbtc','dtaeth','btcusdt','gaseth','neobtc','btmbtc','blzeth','bateth','appcbtc','cmtbtc','ontbtc','qtumeth','iostbtc','reqbtc','btmeth','ruffbtc','zecbtc','dgdeth','dateth','stketh','hteth','quneth','elfbtc','cmteth','mdsusdt','sntusdt','storjusdt','waxeth','powrbtc','sncbtc','venusdt','tnbeth','swftceth','eosbtc','linketh','htusdt','rdnbtc','lunbtc','gnxbtc','elabtc','leteth','iostusdt','evxeth','acteth','bchusdt','icxeth','bcxbtc','mtneth','propyeth','xrpusdt','icxbtc','thetaeth','snceth','dbceth','itcusdt','smtbtc','srneth','ethusdt','itcbtc','omgbtc','stkbtc','mdsbtc','topcbtc','adxbtc','etcbtc','kncbtc','cvcbtc','qspeth','btgbtc','edubtc','zlaeth','mtxeth','eosusdt','rcnbtc','utketh','rcneth','gntusdt','appceth','wiccbtc','yeebtc','yeeeth','omgusdt','linkbtc','xemusdt','hsrusdt','dashbtc','qunbtc','qasheth','dtabtc','datbtc','aidocbtc','ruffeth','saltbtc','iosteth','elaeth','thetabtc','letbtc','dtausdt','soceth','elausdt','zilusdt','manabtc','xrpbtc','onteth','ltcusdt','dbcbtc','bcdbtc','swftcbtc','cvcusdt','nasusdt','gnteth','trxusdt','htbtc','engeth','payeth','cvceth','tnbbtc','meeeth','powreth','paybtc','blzbtc','adxeth','ekoeth','srnbtc','ocneth','veneth','abteth','bifibtc','actbtc','etcusdt','ostbtc','mcoeth','abtbtc','storjbtc','hsrbtc','venbtc','gntbtc','lskbtc','ekobtc','ltcbtc','ocnbtc','ruffusdt','wpreth','astbtc','dashusdt','dgdbtc','zileth','zrxbtc','ethbtc','reqeth','trxbtc','wicceth','nasbtc','meebtc','engbtc','lsketh','rpxbtc','tnteth','mtxbtc','socbtc','edueth','qtumbtc','qspbtc','batbtc','qashbtc','itceth','xembtc','manaeth','gasbtc','mtnbtc','chatbtc','bt1btc','zlabtc','omgeth','propybtc','rdneth','elfusdt']";
}
