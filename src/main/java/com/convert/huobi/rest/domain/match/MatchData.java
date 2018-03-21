package com.convert.huobi.rest.domain.match;

import lombok.Data;

/**
 * @author luoxuri
 * @create 2018-03-14 18:24
 **/
@Data
public class MatchData {
    private String id;
    private String orderid;
    private String matchid;
    private String symbol;
    private String type;
    private String source;
    private String price;
    private String filledamount;
    private String filledfees;
    private String filledpoints;
    private String createdat;
}
