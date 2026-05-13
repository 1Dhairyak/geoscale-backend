package com.geoscale.dto.response;

import com.geoscale.entity.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchCreateResponse {
    private Long matchId;
    private MatchStatus status;
}
