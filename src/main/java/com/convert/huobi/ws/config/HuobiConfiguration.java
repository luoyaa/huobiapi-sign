package com.convert.huobi.ws.config;

import com.convert.huobi.ws.common.Const;
import com.convert.huobi.ws.domain.Kline;
import com.convert.huobi.ws.handler.HuobiWebSocketHandler;
import com.convert.huobi.ws.handler.message.IHandleMessage;
import com.convert.huobi.ws.handler.message.KlineHandleMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;

/**
 * @author luotaishuai
 * @create 2018-03-21 14:09
 **/
@Configuration
public class HuobiConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebSocketConnectionManager webSocketConnectionManager(WebSocketClient webSocketClient, WebSocketHandler handler) {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(webSocketClient, handler, Const.WEB_SOCKET_URL);
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketClient client() {
        return new JettyWebSocketClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandler handler() {
        return new HuobiWebSocketHandler(handleMessage());
    }

    @Bean
    @ConditionalOnMissingBean
    public IHandleMessage handleMessage() {
        return new KlineHandleMessage(Kline.class);
    }


}
