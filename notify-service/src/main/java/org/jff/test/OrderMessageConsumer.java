package org.jff.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.EmailNotificationService;
import org.jff.Entity.NotificationEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(OrderInputChannelProcessor.class)
@RequiredArgsConstructor
public class OrderMessageConsumer {

    private final EmailNotificationService emailNotificationService;

    @StreamListener(OrderInputChannelProcessor.SAVE_ORDER_INPUT)
    //@SendTo(OrderOutputChannelProcessor.ORDER_OUTPUT)
    public void saveOrderMessage(Message<NotificationEvent> message) {
        NotificationEvent payload = message.getPayload();
        log.info("接收到的Event {}",payload);
        try {
            emailNotificationService.sendNotification(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //处理之后的订单消息
        //return "【" + message.getPayload() + "】";
    }

}
