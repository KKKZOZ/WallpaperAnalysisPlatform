package org.jff.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TargetInfo {

    private Long targetId;
    private String targetName;

    private String targetUrl;
}
