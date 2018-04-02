package com.convert.huobi.rest.domain;

import lombok.Data;

/**
 * @author luoxuri
 * @create 2018-04-02 18:53
 **/
@Data
public class Secret {
    private String accessKey;
    private String secretKey;
    private String symbol;
}
