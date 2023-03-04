package org.jff.test;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface OrderInputChannelProcessor {
    String SAVE_ORDER_INPUT ="saveOrderInput";
    @Input(SAVE_ORDER_INPUT)
    SubscribableChannel saveOrderInput();
}
