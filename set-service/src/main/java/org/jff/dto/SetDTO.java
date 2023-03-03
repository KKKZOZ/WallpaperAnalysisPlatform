package org.jff.dto;

import lombok.Data;

@Data
public class SetDTO {

    private Long setID;
    private String setName;
    private boolean isPublic;
    private String coverUrl;
}
