package org.jff.test;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(value ={OrderOutputChannelProcessor.class})
public class OrderMessageProducer {

    @Autowired
    @Output(OrderOutputChannelProcessor.SAVE_ORDER_OUTPUT)
    private final MessageChannel channel;

    public OrderMessageProducer(@Qualifier("saveOrderOutput") MessageChannel channel) {
        this.channel = channel;
    }

    public<T> void sendEvent(T data) {
        channel.send(MessageBuilder.withPayload(data).build());
        log.info("消息发送成功：" + data);
    }

}
