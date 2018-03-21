package com.convert.huobi.rest.domain.price;

import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-13 10:07
 **/
@lombok.Data
public class Data {

    private Long id;
    private String type;
    private String state;
    private List list;
}
