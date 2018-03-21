package com.convert.huobi.ws.handler.message;

/**
 * @author luotaishuai
 * @create 2018-03-21 13:52
 **/
public interface IHandleMessage {
    /**
     * 需要订阅的目标
     *
     * @return
     */
    String subscribe();

    /**
     * 订阅成功消息处理
     *
     * @param serverMessage
     */
    void subscribeCallback(String serverMessage);

    /**
     * 服务器返回数据回调处理
     *
     * @param message
     */
    void call(String message);

    /**
     * webSocket连接异常处理
     *
     * @param exception
     */
    void webSocketError(Throwable exception);

    /**
     * connection连接关闭通知
     */
    void afterConnectionClosed();

}
