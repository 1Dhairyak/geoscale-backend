package com.geoscale.dto.request;

import lombok.Data;

@Data
public class InviteRequest {
    private String email;
    private String message;
}