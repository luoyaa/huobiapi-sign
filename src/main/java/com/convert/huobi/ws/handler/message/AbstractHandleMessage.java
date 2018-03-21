package com.convert.huobi.ws.handler.message;

import com.convert.huobi.ws.common.Const;
import com.convert.huobi.ws.domain.SubscribeFailure;
import com.convert.huobi.ws.domain.SubscribeSuccess;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 订阅数据处理类，对服务器返回的数据进行处理
 *
 * @author luotaishuai
 * @create 2018-03-21 13:50
 **/
public abstract class AbstractHandleMessage<T> implements IHandleMessage {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ObjectMapper objectMapper;

    private Class<T> clazz;

    public AbstractHandleMessage(Class<T> clazz) {
        this.clazz = clazz;
    }

    // 需要订阅的目标数据
    @Override
    public abstract String subscribe();

    @Override
    public void subscribeCallback(String serverMessage) {
        try {
            JsonNode jsonNode = this.objectMapper.readTree(serverMessage);
            if (jsonNode.hasNonNull(Const.STATUS)) {
                JsonNode status = jsonNode.get(Const.STATUS);
                if (Const.ERROR.equals(status)) {
                    subscribeFailed(this.objectMapper.convertValue(jsonNode, SubscribeFailure.class));
                } else {
                    subscribeSucceed(this.objectMapper.convertValue(jsonNode, SubscribeSuccess.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void call(String message) {
        try {
            JsonNode jsonNode = this.objectMapper.readTree(message);
            JsonNode tick = jsonNode.get("tick");
            success(this.objectMapper.convertValue(tick, this.clazz));
        } catch (Exception e) {
            failed(e);
        }
    }

    @Override
    public void webSocketError(Throwable exception) {
        LOG.error("WebSocket异常", exception);
    }

    @Override
    public void afterConnectionClosed() {
        LOG.error("连接关闭通知！！");
    }

    // 订阅成功时websocket返回消息处理
    public void subscribeSucceed(SubscribeSuccess subscribeSuccess) {
        LOG.info("订阅成功:{}", subscribeSuccess);
    }

    // 订阅失败时websocket返回消息处理
    public void subscribeFailed(SubscribeFailure error) {
        LOG.info("订阅失败:{}", error);
    }

    // 接受websocket服务成功 消息推送
    public abstract void success(T tick);

    // 异常处理
    public void failed(Exception e) {
        LOG.error("处理消息时异常：{}", e.getMessage(), e);
    }
}
