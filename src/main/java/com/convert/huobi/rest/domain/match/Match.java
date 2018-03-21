package com.convert.huobi.rest.domain.match;

import lombok.Data;

import java.util.List;

/**
 * @author luoxuri
 * @create 2018-03-14 18:25
 **/
@Data
public class Match {
    private String status;
    private List<MatchData> data;
}
