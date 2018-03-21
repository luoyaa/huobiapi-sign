package com.convert.huobi.rest.domain.support;

import lombok.Data;


/**
 * @author luoxuri
 * @create 2018-03-13 11:18
 **/
@Data
public class CoinPrice {
    private String status;
    private String ch;
    private String ts;
    private Tick tick;
}
