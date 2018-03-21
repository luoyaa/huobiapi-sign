package com.convert.huobi.rest.domain.symbol;

import lombok.Data;

/**
 * @author luoxuri
 * @create 2018-03-15 18:18
 **/
@Data
public class SymbolData {
    private String basecurrency;
    private String quotecurrency;
    private String amountprecision;
    private String symbolpartition;
}
