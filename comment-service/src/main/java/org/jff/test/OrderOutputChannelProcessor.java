package org.jff.test;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface OrderOutputChannelProcessor {

    String SAVE_ORDER_OUTPUT ="saveOrderOutput";

    @Output(SAVE_ORDER_OUTPUT)
    MessageChannel saveOrderOutput();
}
