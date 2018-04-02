package com.convert.huobi.rest.controller;

import com.convert.huobi.rest.domain.Secret;
import com.convert.huobi.rest.domain.order.OrderData;
import com.convert.huobi.rest.service.HuoBiApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoxuri
 * @create 2018-04-02 18:51
 **/
@RestController
@RequestMapping(value = "/huobi")
public class HuobiApiController {

    @Resource
    private HuoBiApiService huoBiApiService;

    @PostMapping(value = "/total")
    public String[] getMyTotal(@RequestBody Secret secret){
        return huoBiApiService.getMyTotal(secret.getAccessKey(), secret.getSecretKey());
    }

    @PostMapping(value = "/ordersInfo")
    public List<OrderData> getOrdersInfo(@RequestBody Secret secret){
        return huoBiApiService.getOrdersInfo(secret.getAccessKey(), secret.getSecretKey(), secret.getSymbol());
    }
}
