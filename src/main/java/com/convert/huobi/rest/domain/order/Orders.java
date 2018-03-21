package com.convert.huobi.rest.domain.order;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-14 16:53
 **/
@Data
public class Orders {
    private String status;
    private List<OrderData> data;
}
