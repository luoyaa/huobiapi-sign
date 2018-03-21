package com.convert.huobi.rest.domain.support;

import lombok.Data;

/**
 * @author luoxuri
 * @create 2018-03-13 11:20
 **/
@Data
public class Tick {
    private Long id;
    private String amount;
    private String open;
    private String close;
    private String high;
    private String count;
    private String low;
    private String version;
    private String vol;
}
