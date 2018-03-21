package com.convert.huobi.ws.handler;

import com.convert.huobi.ws.common.Const;
import com.convert.huobi.ws.handler.message.IHandleMessage;
import com.convert.huobi.ws.util.GzipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.nio.ByteBuffer;

/**
 * @author luotaishuai
 * @create 2018-03-21 14:17
 **/
public class HuobiWebSocketHandler extends AbstractWebSocketHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private IHandleMessage handleMessage;
    private WebSocketSession session;

    @Resource
    private ApplicationContext applicationContext;

    public HuobiWebSocketHandler(IHandleMessage handleMessage) {
        this.handleMessage = handleMessage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        session.sendMessage(new TextMessage(handleMessage.subscribe()));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        String serverMsg = new String(GzipUtil.decompress(payload.array()));
        LOG.debug("context:[{}]", serverMsg);

        if (serverMsg.contains(Const.PING)) {
            String clentMsg = serverMsg.replaceAll(Const.PING, Const.PONG);
            LOG.debug("heart:[{}]", clentMsg);
            session.sendMessage(new TextMessage(clentMsg));
        } else if (serverMsg.contains(Const.PONG)) {
            LOG.debug("接收服务器返回的心跳：{}", serverMsg);
        } else if (serverMsg.contains(Const.STATUS)) {
            handleMessage.subscribeCallback(serverMsg);
        } else {
            handleMessage.call(serverMsg);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.debug("handleTextMessage:[{}]", message.getPayload());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LOG.debug("handlePongMessage:[{}]", message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        handleMessage.webSocketError(exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.warn("失败后自动重连!!");
        try {
            WebSocketConnectionManager bean = this.applicationContext.getBean(WebSocketConnectionManager.class);
            bean.stop();
            bean.start();
            LOG.warn("失败后重连成功!!");
        } catch (Exception e) {
            LOG.error("重启 WebSocket 异常:{}", e.getMessage(), e);
        }
        handleMessage.afterConnectionClosed();
    }
}
