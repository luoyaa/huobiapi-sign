package com.convert.huobi.ws.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author luotaishuai
 * @date 2018-03-21 13:55
 */
@Data
public class Kline {
    private int id;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal amount;
    private BigDecimal vol;
    private int count;
}
