package org.jff.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {


    // Type
    public static final int COMMENT = 1,ACTIVATION=2;

    // Category
    public static final int SET=1,ARTICLE=2;

    private Integer type;

    private Integer category;

    private Long recipientId;

    private Long publisherId;

    private String content;


}
