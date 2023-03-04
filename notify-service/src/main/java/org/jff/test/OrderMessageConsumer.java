package org.jff.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(OrderInputChannelProcessor.class)
public class OrderMessageConsumer {
    @StreamListener(OrderInputChannelProcessor.SAVE_ORDER_INPUT)
    //@SendTo(OrderOutputChannelProcessor.ORDER_OUTPUT)
    public void saveOrderMessage(Message<String> message) {
        log.info("保存订单的消息：" + message);
        //处理之后的订单消息
        //return "【" + message.getPayload() + "】";
    }

}
