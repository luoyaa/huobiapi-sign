package com.convert.huobi.ws.domain;

import lombok.Data;

/**
 * @author luotaishuai
 * @date 2018-03-21 13:56
 */
@Data
public class SubscribeFailure {
    private String status;
    private String id;
    private String errCode;
    private String errMsg;
    private long ts;
}
