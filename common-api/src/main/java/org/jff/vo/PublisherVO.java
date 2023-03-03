package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublisherVO {

    private Long publisherId;
    private String publisherName;
    private String publisherAvatarUrl;

    public PublisherVO(UserVO userVO) {
        this.publisherId = userVO.getUserId();
        this.publisherName = userVO.getUsername();
        this.publisherAvatarUrl = userVO.getAvatarUrl();
    }
}
