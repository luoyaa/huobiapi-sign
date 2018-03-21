package com.convert.huobi.rest.domain.balance;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-13 19:05
 **/
@Data
public class Account {
    private String status;
    private List<com.convert.huobi.rest.domain.balance.Data> data;
}
