package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetInfoVO {

    private Long setId;
    private String setName;
    private String coverUrl;

}
