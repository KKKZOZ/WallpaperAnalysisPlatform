package org.jff.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {


    public final int COMMENT = 1;
    public final int SET=1,ARTICLE=2;

    private Long recipientId;

    private Long publisherId;

    private String content;


}
