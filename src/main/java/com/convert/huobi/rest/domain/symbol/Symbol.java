package com.convert.huobi.rest.domain.symbol;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-15 18:17
 **/
@Data
public class Symbol {
    private String status;
    private List<SymbolData> data;
}
