package org.jff.client;

import org.jff.Entity.Article;
import org.jff.Entity.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("set-service")
public interface SetServiceClient {
    @GetMapping("/api/v1/set/publisherId")
    public Long getPublisherIdBySetId(@RequestParam("setId") Long setId);

    @GetMapping("/api/v1/set/info")
    public Set getSetInfoBySetId(@RequestParam("setId") Long setId);
}
