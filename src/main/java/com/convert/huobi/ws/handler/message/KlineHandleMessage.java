package com.convert.huobi.ws.handler.message;

import com.convert.huobi.ws.common.Const;
import com.convert.huobi.ws.domain.Kline;
import com.convert.huobi.ws.handler.message.AbstractHandleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luotaishuai
 * @create 2018-03-21 13:49
 **/
public class KlineHandleMessage extends AbstractHandleMessage<Kline> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public KlineHandleMessage(Class<Kline> clazz) {
        super(clazz);
    }

    @Override
    public String subscribe() {
        return Const.TOPIC;
    }

    @Override
    public void success(Kline tick) {
        LOG.info("{}", tick);
    }
}
